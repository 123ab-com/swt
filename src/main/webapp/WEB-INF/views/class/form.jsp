<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglib.jsp" %>
<c:set var="isEdit" value="${not empty classInfo.id}"/>
<c:set var="pageTitle" value="${isEdit ? '编辑班级' : '新增班级'}"/>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div class="card" style="max-width: 680px;">
    <div class="card-header bg-white fw-semibold">${pageTitle}</div>
    <div class="card-body">
        <c:if test="${not empty error}">
            <div class="alert alert-danger py-2">${error}</div>
        </c:if>

        <form method="post" action="${ctx}/class/save">
            <input type="hidden" name="id" value="${classInfo.id}">
            <div class="row g-3">
                <div class="col-md-6">
                    <label class="form-label">班级名称 <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" name="className" value="${classInfo.className}" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label">年级</label>
                    <input type="text" class="form-control" name="grade" value="${classInfo.grade}" placeholder="如 2024级">
                </div>
                <div class="col-md-6">
                    <label class="form-label">专业</label>
                    <input type="text" class="form-control" name="major" value="${classInfo.major}">
                </div>
                <div class="col-md-6">
                    <label class="form-label">班主任</label>
                    <select class="form-select" name="headTeacherId">
                        <option value="">请选择</option>
                        <c:forEach items="${teachers}" var="t">
                            <option value="${t.id}" ${classInfo.headTeacherId == t.id ? 'selected' : ''}>${t.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-12">
                    <label class="form-label">备注</label>
                    <textarea class="form-control" name="remark" rows="2">${classInfo.remark}</textarea>
                </div>
            </div>

            <div class="mt-4 d-flex gap-2">
                <button type="submit" class="btn btn-primary">保存</button>
                <a class="btn btn-outline-secondary" href="${ctx}/class/list">返回列表</a>
            </div>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
