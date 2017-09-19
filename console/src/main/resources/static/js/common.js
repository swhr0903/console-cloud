$(function () {
    //模态窗口居中
    function reposition() {
        var modal = $(this);
        dialog = modal.find('.modal-dialog');
        modal.css('display', 'block');
        dialog.css('margin-top', Math.max(0, ($(window).height() - dialog.height()) / 2));
    }

    $('.modal').on('show.bs.modal', reposition);
    $(window).on('resize', function () {
        $('.modal:visible').each(reposition);
    });

    var oFileInput = new FileInput();
    oFileInput.Init('user_img', '/user/uploadImg');
});

//表格数据日期格式化
var timeFormater = function (value, row, index) {
    if (index >= 0) {
        return moment(value).format('YYYY-MM-DD HH:mm:ss');
    }
};

//表格数据状态格式化
var statusFormater = function (value) {
    if (value == '1') {
        return '是';
    } else {
        return '否';
    }
};

//Form数据绑定
$.fn.setForm = function (data) {
    var obj = this;
    for (var i = 0; i < data.length; i++) {
        for (var name in data[i]) {
            if (name == '0') {
                continue;
            }
            var val = data[i][name];
            var $oinput = obj.find('input[name=' + name + ']');
            if ($oinput.is('input')) {
                if ($oinput.attr('type') == 'checkbox') {
                    if (val !== null) {
                        var checkboxObj = $('[name=' + name + ']');
                        var checkArray = val.split(';');
                        for (var i = 0; i < checkboxObj.length; i++) {
                            for (var j = 0; j < checkArray.length; j++) {
                                if (checkboxObj[i].value == checkArray[j]) {
                                    checkboxObj[i].click();
                                }
                            }
                        }
                    }
                } else if ($oinput.attr('type') == 'radio') {
                    $oinput.each(function () {
                        var radioObj = $('[name=' + name + ']');
                        for (var i = 0; i < radioObj.length; i++) {
                            if (radioObj[i].value == val) {
                                radioObj[i].click();
                            }
                        }
                    });
                } else if ($oinput.attr('type') == 'textarea') {
                    obj.find('[name=' + name + ']').html(val);
                } else {
                    obj.find('[name=' + name + ']').val(val);
                }
            }
            var $oselect = obj.find('select[name=' + name + '] option');
            if ($oselect.is('option')) {
                $oselect.each(function () {
                    if (val == $(this).val() || val == $(this).text()) {
                        $('#' + name + '').selectpicker('val', '');
                        $('#' + name + '').selectpicker('val', $(this).val());
                    }
                });
            }
        }
    }
}

var FileInput = function () {
    var oFile = new Object();
    oFile.Init = function (ctrlName, uploadUrl) {
        var control = $('#' + ctrlName);
        control.fileinput({
            language: 'zh', //设置语言
            uploadUrl: uploadUrl, //上传的地址
            allowedFileExtensions: ['jpg', 'gif', 'png'],//接收的文件后缀
            showUpload: true, //是否显示上传按钮
            showCaption: false,//是否显示标题
            browseClass: 'btn btn-primary', //按钮样式
            //dropZoneEnabled: false,//是否显示拖拽区域
            //minImageWidth: 50, //图片的最小宽度
            //minImageHeight: 50,//图片的最小高度
            //maxImageWidth: 1000,//图片的最大宽度
            //maxImageHeight: 1000,//图片的最大高度
            //maxFileSize: 0,//单位为kb，如果为0表示不限制文件大小
            //minFileCount: 0,
            maxFileCount: 10, //表示允许同时上传的最大文件个数
            enctype: 'multipart/form-data',
            validateInitialCount: true,
            previewFileIcon: '<i class="glyphicon glyphicon-king"></i>',
            msgFilesTooMany: '选择上传的文件数量({n}) 超过允许的最大数值{m}！',
        });
    };
    return oFile;
};
