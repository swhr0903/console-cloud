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
        <p class="login-box-msg">重置密码</p>
        <div class="form-group has-feedback">
            <input id="username" type="hidden" value="${userName}"/>
            <input id="newPwd" type="password" class="form-control" placeholder="新密码">
            <input id="sureNewPwd" type="password" class="form-control" style="margin-top: 10px" placeholder="确认新密码">
            <span class="glyphicon glyphicon-user form-control-feedback"></span>
            <div id="notExist" style="color: #a94442;"></div>
        </div>
        <div class="row">
            <div class="col-xs-8">
                <div class="checkbox icheck">
                    <label>
                        <a href="/login">返回登录界面</a><br>
                    </label>
                </div>
            </div>
            <div class="col-xs-4">
                <button id="restPwd" type="button" class="btn btn-primary btn-block btn-flat">提交</button>
            </div>
        </div>
    </div>
</div>
<script>
    $('#restPwd').click(function () {
        var userName = $('#username').val();
        var newPwd = $('#newPwd').val();
        var sureNewPwd = $('#sureNewPwd').val();
        if (newPwd != sureNewPwd) {
            $('#warnModal').find('.modal-body').text("两次密码不一致");
            $('#warnModal').modal('show');
            return;
        }
        $.ajax({
            url: '/restPwd',
            type: "post",
            data: {"username": userName, "password": newPwd},
            success: function (result) {
                if (result == '1') {
                    $('#warnModal').find('.modal-body').text("密码重置成功");
                    $('#warnModal').modal('show');
                } else {
                    $('#notExist').html('密码重置失败');
                }
            }
        });
    });
</script>
</body>
</html>
