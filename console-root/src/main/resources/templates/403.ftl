<!DOCTYPE html>
<html>
<head>
    <#include "common/common.ftl">
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <#include "common/header.ftl">
    <#include "common/left.ftl">
        <div class="content-wrapper">
            <section class="content-header">
                <h1>
                    403错误页面
                </h1>
                <ol class="breadcrumb">
                    <li><a href="/"><i class="fa fa-dashboard"></i> 主页</a></li>
                    <li class="active">403</li>
                </ol>
            </section>
            <section class="content">
                <div class="error-page">
                    <h2 class="headline text-yellow"> 403</h2>
                    <div class="error-content">
                        <h3><i class="fa fa-warning text-yellow"></i> 访问被拒绝.</h3>
                        <p>
                            您没有权限做此操作
                        </p>
                    </div>
                </div>
            </section>
        </div>
    <#include "common/footer.ftl">
</div>
</body>
</html>