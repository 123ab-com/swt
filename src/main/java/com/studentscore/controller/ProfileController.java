package com.studentscore.controller;

import com.studentscore.common.AuthUtils;
import com.studentscore.common.BusinessException;
import com.studentscore.entity.LoginUser;
import com.studentscore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 * 个人中心：修改本人密码（三种角色通用）。
 */
@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/password")
    public String password() {
        return "profile/password";
    }

    @PostMapping("/password")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 HttpSession session,
                                 Model model) {
        LoginUser user = AuthUtils.currentUser(session);
        try {
            if (user == null || user.getId() == null) {
                throw new BusinessException("登录已失效，请重新登录");
            }
            if (!newPassword.equals(confirmPassword)) {
                throw new BusinessException("两次输入的新密码不一致");
            }
            userService.changePassword(user.getId(), oldPassword, newPassword);
        } catch (BusinessException e) {
            model.addAttribute("error", e.getMessage());
            return "profile/password";
        }
        model.addAttribute("msg", "密码修改成功，请牢记新密码");
        return "profile/password";
    }
}
