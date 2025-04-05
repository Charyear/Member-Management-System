<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../layout/header.jsp" />

<div class="table-container">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h4 class="mb-0">编辑会员</h4>
        <button class="btn btn-secondary" onclick="history.back()">
            <i class="fas fa-arrow-left"></i> 返回
        </button>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            ${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/member/edit" class="needs-validation" novalidate>
        <input type="hidden" name="member_id" value="${member.memberId}">
        <input type="hidden" name="consumption" value="${member.consumption}">
        <input type="hidden" name="points" value="${member.points}">
        
        <div class="row">
            <div class="col-md-6 mb-3">
                <label for="name" class="form-label">姓名</label>
                <input type="text" class="form-control" id="name" name="name" 
                       value="${member.name}" required>
                <div class="invalid-feedback">请输入会员姓名</div>
            </div>
            
            <div class="col-md-6 mb-3">
                <label for="phone" class="form-label">手机号</label>
                <input type="tel" class="form-control" id="phone" name="phone" 
                       value="${member.phone}" pattern="[0-9]{11}" required>
                <div class="invalid-feedback">请输入11位手机号码</div>
            </div>
        </div>
        
        <div class="row">
            <div class="col-md-4 mb-3">
                <label for="type" class="form-label">会员类型</label>
                <select class="form-select" id="type" name="type" required>
                    <option value="">请选择会员类型</option>
                    <option value="金" ${member.type == '金' ? 'selected' : ''}>金卡会员</option>
                    <option value="银" ${member.type == '银' ? 'selected' : ''}>银卡会员</option>
                </select>
                <div class="invalid-feedback">请选择会员类型</div>
            </div>
            
            <div class="col-md-4 mb-3">
                <label for="consumption" class="form-label">消费金额</label>
                <input type="number" class="form-control" id="consumption" 
                       value="${member.consumption}" step="0.01" readonly>
                <div class="form-text">消费金额只能通过更新积分功能修改</div>
            </div>
            
            <div class="col-md-4 mb-3">
                <label for="points" class="form-label">积分</label>
                <input type="number" class="form-control" id="points" 
                       value="${member.points}" readonly>
                <div class="form-text">积分会根据消费金额自动计算</div>
            </div>
        </div>
        
        <div class="mt-4">
            <button type="submit" class="btn btn-primary" id="submitBtn">
                <i class="fas fa-save"></i> 保存
            </button>
            <button type="reset" class="btn btn-secondary ms-2">
                <i class="fas fa-redo"></i> 重置
            </button>
        </div>
    </form>
</div>

<script>
// 表单验证
(function () {
    'use strict'
    var forms = document.querySelectorAll('.needs-validation')
    Array.prototype.slice.call(forms)
        .forEach(function (form) {
            form.addEventListener('submit', function (event) {
                if (!form.checkValidity()) {
                    event.preventDefault()
                    event.stopPropagation()
                } else {
                    // 如果表单验证通过，禁用提交按钮
                    document.getElementById('submitBtn').disabled = true;
                }
                form.classList.add('was-validated')
            }, false)
        })
})()
</script>

<jsp:include page="../layout/footer.jsp" />
