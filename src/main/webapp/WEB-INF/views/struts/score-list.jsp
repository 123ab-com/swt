<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglib.jsp" %>
<c:set var="pageTitle" value="Struts2 · 成绩查询"/>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div class="card mb-3">
    <div class="card-body">
        <form class="row g-2 align-items-end" method="get" action="${ctx}/score/list.action">
            <div class="col-6 col-md-2">
                <label class="form-label small text-muted mb-1">班级</label>
                <select class="form-select form-select-sm" name="classId">
                    <option value="">全部班级</option>
                    <c:forEach items="${classes}" var="cl">
                        <option value="${cl.id}" ${classId == cl.id ? 'selected' : ''}>${cl.className}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-6 col-md-3">
                <label class="form-label small text-muted mb-1">课程</label>
                <select class="form-select form-select-sm" name="courseId">
                    <option value="">全部课程</option>
                    <c:forEach items="${courses}" var="c">
                        <option value="${c.id}" ${courseId == c.id ? 'selected' : ''}>${c.courseName}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-6 col-md-2">
                <label class="form-label small text-muted mb-1">学期</label>
                <input type="text" class="form-control form-control-sm" name="semester" value="${semester}">
            </div>
            <div class="col-6 col-md-2">
                <label class="form-label small text-muted mb-1">关键字</label>
                <input type="text" class="form-control form-control-sm" name="keyword"
                       value="${keyword}" placeholder="学号 / 姓名">
            </div>
            <div class="col-12 col-md-3 d-flex gap-2">
                <button class="btn btn-sm btn-primary" type="submit"><i class="bi bi-search"></i> 查询</button>
                <a class="btn btn-sm btn-outline-secondary" href="${ctx}/score/list.action">重置</a>
            </div>
        </form>
    </div>
</div>

<div class="row g-3 mb-3">
    <div class="col-6 col-lg-3">
        <div class="card stat-card">
            <div class="card-body">
                <div class="stat-label">平均分</div>
                <div class="stat-value">${empty summary.avgScore ? '—' : summary.avgScore}</div>
            </div>
        </div>
    </div>
    <div class="col-6 col-lg-3">
        <div class="card stat-card">
            <div class="card-body">
                <div class="stat-label">最高分</div>
                <div class="stat-value text-success">${empty summary.maxScore ? '—' : summary.maxScore}</div>
            </div>
        </div>
    </div>
    <div class="col-6 col-lg-3">
        <div class="card stat-card">
            <div class="card-body">
                <div class="stat-label">最低分</div>
                <div class="stat-value text-danger">${empty summary.minScore ? '—' : summary.minScore}</div>
            </div>
        </div>
    </div>
    <div class="col-6 col-lg-3">
        <div class="card stat-card">
            <div class="card-body">
                <div class="stat-label">及格人数 / 参考人数</div>
                <div class="stat-value">${summary.passCount} / ${summary.totalCount}</div>
            </div>
        </div>
    </div>
</div>

<div class="card">
    <div class="card-header bg-white d-flex justify-content-between align-items-center">
        <span class="fw-semibold">成绩列表（Struts2）</span>
        <span class="text-muted small">请求：/score/list.action</span>
    </div>
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
            <c:forEach items="${scores}" var="s">
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
            <c:if test="${empty scores}">
                <tr>
                    <td colspan="8" class="text-center text-muted py-4">暂无成绩数据</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
