package com.studentscore.struts.action;

import com.opensymphony.xwork2.ActionSupport;
import com.studentscore.common.Constants;
import com.studentscore.common.ScoreStatUtil;
import com.studentscore.dto.ScoreQuery;
import com.studentscore.entity.ClassInfo;
import com.studentscore.entity.Course;
import com.studentscore.service.ClassService;
import com.studentscore.service.CourseService;
import com.studentscore.service.ScoreService;
import com.studentscore.vo.ScoreSummary;
import com.studentscore.vo.ScoreVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Struts2 版成绩查询：与 SpringMVC 的 ScoreController 共用同一套 Service。
 */
@Component("scoreAction")
@Scope("prototype")
public class ScoreAction extends ActionSupport {

    private static final long serialVersionUID = 1L;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private ClassService classService;

    @Autowired
    private CourseService courseService;

    /**
     * 查询条件。可能被提交为空串的筛选项声明为 String，查询时再手动转类型。
     */
    private String classId;
    private String courseId;
    private String semester;
    private String keyword;

    /** 出参 */
    private List<ScoreVO> scores;
    private List<ClassInfo> classes;
    private List<Course> courses;
    private ScoreSummary summary;

    /** /score/list.action */
    public String list() {
        classes = classService.list();
        courses = courseService.list();
        if (semester == null || semester.trim().isEmpty()) {
            semester = resolveSemester(courses);
        }

        ScoreQuery query = new ScoreQuery(toLong(classId), toLong(courseId), semester, keyword);
        scores = scoreService.list(query);
        summary = ScoreStatUtil.summary(scores);
        return SUCCESS;
    }

    private static Long toLong(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return Long.valueOf(value.trim());
    }

    private String resolveSemester(List<Course> courses) {
        for (Course course : courses) {
            if (course.getSemester() != null && !course.getSemester().trim().isEmpty()) {
                return course.getSemester();
            }
        }
        return Constants.currentSemester();
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<ScoreVO> getScores() {
        return scores;
    }

    public List<ClassInfo> getClasses() {
        return classes;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public ScoreSummary getSummary() {
        return summary;
    }
}
