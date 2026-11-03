<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglib.jsp" %>
<c:set var="pageTitle" value="成绩录入"/>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div class="card mb-3">
    <div class="card-body">
        <form class="row g-2 align-items-end" method="get" action="${ctx}/score/entry">
            <div class="col-6 col-md-3">
                <label class="form-label small text-muted mb-1">班级</label>
                <select class="form-select form-select-sm" name="classId">
                    <c:forEach items="${classes}" var="cl">
                        <option value="${cl.id}" ${classId == cl.id ? 'selected' : ''}>${cl.className}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-6 col-md-4">
                <label class="form-label small text-muted mb-1">课程</label>
                <select class="form-select form-select-sm" name="courseId">
                    <c:forEach items="${courses}" var="c">
                        <option value="${c.id}" ${courseId == c.id ? 'selected' : ''}>
                            ${c.courseName}（${c.courseNo}）
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-6 col-md-3">
                <label class="form-label small text-muted mb-1">学期</label>
                <input type="text" class="form-control form-control-sm" name="semester"
                       value="${semester}" placeholder="如 2025-2026-1">
            </div>
            <div class="col-6 col-md-2">
                <button class="btn btn-sm btn-primary w-100" type="submit">
                    <i class="bi bi-arrow-repeat"></i> 切换
                </button>
            </div>
        </form>
    </div>
</div>

<div class="card">
    <div class="card-header bg-white d-flex justify-content-between align-items-center flex-wrap gap-2">
        <span class="fw-semibold">成绩录入 <span class="text-muted small">（总评 = 平时 × 30% + 期末 × 70%）</span></span>
        <div class="d-flex gap-2 align-items-center flex-wrap">
            <form id="importForm" class="d-flex gap-2 align-items-center" method="post"
                  action="${ctx}/score/import" enctype="multipart/form-data">
                <input type="file" class="form-control form-control-sm" name="file"
                       accept=".xlsx,.xls" required style="width: 220px;">
                <button class="btn btn-sm btn-outline-primary" type="submit">
                    <i class="bi bi-upload"></i> 导入 Excel
                </button>
            </form>
            <a class="btn btn-sm btn-outline-secondary" href="${ctx}/score/template">
                <i class="bi bi-download"></i> 导入模板
            </a>
            <button class="btn btn-sm btn-success" type="button" onclick="saveScores()">
                <i class="bi bi-save"></i> 保存本次录入
            </button>
        </div>
    </div>
    <div class="table-responsive">
        <table class="table table-hover mb-0" id="scoreTable">
            <thead>
            <tr>
                <th style="width:60px">序号</th>
                <th>学号</th>
                <th>姓名</th>
                <th style="width:160px">平时成绩</th>
                <th style="width:160px">期末成绩</th>
                <th style="width:110px">总评</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${rows}" var="r" varStatus="st">
                <tr data-student-id="${r.studentId}" data-student-no="${r.studentNo}">
                    <td>${st.index + 1}</td>
                    <td>${r.studentNo}</td>
                    <td>${r.studentName}</td>
                    <td>
                        <input type="number" class="form-control form-control-sm usual" min="0" max="100"
                               step="0.1" value="${r.usualScore}" oninput="recalc(this)">
                    </td>
                    <td>
                        <input type="number" class="form-control form-control-sm exam" min="0" max="100"
                               step="0.1" value="${r.examScore}" oninput="recalc(this)">
                    </td>
                    <td class="total fw-semibold">${empty r.totalScore ? '—' : r.totalScore}</td>
                </tr>
            </c:forEach>
            <c:if test="${empty rows}">
                <tr>
                    <td colspan="6" class="text-center text-muted py-4">
                        当前班级暂无学生，请先在【学生管理】中添加学生
                    </td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

<script>
    var courseId = ${empty courseId ? 'null' : courseId};
    var semester = '${semester}';

    function recalc(input) {
        var tr = input.closest('tr');
        var cell = tr.querySelector('.total');
        var usual = parseFloat(tr.querySelector('.usual').value);
        var exam = parseFloat(tr.querySelector('.exam').value);
        if (isNaN(usual) || isNaN(exam)) {
            cell.textContent = '—';
            return;
        }
        cell.textContent = (Math.round((usual * 0.3 + exam * 0.7) * 10) / 10).toFixed(1);
    }

    function collectRows() {
        var rows = [];
        var trs = document.querySelectorAll('#scoreTable tbody tr[data-student-id]');
        for (var i = 0; i < trs.length; i++) {
            var tr = trs[i];
            var usualVal = tr.querySelector('.usual').value.trim();
            var examVal = tr.querySelector('.exam').value.trim();
            if (usualVal === '' && examVal === '') {
                continue;
            }
            if (usualVal === '' || examVal === '') {
                alert('学号 ' + tr.dataset.studentNo + '：平时成绩与期末成绩需同时填写');
                return null;
            }
            var usual = parseFloat(usualVal);
            var exam = parseFloat(examVal);
            if (isNaN(usual) || isNaN(exam) || usual < 0 || usual > 100 || exam < 0 || exam > 100) {
                alert('学号 ' + tr.dataset.studentNo + '：成绩必须在 0~100 之间');
                return null;
            }
            rows.push({
                studentId: parseInt(tr.dataset.studentId, 10),
                courseId: courseId,
                semester: semester,
                usualScore: usual,
                examScore: exam
            });
        }
        return rows;
    }

    document.getElementById('importForm').addEventListener('submit', function (e) {
        e.preventDefault();
        var form = e.target;
        fetch('${ctx}/score/import', {
            method: 'POST',
            body: new FormData(form),
            headers: {'X-Requested-With': 'XMLHttpRequest'}
        }).then(function (resp) {
            if (resp.status === 401) {
                alert('登录已失效，请重新登录');
                window.location.href = '${ctx}/login';
                return null;
            }
            return resp.json();
        }).then(function (res) {
            if (!res) {
                return;
            }
            alert(res.msg);
            if (res.code === 200) {
                window.location.reload();
            }
        });
    });

    function saveScores() {
        if (!courseId) {
            alert('请先选择班级与课程');
            return;
        }
        var rows = collectRows();
        if (rows === null) {
            return;
        }
        if (rows.length === 0) {
            alert('没有需要保存的成绩');
            return;
        }
        postJson('${ctx}/score/save', rows).then(function (res) {
            alert(res.msg);
            if (res.code === 200) {
                window.location.reload();
            }
        });
    }
</script>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
