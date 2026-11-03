<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglib.jsp" %>
<c:set var="pageTitle" value="班级管理"/>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<c:if test="${not empty msg}">
    <div class="alert alert-success py-2">${msg}</div>
</c:if>

<div class="card">
    <div class="card-header bg-white d-flex justify-content-between align-items-center">
        <span class="fw-semibold">班级列表</span>
        <a class="btn btn-sm btn-success" href="${ctx}/class/add"><i class="bi bi-plus-lg"></i> 新增班级</a>
    </div>
    <div class="table-responsive">
        <table class="table table-hover mb-0">
            <thead>
            <tr>
                <th>班级名称</th>
                <th>年级</th>
                <th>专业</th>
                <th>班主任</th>
                <th class="text-center">学生人数</th>
                <th>备注</th>
                <th class="text-end">操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${classes}" var="cl">
                <tr>
                    <td class="fw-semibold">${cl.className}</td>
                    <td>${cl.grade}</td>
                    <td>${cl.major}</td>
                    <td>${cl.headTeacherName}</td>
                    <td class="text-center">${cl.studentCount}</td>
                    <td class="text-muted small">${cl.remark}</td>
                    <td class="text-end">
                        <a class="btn btn-sm btn-outline-primary" href="${ctx}/class/edit?id=${cl.id}">编辑</a>
                        <button class="btn btn-sm btn-outline-danger"
                                onclick="delClass(${cl.id}, '${cl.className}')">删除</button>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty classes}">
                <tr>
                    <td colspan="7" class="text-center text-muted py-4">暂无班级数据</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

<script>
    function delClass(id, name) {
        confirmPost('${ctx}/class/delete', {id: id}, '确定删除班级【' + name + '】吗？');
    }
</script>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
