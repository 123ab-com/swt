package com.studentscore.controller;

import com.studentscore.annotation.RequireRole;
import com.studentscore.common.AuthUtils;
import com.studentscore.common.BusinessException;
import com.studentscore.common.Constants;
import com.studentscore.common.Result;
import com.studentscore.common.Role;
import com.studentscore.dto.UserQuery;
import com.studentscore.entity.LoginUser;
import com.studentscore.entity.SysUser;
import com.studentscore.service.LogService;
import com.studentscore.service.UserService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

/**
 * 账号管理：仅管理员可用。
 */
@Controller
@RequestMapping("/user")
@RequireRole({Role.ADMIN})
public class UserController {

    private static final List<String> ROLES = Arrays.asList(Role.ADMIN, Role.TEACHER, Role.STUDENT);

    @Autowired
    private UserService userService;

    @Autowired
    private LogService logService;

    @GetMapping("/list")
    public String list(@ModelAttribute("query") UserQuery query, Model model) {
        model.addAttribute("pageInfo", userService.page(query));
        model.addAttribute("roles", ROLES);
        return "user/list";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("user", new SysUser());
        model.addAttribute("roles", ROLES);
        return "user/form";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam("id") Long id, Model model) {
        SysUser user = userService.get(id);
        if (user == null) {
            return "redirect:/user/list";
        }
        user.setPassword(null);
        model.addAttribute("user", user);
        model.addAttribute("roles", ROLES);
        return "user/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("user") SysUser user, Model model, RedirectAttributes redirectAttributes) {
        try {
            userService.saveOrUpdate(user);
        } catch (BusinessException e) {
            user.setPassword(null);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", user);
            model.addAttribute("roles", ROLES);
            return "user/form";
        }
        redirectAttributes.addFlashAttribute("msg", "账号保存成功");
        return "redirect:/user/list";
    }

    @PostMapping("/resetPwd")
    @ResponseBody
    public Result<?> resetPwd(@RequestParam("id") Long id, HttpSession session, HttpServletRequest request) {
        try {
            userService.resetPassword(id);
            LoginUser current = AuthUtils.currentUser(session);
            logService.record(current == null ? null : current.getId(),
                    current == null ? "系统" : current.getUsername(),
                    "账号管理", "重置账号ID " + id + " 的密码", request.getRemoteAddr());
            return Result.ok("密码已重置为 " + Constants.DEFAULT_PASSWORD, null);
        } catch (BusinessException e) {
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    public Result<?> delete(@RequestParam("id") Long id, HttpSession session) {
        LoginUser current = AuthUtils.currentUser(session);
        if (current != null && current.getId() != null && current.getId().equals(id)) {
            return Result.fail("不能删除当前登录的账号");
        }
        try {
            userService.delete(id);
            return Result.ok();
        } catch (BusinessException e) {
            return Result.fail(e.getMessage());
        }
    }

    /** 为尚无账号的在读学生批量开通账号 */
    @PostMapping("/genStudents")
    @ResponseBody
    public Result<?> genStudents() {
        try {
            int count = userService.generateStudentAccounts();
            if (count == 0) {
                return Result.ok("所有在读学生均已拥有账号", null);
            }
            return Result.ok("已生成 " + count + " 个学生账号，默认口令 " + Constants.DEFAULT_PASSWORD, null);
        } catch (BusinessException e) {
            return Result.fail(e.getMessage());
        }
    }
}
