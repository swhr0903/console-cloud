$(function () {
    $("[data-toggle='popover']").popover();
    var tableInit = new TableInit();
    tableInit.Init();
    var queryInit = new QueryInit();
    queryInit.Init();
    var editInit = new EditInit();
    editInit.Init();
    var delInit = new DelInit();
    delInit.Init();
    //查询用日期条件框初始化
    $('#startTime,#endTime').datetimepicker({
        format: 'yyyy-mm-dd hh:mm:ss',
        pickDate: false,
        pickSeconds: false,
        pick12HourFormat: false,
        autoclose: true,
        todayBtn: true,
        pickerPosition: "bottom-left",
        language: "zh-CN"
    });
    //校验是否选择用户编辑
    $('#btn_edit').click(function () {
        var selects = $('#tb_users').bootstrapTable('getSelections');
        if (selects.length != 1) {
            $('#warnModal').find('.modal-body').text("请选择一行进行编辑");
            $('#warnModal').modal('show');
            return;
        } else {
            $('#editModal').modal('show');
        }
    });
    //校验是否选择用户授权
    $('#btn_auth').click(function () {
        var selects = $('#tb_users').bootstrapTable('getSelections');
        if (selects.length != 1) {
            $('#warnModal').find('.modal-body').text("请选择一个用户进行授予");
            $('#warnModal').modal('show');
            return;
        } else {
            $('#authModal').modal('show');
        }
    });
    //编辑表单数据初始化
    $('#editModal').on('show.bs.modal', function (e) {
        var selects = $('#tb_users').bootstrapTable('getSelections');
        if (!selects) {
            return e.preventDefault();
        }
        var data = JSON.parse(JSON.stringify(selects));
        $('#editForm').setForm(data);
    });
    //编辑表单校验设置
    $('#editForm').bootstrapValidator({
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
            }
        }
    });
    //校验用户名唯一
    $('#usernameAdd').change(function () {
        var params = JSON.stringify({
            "username": $('#usernameAdd').val(),
        });
        $.ajax({
            url: '/user/isExist',
            type: "post",
            contentType: 'application/json',
            dataType: "json",
            data: params,
            success: function (result) {
                if (result.code == '0') {
                    $('#existTip').html(result.msg);
                    $('#usernameAdd').focus();
                    $('#edit').prop('disabled', true);
                } else {
                    $('#edit').prop('disabled', false);
                    $('#existTip').html('');
                }
            }
        });
    });
    //初始化角色下拉框
    $('#authModal').on('show.bs.modal', function (e) {
        $.ajax({
            type: 'get',
            url: '/role/getRoles',
            success: function (data) {
                var options = '';
                $.each(data, function (index, value) {
                    options += '<option value="' + value.id + '">' + value.name + '</option>';
                });
                $("#role").empty();
                $("#role").append(options);
                $('#role').selectpicker('render');
                $('#role').selectpicker('refresh');
            }
        });
    });
    //初始化角色下拉框用户已拥有角色
    $('#authModal').on('shown.bs.modal', function (e) {
        //$('#role').on('refresh.bs.select', function (e) {
        var selects = $('#tb_users').bootstrapTable('getSelections');
        if (!selects) {
            return e.preventDefault();
        }
        var data = JSON.parse(JSON.stringify(selects));
        var userId = data[0].id;
        $.ajax({
            type: 'get',
            url: '/user/getUserRoles',
            dataType: 'json',
            data: {'param': userId},
            success: function (data) {
                if (data != undefined) {
                    var selected = [];
                    $.each(data, function (index, value) {
                        selected.push(value.role_id);
                    });
                    $("#role").selectpicker('val', selected);
                }
            }
        });
    });
    //授权提交
    $('#auth').click(function () {
        var selects = $('#tb_users').bootstrapTable('getSelections');
        if (!selects) {
            return e.preventDefault();
        }
        var data = JSON.parse(JSON.stringify(selects));
        var userId = data[0].id;
        var roles = $('#role').val();
        var roleStr = '';
        $.each(roles, function (index, value) {
            roleStr += value + ",";
        });
        roleStr = roleStr.substring(0, roleStr.length - 1);
        $.ajax({
            url: '/user/auth',
            type: 'post',
            dataType: 'json',
            data: {'userId': userId, 'roles': roleStr},
            success: function (result) {
                if (result == '1') {
                    $('#errorAuthTip').html("授权成功");
                } else {
                    $('#errorAuthTip').html("授权失败，请联系管理员");
                }
                $('#warnModal').modal('show');
            }, error: function () {
                $('#errorAuthTip').html("您没有此操作权限");
            }
        });
    });
});

var TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#tb_users').bootstrapTable({
            url: '/user/getUsers',
            method: 'get',
            toolbar: '#toolbar',
            idField: 'id',
            striped: true,      //是否显示行间隔色
            cache: false,      //是否使用缓存，默认为true
            pagination: true,
            sortable: true,
            sortOrder: 'asc',
            queryParams: oTableInit.queryParams,
            sidePagination: 'server',
            pageNumber: 1,
            pageSize: 20,
            pageList: [10, 25, 50, 100],
            search: false,      //是否显示表格搜索，此搜索是客户端搜索
            strictSearch: true,
            showColumns: true,     //是否显示所有的列
            showRefresh: true,     //是否显示刷新按钮
            minimumCountColumns: 2,    //最少允许的列数
            clickToSelect: true,    //是否启用点击选中行
            height: 500,      //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: 'ID',      //每一行的唯一标识，一般为主键列
            showToggle: true,     //是否显示详细视图和列表视图的切换按钮
            cardView: false,     //是否显示详细视图
            detailView: false,     //是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'id',
                title: '用户ID'
            }, {
                field: 'username',
                title: '帐号名'
            }, {
                field: 'name',
                title: '姓名'
            }, {
                field: 'email',
                title: '邮箱'
            }, {
                field: 'create_time',
                title: '加入时间',
                sortable: "true",
                formatter: timeFormater
            }],
            onLoadError: function () {
                $('#warnModal').find('.modal-body').text('您没有此操作权限');
                $('#warnModal').modal('show');
            }
        });
    };
    //得到查询的参数
    oTableInit.queryParams = function (params) {
        var temp = {
            limit: params.limit - 1,
            offset: params.offset,
            sortName: this.sortName,
            sortOrder: this.sortOrder,
            username: $('#username').val(),
            startTime: $('#startTime').val(),
            endTime: $('#endTime').val()
        };
        return temp;
    };
    return oTableInit;
};
//查询栏初始化
var QueryInit = function () {
    var oInit = new Object();
    oInit.Init = function () {
        $('#query').click(function () {
            $('#tb_users').bootstrapTable('refresh', {url: '/user/getUsers'});
        });
        $(document).keydown(function (event) {
            if (event.keyCode == 13) {
                $('#query').click();
            }
        });
    };
    return oInit;
};
//编辑初始化
var EditInit = function () {
    var oInit = new Object();
    oInit.Init = function () {
        $('#edit').click(function () {
            var params = JSON.stringify({
                'id': $('#id').val(),
                'username': $('#usernameAdd').val(),
                'name': $('#name').val(),
                'email': $('#email').val()
            });
            $.ajax({
                url: '/user/edit',
                type: 'post',
                contentType: 'application/json',
                data: params,
                success: function (data) {
                    if (data == '1') {
                        $('#tb_users').bootstrapTable('refresh', {url: '/user/getUsers'});
                    }
                },
                error: function () {
                    $('#errorEditTip').html("您没有此操作权限");
                }
            });
        });
        $('#reset').click(function () {
            $('#editForm').data('bootstrapValidator').resetForm(true);
        });
    };
    return oInit;
};
//删除初始化
var DelInit = function () {
    var oInit = new Object();
    oInit.Init = function () {
        $('#btn_delete').click(function () {
            $('#sureModal').find('.modal-body').text('确定删除？');
            $('#sureModal').modal('show');
            $('#sure').click(function () {
                del();
            });
        });
    }
    return oInit;
};

//删除操作
function del() {
    var selects = $('#tb_users').bootstrapTable('getSelections');
    if (!selects) {
        return e.preventDefault();
    }
    var params = JSON.stringify(selects);
    $.ajax({
        url: '/user/del',
        type: 'post',
        contentType: 'application/json',
        data: params,
        success: function (data) {
            if (data == '1') {
                $('#tb_users').bootstrapTable('refresh', {url: '/user/getUsers'});
            } else {
                $('#warnModal').find('.modal-body').text('删除失败，请联系管理');
                $('#warnModal').modal('show');
            }
        },
        error: function () {
            $('#warnModal').find('.modal-body').text('您没有此操作权限');
            $('#warnModal').modal('show');
        }
    });
}