package com.studentscore.controller;

import com.studentscore.annotation.RequireRole;
import com.studentscore.common.BusinessException;
import com.studentscore.common.Result;
import com.studentscore.common.Role;
import com.studentscore.entity.Course;
import com.studentscore.service.CourseService;
import com.studentscore.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/course")
@RequireRole({Role.ADMIN, Role.TEACHER})
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("courses", courseService.list());
        return "course/list";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("teachers", teacherService.list());
        return "course/form";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam("id") Long id, Model model) {
        model.addAttribute("course", courseService.get(id));
        model.addAttribute("teachers", teacherService.list());
        return "course/form";
    }

    @PostMapping("/save")
    public String save(Course course, Model model, RedirectAttributes redirectAttributes) {
        try {
            courseService.saveOrUpdate(course);
        } catch (BusinessException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("course", course);
            model.addAttribute("teachers", teacherService.list());
            return "course/form";
        }
        redirectAttributes.addFlashAttribute("msg", "课程信息保存成功");
        return "redirect:/course/list";
    }

    @PostMapping("/delete")
    @ResponseBody
    public Result<?> delete(@RequestParam("id") Long id) {
        courseService.delete(id);
        return Result.ok();
    }
}
