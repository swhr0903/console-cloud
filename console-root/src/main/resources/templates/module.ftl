<script src="/static/js/module.js"></script>
<div class="box">
    <div class="box-header" style="height: 30px;font-size: 10px;background-color: #b8c7ce">搜索栏</div>
    <div class="box-body">
        <div class="form-group">
            <div class="col-xs-8 col-sm-3">
                <input id="name" class="form-control" placeholder="请输入模块名" / >
            </div>
            <div class="col-xs-2 col-sm-1" style="text-align:left;">
                <button id="query" class="btn btn-info btn-search">查询</button>
            </div>
        </div>
    </div>
</div>
<div class="box">
    <div id="box_body" class="box-body">
        <div id="toolbar" class="btn-group">
            <button class="btn btn-default dropdown-toggle" type="button" id="dataDomain" data-toggle="dropdown"
                    aria-expanded="false">
                数据导出范围
                <span class="caret"></span>
            </button>
            <ul class="dropdown-menu" aria-labelledby="dataDomain">
                <li><a href="javascript:selDomain('basic','当前数据');">当前数据</a></li>
                <li><a href="javascript:selDomain('selected','选中数据');">选中数据</a></li>
                <li><a href="javascript:selDomain('all','全部数据');">全部数据</a></li>
            </ul>
            <button id="btn_add" type="button" class="btn btn-default">
                <span class="glyphicon glyphicon-plus"></span>新增
            </button>
            <button id="btn_edit" type="button" class="btn btn-default">
                <span class="glyphicon glyphicon-pencil"></span>修改
            </button>
            <button id="btn_delete" type="button" class="btn btn-default">
                <span class="glyphicon glyphicon-remove"></span>删除
            </button>
        </div>
        <table id="tb_modules"></table>
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
                        <label class="col-lg-3 control-label">模块名</label>
                        <div class="col-lg-5">
                            <input id="id" name="id" type="hidden"/>
                            <input id="mName" class="form-control" name="name"/>
                            <div id="existTip" style="color: #a94442;"></div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label">模块URL</label>
                        <div class="col-lg-5">
                            <input id="mUrl" class="form-control" name="url"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label">父模块</label>
                        <div class="col-lg-5">
                            <select id="parent" name="parent" class="lot-tagSelect form-control" data-live-search="true"
                                    title="根模块">
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label">功能项</label>
                        <div class="col-lg-5">
                            <select id="options" name="options" class="selectpicker show-tick form-control"
                                    multiple data-live-search="false">
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label">是否启用</label>
                        <div class="col-lg-5">
                            <select id="status" name="status" class="lot-tagSelect form-control" title="状态">
                                <option value="1">是</option>
                                <option value="0">否</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-lg-9 col-lg-offset-3">
                            <button id="edit" class="btn btn-primary" name="edit" value="edit">提交
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

