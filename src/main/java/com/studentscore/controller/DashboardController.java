package com.studentscore.controller;

import com.studentscore.annotation.RequireRole;
import com.studentscore.common.Role;
import com.studentscore.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequireRole({Role.ADMIN, Role.TEACHER})
public class DashboardController {

    @Autowired
    private ScoreService scoreService;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("dashboard", scoreService.dashboard());
        return "dashboard";
    }
}
