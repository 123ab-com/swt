package com.studentscore.controller;

import com.studentscore.annotation.RequireRole;
import com.studentscore.common.Role;
import com.studentscore.dto.LogQuery;
import com.studentscore.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

/**
 * 操作日志：仅管理员可查看。
 */
@Controller
@RequestMapping("/log")
@RequireRole({Role.ADMIN})
public class LogController {

    private static final List<String> MODULES = Arrays.asList("登录", "成绩录入", "账号管理");

    @Autowired
    private LogService logService;

    @GetMapping("/list")
    public String list(@ModelAttribute("query") LogQuery query, Model model) {
        model.addAttribute("pageInfo", logService.page(query));
        model.addAttribute("modules", MODULES);
        return "log/list";
    }
}
