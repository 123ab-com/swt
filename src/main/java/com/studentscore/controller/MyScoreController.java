package com.studentscore.controller;

import com.studentscore.annotation.RequireRole;
import com.studentscore.common.AuthUtils;
import com.studentscore.common.Role;
import com.studentscore.entity.LoginUser;
import com.studentscore.entity.Student;
import com.studentscore.service.ScoreService;
import com.studentscore.service.StudentService;
import com.studentscore.vo.ScoreVO;
import com.studentscore.vo.StudentStatVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 学生端：查看个人成绩。
 */
@Controller
@RequestMapping("/my")
@RequireRole({Role.STUDENT})
public class MyScoreController {

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private StudentService studentService;

    @GetMapping("/score")
    public String score(@RequestParam(value = "semester", required = false) String semester,
                        HttpSession session,
                        Model model) {
        LoginUser user = AuthUtils.currentUser(session);
        Student student = user == null || user.getRefId() == null ? null : studentService.get(user.getRefId());

        List<ScoreVO> all = student == null
                ? Collections.<ScoreVO>emptyList()
                : scoreService.listByStudent(student.getId(), null);
        Set<String> semesters = new LinkedHashSet<>();
        for (ScoreVO vo : all) {
            if (StringUtils.hasText(vo.getSemester())) {
                semesters.add(vo.getSemester());
            }
        }

        List<ScoreVO> scores = student == null
                ? Collections.<ScoreVO>emptyList()
                : scoreService.listByStudent(student.getId(), semester);
        BigDecimal avg = student == null ? null : scoreService.avgByStudent(student.getId(), semester);
        BigDecimal weightedAvg = student == null ? null : scoreService.weightedAvgByStudent(student.getId(), semester);

        int rank = 0;
        int classTotal = 0;
        if (student != null) {
            List<StudentStatVO> classStats = scoreService.statByStudent(student.getClassId(), semester, null);
            classTotal = classStats.size();
            for (int i = 0; i < classStats.size(); i++) {
                StudentStatVO vo = classStats.get(i);
                if (vo.getStudentId() != null && vo.getStudentId().equals(student.getId())) {
                    rank = i + 1;
                    break;
                }
            }
        }

        model.addAttribute("student", student);
        model.addAttribute("scores", scores);
        model.addAttribute("semesters", semesters);
        model.addAttribute("semester", semester);
        model.addAttribute("avgScore", avg);
        model.addAttribute("weightedAvg", weightedAvg);
        model.addAttribute("rank", rank);
        model.addAttribute("classTotal", classTotal);
        return "my/score";
    }
}
