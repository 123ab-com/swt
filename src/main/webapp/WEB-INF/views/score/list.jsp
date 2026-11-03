<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglib.jsp" %>
<c:set var="pageTitle" value="成绩查询"/>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div class="card mb-3">
    <div class="card-body">
        <form class="row g-2 align-items-end" method="get" action="${ctx}/score/list">
            <div class="col-6 col-md-2">
                <label class="form-label small text-muted mb-1">班级</label>
                <select class="form-select form-select-sm" name="classId">
                    <option value="">全部班级</option>
                    <c:forEach items="${classes}" var="cl">
                        <option value="${cl.id}" ${query.classId == cl.id ? 'selected' : ''}>${cl.className}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-6 col-md-3">
                <label class="form-label small text-muted mb-1">课程</label>
                <select class="form-select form-select-sm" name="courseId">
                    <option value="">全部课程</option>
                    <c:forEach items="${courses}" var="c">
                        <option value="${c.id}" ${query.courseId == c.id ? 'selected' : ''}>${c.courseName}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-6 col-md-2">
                <label class="form-label small text-muted mb-1">学期</label>
                <input type="text" class="form-control form-control-sm" name="semester" value="${query.semester}">
            </div>
            <div class="col-6 col-md-2">
                <label class="form-label small text-muted mb-1">关键字</label>
                <input type="text" class="form-control form-control-sm" name="keyword"
                       value="${query.keyword}" placeholder="学号 / 姓名">
            </div>
            <div class="col-12 col-md-3 d-flex gap-2">
                <button class="btn btn-sm btn-primary" type="submit"><i class="bi bi-search"></i> 查询</button>
                <a class="btn btn-sm btn-outline-secondary" href="${ctx}/score/list">重置</a>
                <c:url var="exportUrl" value="/score/export">
                    <c:if test="${not empty query.classId}"><c:param name="classId" value="${query.classId}"/></c:if>
                    <c:if test="${not empty query.courseId}"><c:param name="courseId" value="${query.courseId}"/></c:if>
                    <c:if test="${not empty query.semester}"><c:param name="semester" value="${query.semester}"/></c:if>
                    <c:if test="${not empty query.keyword}"><c:param name="keyword" value="${query.keyword}"/></c:if>
                </c:url>
                <a class="btn btn-sm btn-outline-success ms-auto" href="${exportUrl}">
                    <i class="bi bi-file-earmark-excel"></i> 导出 Excel
                </a>
            </div>
        </form>
    </div>
</div>

<div class="row g-3 mb-3">
    <div class="col-6 col-lg-3">
        <div class="card stat-card">
            <div class="card-body">
                <div class="stat-label">平均分</div>
                <div class="stat-value">${empty avgScore ? '—' : avgScore}</div>
            </div>
        </div>
    </div>
    <div class="col-6 col-lg-3">
        <div class="card stat-card">
            <div class="card-body">
                <div class="stat-label">最高分</div>
                <div class="stat-value text-success">${empty maxScore ? '—' : maxScore}</div>
            </div>
        </div>
    </div>
    <div class="col-6 col-lg-3">
        <div class="card stat-card">
            <div class="card-body">
                <div class="stat-label">最低分</div>
                <div class="stat-value text-danger">${empty minScore ? '—' : minScore}</div>
            </div>
        </div>
    </div>
    <div class="col-6 col-lg-3">
        <div class="card stat-card">
            <div class="card-body">
                <div class="stat-label">及格率</div>
                <div class="stat-value">${empty passRate ? '—' : passRate}%</div>
                <div class="text-muted small">及格 ${passCount} 人 / 共 ${totalCount} 条</div>
            </div>
        </div>
    </div>
</div>

<div class="card">
    <div class="table-responsive">
        <table class="table table-hover mb-0">
            <thead>
            <tr>
                <th>学号</th>
                <th>姓名</th>
                <th>班级</th>
                <th>课程</th>
                <th class="text-center">平时</th>
                <th class="text-center">期末</th>
                <th class="text-center">总评</th>
                <th>学期</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${pageInfo.list}" var="s">
                <tr>
                    <td>${s.studentNo}</td>
                    <td>${s.studentName}</td>
                    <td>${s.className}</td>
                    <td>${s.courseName}</td>
                    <td class="text-center">${s.usualScore}</td>
                    <td class="text-center">${s.examScore}</td>
                    <td class="text-center fw-semibold">
                        <c:choose>
                            <c:when test="${s.totalScore >= 60}">
                                <span class="text-success">${s.totalScore}</span>
                            </c:when>
                            <c:otherwise>
                                <span class="text-danger">${s.totalScore}</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td class="text-muted small">${s.semester}</td>
                </tr>
            </c:forEach>
            <c:if test="${empty pageInfo.list}">
                <tr>
                    <td colspan="8" class="text-center text-muted py-4">暂无成绩数据</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
    <div class="card-body">
        <%@ include file="/WEB-INF/views/common/pagination.jsp" %>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
