<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>页面不存在</title>
    <link rel="stylesheet" href="${ctx}/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="${ctx}/static/css/style.css">
</head>
<body class="error-page">
<div class="text-center">
    <div class="error-code">404</div>
    <p class="text-muted mt-2">你访问的页面不存在或已被移除。</p>
    <a class="btn btn-primary" href="${ctx}/dashboard">返回工作台</a>
</div>
</body>
</html>
