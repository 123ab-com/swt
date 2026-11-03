<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglib.jsp" %>
<c:set var="pageTitle" value="我的成绩"/>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div class="row g-3 mb-3">
    <div class="col-lg-6">
        <div class="card h-100">
            <div class="card-body">
                <div class="d-flex justify-content-between flex-wrap gap-3">
                    <div>
                        <div class="text-muted small">学生</div>
                        <div class="fs-5 fw-semibold">${student.name}</div>
                    </div>
                    <div>
                        <div class="text-muted small">学号</div>
                        <div class="fs-6">${student.studentNo}</div>
                    </div>
                    <div>
                        <div class="text-muted small">班级</div>
                        <div class="fs-6">${student.className}</div>
                    </div>
                    <div>
                        <div class="text-muted small">联系电话</div>
                        <div class="fs-6">${empty student.phone ? '—' : student.phone}</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="col-6 col-lg-2">
        <div class="card stat-card h-100">
            <div class="card-body">
                <div class="stat-label">算术平均分</div>
                <div class="stat-value">${empty avgScore ? '—' : avgScore}</div>
            </div>
        </div>
    </div>
    <div class="col-6 col-lg-2">
        <div class="card stat-card h-100">
            <div class="card-body">
                <div class="stat-label">学分加权平均分</div>
                <div class="stat-value text-primary">${empty weightedAvg ? '—' : weightedAvg}</div>
            </div>
        </div>
    </div>
    <div class="col-6 col-lg-2">
        <div class="card stat-card h-100">
            <div class="card-body">
                <div class="stat-label">班级排名</div>
                <div class="stat-value">${rank == 0 ? '—' : rank}<span class="fs-6 text-muted"> / ${classTotal}</span></div>
            </div>
        </div>
    </div>
</div>

<div class="card">
    <div class="card-header bg-white d-flex justify-content-between align-items-center flex-wrap gap-2">
        <span class="fw-semibold">成绩明细</span>
        <form class="d-flex gap-2" method="get" action="${ctx}/my/score">
            <select class="form-select form-select-sm" name="semester" style="width: 180px;">
                <option value="">全部学期</option>
                <c:forEach items="${semesters}" var="sm">
                    <option value="${sm}" ${semester eq sm ? 'selected' : ''}>${sm}</option>
                </c:forEach>
            </select>
            <button class="btn btn-sm btn-primary" type="submit">筛选</button>
        </form>
    </div>
    <div class="table-responsive">
        <table class="table table-hover mb-0">
            <thead>
            <tr>
                <th>课程</th>
                <th class="text-center">平时成绩</th>
                <th class="text-center">期末成绩</th>
                <th class="text-center">总评成绩</th>
                <th class="text-center">是否及格</th>
                <th>学期</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${scores}" var="s">
                <tr>
                    <td>${s.courseName}</td>
                    <td class="text-center">${s.usualScore}</td>
                    <td class="text-center">${s.examScore}</td>
                    <td class="text-center fw-semibold">${s.totalScore}</td>
                    <td class="text-center">
                        <c:choose>
                            <c:when test="${s.totalScore >= 60}">
                                <span class="badge bg-success-subtle text-success-emphasis">及格</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-danger-subtle text-danger-emphasis">不及格</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td class="text-muted small">${s.semester}</td>
                </tr>
            </c:forEach>
            <c:if test="${empty scores}">
                <tr>
                    <td colspan="6" class="text-center text-muted py-4">暂无成绩记录</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
