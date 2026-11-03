<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglib.jsp" %>
<c:set var="pageTitle" value="工作台"/>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div class="row g-3">
    <div class="col-6 col-lg-3">
        <div class="card stat-card h-100">
            <div class="card-body d-flex justify-content-between align-items-center">
                <div>
                    <div class="stat-label">学生总数</div>
                    <div class="stat-value">${dashboard.studentCount}</div>
                </div>
                <i class="bi bi-people stat-icon text-primary"></i>
            </div>
        </div>
    </div>
    <div class="col-6 col-lg-3">
        <div class="card stat-card h-100">
            <div class="card-body d-flex justify-content-between align-items-center">
                <div>
                    <div class="stat-label">班级数量</div>
                    <div class="stat-value">${dashboard.classCount}</div>
                </div>
                <i class="bi bi-diagram-3 stat-icon text-success"></i>
            </div>
        </div>
    </div>
    <div class="col-6 col-lg-3">
        <div class="card stat-card h-100">
            <div class="card-body d-flex justify-content-between align-items-center">
                <div>
                    <div class="stat-label">课程数量</div>
                    <div class="stat-value">${dashboard.courseCount}</div>
                </div>
                <i class="bi bi-journal-bookmark stat-icon text-warning"></i>
            </div>
        </div>
    </div>
    <div class="col-6 col-lg-3">
        <div class="card stat-card h-100">
            <div class="card-body">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <div class="stat-label">平均分 / 及格率</div>
                        <div class="stat-value">
                            ${empty dashboard.avgScore ? '—' : dashboard.avgScore}
                            <span class="fs-6 text-muted">
                                / ${empty dashboard.passRate ? '—' : dashboard.passRate}%
                            </span>
                        </div>
                    </div>
                    <i class="bi bi-graph-up-arrow stat-icon text-danger"></i>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="row g-3 mt-1">
    <div class="col-lg-7">
        <div class="card h-100">
            <div class="card-header bg-white fw-semibold">成绩排名 TOP5</div>
            <div class="table-responsive">
                <table class="table table-hover mb-0">
                    <thead>
                    <tr>
                        <th style="width:70px">排名</th>
                        <th>学号</th>
                        <th>姓名</th>
                        <th class="text-end">平均分</th>
                        <th class="text-end">最高分</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${dashboard.topStudents}" var="s" varStatus="st">
                        <tr>
                            <td><span class="badge bg-secondary">${st.index + 1}</span></td>
                            <td>${s.studentNo}</td>
                            <td>${s.studentName}</td>
                            <td class="text-end fw-semibold">${empty s.avgScore ? '—' : s.avgScore}</td>
                            <td class="text-end">${empty s.maxScore ? '—' : s.maxScore}</td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty dashboard.topStudents}">
                        <tr>
                            <td colspan="5" class="text-center text-muted py-4">暂无成绩数据</td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="col-lg-5">
        <div class="card h-100">
            <div class="card-header bg-white fw-semibold">快捷入口</div>
            <div class="card-body d-grid gap-2">
                <a class="btn btn-outline-primary text-start" href="${ctx}/score/entry">
                    <i class="bi bi-pencil-square me-2"></i>录入 / 修改班级成绩
                </a>
                <a class="btn btn-outline-success text-start" href="${ctx}/score/stats">
                    <i class="bi bi-bar-chart me-2"></i>查看成绩统计分析
                </a>
                <a class="btn btn-outline-secondary text-start" href="${ctx}/student/add">
                    <i class="bi bi-person-plus me-2"></i>新增学生档案
                </a>
                <a class="btn btn-outline-secondary text-start" href="${ctx}/course/list">
                    <i class="bi bi-journal-plus me-2"></i>维护课程信息
                </a>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
