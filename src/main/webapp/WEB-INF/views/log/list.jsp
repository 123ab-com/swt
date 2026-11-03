<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglib.jsp" %>
<c:set var="pageTitle" value="操作日志"/>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div class="card mb-3">
    <div class="card-body">
        <form class="row g-2 align-items-end" method="get" action="${ctx}/log/list">
            <div class="col-6 col-md-3">
                <label class="form-label small text-muted mb-1">操作账号</label>
                <input type="text" class="form-control form-control-sm" name="username" value="${query.username}">
            </div>
            <div class="col-6 col-md-3">
                <label class="form-label small text-muted mb-1">模块</label>
                <select class="form-select form-select-sm" name="module">
                    <option value="">全部模块</option>
                    <c:forEach items="${modules}" var="m">
                        <option value="${m}" ${query.module eq m ? 'selected' : ''}>${m}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-12 col-md-3 d-flex gap-2">
                <button class="btn btn-sm btn-primary" type="submit"><i class="bi bi-search"></i> 查询</button>
                <a class="btn btn-sm btn-outline-secondary" href="${ctx}/log/list">重置</a>
            </div>
        </form>
    </div>
</div>

<div class="card">
    <div class="card-header bg-white fw-semibold">操作记录</div>
    <div class="table-responsive">
        <table class="table table-hover mb-0">
            <thead>
            <tr>
                <th style="width:170px">时间</th>
                <th>操作账号</th>
                <th>模块</th>
                <th>操作内容</th>
                <th style="width:140px">来源IP</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${pageInfo.list}" var="l">
                <tr>
                    <td class="text-muted small">
                        <fmt:formatDate value="${l.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                    </td>
                    <td class="fw-semibold">${l.username}</td>
                    <td><span class="badge bg-primary-subtle text-primary-emphasis">${l.module}</span></td>
                    <td>${l.operation}</td>
                    <td class="text-muted small">${empty l.ip ? '—' : l.ip}</td>
                </tr>
            </c:forEach>
            <c:if test="${empty pageInfo.list}">
                <tr>
                    <td colspan="5" class="text-center text-muted py-4">暂无日志记录</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
    <div class="card-body">
        <%@ include file="/WEB-INF/views/common/pagination.jsp" %>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
