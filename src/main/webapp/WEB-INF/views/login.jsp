<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>登录 - 4A班级学生成绩管理系统</title>
    <link rel="stylesheet" href="${ctx}/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="${ctx}/static/css/style.css">
</head>
<body class="login-page">
<div class="login-card card">
    <div class="card-body p-4 p-md-5">
        <div class="text-center mb-4">
            <h4 class="mb-1">4A班级学生成绩管理系统</h4>
            <div class="text-muted small">Student Score Management System</div>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger py-2 small">${error}</div>
        </c:if>

        <form method="post" action="${ctx}/login">
            <div class="mb-3">
                <label class="form-label">账号</label>
                <input type="text" name="username" class="form-control" value="${username}"
                       placeholder="管理员 / 教师工号 / 学号" required autofocus>
            </div>
            <div class="mb-4">
                <label class="form-label">密码</label>
                <input type="password" name="password" class="form-control" placeholder="请输入密码" required>
            </div>
            <button type="submit" class="btn btn-primary w-100">登 录</button>
        </form>

        <div class="mt-4 pt-3 border-top text-muted small">
            测试账号：<b>admin</b>（管理员）、<b>teacher</b>（教师）、<b>2024001</b>（学生），密码均为 <b>123456</b>
        </div>
    </div>
</div>
</body>
</html>
