<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglib.jsp" %>
<c:set var="pageTitle" value="课程管理"/>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<c:if test="${not empty msg}">
    <div class="alert alert-success py-2">${msg}</div>
</c:if>

<div class="card">
    <div class="card-header bg-white d-flex justify-content-between align-items-center">
        <span class="fw-semibold">课程列表</span>
        <a class="btn btn-sm btn-success" href="${ctx}/course/add"><i class="bi bi-plus-lg"></i> 新增课程</a>
    </div>
    <div class="table-responsive">
        <table class="table table-hover mb-0">
            <thead>
            <tr>
                <th>课程编号</th>
                <th>课程名称</th>
                <th class="text-center">学分</th>
                <th>授课教师</th>
                <th>开课学期</th>
                <th class="text-end">操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${courses}" var="c">
                <tr>
                    <td>${c.courseNo}</td>
                    <td class="fw-semibold">${c.courseName}</td>
                    <td class="text-center">${c.credit}</td>
                    <td>${c.teacherName}</td>
                    <td>${c.semester}</td>
                    <td class="text-end">
                        <a class="btn btn-sm btn-outline-primary" href="${ctx}/course/edit?id=${c.id}">编辑</a>
                        <button class="btn btn-sm btn-outline-danger"
                                onclick="delCourse(${c.id}, '${c.courseName}')">删除</button>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty courses}">
                <tr>
                    <td colspan="6" class="text-center text-muted py-4">暂无课程数据</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

<script>
    function delCourse(id, name) {
        confirmPost('${ctx}/course/delete', {id: id}, '确定删除课程【' + name + '】吗？');
    }
</script>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
