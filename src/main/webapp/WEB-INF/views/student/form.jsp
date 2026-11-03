<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglib.jsp" %>
<c:set var="isEdit" value="${not empty student.id}"/>
<c:set var="pageTitle" value="${isEdit ? '编辑学生' : '新增学生'}"/>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<fmt:formatDate value="${student.birthDate}" pattern="yyyy-MM-dd" var="birthStr"/>

<div class="card" style="max-width: 720px;">
    <div class="card-header bg-white fw-semibold">${pageTitle}</div>
    <div class="card-body">
        <c:if test="${not empty error}">
            <div class="alert alert-danger py-2">${error}</div>
        </c:if>

        <form method="post" action="${ctx}/student/save">
            <input type="hidden" name="id" value="${student.id}">
            <div class="row g-3">
                <div class="col-md-6">
                    <label class="form-label">学号 <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" name="studentNo" value="${student.studentNo}" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label">姓名 <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" name="name" value="${student.name}" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label">性别</label>
                    <select class="form-select" name="gender">
                        <option value="男" ${student.gender eq '女' ? '' : 'selected'}>男</option>
                        <option value="女" ${student.gender eq '女' ? 'selected' : ''}>女</option>
                    </select>
                </div>
                <div class="col-md-6">
                    <label class="form-label">出生日期</label>
                    <input type="date" class="form-control" name="birthDate" value="${birthStr}">
                </div>
                <div class="col-md-6">
                    <label class="form-label">所属班级 <span class="text-danger">*</span></label>
                    <select class="form-select" name="classId" required>
                        <option value="">请选择</option>
                        <c:forEach items="${classes}" var="cl">
                            <option value="${cl.id}" ${student.classId == cl.id ? 'selected' : ''}>${cl.className}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-6">
                    <label class="form-label">联系电话</label>
                    <input type="text" class="form-control" name="phone" value="${student.phone}">
                </div>
                <div class="col-md-6">
                    <label class="form-label">状态</label>
                    <select class="form-select" name="status">
                        <option value="1" ${student.status == 0 ? '' : 'selected'}>在读</option>
                        <option value="0" ${student.status == 0 ? 'selected' : ''}>离校</option>
                    </select>
                </div>
            </div>

            <div class="mt-4 d-flex gap-2">
                <button type="submit" class="btn btn-primary">保存</button>
                <a class="btn btn-outline-secondary" href="${ctx}/student/list">返回列表</a>
            </div>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
