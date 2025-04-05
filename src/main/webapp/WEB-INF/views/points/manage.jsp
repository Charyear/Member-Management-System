<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>积分管理 - 会员管理系统</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
    <style>
        html, body {
            height: 100%;
            margin: 0;
            overflow: hidden;
        }

        .container {
            height: calc(100vh - 56px); /* 减去顶部导航栏的高度 */
            overflow: hidden;
            padding: 1rem;
        }

        .points-card {
            height: 100%;
            display: flex;
            flex-direction: column;
            background: #fff;
            border-radius: 15px;
            box-shadow: 0 2px 15px rgba(0, 0, 0, 0.1);
            overflow: hidden;
        }
        
        .table-container {
            flex: 1;
            overflow: hidden;
            min-height: 0;
        }
        
        .points-table {
            margin-bottom: 0;
            height: 100%;
            table-layout: fixed;
        }
        
        .points-header {
            background: linear-gradient(45deg, #2196F3, #3F51B5);
            padding: 1.5rem;
            margin-bottom: 0;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        
        .points-header h2 {
            color: white;
            margin: 0;
            display: flex;
            align-items: center;
            font-size: 1.5rem;
        }
        
        .search-box {
            background: rgba(255, 255, 255, 0.2);
            border-radius: 50px;
            padding: 0.5rem 1rem;
            display: flex;
            align-items: center;
            transition: all 0.3s ease;
        }
        
        .search-box:focus-within {
            background: rgba(255, 255, 255, 0.3);
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        
        .search-box input {
            background: transparent;
            border: none;
            color: white;
            padding: 0.25rem 0.5rem;
            width: 200px;
        }
        
        .search-box input::placeholder {
            color: rgba(255, 255, 255, 0.8);
        }
        
        .search-box input:focus {
            outline: none;
        }
        
        .search-box i {
            color: rgba(255, 255, 255, 0.8);
        }
        
        .points-table th {
            background-color: #f8f9fa;
            border-bottom: 2px solid #dee2e6;
            color: #495057;
            font-weight: 600;
            text-transform: uppercase;
            font-size: 0.85rem;
            padding: 1rem;
            text-align: center;  /* 所有表头居中对齐 */
        }

        .points-table td {
            padding: 1rem;
            vertical-align: middle;
            color: #495057;
            text-align: center;  /* 所有单元格居中对齐 */
        }
        
        .points-table th:first-child {
            padding-left: 2rem;
        }
        
        .points-table td:first-child {
            padding-left: 2rem;
        }
        
        .points-table tr:nth-child(even) {
            background-color: #f8f9fa;
        }
        
        .points-table tr:hover {
            background-color: #f1f3f5;
            transition: background-color 0.2s ease;
        }
        
        .member-id {
            font-family: monospace;
            font-weight: 600;
            color: #2196F3;
        }
        
        .member-type {
            display: inline-block;
            padding: 0.25rem 0.75rem;
            border-radius: 50px;
            font-size: 0.85rem;
            font-weight: 600;
        }
        
        .member-type.gold {
            background-color: #ffd700;
            color: #856404;
        }
        
        .member-type.silver {
            background-color: #C0C0C0;
            color: #666;
        }
        
        .points-value {
            font-weight: 600;
            color: #28a745;
            font-size: 1.1rem;
        }
        
        .consumption-value {
            color: #6c757d;
        }
        
        .btn-points {
            padding: 0.375rem 1rem;
            font-size: 0.875rem;
            border-radius: 50px;
            transition: all 0.2s ease;
            margin: 0 0.25rem;
        }
        
        .btn-points:hover {
            transform: translateY(-1px);
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
        }
        
        .pagination {
            margin-top: 1rem;
            justify-content: center;
        }
        
        .pagination .page-link {
            border-radius: 50%;
            margin: 0 0.25rem;
            width: 35px;
            height: 35px;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #2196F3;
            border: none;
            background-color: #fff;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }
        
        .pagination .page-link:hover {
            background-color: #2196F3;
            color: #fff;
        }
        
        .pagination .page-item.active .page-link {
            background-color: #2196F3;
            color: #fff;
        }
        
        .table-footer {
            background-color: #f8f9fa;
            padding: 1rem;
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-top: 1px solid #dee2e6;
        }
        
        .table-info {
            color: #6c757d;
            font-size: 0.875rem;
        }

        /* 积分更新模态框样式 */
        .modal-content {
            border-radius: 15px;
            box-shadow: 0 2px 15px rgba(0, 0, 0, 0.1);
        }

        .modal-header {
            background: linear-gradient(45deg, #2196F3, #3F51B5);
            color: white;
            border-top-left-radius: 15px;
            border-top-right-radius: 15px;
        }

        .modal-title {
            font-size: 1.25rem;
            font-weight: 600;
        }

        .modal-body {
            padding: 2rem;
        }

        .form-label {
            font-weight: 600;
            color: #495057;
        }

        .form-control {
            border-radius: 8px;
            border: 1px solid #ced4da;
            padding: 0.75rem;
        }

        .form-control:focus {
            border-color: #2196F3;
            box-shadow: 0 0 0 0.2rem rgba(33, 150, 243, 0.25);
        }

        .btn-update {
            background: #2196F3;
            color: white;
            border: none;
            padding: 0.75rem 1.5rem;
            border-radius: 50px;
            font-weight: 600;
            transition: all 0.3s ease;
        }

        .btn-update:hover {
            background: #1976D2;
            transform: translateY(-1px);
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
        }
    </style>
</head>
<body>
    <jsp:include page="../layout/header.jsp" />
    
    <div class="container py-4">
        <div class="points-card">
            <div class="points-header">
                <h2>
                    <i class="fas fa-star me-2"></i>积分管理
                </h2>
                <div class="search-box">
                    <i class="fas fa-search me-2"></i>
                    <input type="text" id="searchInput" placeholder="搜索会员ID或姓名" 
                           onkeyup="searchTable()">
                </div>
            </div>
            
            <div class="table-container">
                <table class="table points-table" id="pointsTable">
                    <thead>
                        <tr>
                            <th>会员ID</th>
                            <th>姓名</th>
                            <th>会员类型</th>
                            <th>当前积分</th>
                            <th>消费金额</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${members}" var="member">
                            <tr>
                                <td><span class="member-id">${member.memberId}</span></td>
                                <td>${member.name}</td>
                                <td>
                                    <span class="member-type ${member.type eq '金' ? 'gold' : 'silver'}">
                                        ${member.type}卡会员
                                    </span>
                                </td>
                                <td><span class="points-value">${member.points}</span></td>
                                <td>
                                    <span class="consumption-value">
                                        ¥<fmt:formatNumber value="${member.consumption}" pattern="#,##0.00"/>
                                    </span>
                                </td>
                                <td class="text-center">
                                    <button class="btn btn-success btn-points" 
                                            onclick="showUpdateModal('${member.memberId}', '${member.name}')">
                                        <i class="fas fa-sync-alt me-1"></i>更新积分
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            
            <div class="table-footer">
                <div class="table-info">
                    总会员：${totalMembers} 人 | 第 ${currentPage} / ${totalPages} 页
                </div>
                <nav aria-label="Page navigation">
                    <ul class="pagination mb-0">
                        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                            <a class="page-link" href="?page=${currentPage - 1}" aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>
                        <li class="page-item active">
                            <span class="page-link">${currentPage}</span>
                        </li>
                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                            <a class="page-link" href="?page=${currentPage + 1}" aria-label="Next">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>
                    </ul>
                </nav>
            </div>
        </div>
    </div>

    <!-- 积分更新模态框 -->
    <div class="modal fade" id="updatePointsModal" tabindex="-1" aria-labelledby="updatePointsModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="updatePointsModalLabel">更新会员积分</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="updatePointsForm" action="${pageContext.request.contextPath}/points/update" method="post">
                        <input type="hidden" id="memberId" name="memberId">
                        <div class="mb-3">
                            <label class="form-label">会员姓名</label>
                            <input type="text" class="form-control" id="memberName" readonly>
                        </div>
                        <div class="mb-3">
                            <label for="spendAmount" class="form-label">消费金额</label>
                            <input type="number" class="form-control" id="spendAmount" name="spendAmount" 
                                   step="0.01" min="0" required>
                        </div>
                        <div class="text-end">
                            <button type="submit" class="btn btn-update">
                                <i class="fas fa-check me-1"></i>确认更新
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    
    <jsp:include page="../layout/footer.jsp" />
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function searchTable() {
            const input = document.getElementById('searchInput');
            const filter = input.value.toUpperCase();
            const table = document.getElementById('pointsTable');
            const tr = table.getElementsByTagName('tr');
            
            for (let i = 1; i < tr.length; i++) {
                const td = tr[i].getElementsByTagName('td');
                let txtValue = '';
                // 只搜索会员ID和姓名
                for (let j = 0; j < 2; j++) {
                    txtValue += td[j].textContent || td[j].innerText;
                }
                
                if (txtValue.toUpperCase().indexOf(filter) > -1) {
                    tr[i].style.display = '';
                } else {
                    tr[i].style.display = 'none';
                }
            }
        }
        
        function showUpdateModal(memberId, memberName) {
            document.getElementById('memberId').value = memberId;
            document.getElementById('memberName').value = memberName;
            
            // 重置消费金额输入框
            document.getElementById('spendAmount').value = '';
            
            // 显示模态框
            new bootstrap.Modal(document.getElementById('updatePointsModal')).show();
        }
    </script>
</body>
</html>
