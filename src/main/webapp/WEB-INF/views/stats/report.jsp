<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="../layout/header.jsp" />

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>统计报表</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .stats-card {
            border: none;
            border-radius: 15px;
            height: 160px;
            margin-bottom: 20px;
            transition: transform 0.2s, box-shadow 0.2s;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }
        
        .stats-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
        }
        
        .stats-card .card-body {
            display: flex;
            flex-direction: column;
            padding: 1.25rem;
        }
        
        .stats-card h6.card-subtitle {
            font-size: 0.9rem;
            opacity: 0.85;
            margin-bottom: 0.75rem;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }
        
        .stats-card .number-display {
            font-size: 2.25rem;
            font-weight: 600;
            margin: 0.25rem 0;
            line-height: 1.2;
        }
        
        .stats-card .description {
            font-size: 0.85rem;
            opacity: 0.85;
            margin-top: auto;
        }
        
        .stats-distribution {
            flex-grow: 1;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
        }
        
        .member-progress {
            margin-bottom: 0.75rem;
        }
        
        .member-progress:last-child {
            margin-bottom: 0;
        }
        
        .member-progress .fw-bold {
            font-size: 0.85rem;
            opacity: 0.9;
        }
        
        .custom-progress {
            height: 6px;
            border-radius: 3px;
            border: none;
            background-color: rgba(255, 255, 255, 0.2);
            margin-top: 0.35rem;
        }
        
        .custom-progress::-webkit-progress-bar {
            background-color: rgba(255, 255, 255, 0.2);
            border-radius: 3px;
        }
        
        .custom-progress::-webkit-progress-value {
            background-color: rgba(255, 255, 255, 0.85);
            border-radius: 3px;
            transition: width 0.3s ease;
        }
        
        .custom-progress::-moz-progress-bar {
            background-color: rgba(255, 255, 255, 0.85);
            border-radius: 3px;
            transition: width 0.3s ease;
        }
        
        .container {
            padding-top: 2rem;
            padding-bottom: 2rem;
        }
        
        .row {
            margin: 0 -0.75rem;
        }
        
        .col-md-3 {
            padding: 0 0.75rem;
        }
    </style>
</head>
<body>
    <div class="container mt-4">
        <!-- 统计卡片 -->
        <div class="row g-4">
            <!-- 总会员数 -->
            <div class="col-md-3">
                <div class="card text-white bg-primary stats-card">
                    <div class="card-body">
                        <h6 class="card-subtitle">总会员数</h6>
                        <div class="number-display">${totalMembers}</div>
                        <div class="description">活跃会员人数</div>
                    </div>
                </div>
            </div>

            <!-- 总消费金额 -->
            <div class="col-md-3">
                <div class="card text-white bg-success stats-card">
                    <div class="card-body">
                        <h6 class="card-subtitle">总消费金额</h6>
                        <div class="number-display">¥${String.format("%,.2f", totalConsumption)}</div>
                        <div class="description">累计消费总额</div>
                    </div>
                </div>
            </div>

            <!-- 总积分 -->
            <div class="col-md-3">
                <div class="card text-white bg-info stats-card">
                    <div class="card-body">
                        <h6 class="card-subtitle">总积分</h6>
                        <div class="number-display">${String.format("%,d", totalPoints)}</div>
                        <div class="description">会员积分总数</div>
                    </div>
                </div>
            </div>

            <!-- 会员类型分布 -->
            <div class="col-md-3">
                <div class="card text-white bg-warning stats-card">
                    <div class="card-body">
                        <h6 class="card-subtitle">会员类型分布</h6>
                        <div class="stats-distribution">
                            <div class="member-progress">
                                <div class="d-flex justify-content-between align-items-center">
                                    <span class="fw-bold">金卡会员</span>
                                    <span class="fw-bold">${typeDistribution['金'] != null ? typeDistribution['金'] : 0}</span>
                                </div>
                                <progress 
                                    class="w-100 custom-progress" 
                                    value="${typeDistribution['金'] != null ? typeDistribution['金'] : 0}" 
                                    max="${totalMembers}"
                                    aria-label="金卡会员占比">
                                </progress>
                            </div>
                            <div class="member-progress">
                                <div class="d-flex justify-content-between align-items-center">
                                    <span class="fw-bold">银卡会员</span>
                                    <span class="fw-bold">${typeDistribution['银'] != null ? typeDistribution['银'] : 0}</span>
                                </div>
                                <progress 
                                    class="w-100 custom-progress" 
                                    value="${typeDistribution['银'] != null ? typeDistribution['银'] : 0}" 
                                    max="${totalMembers}"
                                    aria-label="银卡会员占比">
                                </progress>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- 会员消费排行 -->
        <div class="card mt-4">
            <div class="card-header d-flex justify-content-between align-items-center">
                <h5 class="mb-0">会员消费排行</h5>
                <div class="d-flex align-items-center">
                    <label class="me-2">会员类型：</label>
                    <select class="form-select form-select-sm" style="width: auto;" 
                            onchange="window.location.href='${pageContext.request.contextPath}/stats?memberType=' + this.value">
                        <option value="金" ${selectedType == '金' ? 'selected' : ''}>金卡会员</option>
                        <option value="银" ${selectedType == '银' ? 'selected' : ''}>银卡会员</option>
                    </select>
                </div>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th>会员ID</th>
                                <th>姓名</th>
                                <th>电话</th>
                                <th>会员类型</th>
                                <th>消费金额</th>
                                <th>积分</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${members}" var="member">
                                <tr>
                                    <td>${member.memberId}</td>
                                    <td>${member.name}</td>
                                    <td>${member.phone}</td>
                                    <td>${member.type}</td>
                                    <td><fmt:formatNumber value="${member.consumption}" pattern="¥#,##0.00"/></td>
                                    <td>${member.points}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        
        <!-- 分页导航 -->
        <nav aria-label="Page navigation" class="mt-3">
            <ul class="pagination justify-content-center">
                <li class="page-item ${currentPage <= 1 ? 'disabled' : ''}">
                    <a class="page-link" href="${pageContext.request.contextPath}/stats?page=${currentPage - 1}&memberType=${selectedType}" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <li class="page-item ${currentPage == i ? 'active' : ''}">
                        <a class="page-link" href="${pageContext.request.contextPath}/stats?page=${i}&memberType=${selectedType}">${i}</a>
                    </li>
                </c:forEach>
                <li class="page-item ${currentPage >= totalPages ? 'disabled' : ''}">
                    <a class="page-link" href="${pageContext.request.contextPath}/stats?page=${currentPage + 1}&memberType=${selectedType}" aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </ul>
        </nav>
    </div>
</body>

<jsp:include page="../layout/footer.jsp" />
