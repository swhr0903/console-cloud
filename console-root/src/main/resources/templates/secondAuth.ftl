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
        <p class="login-box-msg">二次认证</p>
        <div class="form-group has-feedback">
            <input id="authCode" class="form-control" maxlength="6" placeholder="Google认证码">
            <span class="glyphicon glyphicon-user form-control-feedback"></span>
            <div id="notExist" style="color: #a94442;"></div>
        </div>
        <div class="row">
            <div class="col-xs-8">
                <div class="checkbox icheck">
                    <label>
                        <a href="/returnLogin">返回登录界面</a><br>
                    </label>
                </div>
            </div>
            <div class="col-xs-4">
                <button id="secondAuth" type="button" class="btn btn-primary btn-block btn-flat">提交</button>
            </div>
        </div>
    </div>
</div>
<script>
    $('#secondAuth').click(function () {
        var authCode = $('#authCode').val();
        if (authCode == '') {
            $('#warnModal').find('.modal-body').text("验证码不能为空");
            $('#warnModal').modal('show');
            return;
        }
        $.ajax({
            url: '/authGoogleCode',
            data: {'authCode': authCode},
            type: 'post',
            success: function (result) {
                if (result.code == "1") {
                    window.location.href = "/";
                } else {
                    $('#warnModal').find('.modal-body').text(result.msg);
                    $('#warnModal').modal('show');
                }
            },
            error: function () {
                $('#warnModal').find('.modal-body').text("验证码错误");
                $('#warnModal').modal('show');
            }
        });
    });
</script>
</body>
</html>
