package com.studentscore.dto;

import com.studentscore.common.PageQuery;

/**
 * 账号查询条件。
 */
public class UserQuery extends PageQuery {

    private String username;
    private String role;
    private Integer status;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
