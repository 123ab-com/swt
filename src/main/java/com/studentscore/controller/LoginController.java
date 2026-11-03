package com.studentscore.controller;

import com.studentscore.common.AuthUtils;
import com.studentscore.common.BusinessException;
import com.studentscore.common.Role;
import com.studentscore.entity.LoginUser;
import com.studentscore.service.LogService;
import com.studentscore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private LogService logService;

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        LoginUser user = AuthUtils.currentUser(session);
        if (user != null) {
            return Role.STUDENT.equals(user.getRole()) ? "redirect:/my/score" : "redirect:/dashboard";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session,
                        HttpServletRequest request,
                        Model model) {
        try {
            LoginUser user = userService.login(username, password);
            session.setAttribute(AuthUtils.SESSION_USER, user);
            logService.record(user.getId(), user.getUsername(), "登录", "登录系统", request.getRemoteAddr());
            if (Role.STUDENT.equals(user.getRole())) {
                return "redirect:/my/score";
            }
            return "redirect:/dashboard";
        } catch (BusinessException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("username", username);
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
