<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglib.jsp" %>
<c:set var="pageTitle" value="成绩统计"/>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div class="card mb-3">
    <div class="card-body">
        <form class="row g-2 align-items-end" method="get" action="${ctx}/score/stats">
            <div class="col-6 col-md-2">
                <label class="form-label small text-muted mb-1">班级</label>
                <select class="form-select form-select-sm" name="classId">
                    <c:forEach items="${classes}" var="cl">
                        <option value="${cl.id}" ${classId == cl.id ? 'selected' : ''}>${cl.className}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-6 col-md-3">
                <label class="form-label small text-muted mb-1">分数段分布课程</label>
                <select class="form-select form-select-sm" name="courseId">
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
                <button class="btn btn-sm btn-primary w-100" type="submit">
                    <i class="bi bi-bar-chart"></i> 统计
                </button>
            </div>
        </form>
    </div>
</div>

<div class="row g-3">
    <div class="col-lg-6">
        <div class="card h-100">
            <div class="card-header bg-white fw-semibold">分数段分布</div>
            <div class="card-body">
                <div id="distChart" style="height: 320px;"></div>
            </div>
        </div>
    </div>
    <div class="col-lg-6">
        <div class="card h-100">
            <div class="card-header bg-white fw-semibold">课程成绩分析</div>
            <div class="table-responsive">
                <table class="table table-sm table-hover mb-0">
                    <thead>
                    <tr>
                        <th>课程</th>
                        <th>教师</th>
                        <th class="text-center">人数</th>
                        <th class="text-center">平均分</th>
                        <th class="text-center">及格率</th>
                        <th class="text-center">优秀率</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${courseStats}" var="cs">
                        <tr>
                            <td>${cs.courseName}</td>
                            <td>${cs.teacherName}</td>
                            <td class="text-center">${cs.studentCount}</td>
                            <td class="text-center">${empty cs.avgScore ? '—' : cs.avgScore}</td>
                            <td class="text-center">${empty cs.passRate ? '—' : cs.passRate}%</td>
                            <td class="text-center">${empty cs.excellentRate ? '—' : cs.excellentRate}%</td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty courseStats}">
                        <tr>
                            <td colspan="6" class="text-center text-muted py-4">暂无数据</td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<div class="card mt-3">
    <div class="card-header bg-white fw-semibold">学生成绩排名</div>
    <div class="table-responsive">
        <table class="table table-hover mb-0">
            <thead>
            <tr>
                <th style="width:70px">排名</th>
                <th>学号</th>
                <th>姓名</th>
                <th class="text-center">课程数</th>
                <th class="text-center">平均分</th>
                <th class="text-center">最高分</th>
                <th class="text-center">最低分</th>
                <th class="text-center">及格门数</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${studentStats}" var="st" varStatus="vs">
                <tr>
                    <td><span class="badge bg-secondary">${vs.index + 1}</span></td>
                    <td>${st.studentNo}</td>
                    <td>${st.studentName}</td>
                    <td class="text-center">${st.courseCount}</td>
                    <td class="text-center fw-semibold">${empty st.avgScore ? '—' : st.avgScore}</td>
                    <td class="text-center">${empty st.maxScore ? '—' : st.maxScore}</td>
                    <td class="text-center">${empty st.minScore ? '—' : st.minScore}</td>
                    <td class="text-center">${st.passCount}</td>
                </tr>
            </c:forEach>
            <c:if test="${empty studentStats}">
                <tr>
                    <td colspan="8" class="text-center text-muted py-4">暂无数据</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

<script src="${ctx}/static/js/echarts.min.js"></script>
<script>
    (function () {
        var chartDom = document.getElementById('distChart');
        if (!chartDom || typeof echarts === 'undefined') {
            return;
        }
        var data = [
            <c:forEach items="${distribution}" var="d" varStatus="vs">
            {name: '${d.segment}', value: ${d.cnt}}<c:if test="${not vs.last}">,</c:if>
            </c:forEach>
        ];
        var chart = echarts.init(chartDom);
        chart.setOption({
            tooltip: {trigger: 'axis'},
            grid: {left: '3%', right: '4%', bottom: '3%', top: 30, containLabel: true},
            xAxis: {type: 'category', data: data.map(function (d) { return d.name; })},
            yAxis: {type: 'value', name: '人数', minInterval: 1},
            series: [{
                type: 'bar',
                barWidth: '46%',
                itemStyle: {borderRadius: [6, 6, 0, 0], color: '#4d8dff'},
                data: data.map(function (d) { return d.value; })
            }]
        });
        window.addEventListener('resize', function () {
            chart.resize();
        });
    })();
</script>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
