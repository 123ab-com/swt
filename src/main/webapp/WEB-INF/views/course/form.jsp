<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglib.jsp" %>
<c:set var="isEdit" value="${not empty course.id}"/>
<c:set var="pageTitle" value="${isEdit ? '编辑课程' : '新增课程'}"/>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div class="card" style="max-width: 680px;">
    <div class="card-header bg-white fw-semibold">${pageTitle}</div>
    <div class="card-body">
        <c:if test="${not empty error}">
            <div class="alert alert-danger py-2">${error}</div>
        </c:if>

        <form method="post" action="${ctx}/course/save">
            <input type="hidden" name="id" value="${course.id}">
            <div class="row g-3">
                <div class="col-md-6">
                    <label class="form-label">课程编号 <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" name="courseNo" value="${course.courseNo}" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label">课程名称 <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" name="courseName" value="${course.courseName}" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label">学分</label>
                    <input type="number" step="0.5" min="0" max="20" class="form-control"
                           name="credit" value="${course.credit}">
                </div>
                <div class="col-md-6">
                    <label class="form-label">授课教师</label>
                    <select class="form-select" name="teacherId">
                        <option value="">请选择</option>
                        <c:forEach items="${teachers}" var="t">
                            <option value="${t.id}" ${course.teacherId == t.id ? 'selected' : ''}>${t.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-6">
                    <label class="form-label">开课学期 <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" name="semester" value="${course.semester}"
                           placeholder="如 2025-2026-1" required>
                </div>
            </div>

            <div class="mt-4 d-flex gap-2">
                <button type="submit" class="btn btn-primary">保存</button>
                <a class="btn btn-outline-secondary" href="${ctx}/course/list">返回列表</a>
            </div>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
