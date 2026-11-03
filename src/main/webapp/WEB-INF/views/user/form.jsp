<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglib.jsp" %>
<c:set var="isEdit" value="${not empty user.id}"/>
<c:set var="pageTitle" value="${isEdit ? '编辑账号' : '新增账号'}"/>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div class="card" style="max-width: 640px;">
    <div class="card-header bg-white fw-semibold">${pageTitle}</div>
    <div class="card-body">
        <c:if test="${not empty error}">
            <div class="alert alert-danger py-2">${error}</div>
        </c:if>

        <form method="post" action="${ctx}/user/save">
            <c:if test="${isEdit}">
                <input type="hidden" name="id" value="${user.id}">
            </c:if>
            <div class="row g-3">
                <div class="col-md-6">
                    <label class="form-label">登录账号 <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" name="username" value="${user.username}" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label">姓名</label>
                    <input type="text" class="form-control" name="realName" value="${user.realName}">
                </div>
                <div class="col-md-6">
                    <label class="form-label">角色 <span class="text-danger">*</span></label>
                    <select class="form-select" name="role" required>
                        <c:forEach items="${roles}" var="r">
                            <option value="${r}" ${user.role eq r ? 'selected' : ''}>
                                <c:choose>
                                    <c:when test="${r eq 'ADMIN'}">管理员</c:when>
                                    <c:when test="${r eq 'TEACHER'}">教师</c:when>
                                    <c:otherwise>学生</c:otherwise>
                                </c:choose>
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-6">
                    <label class="form-label">状态</label>
                    <select class="form-select" name="status">
                        <option value="1" ${user.status == 0 ? '' : 'selected'}>正常</option>
                        <option value="0" ${user.status == 0 ? 'selected' : ''}>禁用</option>
                    </select>
                </div>
                <div class="col-md-6">
                    <label class="form-label">关联ID</label>
                    <input type="number" class="form-control" name="refId" value="${user.refId}"
                           placeholder="教师填 teacher.id，学生填 student.id">
                    <div class="form-text">学生账号必须正确填写，否则登录后查不到本人成绩。</div>
                </div>
                <div class="col-md-6">
                    <label class="form-label">
                        <c:choose>
                            <c:when test="${isEdit}">重置密码（留空则不修改）</c:when>
                            <c:otherwise>初始密码（留空默认 123456）</c:otherwise>
                        </c:choose>
                    </label>
                    <input type="text" class="form-control" name="password" value="" placeholder="6 ~ 20 位">
                </div>
            </div>

            <div class="mt-4 d-flex gap-2">
                <button type="submit" class="btn btn-primary">保存</button>
                <a class="btn btn-outline-secondary" href="${ctx}/user/list">返回列表</a>
            </div>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
