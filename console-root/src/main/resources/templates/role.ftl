<script src="/static/js/role.js"></script>
<div class="box">
    <div class="box-header" style="height: 30px;font-size: 10px;background-color: #b8c7ce">搜索栏</div>
    <div class="box-body">
        <div class="form-group">
            <div class="col-xs-8 col-sm-3">
                <input id="name" class="form-control" placeholder="请输入角色名" / >
            </div>
            <div class="col-xs-2 col-sm-1">
                <button id="query" class="btn btn-info btn-search">查询</button>
            </div>
        </div>
    </div>
</div>
<div class="box">
    <div id="box_body" class="box-body">
        <div id="toolbar" class="btn-group">
            <button id="btn_add" type="button" class="btn btn-default">
                <span class="glyphicon glyphicon-plus"></span>新增
            </button>
            <button id="btn_edit" type="button" class="btn btn-default">
                <span class="glyphicon glyphicon-pencil"></span>修改
            </button>
            <button id="btn_delete" type="button" class="btn btn-default">
                <span class="glyphicon glyphicon-remove"></span>删除
            </button>
            <button id="btn_auth" type="button" class="btn btn-default">
                <span class="glyphicon glyphicon-alert"></span>授权
            </button>
        </div>
        <table id="tb_roles"></table>
    </div>
</div>

<!-- 新增修改 -->
<div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title" id="myModalLabel">修改</h4>
            </div>
            <div class="modal-body">
                <form id="editForm" class="form-horizontal">
                    <div class="form-group">
                        <label class="col-lg-3 control-label">角色代码</label>
                        <div class="col-lg-5">
                            <input id="id" name="id" type="hidden"/>
                            <input id="rCode" class="form-control" name="code"/>
                            <div id="existTip" style="color: #a94442;"></div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label">角色名</label>
                        <div class="col-lg-5">
                            <input id="rName" class="form-control" name="name"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label">是否启用</label>
                        <div class="col-lg-5">
                            <select id="rStatus" name="status" class="lot-tagSelect form-control" title="状态">
                                <option value="1">是</option>
                                <option value="0">否</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-lg-9 col-lg-offset-3">
                            <button id="edit" class="btn btn-primary" name="edit" value="edit">提交</button>
                            <button type="button" class="btn btn-info" id="reset">重置表单</button>
                            <div id="errorEditTip" style="color: #a94442;"></div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- 授权 -->
<div class="modal fade" id="authModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title" id="myModalLabel">授权</h4>
            </div>
            <div class="modal-body">
                <form id="authForm" class="form-horizontal">
                    <#--<div class="form-group">
                        <div class="col-lg-4">
                            <button type="button" class="btn btn-info" id="all">全选</button>
                            <button type="button" class="btn btn-info" id="nobody">全不选</button>
                        </div>
                    </div>-->
                    <div id="treeview-checkable" class=""></div>
                    <div class="form-group">
                        <div class="col-lg-2">
                            <input id="roleId" name="roleId" type="hidden"/>
                            <button id="auth" class="btn btn-primary">提交</button>
                            <div id="errorAuthTip" style="color: #a94442;"></div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

