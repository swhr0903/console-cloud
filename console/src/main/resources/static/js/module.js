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
    $('#btn_edit').click(function () {
        var selects = $('#tb_modules').bootstrapTable('getSelections');
        if (selects.length != 1) {
            $('#warnModal').find('.modal-body').text("请选择一行进行编辑");
            $('#warnModal').modal('show');
            return;
        } else {
            $('#editModal').modal('show');
        }
    });
    $('#btn_add').click(function () {
        $('#editModal').modal('show');
        $('#id').val(null);
        $('#editForm').data('bootstrapValidator').resetForm(true);
    });
    $('#editModal').on('show.bs.modal', function (e) {
        //初始化表单
        var selects = $('#tb_modules').bootstrapTable('getSelections');
        if (selects) {
            var data = JSON.parse(JSON.stringify(selects));
            $('#editForm').setForm(data);
        }
        var moduleName = selects[0].name;
        //初始化功能项
        $.ajax({
            type: 'get',
            url: '/module/getOptions',
            data: {"moduleName": moduleName},
            success: function (data) {
                var options = '';
                $.each(data.options, function (index, value) {
                    options += '<option value="' + value.code + '">' + value.name + '</option>';
                });
                var oArray = data.option.split(",");
                $("#options").empty();
                $("#options").append(options);
                $('#options').selectpicker('render');
                $('#options').selectpicker('refresh');
                $('#options').selectpicker('val', oArray);
            }
        });
    });
    $('#editForm').bootstrapValidator({
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            name: {
                validators: {
                    notEmpty: {
                        message: '模块名不能为空'
                    }
                }
            },
            url: {
                validators: {
                    notEmpty: {
                        message: '模块URL不能为空'
                    }
                }
            }
        }
    });
    $('#mName').change(function () {
        var params = JSON.stringify({
            "name": $('#mName').val(),
        });
        $.ajax({
            url: '/module/isExist',
            type: "post",
            contentType: 'application/json',
            dataType: "json",
            data: params,
            success: function (result) {
                if (result.code == '0') {
                    $('#existTip').html(result.msg);
                    $('#name').focus();
                    $('#edit').prop('disabled', true);
                } else {
                    $('#edit').prop('disabled', false);
                    $('#existTip').html('');
                }
            }
        });
    });
    $.ajax({
        type: 'get',
        url: '/module/getModules',
        success: function (data) {
            var options = '';
            $.each(data, function (index, value) {
                options += '<option value="' + value.id + '">' + value.name + '</option>';
            });
            $("#parent").empty();
            $("#parent").append(options);
            $('#parent').selectpicker('render');
            $('#parent').selectpicker('refresh');
        }
    });
});

var TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#tb_modules').bootstrapTable({
            url: '/module/getModulesPaging',
            method: 'get',
            toolbar: '#toolbar',
            idField: 'id',
            striped: true,      //是否显示行间隔色
            cache: false,      //是否使用缓存，默认为true
            pagination: true,
            showExport: true,
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
                title: '模块ID'
            }, {
                field: 'name',
                title: '模块名'
            }, {
                field: 'url',
                title: '模块URL'
            }, {
                field: 'parent',
                title: '父模块'
            }, {
                field: 'is_leaf',
                title: '是否子模块',
                formatter: statusFormater
            }, {
                field: 'options',
                title: '功能项'
            }, {
                field: 'status',
                title: '是否启用',
                formatter: statusFormater
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
            name: $('#name').val()
        };
        return temp;
    };
    return oTableInit;
};

var QueryInit = function () {
    var oInit = new Object();
    oInit.Init = function () {
        $('#query').click(function () {
            $('#tb_modules').bootstrapTable('refresh', {url: '/module/getModulesPaging'});
        });
        $(document).keydown(function (event) {
            if (event.keyCode == 13) {
                $('#query').click();
            }
        });
    };
    return oInit;
};

var EditInit = function () {
    var oInit = new Object();
    oInit.Init = function () {
        $('#edit').click(function () {
            var id = $('#id').val();
            var oArray = $('#options').val();
            var options = '';
            $.each(oArray, function (index, value) {
                options += value;
                if (index < oArray.length - 1) {
                    options += ",";
                }
            });
            var params = JSON.stringify({
                'id': id,
                'name': $('#mName').val(),
                'url': $('#mUrl').val(),
                'parent_id': $('#parent').val(),
                'options': options,
                'status': $('#status').val()
            });
            var url;
            if (id != undefined && id != '') {
                url = '/module/edit'
            } else {
                url = '/module/add'
            }
            $.ajax({
                url: url,
                type: 'post',
                contentType: 'application/json',
                data: params,
                success: function (data) {
                    if (data == '1') {
                        $('#editModal').modal('hide');
                        $('#tb_modules').bootstrapTable('refresh', {url: '/module/getModulesPaging'});
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


function del() {
    var selects = $('#tb_modules').bootstrapTable('getSelections');
    if (!selects) {
        return e.preventDefault();
    }
    var params = JSON.stringify(selects);
    $.ajax({
        url: '/module/del',
        type: 'post',
        contentType: 'application/json',
        data: params,
        success: function (data) {
            if (data == '1') {
                $('#tb_modules').bootstrapTable('refresh', {url: '/module/getModulesPaging'});
            } else {
                $('#warnModal').find('.modal-body').text('删除失败，请联系管理员');
                $('#warnModal').modal('show');
            }
        },
        error: function () {
            $('#warnModal').find('.modal-body').text('您没有此操作权限');
            $('#warnModal').modal('show');
        }
    });
}

function selDomain(val, text) {
    $('#dataDomain').text(text);
    $('#tb_modules').bootstrapTable('refreshOptions', {
        exportDataType: val
    });
}