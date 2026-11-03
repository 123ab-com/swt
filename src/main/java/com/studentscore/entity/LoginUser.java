package com.studentscore.entity;

import com.studentscore.common.Role;

import java.io.Serializable;

/**
 * 登录会话用户。
 */
public class LoginUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String realName;
    private String role;
    /** 关联的业务主键：学生对应 student.id，教师对应 teacher.id */
    private Long refId;

    public LoginUser() {
    }

    public LoginUser(Long id, String username, String realName, String role, Long refId) {
        this.id = id;
        this.username = username;
        this.realName = realName;
        this.role = role;
        this.refId = refId;
    }

    public String getRoleLabel() {
        return Role.label(role);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getRefId() {
        return refId;
    }

    public void setRefId(Long refId) {
        this.refId = refId;
    }
}
