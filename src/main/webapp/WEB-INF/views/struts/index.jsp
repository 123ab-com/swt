<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglib.jsp" %>
<c:set var="pageTitle" value="Struts2 演示"/>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div class="card mb-3">
    <div class="card-header bg-white fw-semibold">Struts2 与 SpringMVC 共存说明</div>
    <div class="card-body">
        <p class="mb-2">
            本项目同时提供两套 Web 层，二者共用同一套 <b>Service / MyBatis</b> 业务代码，互不干扰：
        </p>
        <ul class="mb-3">
            <li><b>SpringMVC</b>：<code>DispatcherServlet</code> 映射 <code>/</code>，处理除 <code>*.action</code> 之外的所有请求。</li>
            <li><b>Struts2</b>：<code>StrutsPrepareAndExecuteFilter</code> 映射 <code>*.action</code>，Action 由 Spring 容器创建（<code>struts2-spring-plugin</code>）。</li>
            <li>登录态共用同一个 <code>Session</code>，Struts2 侧由 <code>StrutsLoginInterceptor</code> 校验。</li>
        </ul>
        <div class="d-grid gap-2 d-md-flex">
            <a class="btn btn-primary" href="${ctx}/student/list.action">
                <i class="bi bi-people"></i> Struts2 学生管理
            </a>
            <a class="btn btn-success" href="${ctx}/score/list.action">
                <i class="bi bi-search"></i> Struts2 成绩查询
            </a>
            <a class="btn btn-outline-secondary" href="${ctx}/dashboard">
                <i class="bi bi-arrow-left"></i> 返回 SpringMVC 工作台
            </a>
        </div>
    </div>
</div>

<div class="card">
    <div class="card-header bg-white fw-semibold">已接入的 Struts2 功能</div>
    <div class="table-responsive">
        <table class="table table-hover mb-0">
            <thead>
            <tr>
                <th>请求 URL</th>
                <th>Action</th>
                <th>方法</th>
                <th>说明</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td><code>/struts/index.action</code></td>
                <td>ActionSupport（默认类）</td>
                <td>execute</td>
                <td>演示首页</td>
            </tr>
            <tr>
                <td><code>/student/list.action</code></td>
                <td>studentAction</td>
                <td>list</td>
                <td>分页 + 多条件查询</td>
            </tr>
            <tr>
                <td><code>/student/input.action</code></td>
                <td>studentAction</td>
                <td>input</td>
                <td>新增 / 编辑表单</td>
            </tr>
            <tr>
                <td><code>/student/save.action</code></td>
                <td>studentAction</td>
                <td>save</td>
                <td>保存后重定向到列表（Session 闪存提示）</td>
            </tr>
            <tr>
                <td><code>/student/delete.action</code></td>
                <td>studentAction</td>
                <td>delete</td>
                <td>删除学生及其成绩</td>
            </tr>
            <tr>
                <td><code>/score/list.action</code></td>
                <td>scoreAction</td>
                <td>list</td>
                <td>成绩查询 + 汇总统计</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
