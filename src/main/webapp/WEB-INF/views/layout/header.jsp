<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>会员管理系统</title>
    <link href="https://cdn.bootcdn.net/ajax/libs/twitter-bootstrap/5.3.1/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            margin: 0;
            padding: 0;
        }
        #sidebar {
            position: fixed;
            left: 0;
            top: 0;
            bottom: 0;
            width: 200px;
            background-color: #2c3e50;
            padding: 0;
            z-index: 100;
        }
        #sidebar .nav-link {
            color: #ecf0f1;
            padding: 10px 15px;
            border-bottom: 1px solid #34495e;
        }
        #sidebar .nav-link:hover {
            background-color: #34495e;
        }
        #sidebar .nav-link.active {
            background-color: #3498db;
        }
        #content {
            margin-left: 200px;
            padding: 20px;
            min-height: 100vh;
            background-color: #f8f9fa;
        }
        .system-header {
            background-color: #2c3e50;
            color: white;
            padding: 15px;
            margin-bottom: 20px;
            border-bottom: 1px solid #34495e;
        }
        .search-box {
            margin-bottom: 20px;
        }
        .table-container {
            background-color: white;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>
    <div id="sidebar">
        <div class="system-header">
            <h4 class="mb-0">会员管理系统</h4>
        </div>
        <div class="nav flex-column">
            <a class="nav-link ${param.active == 'member' ? 'active' : ''}" href="${pageContext.request.contextPath}/member/">
                <i class="fas fa-users"></i> 会员管理
            </a>
            <a class="nav-link ${param.active == 'points' ? 'active' : ''}" href="${pageContext.request.contextPath}/points/">
                <i class="fas fa-star"></i> 积分管理
            </a>
            <a class="nav-link ${param.active == 'stats' ? 'active' : ''}" href="${pageContext.request.contextPath}/stats/">
                <i class="fas fa-chart-bar"></i> 统计报表
            </a>
        </div>
    </div>
    <div id="content">
        <div class="col-md-10 main-content">
