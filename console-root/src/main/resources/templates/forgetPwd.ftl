<!DOCTYPE html>
<html>
<head>
    <#include "common/common.ftl">
</head>
<body class="hold-transition login-page">
<div class="login-box">
    <div class="login-logo">
        <a href="/"><b>大西洋</b>管理后台</a>
    </div>
    <div class="login-box-body">
        <p class="login-box-msg">输入帐号重置密码</p>
        <div class="form-group has-feedback">
            <input id="username" name="username" class="form-control" placeholder="账户名">
            <span class="glyphicon glyphicon-user form-control-feedback"></span>
            <div id="notExist" style="color: #a94442;"></div>
        </div>
        <div class="row">
            <div class="col-xs-4" style="margin-top: 15px">
                <button id="getBackPwd" type="button" class="btn btn-primary btn-block btn-flat">提交</button>
            </div>
        </div>
    </div>
</div>
<script>
    $('#getBackPwd').click(function () {
        var userName = $('#username').val();
        $.ajax({
            url: '/getBackPwd',
            type: "post",
            data: {"userName": userName},
            success: function (result) {
                if (result == '1') {
                    $('#warnModal').find('.modal-body').text("重置链接已发往帐号所注册邮箱，请打开邮件操作重置密码");
                    $('#warnModal').modal('show');
                } else {
                    $('#notExist').html('帐号不存在');
                    $('#userForget').focus();
                }
            }
        });
    });
</script>
</body>
</html>
