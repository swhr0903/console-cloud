<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>大西洋管理后台</title>
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
<link rel="stylesheet" href="/static/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="/static/bootstrap/css/AdminLTE.min.css">
<link rel="stylesheet" href="/static/bootstrap/css/bootstrap-table.min.css">
<link rel="stylesheet" href="/static/bootstrap/css/bootstrap-select.min.css">
<link rel="stylesheet" href="/static/bootstrap/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="/static/bootstrap/css/bootstrap-treeview.min.css">
<link rel="stylesheet" href="/static/bootstrap/css/font-awesome.min.css">
<link rel="stylesheet" href="/static/bootstrap/css/skin-blue.min.css">
<link rel="stylesheet" href="/static/bootstrap/css/bootstrapValidator.min.css">
<link rel="stylesheet" href="/static/bootstrap/css/fileinput.min.css">
<script src="/static/jquery/jquery-3.2.1.min.js"></script>
<script src="/static/jquery/jquery.cookie.js"></script>
<script src="/static/bootstrap/js/bootstrap.min.js"></script>
<script src="/static/bootstrap/js/adminlte.min.js"></script>
<script src="/static/bootstrap/js/app.min.js"></script>
<script src="/static/bootstrap/js/bootstrap-table.min.js"></script>
<script src="/static/bootstrap/js/bootstrap-select.min.js"></script>
<script src="/static/bootstrap/js/bootstrap-table-zh-CN.min.js"></script>
<script src="/static/bootstrap/js/bootstrapValidator.min.js"></script>
<script src="/static/bootstrap/js/bootstrap-datetimepicker.min.js"></script>
<script src="/static/bootstrap/js/bootstrap-treeview.min.js"></script>
<script src="/static/bootstrap/js/fileinput.min.js"></script>
<script src="/static/bootstrap/js/bootstrap-table-export.js"></script>
<script src="/static/bootstrap/js/tableExport.js"></script>
<script src="/static/js/moment.min.js"></script>
<script src="/static/js/common.js"></script>
<!-- 提示框 -->
<div class="modal fade" id="warnModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title" id="warnModalLabel">提示</h4>
            </div>
            <div class="modal-body"></div>
        </div>
    </div>
</div>
<!-- 确认框 -->
<div class="modal fade" id="sureModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title" id="sureModalLabel">确认</h4>
            </div>
            <div class="modal-body"></div>
            <div class="modal-footer">
                <input type="hidden" id="url"/>
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <a id="sure" class="btn btn-success" data-dismiss="modal">确定</a>
            </div>
        </div>
    </div>
</div>
