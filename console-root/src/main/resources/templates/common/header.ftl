<header class="main-header">
    <!-- Logo -->
    <a href="/" class="logo">
        <!-- mini logo for sidebar mini 50x50 pixels -->
        <span class="logo-mini">首页</span>
        <!-- logo for regular state and mobile devices -->
        <span class="logo-lg">大西洋管理后台</span>
    </a>
    <nav class="navbar navbar-static-top" role="navigation">
        <a href="#" class="sidebar-toggle" data-toggle="push-menu" role="button">
            <span class="sr-only">Toggle navigation</span>
        </a>
        <div class="navbar-custom-menu">
            <ul class="nav navbar-nav">
                <#--<li class="dropdown messages-menu">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <i class="fa fa-envelope-o"></i>
                        <span class="label label-success">4</span>
                    </a>
                    <ul class="dropdown-menu">
                        <li class="header">你有4个消息</li>
                        <li>
                            <ul class="menu">
                                <li>
                                    <a href="#">
                                        <div class="pull-left">
                                            <img src="/static/bootstrap/img/user2-160x160.jpg" class="img-circle" alt="User Image">
                                        </div>
                                        <h4>
                                            技术部
                                            <small><i class="fa fa-clock-o"></i> 5 分钟前</small>
                                        </h4>
                                        <p>专题页面做好了吗？</p>
                                    </a>
                                </li>
                            </ul>
                        </li>
                        <li class="footer"><a href="#">查看所有消息</a></li>
                    </ul>
                </li>-->
                <li class="dropdown notifications-menu">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <i class="fa fa-bell-o"></i>
                        <span id="dwSpan" class="label label-warning">0</span>
                    </a>
                    <ul class="dropdown-menu">
                        <#--<li id="depositHeader" class="header"></li>-->
                        <li>
                            <ul id="dwUl" class="menu">

                            </ul>
                        </li>
                        <li class="footer"><a href="#">查看所有</a></li>
                    </ul>
                </li>
                <#--<li class="dropdown tasks-menu">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <i class="fa fa-flag-o"></i>
                        <span class="label label-danger">9</span>
                    </a>
                    <ul class="dropdown-menu">
                        <li class="header">您有9个未完成任务</li>
                        <li>
                            <ul class="menu">
                                <li>
                                    <a href="#">
                                        <h3>
                                            任务1
                                            <small class="pull-right">20%</small>
                                        </h3>
                                        <div class="progress xs">
                                            <div class="progress-bar progress-bar-aqua" style="width: 20%"
                                                 role="progressbar" aria-valuenow="20" aria-valuemin="0"
                                                 aria-valuemax="100">
                                                <span class="sr-only">20% 完成</span>
                                            </div>
                                        </div>
                                    </a>
                                </li>
                            </ul>
                        </li>
                        <li class="footer">
                            <a href="#">查看所有</a>
                        </li>
                    </ul>
                </li>-->
                <li class="dropdown user user-menu">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <img src="<#if avatar??>${avatar}<#else>/static/bootstrap/img/user2-160x160.jpg</#if>"
                             class="user-image" alt="User Image">
                        <span class="hidden-xs">${user}</span>
                    </a>
                    <ul class="dropdown-menu">
                        <li class="user-header">
                            <img src="<#if avatar??>${avatar}<#else>/static/bootstrap/img/user2-160x160.jpg</#if>"
                                 class="img-circle" alt="User Image">
                            <p>
                                ${user} - ${role}
                                <small>Member since ${createTime}</small>
                            </p>
                        </li>
                        <!-- Menu Body -->
                        <!--<li class="user-body">
                            <div class="row">
                                <div class="col-xs-4 text-center">
                                    <a href="#">粉丝</a>
                                </div>
                                <div class="col-xs-4 text-center">
                                    <a href="#">成绩</a>
                                </div>
                                <div class="col-xs-4 text-center">
                                    <a href="#">朋友</a>
                                </div>
                            </div>
                            &lt;!&ndash; /.row &ndash;&gt;
                        </li>-->
                        <!-- Menu Footer-->
                        <li class="user-footer">
                            <div class="pull-left">
                                <a id="href_config" href="#" class="btn btn-default btn-flat">配置</a>
                            </div>
                            <div class="pull-right">
                                <a href="/logout" class="btn btn-default btn-flat">退出</a>
                            </div>
                        </li>
                    </ul>
                </li>
                <!-- Control Sidebar Toggle Button -->
                <!--<li>
                    <a href="#" data-toggle="control-sidebar"><i class="fa fa-gears"></i></a>
                </li>-->
            </ul>
        </div>
    </nav>
</header>
<!-- 配置个人信息 -->
<div class="modal fade" id="configModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title" id="myModalLabel">个人信息</h4>
            </div>
            <div class="modal-body">
                <form id="configForm" class="form-horizontal">
                    <div class="form-group">
                        <label class="col-lg-3 control-label">帐号</label>
                        <div class="col-lg-5">
                            <input id="idConfig" name="id" type="hidden"/>
                            <input id="usernameConfig" class="form-control" name="username"/>
                            <div id="existTip1" style="color: #a94442;"></div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label">姓名</label>
                        <div class="col-lg-5">
                            <input id="nameConfig" class="form-control" name="name"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label">邮箱</label>
                        <div class="col-lg-5">
                            <input id="emailConfig" class="form-control" name="email"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label">自定义头像</label>
                        <div class="col-lg-5">
                            <input type="file" name="user_img" id="user_img" class="file-loading"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label">密码</label>
                        <div class="col-lg-5">
                            <input id="passwordConfig" type="password" class="form-control" name="password"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label">确认密码</label>
                        <div class="col-lg-5">
                            <input id="ConfigConfirmPassword" type="password" class="form-control"
                                   name="confirmPassword"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-lg-9 col-lg-offset-3">
                            <button id="config" type="submit" class="btn btn-primary" name="signup" value="提交">
                                提交
                            </button>
                            <button type="button" class="btn btn-info" id="resetConfig">重置表单</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<script>
    $('#href_config').click(function () {
        $('#configModal').modal('show');
    });
    $('#configModal').on('show.bs.modal', function (e) {
        var params = JSON.stringify({
            "username": '${user}',
        });
        $.ajax({
            url: '/user/getUser',
            type: "post",
            contentType: 'application/json',
            data: params,
            dataType: 'json',
            success: function (data) {
                if (data != null && data != '') {
                    var datas = [];
                    datas[0] = data;
                    $('#configForm').setForm(datas);
                } else {
                    $('#configModal').modal('hide');
                }
            }
        });
    });
    $('#configForm').bootstrapValidator({
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            username: {
                validators: {
                    notEmpty: {
                        message: '帐号不能为空'
                    }
                }
            },
            name: {
                validators: {
                    notEmpty: {
                        message: '姓名不能为空'
                    }
                }
            },
            email: {
                validators: {
                    notEmpty: {
                        message: '邮箱是紧急情况下你找回密码的唯一途径，不能为空'
                    },
                    emailAddress: {
                        message: '邮箱格式无效'
                    }
                }
            },
            password: {
                validators: {
                    notEmpty: {
                        message: '密码不鞥为空'
                    },
                    identical: {
                        field: 'confirmPassword',
                        message: '密码与确认密码不一致'
                    },
                    different: {
                        field: 'username',
                        message: '密码不能与帐号相同'
                    }
                }
            },
            confirmPassword: {
                validators: {
                    identical: {
                        field: 'password',
                        message: '密码与确认密码不一致'
                    },
                    different: {
                        field: 'username',
                        message: '密码不能与帐号相同'
                    }
                }
            }
        }
    });
    $('#usernameConfig').change(function () {
        var params = JSON.stringify({
            "username": $('#usernameConfig').val(),
        });
        $.ajax({
            url: '/user/isExist',
            type: "post",
            contentType: 'application/json',
            dataType: "json",
            data: params,
            success: function (result) {
                if (result.code == '0') {
                    $('#existTip1').html('帐号已存在');
                    $('#usernameConfig').focus();
                    $('#config').prop('disabled', true);
                } else {
                    $('#config').prop('disabled', false);
                    $('#existTip1').html('');
                }
            }
        });
    });
    $('#config').click(function () {
        $('#registerForm').bootstrapValidator('validate');
        var params = JSON.stringify({
            "id": $('#idConfig').val(),
            "username": $('#usernameConfig').val(),
            "password": $('#passwordConfig').val(),
            "name": $('#nameConfig').val(),
            "email": $('#emailConfig').val()
        });
        $.ajax({
            url: '/user/edit',
            type: "post",
            contentType: 'application/json',
            data: params,
            success: function (data) {
                if (data == '1') {
                }
            }
        });
    });
    $('#resetConfig').click(function () {
        $('#registerForm').data('bootstrapValidator').resetForm(true);
    });

    $(function () {
        dwRemind();
    });

    function dwRemind() {
        $.ajax({
            type: 'get',
            url: '/user/getDWCount',
            success: function (data) {
                if (data)
                    $('#dwSpan').html(data.length);
                $('#dwUl').empty();
                $.each(data, function (index, value) {
                    var type, style;
                    switch (value.paytyple) {
                        case 1:
                            type = '提款';
                            style = 'red';
                            break;
                        case 2:
                            type = '加款';
                            style = 'yellow';
                            break;
                        case 3:
                            type = '扣款';
                            style = 'black';
                            break;
                        default:
                            type = '存款';
                            style = 'aqua';
                    }
                    $('#dwUl').append('<li><a href="#">' +
                        '<i class="fa fa-users text-' + style + '"></i>' +
                        '有' + value.num + '笔' + type + '未处理' +
                        '</a></li>');
                });
            }
        });
    }

    setInterval(dwRemind, 10000);
</script>