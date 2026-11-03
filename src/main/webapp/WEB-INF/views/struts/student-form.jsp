<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglib.jsp" %>
<c:set var="isEdit" value="${not empty student.id}"/>
<c:set var="pageTitle" value="${isEdit ? 'Struts2 · 编辑学生' : 'Struts2 · 新增学生'}"/>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div class="card" style="max-width: 720px;">
    <div class="card-header bg-white fw-semibold">${pageTitle}</div>
    <div class="card-body">
        <c:if test="${not empty error}">
            <div class="alert alert-danger py-2">${error}</div>
        </c:if>

        <form method="post" action="${ctx}/student/save.action">
            <c:if test="${isEdit}">
                <input type="hidden" name="id" value="${student.id}">
            </c:if>
            <div class="row g-3">
                <div class="col-md-6">
                    <label class="form-label">学号 <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" name="student.studentNo"
                           value="${student.studentNo}" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label">姓名 <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" name="student.name" value="${student.name}" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label">性别</label>
                    <select class="form-select" name="student.gender">
                        <option value="男" ${student.gender eq '女' ? '' : 'selected'}>男</option>
                        <option value="女" ${student.gender eq '女' ? 'selected' : ''}>女</option>
                    </select>
                </div>
                <div class="col-md-6">
                    <label class="form-label">出生日期</label>
                    <input type="date" class="form-control" name="birthDate" value="${birthDate}">
                </div>
                <div class="col-md-6">
                    <label class="form-label">所属班级 <span class="text-danger">*</span></label>
                    <select class="form-select" name="student.classId" required>
                        <c:forEach items="${classes}" var="cl" varStatus="vs">
                            <option value="${cl.id}"
                                    ${student.classId == cl.id or (empty student.classId and vs.first) ? 'selected' : ''}>
                                    ${cl.className}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-6">
                    <label class="form-label">联系电话</label>
                    <input type="text" class="form-control" name="student.phone" value="${student.phone}">
                </div>
                <div class="col-md-6">
                    <label class="form-label">状态</label>
                    <select class="form-select" name="student.status">
                        <option value="1" ${student.status == 0 ? '' : 'selected'}>在读</option>
                        <option value="0" ${student.status == 0 ? 'selected' : ''}>离校</option>
                    </select>
                </div>
            </div>

            <div class="mt-4 d-flex gap-2">
                <button type="submit" class="btn btn-primary">保存</button>
                <a class="btn btn-outline-secondary" href="${ctx}/student/list.action">返回列表</a>
            </div>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
