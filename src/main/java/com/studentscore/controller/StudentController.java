package com.studentscore.controller;

import com.github.pagehelper.PageInfo;
import com.studentscore.annotation.RequireRole;
import com.studentscore.common.BusinessException;
import com.studentscore.common.Result;
import com.studentscore.common.Role;
import com.studentscore.dto.StudentQuery;
import com.studentscore.entity.Student;
import com.studentscore.service.ClassService;
import com.studentscore.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/student")
@RequireRole({Role.ADMIN, Role.TEACHER})
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ClassService classService;

    @GetMapping("/list")
    public String list(@ModelAttribute("query") StudentQuery query, Model model) {
        PageInfo<Student> pageInfo = studentService.page(query);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("query", query);
        model.addAttribute("classes", classService.list());
        return "student/list";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("classes", classService.list());
        return "student/form";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam("id") Long id, Model model) {
        model.addAttribute("student", studentService.get(id));
        model.addAttribute("classes", classService.list());
        return "student/form";
    }

    @PostMapping("/save")
    public String save(Student student, Model model, RedirectAttributes redirectAttributes) {
        try {
            studentService.saveOrUpdate(student);
        } catch (BusinessException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("student", student);
            model.addAttribute("classes", classService.list());
            return "student/form";
        }
        redirectAttributes.addFlashAttribute("msg", "学生信息保存成功");
        return "redirect:/student/list";
    }

    @PostMapping("/delete")
    @ResponseBody
    public Result<?> delete(@RequestParam("id") Long id) {
        studentService.delete(id);
        return Result.ok();
    }
}
