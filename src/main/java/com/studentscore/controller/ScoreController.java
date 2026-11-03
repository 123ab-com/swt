package com.studentscore.controller;

import com.github.pagehelper.PageInfo;
import com.studentscore.annotation.RequireRole;
import com.studentscore.common.AuthUtils;
import com.studentscore.common.BusinessException;
import com.studentscore.common.Constants;
import com.studentscore.common.Result;
import com.studentscore.common.Role;
import com.studentscore.common.ScoreExcelUtil;
import com.studentscore.dto.ScoreQuery;
import com.studentscore.entity.ClassInfo;
import com.studentscore.entity.Course;
import com.studentscore.entity.LoginUser;
import com.studentscore.entity.Score;
import com.studentscore.service.ClassService;
import com.studentscore.service.CourseService;
import com.studentscore.service.LogService;
import com.studentscore.service.ScoreService;
import com.studentscore.vo.ScoreSummary;
import com.studentscore.vo.ScoreVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
@RequestMapping("/score")
@RequireRole({Role.ADMIN, Role.TEACHER})
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private ClassService classService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private LogService logService;

    /** 成绩录入 / 修改 */
    @GetMapping("/entry")
    public String entry(@RequestParam(value = "classId", required = false) Long classId,
                        @RequestParam(value = "courseId", required = false) Long courseId,
                        @RequestParam(value = "semester", required = false) String semester,
                        HttpSession session,
                        Model model) {
        Long teacherId = teacherIdOf(session);
        List<ClassInfo> classes = classService.list();
        List<Course> courses = courseService.listByTeacher(teacherId);
        classId = firstClassId(classes, classId);
        courseId = firstCourseId(courses, courseId);
        semester = resolveSemester(courses, semester);
        checkCourseOwned(teacherId, courseId);

        model.addAttribute("classes", classes);
        model.addAttribute("courses", courses);
        model.addAttribute("classId", classId);
        model.addAttribute("courseId", courseId);
        model.addAttribute("semester", semester);
        model.addAttribute("rows", scoreService.entryRows(classId, courseId, semester));
        return "score/entry";
    }

    @PostMapping("/save")
    @ResponseBody
    public Result<?> save(@RequestBody List<Score> scores, HttpSession session, HttpServletRequest request) {
        Long teacherId = teacherIdOf(session);
        scoreService.saveBatch(scores, teacherId);
        LoginUser user = AuthUtils.currentUser(session);
        logService.record(user.getId(), user.getUsername(), "成绩录入",
                "保存 " + (scores == null ? 0 : scores.size()) + " 条成绩",
                request.getRemoteAddr());
        return Result.ok("成绩保存成功", null);
    }

    /** 成绩查询（分页） */
    @GetMapping("/list")
    public String list(@ModelAttribute("query") ScoreQuery query, HttpSession session, Model model) {
        Long teacherId = teacherIdOf(session);
        List<ClassInfo> classes = classService.list();
        List<Course> courses = courseService.listByTeacher(teacherId);
        if (!StringUtils.hasText(query.getSemester())) {
            query.setSemester(resolveSemester(courses, null));
        }
        checkCourseOwned(teacherId, query.getCourseId());

        PageInfo<ScoreVO> pageInfo = scoreService.listPage(query);
        ScoreSummary summary = scoreService.summary(query);

        model.addAttribute("classes", classes);
        model.addAttribute("courses", courses);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("summary", summary);
        model.addAttribute("avgScore", summary.getAvgScore());
        model.addAttribute("maxScore", summary.getMaxScore());
        model.addAttribute("minScore", summary.getMinScore());
        model.addAttribute("passCount", summary.getPassCount());
        model.addAttribute("passRate", summary.getTotalCount() == 0 ? null
                : summary.getPassCount() * 100.0 / summary.getTotalCount());
        model.addAttribute("totalCount", summary.getTotalCount());
        return "score/list";
    }

    /** 导出当前查询条件的成绩（不受分页影响） */
    @GetMapping("/export")
    public void export(@ModelAttribute("query") ScoreQuery query, HttpSession session,
                       HttpServletResponse response) throws IOException {
        Long teacherId = teacherIdOf(session);
        checkCourseOwned(teacherId, query.getCourseId());
        List<ScoreVO> rows = scoreService.list(query);
        String fileName = StringUtils.hasText(query.getSemester()) ? "成绩_" + query.getSemester() : "成绩";
        ScoreExcelUtil.write(response, fileName, rows);
    }

    /** 成绩导入模板 */
    @GetMapping("/template")
    public void template(HttpServletResponse response) throws IOException {
        ScoreExcelUtil.writeTemplate(response, "成绩导入模板");
    }

    /** Excel 批量导入成绩 */
    @PostMapping("/import")
    @ResponseBody
    public Result<?> importScores(@RequestParam("file") MultipartFile file,
                                  HttpSession session,
                                  HttpServletRequest request) {
        if (file == null || file.isEmpty()) {
            return Result.fail("请选择要导入的 Excel 文件");
        }
        String originalName = file.getOriginalFilename();
        if (originalName != null && !originalName.toLowerCase().endsWith(".xlsx")
                && !originalName.toLowerCase().endsWith(".xls")) {
            return Result.fail("仅支持 .xlsx / .xls 格式的 Excel 文件");
        }
        Long teacherId = teacherIdOf(session);
        try (InputStream inputStream = file.getInputStream()) {
            int count = scoreService.importScores(inputStream, teacherId);
            LoginUser user = AuthUtils.currentUser(session);
            logService.record(user.getId(), user.getUsername(), "成绩录入",
                    "Excel 导入 " + count + " 条成绩（" + originalName + "）",
                    request.getRemoteAddr());
            return Result.ok("成功导入 " + count + " 条成绩", null);
        } catch (BusinessException e) {
            return Result.fail(e.getMessage());
        } catch (IOException e) {
            return Result.fail("文件读取失败：" + e.getMessage());
        }
    }

    /** 成绩统计 */
    @GetMapping("/stats")
    public String stats(@RequestParam(value = "classId", required = false) Long classId,
                        @RequestParam(value = "courseId", required = false) Long courseId,
                        @RequestParam(value = "semester", required = false) String semester,
                        HttpSession session,
                        Model model) {
        Long teacherId = teacherIdOf(session);
        List<ClassInfo> classes = classService.list();
        List<Course> courses = courseService.listByTeacher(teacherId);
        classId = firstClassId(classes, classId);
        courseId = firstCourseId(courses, courseId);
        semester = resolveSemester(courses, semester);
        checkCourseOwned(teacherId, courseId);

        model.addAttribute("classes", classes);
        model.addAttribute("courses", courses);
        model.addAttribute("classId", classId);
        model.addAttribute("courseId", courseId);
        model.addAttribute("semester", semester);
        model.addAttribute("studentStats", scoreService.statByStudent(classId, semester, null));
        model.addAttribute("courseStats", scoreService.statByCourse(classId, semester));
        model.addAttribute("distribution", scoreService.distribution(classId, courseId, semester));
        return "score/stats";
    }

    /** 管理员返回 null（不限制），教师返回其 teacher.id */
    private Long teacherIdOf(HttpSession session) {
        LoginUser user = AuthUtils.currentUser(session);
        if (user == null) {
            throw new BusinessException("登录已失效，请重新登录");
        }
        return Role.ADMIN.equals(user.getRole()) ? null : user.getRefId();
    }

    /** 教师越权访问他人课程时拒绝 */
    private void checkCourseOwned(Long teacherId, Long courseId) {
        if (teacherId == null || courseId == null) {
            return;
        }
        Course course = courseService.get(courseId);
        if (course == null || !teacherId.equals(course.getTeacherId())) {
            throw new BusinessException("只能操作自己所授课程的成绩");
        }
    }

    private Long firstClassId(List<ClassInfo> classes, Long classId) {
        if (classId != null) {
            return classId;
        }
        return classes.isEmpty() ? null : classes.get(0).getId();
    }

    private Long firstCourseId(List<Course> courses, Long courseId) {
        if (courseId != null) {
            return courseId;
        }
        return courses.isEmpty() ? null : courses.get(0).getId();
    }

    private String resolveSemester(List<Course> courses, String semester) {
        if (StringUtils.hasText(semester)) {
            return semester;
        }
        for (Course course : courses) {
            if (StringUtils.hasText(course.getSemester())) {
                return course.getSemester();
            }
        }
        return Constants.currentSemester();
    }
}
