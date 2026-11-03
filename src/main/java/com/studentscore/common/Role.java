package com.studentscore.common;

/**
 * 系统角色常量。
 */
public final class Role {

    /** 系统管理员 */
    public static final String ADMIN = "ADMIN";
    /** 教师 */
    public static final String TEACHER = "TEACHER";
    /** 学生 */
    public static final String STUDENT = "STUDENT";

    private Role() {
    }

    public static String label(String role) {
        if (ADMIN.equals(role)) {
            return "管理员";
        }
        if (TEACHER.equals(role)) {
            return "教师";
        }
        if (STUDENT.equals(role)) {
            return "学生";
        }
        return "未知";
    }
}
