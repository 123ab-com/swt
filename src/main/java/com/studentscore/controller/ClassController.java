package com.studentscore.controller;

import com.studentscore.annotation.RequireRole;
import com.studentscore.common.BusinessException;
import com.studentscore.common.Result;
import com.studentscore.common.Role;
import com.studentscore.entity.ClassInfo;
import com.studentscore.service.ClassService;
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
@RequestMapping("/class")
@RequireRole({Role.ADMIN, Role.TEACHER})
public class ClassController {

    @Autowired
    private ClassService classService;

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("classes", classService.list());
        return "class/list";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("classInfo", new ClassInfo());
        model.addAttribute("teachers", teacherService.list());
        return "class/form";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam("id") Long id, Model model) {
        model.addAttribute("classInfo", classService.get(id));
        model.addAttribute("teachers", teacherService.list());
        return "class/form";
    }

    @PostMapping("/save")
    public String save(ClassInfo classInfo, Model model, RedirectAttributes redirectAttributes) {
        try {
            classService.saveOrUpdate(classInfo);
        } catch (BusinessException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("classInfo", classInfo);
            model.addAttribute("teachers", teacherService.list());
            return "class/form";
        }
        redirectAttributes.addFlashAttribute("msg", "班级信息保存成功");
        return "redirect:/class/list";
    }

    @PostMapping("/delete")
    @ResponseBody
    public Result<?> delete(@RequestParam("id") Long id) {
        classService.delete(id);
        return Result.ok();
    }
}
