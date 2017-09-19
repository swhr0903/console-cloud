<!DOCTYPE html>
<html>
<head>
    <#include "common/common.ftl">
    <script src="/static/js/auth.js"></script>
</head>
<body class="hold-transition login-page">
<div class="login-box">
    <div class="login-logo">
        <a href="/"><b>大西洋</b>管理后台</a>
    </div>
    <div class="login-box-body">
        <p class="login-box-msg">登入开始您一天的工作</p>
        <div class="form-group has-feedback">
            <input id="username" name="username" class="form-control" placeholder="账户名">
            <span class="glyphicon glyphicon-user form-control-feedback"></span>
        </div>
        <div class="form-group has-feedback">
            <input id="password" name="password" type="password" class="form-control" placeholder="密码">
            <span class="glyphicon glyphicon-lock form-control-feedback"></span>
        </div>
        <div class="row">
            <div class="col-xs-8">
                <div class="checkbox icheck">
                    <!--<label>
                        <input type="checkbox"> 记住我
                    </label>-->
                    <label>
                        <a href="#addModal" data-toggle="modal">注册帐号</a><br>
                    </label>
                </div>
            </div>
            <div class="col-xs-4">
                <button id="login" type="button" class="btn btn-primary btn-block btn-flat">登录</button>
            </div>
        </div>
        <!--<a href="#">忘记密码</a><br>-->
    </div>
</div>
<!-- 注册 -->
<div class="modal fade" id="addModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title" id="myModalLabel">注册</h4>
            </div>
            <div class="modal-body">
                <form id="registerForm" class="form-horizontal" action="/user/register"
                      method="post">
                    <div class="form-group">
                        <label class="col-lg-3 control-label">帐号</label>
                        <div class="col-lg-5">
                            <input id="usernameRegi" class="form-control" name="username"/>
                            <div id="existTip2" style="color: #a94442;"></div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label">姓名</label>
                        <div class="col-lg-5">
                            <input class="form-control" name="name"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label">邮箱</label>
                        <div class="col-lg-5">
                            <input class="form-control" name="email"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label">密码</label>
                        <div class="col-lg-5">
                            <input type="password" class="form-control" name="password"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label">确认密码</label>
                        <div class="col-lg-5">
                            <input type="password" class="form-control" name="confirmPassword"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-lg-9 col-lg-offset-3">
                            <button id="register" type="submit" class="btn btn-primary" name="signup" value="提交">
                                提交
                            </button>
                            <button type="button" class="btn btn-info" id="reset">重置表单</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
