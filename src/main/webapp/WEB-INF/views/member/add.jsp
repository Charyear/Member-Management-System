<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="../layout/header.jsp" />

<div class="table-container">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h4 class="mb-0">添加会员</h4>
        <button class="btn btn-secondary" onclick="history.back()">
            <i class="fas fa-arrow-left"></i> 返回
        </button>
    </div>

    <form method="post" action="${pageContext.request.contextPath}/member/add" class="needs-validation" novalidate>
        <div class="row">
            <div class="col-md-6 mb-3">
                <label for="name" class="form-label">姓名</label>
                <input type="text" class="form-control" id="name" name="name" required>
                <div class="invalid-feedback">请输入会员姓名</div>
            </div>
            
            <div class="col-md-6 mb-3">
                <label for="phone" class="form-label">手机号</label>
                <input type="tel" class="form-control" id="phone" name="phone" 
                       pattern="[0-9]{11}" required>
                <div class="invalid-feedback">请输入11位手机号码</div>
            </div>
        </div>
        
        <div class="mb-3">
            <label for="type" class="form-label">会员类型</label>
            <select class="form-select" id="type" name="type" required>
                <option value="">请选择会员类型</option>
                <option value="金">金卡会员</option>
                <option value="银">银卡会员</option>
            </select>
            <div class="invalid-feedback">请选择会员类型</div>
        </div>
        
        <div class="mt-4">
            <button type="submit" class="btn btn-primary">
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
                }
                form.classList.add('was-validated')
            }, false)
        })
})()
</script>

<jsp:include page="../layout/footer.jsp" />
