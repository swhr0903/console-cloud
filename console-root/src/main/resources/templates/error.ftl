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
                错误页面
            </h1>
            <ol class="breadcrumb">
                <li><a href="/"><i class="fa fa-dashboard"></i> 主页</a></li>
                <li class="active">500</li>
            </ol>
        </section>
        <section class="content">
            <div class="error-page">
                <h2 class="headline text-yellow"> 500</h2>
                <div class="error-content">
                    <h3><i class="fa fa-warning text-yellow"></i> ${url}</h3>
                    <p>
                        ${exception.message}
                    </p>
                </div>
            </div>
        </section>
    </div>
    <#include "common/footer.ftl">
</div>
</body>
</html>