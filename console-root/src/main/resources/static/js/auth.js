$(function () {
    $('#login').click(function () {
        $.ajax({
            url: '/auth',
            data: {'username': $('#username').val(), 'password': $('#password').val()},
            type: 'post',
            success: function () {
                window.location.href = '/secondAuth';
            },
            error: function () {
                $('#warnModal').find('.modal-body').text("帐号或密码错误");
                $('#warnModal').modal('show');
            }
        });
    });

    $('#register').click(function () {
        var user = JSON.stringify({
            'username': $('#usernameRegi').val(),
            'name': $('#name').val(),
            'email': $('#email').val(),
            'password': $('#passwordRegi').val(),
            'confirmPassword': $('#confirmPassword').val()
        });
        $.ajax({
            url: '/user/register',
            data: user,
            type: 'post',
            contentType: 'application/json;charset=UTF-8',
            dataType: "json",
            success: function (result) {
                if (result.code == 1) {
                    $('#regiTip').text(result.msg);
                    $('#username').val($('#usernameRegi').val());
                    $('#password').val($('#passwordRegi').val());
                    setTimeout(function () {
                        window.location.href = '/login';
                    }, 1000);
                } else {
                    $('#regiTip').text(result.msg);
                }
            },
            error: function () {
                $('#warnModal').find('.modal-body').text("帐号或密码错误");
                $('#warnModal').modal('show');
            }
        });
    });

    $('#secondAuth').click(function () {
        var username = $('#username').val();
        var password = $('#password').val();
        if (username == '' || password == '') {
            $('#warnModal').find('.modal-body').text("帐号密码不能为空");
            $('#warnModal').modal('show');
            return;
        }
        $.ajax({
            url: '/getAuthConfig',
            data: {'username': username, 'password': password},
            type: 'post',
            success: function (result) {
                var code = result.code;
                if (code == '1') {
                    var url = result.msg;
                    var win = window.open(url, '_blank');
                    win.focus();
                } else {
                    $('#warnModal').find('.modal-body').text(result.msg);
                    $('#warnModal').modal('show');
                }
            },
            error: function () {
                $('#warnModal').find('.modal-body').text("帐号或密码错误");
                $('#warnModal').modal('show');
            }
        });
    });

    $(document).keydown(function (event) {
        if (event.keyCode == 13) {
            $('#login').click();
        }
    });

    $('#registerForm').bootstrapValidator({
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
                    notEmpty: {
                        message: '确认密码不能为空'
                    },
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

    $('#usernameRegi').change(function () {
        $.ajax({
            url: '/user/isExist/' + $('#usernameRegi').val(),
            type: "get",
            contentType: 'application/json',
            dataType: "json",
            success: function (result) {
                if (result.code == '0') {
                    $('#existTip2').html('帐号已存在');
                    $('#usernameRegi').focus();
                    $('#register').prop('disabled', true);
                } else {
                    $('#register').prop('disabled', false);
                    $('#existTip2').html('');
                }
            }
        });
    });

    $('#reset').click(function () {
        $('#registerForm').data('bootstrapValidator').resetForm(true);
    });
});

