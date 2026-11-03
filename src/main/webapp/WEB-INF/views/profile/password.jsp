<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglib.jsp" %>
<c:set var="pageTitle" value="修改密码"/>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div class="card" style="max-width: 480px;">
    <div class="card-header bg-white fw-semibold">修改密码</div>
    <div class="card-body">
        <c:if test="${not empty error}">
            <div class="alert alert-danger py-2">${error}</div>
        </c:if>
        <c:if test="${not empty msg}">
            <div class="alert alert-success py-2">${msg}</div>
        </c:if>

        <form method="post" action="${ctx}/profile/password">
            <div class="mb-3">
                <label class="form-label">原密码</label>
                <input type="password" class="form-control" name="oldPassword" required>
            </div>
            <div class="mb-3">
                <label class="form-label">新密码</label>
                <input type="password" class="form-control" name="newPassword" required placeholder="6 ~ 20 位">
            </div>
            <div class="mb-3">
                <label class="form-label">确认新密码</label>
                <input type="password" class="form-control" name="confirmPassword" required>
            </div>
            <button type="submit" class="btn btn-primary">确认修改</button>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
