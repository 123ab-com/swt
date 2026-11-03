package com.studentscore.struts.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.studentscore.common.AuthUtils;
import com.studentscore.common.Role;
import com.studentscore.entity.LoginUser;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpSession;

/**
 * Struts2 侧的登录 / 角色拦截器，与 SpringMVC 的 LoginInterceptor 复用同一份会话。
 * 未登录或学生账号返回全局结果 "login"。
 */
public class StrutsLoginInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = 1L;

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        HttpSession session = ServletActionContext.getRequest().getSession(false);
        LoginUser user = AuthUtils.currentUser(session);
        if (user == null) {
            return "login";
        }
        if (Role.STUDENT.equals(user.getRole())) {
            return "login";
        }
        return invocation.invoke();
    }
}
