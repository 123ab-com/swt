<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglib.jsp" %>
<c:set var="pageTitle" value="Struts2 · 学生管理"/>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div class="card mb-3">
    <div class="card-body">
        <form class="row g-2 align-items-end" method="get" action="${ctx}/student/list.action">
            <div class="col-6 col-md-3">
                <label class="form-label small text-muted mb-1">关键字</label>
                <input type="text" class="form-control form-control-sm" name="keyword"
                       value="${keyword}" placeholder="学号 / 姓名">
            </div>
            <div class="col-6 col-md-2">
                <label class="form-label small text-muted mb-1">班级</label>
                <select class="form-select form-select-sm" name="classId">
                    <option value="">全部班级</option>
                    <c:forEach items="${classes}" var="cl">
                        <option value="${cl.id}" ${classId == cl.id ? 'selected' : ''}>${cl.className}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-6 col-md-2">
                <label class="form-label small text-muted mb-1">性别</label>
                <select class="form-select form-select-sm" name="gender">
                    <option value="">全部</option>
                    <option value="男" ${gender eq '男' ? 'selected' : ''}>男</option>
                    <option value="女" ${gender eq '女' ? 'selected' : ''}>女</option>
                </select>
            </div>
            <div class="col-6 col-md-2">
                <label class="form-label small text-muted mb-1">状态</label>
                <select class="form-select form-select-sm" name="status">
                    <option value="">全部</option>
                    <option value="1" ${status == 1 ? 'selected' : ''}>在读</option>
                    <option value="0" ${status == 0 ? 'selected' : ''}>离校</option>
                </select>
            </div>
            <div class="col-12 col-md-3 d-flex gap-2">
                <button class="btn btn-sm btn-primary" type="submit"><i class="bi bi-search"></i> 查询</button>
                <a class="btn btn-sm btn-outline-secondary" href="${ctx}/student/list.action">重置</a>
                <a class="btn btn-sm btn-success ms-auto" href="${ctx}/student/input.action">
                    <i class="bi bi-plus-lg"></i> 新增学生
                </a>
            </div>
        </form>
    </div>
</div>

<c:if test="${not empty msg}">
    <div class="alert alert-success py-2">${msg}</div>
</c:if>

<div class="card">
    <div class="card-header bg-white d-flex justify-content-between align-items-center">
        <span class="fw-semibold">学生列表（Struts2）</span>
        <span class="text-muted small">请求：/student/list.action</span>
    </div>
    <div class="table-responsive">
        <table class="table table-hover mb-0">
            <thead>
            <tr>
                <th>学号</th>
                <th>姓名</th>
                <th>性别</th>
                <th>出生日期</th>
                <th>班级</th>
                <th>联系电话</th>
                <th>状态</th>
                <th class="text-end">操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${pageInfo.list}" var="s">
                <tr>
                    <td>${s.studentNo}</td>
                    <td>${s.name}</td>
                    <td>${s.gender}</td>
                    <td><fmt:formatDate value="${s.birthDate}" pattern="yyyy-MM-dd"/></td>
                    <td>${s.className}</td>
                    <td>${s.phone}</td>
                    <td>
                        <c:choose>
                            <c:when test="${s.status == 1}">
                                <span class="badge bg-success-subtle text-success-emphasis">在读</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-secondary-subtle text-secondary-emphasis">离校</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td class="text-end">
                        <a class="btn btn-sm btn-outline-primary" href="${ctx}/student/input.action?id=${s.id}">编辑</a>
                        <a class="btn btn-sm btn-outline-danger" href="${ctx}/student/delete.action?id=${s.id}"
                           onclick="return confirm('确定删除学生【${s.name}】及其全部成绩吗？')">删除</a>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty pageInfo.list}">
                <tr>
                    <td colspan="8" class="text-center text-muted py-4">暂无学生数据</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
    <div class="card-body pt-0">
        <%@ include file="/WEB-INF/views/common/pagination.jsp" %>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
