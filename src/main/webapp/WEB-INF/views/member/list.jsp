<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../layout/header.jsp" />

<div class="search-box">
    <form method="get" action="${pageContext.request.contextPath}/member/">
        <div class="input-group">
            <input type="text" class="form-control" name="keyword" placeholder="请输入用户名或会员ID" 
                   value="${keyword != null ? keyword : ''}">
            <button type="submit" class="btn btn-primary">搜索</button>
        </div>
    </form>
</div>

<c:if test="${not empty keyword}">
    <div class="alert alert-info mt-3">
        搜索结果：找到 ${members.size()} 个与 "${keyword}" 相关的会员
    </div>
</c:if>

<div class="table-container">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h4 class="mb-0">会员列表</h4>
        <button class="btn btn-primary" onclick="location.href='${pageContext.request.contextPath}/member/add'">
            <i class="fas fa-plus"></i> 添加会员
        </button>
    </div>

    <table class="table table-striped">
        <thead>
            <tr>
                <th>序号</th>
                <th>会员ID</th>
                <th>姓名</th>
                <th>手机号</th>
                <th>会员类型</th>
                <th>消费金额</th>
                <th>积分</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${members}" var="member" varStatus="status">
                <tr>
                    <td>${status.index + 1}</td>
                    <td>${member.memberId}</td>
                    <td>${member.name}</td>
                    <td>${member.phone}</td>
                    <td>${member.type}</td>
                    <td>￥${member.consumption}</td>
                    <td>${member.points}</td>
                    <td>
                        <div class="btn-group">
                            <a href="${pageContext.request.contextPath}/member/edit?member_id=${member.memberId}" 
                               class="btn btn-sm btn-warning">
                                <i class="fas fa-edit"></i> 编辑
                            </a>
                            <a href="${pageContext.request.contextPath}/member/delete?member_id=${member.memberId}" 
                               class="btn btn-sm btn-danger" 
                               onclick="return confirm('确定要删除该会员吗？')">
                                <i class="fas fa-trash"></i> 删除
                            </a>
                        </div>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    
    <!-- 分页导航 -->
    <div class="d-flex justify-content-between align-items-center mt-3">
        <div class="text-muted">
            总共 ${totalMembers} 条记录，共 ${totalPages} 页
        </div>
        <nav aria-label="Page navigation">
            <ul class="pagination mb-0">
                <!-- 上一页 -->
                <li class="page-item ${currentPage <= 1 ? 'disabled' : ''}">
                    <a class="page-link" href="${pageContext.request.contextPath}/member/?page=${currentPage - 1}${not empty keyword ? '&keyword=' : ''}${not empty keyword ? keyword : ''}" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
                
                <!-- 页码 -->
                <c:set var="beginPage" value="${currentPage - 2 < 1 ? 1 : currentPage - 2}" />
                <c:set var="endPage" value="${currentPage + 2 > totalPages ? totalPages : currentPage + 2}" />
                <c:forEach begin="${beginPage}" end="${endPage}" var="i">
                    <li class="page-item ${currentPage == i ? 'active' : ''}">
                        <a class="page-link" href="${pageContext.request.contextPath}/member/?page=${i}${not empty keyword ? '&keyword=' : ''}${not empty keyword ? keyword : ''}">${i}</a>
                    </li>
                </c:forEach>
                
                <!-- 下一页 -->
                <li class="page-item ${currentPage >= totalPages ? 'disabled' : ''}">
                    <a class="page-link" href="${pageContext.request.contextPath}/member/?page=${currentPage + 1}${not empty keyword ? '&keyword=' : ''}${not empty keyword ? keyword : ''}" aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </ul>
        </nav>
    </div>
</div>

<script>
function showUpdateModal(memberId, consumption, points) {
    document.getElementById('updateMemberId').value = memberId;
    document.getElementById('currentConsumption').value = parseFloat(consumption).toFixed(2);
    document.getElementById('currentPoints').value = parseInt(points);
    document.getElementById('spendAmount').value = '';
    
    new bootstrap.Modal(document.getElementById('updatePointsModal')).show();
}

function submitUpdatePoints() {
    const form = document.getElementById('updatePointsForm');
    const spendAmount = document.getElementById('spendAmount').value;
    
    if (!spendAmount || parseFloat(spendAmount) <= 0) {
        alert('请输入有效的消费金额');
        return;
    }
    
    form.submit();
}
</script>

<jsp:include page="../layout/footer.jsp" />
