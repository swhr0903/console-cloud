<script src="/static/js/user.js"></script>
<div class="box">
    <div class="box-header" style="height: 30px;font-size: 10px;background-color: #b8c7ce">搜索栏</div>
    <div class="box-body">
        <div class="form-group">
            <div class="col-xs-12 col-sm-3" style="margin-top:10px">
                <input id="startTime" class="form-control form_datetime" readonly
                       placeholder="请输入开始时间" / >
                <span class="add-on"><i class="icon-remove"></i></span>
                <span class="add-on"><i class="icon-th"></i></span>
            </div>
            <div class="col-xs-12 col-sm-3" style="margin-top:10px">
                <input id="endTime" class="form-control form_datetime" readonly
                       placeholder="请输入截止时间" / >
                <span class="add-on"><i class="icon-calendar"></i></span>
            </div>
            <div class="col-xs-12 col-sm-3" style="margin-top:10px">
                <input id="username" class="form-control"
                       placeholder="请输入帐号" / >
            </div>
            <div class="col-xs-1 col-sm-1" style="margin-top:10px">
                <button id="query" class="btn btn-info btn-search">查询</button>
            </div>
        </div>
    </div>
</div>
<div class="box">
    <div id="box_body" class="box-body">
        <div id="toolbar" class="btn-group">
        <#--<button id="btn_add" type="button" class="btn btn-default" data-toggle="modal" data-target="#addModal">
            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>新增
        </button>-->
            <button id="btn_edit" type="button" class="btn btn-default">
                <span class="glyphicon glyphicon-pencil"></span>修改
            </button>
            <button id="btn_delete" type="button" class="btn btn-default">
                <span class="glyphicon glyphicon-remove"></span>删除
            </button>
            <button id="btn_auth" type="button" class="btn btn-default">
                <span class="glyphicon glyphicon-alert"></span>授予角色
            </button>
        </div>
        <table id="tb_users"></table>
    </div>
</div>

<!-- 编辑 -->
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
                        <label class="col-lg-3 control-label">帐号</label>
                        <div class="col-lg-5">
                            <input id="id" name="id" type="hidden"/>
                            <input id="usernameAdd" class="form-control" name="username"/>
                            <div id="existTip" style="color: #a94442;"></div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label">姓名</label>
                        <div class="col-lg-5">
                            <input id="name" class="form-control" name="name"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label">邮箱</label>
                        <div class="col-lg-5">
                            <input id="email" class="form-control" name="email"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-lg-9 col-lg-offset-3">
                            <button id="edit" type="submit" class="btn btn-primary" name="signup" value="Sign up">提交
                            </button>
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
                <h4 class="modal-title" id="myModalLabel">授予角色</h4>
            </div>
            <div class="modal-body">
                <form id="authForm" class="form-horizontal">
                    <div class="form-group">
                        <div class="col-lg-6">
                            <select id="role" name="role" class="selectpicker show-tick form-control"
                                    multiple data-live-search="false">
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-lg-2">
                            <button id="auth" class="btn btn-primary">提交</button>
                            <div id="errorAuthTip" style="color: #a94442;"></div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

