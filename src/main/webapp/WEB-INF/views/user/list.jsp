<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglib.jsp" %>
<c:set var="pageTitle" value="账号管理"/>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div class="card mb-3">
    <div class="card-body">
        <form class="row g-2 align-items-end" method="get" action="${ctx}/user/list">
            <div class="col-6 col-md-3">
                <label class="form-label small text-muted mb-1">账号 / 姓名</label>
                <input type="text" class="form-control form-control-sm" name="username"
                       value="${query.username}" placeholder="账号或姓名模糊查询">
            </div>
            <div class="col-6 col-md-2">
                <label class="form-label small text-muted mb-1">角色</label>
                <select class="form-select form-select-sm" name="role">
                    <option value="">全部角色</option>
                    <c:forEach items="${roles}" var="r">
                        <option value="${r}" ${query.role eq r ? 'selected' : ''}>
                            <c:choose>
                                <c:when test="${r eq 'ADMIN'}">管理员</c:when>
                                <c:when test="${r eq 'TEACHER'}">教师</c:when>
                                <c:otherwise>学生</c:otherwise>
                            </c:choose>
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-6 col-md-2">
                <label class="form-label small text-muted mb-1">状态</label>
                <select class="form-select form-select-sm" name="status">
                    <option value="">全部</option>
                    <option value="1" ${query.status == 1 ? 'selected' : ''}>正常</option>
                    <option value="0" ${query.status == 0 ? 'selected' : ''}>禁用</option>
                </select>
            </div>
            <div class="col-12 col-md-5 d-flex gap-2">
                <button class="btn btn-sm btn-primary" type="submit"><i class="bi bi-search"></i> 查询</button>
                <a class="btn btn-sm btn-outline-secondary" href="${ctx}/user/list">重置</a>
                <button class="btn btn-sm btn-outline-warning" type="button"
                        onclick="confirmPost('${ctx}/user/genStudents', {}, '为尚无账号的在读学生批量开通账号（账号=学号，默认口令 123456）？')">
                    <i class="bi bi-person-plus"></i> 批量生成学生账号
                </button>
                <a class="btn btn-sm btn-success ms-auto" href="${ctx}/user/add">
                    <i class="bi bi-plus-lg"></i> 新增账号
                </a>
            </div>
        </form>
    </div>
</div>

<c:if test="${not empty msg}">
    <div class="alert alert-success py-2">${msg}</div>
</c:if>

<div class="card">
    <div class="card-header bg-white fw-semibold">账号列表</div>
    <div class="table-responsive">
        <table class="table table-hover mb-0">
            <thead>
            <tr>
                <th style="width:70px">ID</th>
                <th>登录账号</th>
                <th>姓名</th>
                <th>角色</th>
                <th class="text-center">关联ID</th>
                <th class="text-center">状态</th>
                <th class="text-end">操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${pageInfo.list}" var="u">
                <tr>
                    <td class="text-muted">${u.id}</td>
                    <td class="fw-semibold">${u.username}</td>
                    <td>${u.realName}</td>
                    <td>
                        <c:choose>
                            <c:when test="${u.role eq 'ADMIN'}">
                                <span class="badge bg-danger-subtle text-danger-emphasis">管理员</span>
                            </c:when>
                            <c:when test="${u.role eq 'TEACHER'}">
                                <span class="badge bg-primary-subtle text-primary-emphasis">教师</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-success-subtle text-success-emphasis">学生</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td class="text-center">${empty u.refId ? '—' : u.refId}</td>
                    <td class="text-center">
                        <c:choose>
                            <c:when test="${u.status == 1}">
                                <span class="badge bg-success-subtle text-success-emphasis">正常</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-secondary-subtle text-secondary-emphasis">禁用</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td class="text-end">
                        <a class="btn btn-sm btn-outline-primary" href="${ctx}/user/edit?id=${u.id}">编辑</a>
                        <button class="btn btn-sm btn-outline-warning" type="button"
                                onclick="confirmPost('${ctx}/user/resetPwd', {id: ${u.id}}, '确定将该账号密码重置为 123456 吗？')">
                            重置密码
                        </button>
                        <button class="btn btn-sm btn-outline-danger" type="button"
                                onclick="confirmPost('${ctx}/user/delete', {id: ${u.id}}, '确定删除账号【${u.username}】吗？')">
                            删除
                        </button>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty pageInfo.list}">
                <tr>
                    <td colspan="7" class="text-center text-muted py-4">暂无账号</td>
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
