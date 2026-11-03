package com.studentscore.dto;

import com.studentscore.common.PageQuery;

/**
 * 操作日志查询条件。
 */
public class LogQuery extends PageQuery {

    private String username;
    private String module;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
}
