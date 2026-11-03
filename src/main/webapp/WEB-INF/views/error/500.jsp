<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>操作失败</title>
    <link rel="stylesheet" href="${ctx}/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="${ctx}/static/css/style.css">
</head>
<body class="error-page">
<div class="text-center" style="max-width: 520px;">
    <div class="error-code">500</div>
    <p class="text-muted mt-2">${empty message ? '系统处理失败，请稍后再试。' : message}</p>
    <a class="btn btn-primary" href="javascript:history.back()">返回上一页</a>
    <a class="btn btn-outline-secondary" href="${ctx}/dashboard">返回工作台</a>
</div>
</body>
</html>
