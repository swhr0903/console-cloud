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
        var selects = $('#tb_roles').bootstrapTable('getSelections');
        if (selects.length != 1) {
            $('#warnModal').find('.modal-body').text("请选择一行进行编辑");
            $('#warnModal').modal('show');
            return;
        } else {
            $('#editModal').modal('show');
        }
    });
    $('#btn_auth').click(function () {
        var selects = $('#tb_roles').bootstrapTable('getSelections');
        if (selects.length != 1) {
            $('#warnModal').find('.modal-body').text("请选择一个角色进行授权");
            $('#warnModal').modal('show');
            return;
        } else {
            $('#authModal').modal('show');
        }
    });
    $('#btn_add').click(function () {
        $('#editModal').modal('show');
        $('#id').val(null);
        $('#editForm').data('bootstrapValidator').resetForm(true);
    });
    $('#editModal').on('show.bs.modal', function (e) {
        var selects = $('#tb_roles').bootstrapTable('getSelections');
        if (!selects) {
            return e.preventDefault();
        }
        var data = JSON.parse(JSON.stringify(selects));
        $('#editForm').setForm(data);
    });
    $('#authModal').on('show.bs.modal', function (e) {
        var selects = $('#tb_roles').bootstrapTable('getSelections');
        if (!selects) {
            return e.preventDefault();
        }
        var data = JSON.parse(JSON.stringify(selects));
        var roleId = data[0].id;
        $('#roleId').val(roleId);
        //初始化树形菜单
        var $checkableTree = $('#treeview-checkable').treeview({
            data: getModules(),
            showIcon: false,
            showCheckbox: true,
            onNodeChecked: function (event, node) {
                getCheckedDatas($checkableTree, node, 1);
            },
            onNodeUnchecked: function (event, node) {
                getCheckedDatas($checkableTree, node, 0);
            }
        });
        //设置勾选已有权限
        $.ajax({
            url: '/manage/role/getRoleAuths',
            type: 'get',
            dataType: 'json',
            data: {'roleId': roleId},
            success: function (result) {
                $.each(result, function (index, value) {
                    var moduleId = value.module_id;
                    var permission = value.permission;
                    var root = $checkableTree.treeview('getNode', 0);
                    var nodes = getChildNodes(root);
                    $.each(nodes, function (index, value) {
                        var node = $checkableTree.treeview('getNode', value);
                        if (moduleId == node.value || (moduleId == node.value.split('-')[0]
                            && permission.indexOf(node.value.split('-')[1]) >= 0)) {
                            $checkableTree.treeview('checkNode', [node, {silent: true}]);
                            checkParentNode($checkableTree, node);
                        }
                    });
                });
            }
        });
        /*$('#all').click(function () {
            $checkableTree.treeview('checkAll', {silent: true});
        });
        $('#nobody').click(function () {
            $checkableTree.treeview('uncheckAll', {silent: true});
        });*/
        $('#auth').click(function () {
            if (checkedDatas == undefined || checkedDatas.length <= 0) {
                var root = $checkableTree.treeview('getNode', 0);
                getCheckedDatas($checkableTree, root);
            }
            var params = JSON.stringify({
                roleId: $('#roleId').val(),
                permissions: checkedDatas
            });
            $.ajax({
                url: '/manage/role/auth',
                type: 'post',
                contentType: 'application/json;charset=utf-8',
                dataType: 'json',
                data: params,
                success: function (result) {
                    if (result.code == '1') {
                        $('#errorAuthTip').html(result.msg);
                    } else {
                        $('#errorAuthTip').html("授权异常：" + result.msg);
                    }
                    $('#warnModal').modal('show');
                }, error: function () {
                    $('#errorAuthTip').html("服务不可用，请稍后再试");
                }
            });
        });
    });
    /*$('#authModal').on('hide.bs.modal', function (e) {
        $('#treeview-checkable').treeview('uncheckAll', {silent: true});
    });*/
    $('#editForm').bootstrapValidator({
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            code: {
                validators: {
                    notEmpty: {
                        message: '角色代码不能为空'
                    }
                }
            },
            name: {
                validators: {
                    notEmpty: {
                        message: '角色名不能为空'
                    }
                }
            }
        }
    });
    $('#rCode').change(function () {
        var params = {
            "code": $('#rCode').val(),
        };
        $.ajax({
            url: '/manage/role/isExist',
            type: 'get',
            dataType: 'json',
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
        url: '/manage/role/getRoles',
        success: function (data) {
            var options = '';
            $.each(data, function (index, value) {
                options += '<option value="' + value.id + '">' + value.name + '</option>';
            });
            $('#parent').empty();
            $('#parent').append(options);
            $('#parent').selectpicker('render');
            $('#parent').selectpicker('refresh');
        }
    });
});

var TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#tb_roles').bootstrapTable({
            url: '/manage/role/getRolesPaging',
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
            pageSize: 19,
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
                title: '角色ID'
            }, {
                field: 'code',
                title: '角色代码'
            }, {
                field: 'name',
                title: '角色名'
            }, {
                field: 'status',
                title: '是否启用',
                formatter: statusFormater
            }],
            onLoadError: function () {
                $('#warnModal').find('.modal-body').text('服务不可用，请稍后再试');
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
            $('#tb_roles').bootstrapTable('refresh', {url: '/manage/role/getRolesPaging'});
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
            var params = {
                'id': id,
                'code': $('#rCode').val(),
                'name': $('#rName').val(),
                'status': $('#rStatus').val()
            };
            var url, type;
            if (id != undefined && id != '') {
                url = '/manage/role/update'
                type = 'patch';
            } else {
                url = '/manage/role/add'
                type = 'post';
            }
            $.ajax({
                url: url,
                type: type,
                data: params,
                success: function (result) {
                    if (result.code == '1') {
                        $('#editModal').modal('hide');
                        $('#tb_roles').bootstrapTable('refresh', {url: '/manage/role/getRolesPaging'});
                    } else {
                        $('#errorEditTip').html(result.msg);
                    }
                },
                error: function () {
                    $('#errorEditTip').html("服务不可用，请稍后再试");
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
    var selects = $('#tb_roles').bootstrapTable('getSelections');
    if (!selects) {
        return e.preventDefault();
    }
    var params = JSON.stringify(selects);
    $.ajax({
        url: '/manage/role/del',
        type: 'delete',
        contentType: 'application/json;charset=utf-8',
        data: params,
        success: function (result) {
            if (result.code == '1') {
                $('#tb_roles').bootstrapTable('refresh', {url: '/manage/role/getRolesPaging'});
            } else {
                $('#warnModal').find('.modal-body').text(result.msg);
                $('#warnModal').modal('show');
            }
        },
        error: function () {
            $('#warnModal').find('.modal-body').text('服务不可用，请稍后再试');
            $('#warnModal').modal('show');
        }
    });
}

//获得所有模块
function getModules() {
    var modules = '';
    $.ajax({
        url: '/manage/module/buildTree',
        type: 'get',
        async: false,
        success: function (data) {
            modules = data;
        }
    });
    return modules;
}

function checkParentNode(tree, node) {
    var parent = tree.treeview('getParent', node);
    if (parent.value != "0") {
        tree.treeview('checkNode', [parent, {silent: true}]);
        checkParentNode(tree, parent);
    }
}

//获得指定节点的子节点
function getChildNodes(node) {
    var nodes = [];
    if (node.nodes) {
        for (var x in node.nodes) {
            nodes.push(node.nodes[x].nodeId);
            if (node.nodes[x].nodes) {
                var getNodeDieDai = getChildNodes(node.nodes[x]);
                for (var j in getNodeDieDai) {
                    nodes.push(getNodeDieDai[j]);
                }
            }
        }
    } else {
        nodes.push(node.nodeId);
    }
    return nodes;
}

var checkedDatas = [];

function getCheckedDatas(tree, node, type) {
    var checkedNodes = getChildNodes(node);
    if (type == 0) {
        tree.treeview('uncheckNode', [checkedNodes, {silent: true}]);
    } else if (type == 1) {
        tree.treeview('checkNode', [checkedNodes, {silent: true}]);
    }
    checkedDatas = [];
    var checkedValues = tree.treeview('getChecked', checkedNodes);
    builderCheckedDatas(checkedValues);
}

//构造已选权限
function builderCheckedDatas(checkedValues) {
    var permissions;
    var options;
    var tmp;
    $.each(checkedValues, function (index, value) {
        var v = value.value;
        var vArray = v.split('-');
        var id = vArray[0];
        var code = vArray[1];
        if (code != undefined) {
            if (id != tmp) {
                permissions = new Object();
                options = new Array;
                checkedDatas.push(permissions);
            }
            options.push(code);
            permissions.moduleId = id;
            permissions.options = options;
            tmp = id;
        }
    });
}

