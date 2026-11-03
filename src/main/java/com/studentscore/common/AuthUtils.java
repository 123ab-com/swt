package com.studentscore.common;

import com.studentscore.entity.LoginUser;

import javax.servlet.http.HttpSession;

/**
 * 登录态读取工具。
 */
public final class AuthUtils {

    public static final String SESSION_USER = "loginUser";

    private AuthUtils() {
    }

    public static LoginUser currentUser(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object user = session.getAttribute(SESSION_USER);
        return user instanceof LoginUser ? (LoginUser) user : null;
    }

    public static boolean hasAnyRole(HttpSession session, String... roles) {
        LoginUser user = currentUser(session);
        if (user == null || roles == null) {
            return false;
        }
        for (String role : roles) {
            if (role.equals(user.getRole())) {
                return true;
            }
        }
        return false;
    }
}
