<%@ page pageEncoding="UTF-8" %>
<c:if test="${not empty pageInfo and pageInfo.pages > 0}">
    <div class="d-flex justify-content-between align-items-center mt-3">
        <div class="text-muted small">
            共 ${pageInfo.total} 条记录，第 ${pageInfo.pageNum}/${pageInfo.pages} 页
        </div>
        <c:if test="${pageInfo.pages > 1}">
            <ul class="pagination pagination-sm mb-0">
                <li class="page-item ${pageInfo.pageNum <= 1 ? 'disabled' : ''}">
                    <a class="page-link" href="javascript:goPage(${pageInfo.pageNum - 1})">上一页</a>
                </li>
                <c:forEach begin="1" end="${pageInfo.pages}" var="p">
                    <c:if test="${p >= pageInfo.pageNum - 2 and p <= pageInfo.pageNum + 2}">
                        <li class="page-item ${p == pageInfo.pageNum ? 'active' : ''}">
                            <a class="page-link" href="javascript:goPage(${p})">${p}</a>
                        </li>
                    </c:if>
                </c:forEach>
                <li class="page-item ${pageInfo.pageNum >= pageInfo.pages ? 'disabled' : ''}">
                    <a class="page-link" href="javascript:goPage(${pageInfo.pageNum + 1})">下一页</a>
                </li>
            </ul>
        </c:if>
    </div>
</c:if>
