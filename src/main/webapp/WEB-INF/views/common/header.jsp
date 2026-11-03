<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:out value="${empty pageTitle ? '4A班级学生成绩管理系统' : pageTitle}"/></title>
    <link rel="stylesheet" href="${ctx}/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="${ctx}/static/css/bootstrap-icons.css">
    <link rel="stylesheet" href="${ctx}/static/css/style.css">
    <script>window.CTX = '${ctx}';</script>
</head>
<body>
<div class="app-wrapper">
    <aside class="app-sidebar">
        <div class="brand">
            <i class="bi bi-mortarboard-fill"></i>
            <span>4A 成绩管理</span>
        </div>
        <nav class="nav flex-column">
            <c:if test="${loginUser.role ne 'STUDENT'}">
                <a class="nav-link ${fn:contains(uri, '/dashboard') ? 'active' : ''}" href="${ctx}/dashboard">
                    <i class="bi bi-speedometer2"></i><span>工作台</span>
                </a>
                <a class="nav-link ${fn:contains(uri, '/student/') ? 'active' : ''}" href="${ctx}/student/list">
                    <i class="bi bi-people"></i><span>学生管理</span>
                </a>
                <a class="nav-link ${fn:contains(uri, '/class/') ? 'active' : ''}" href="${ctx}/class/list">
                    <i class="bi bi-diagram-3"></i><span>班级管理</span>
                </a>
                <a class="nav-link ${fn:contains(uri, '/course/') ? 'active' : ''}" href="${ctx}/course/list">
                    <i class="bi bi-journal-bookmark"></i><span>课程管理</span>
                </a>
                <a class="nav-link ${fn:contains(uri, '/score/entry') ? 'active' : ''}" href="${ctx}/score/entry">
                    <i class="bi bi-pencil-square"></i><span>成绩录入</span>
                </a>
                <a class="nav-link ${fn:contains(uri, '/score/list') ? 'active' : ''}" href="${ctx}/score/list">
                    <i class="bi bi-search"></i><span>成绩查询</span>
                </a>
                <a class="nav-link ${fn:contains(uri, '/score/stats') ? 'active' : ''}" href="${ctx}/score/stats">
                    <i class="bi bi-bar-chart"></i><span>成绩统计</span>
                </a>
                <a class="nav-link ${fn:contains(uri, '/struts/') ? 'active' : ''}" href="${ctx}/struts/index.action">
                    <i class="bi bi-diagram-2"></i><span>Struts2 演示</span>
                </a>
                <c:if test="${loginUser.role eq 'ADMIN'}">
                    <a class="nav-link ${fn:contains(uri, '/user/') ? 'active' : ''}" href="${ctx}/user/list">
                        <i class="bi bi-person-badge"></i><span>账号管理</span>
                    </a>
                    <a class="nav-link ${fn:contains(uri, '/log/') ? 'active' : ''}" href="${ctx}/log/list">
                        <i class="bi bi-clock-history"></i><span>操作日志</span>
                    </a>
                </c:if>
            </c:if>
            <c:if test="${loginUser.role eq 'STUDENT'}">
                <a class="nav-link ${fn:contains(uri, '/my/score') ? 'active' : ''}" href="${ctx}/my/score">
                    <i class="bi bi-clipboard-data"></i><span>我的成绩</span>
                </a>
            </c:if>
            <a class="nav-link ${fn:contains(uri, '/profile/') ? 'active' : ''}" href="${ctx}/profile/password">
                <i class="bi bi-key"></i><span>修改密码</span>
            </a>
        </nav>
    </aside>
    <div class="app-main">
        <header class="app-topbar">
            <div class="fw-semibold">${pageTitle}</div>
            <div class="d-flex align-items-center gap-3">
                <span class="text-muted small">${loginUser.roleLabel}</span>
                <span class="badge bg-primary-subtle text-primary-emphasis">${loginUser.realName}</span>
                <a class="btn btn-sm btn-outline-secondary" href="${ctx}/logout">
                    <i class="bi bi-box-arrow-right"></i> 退出
                </a>
            </div>
        </header>
        <main class="app-content">
