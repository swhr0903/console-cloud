<!DOCTYPE html>
<html>
<head>
<#--<style>
        img {
            width: 100%;
            height: 100%;
        }
    </style>-->
</head>
<body>
<#--<div><img src="/static/img/a.jpg"/></div>-->
<section class="content">
    <div class="row">
        <div class="col-md-6">
            <div class="box box-primary">
                <div class="box-header with-border">
                    <h3 class="box-title">注册</h3>
                    <div class="box-tools pull-right">
                        <button type="button" class="btn btn-box-tool" data-widget="collapse"><i
                                class="fa fa-minus"></i>
                        </button>
                        <button type="button" class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i>
                        </button>
                    </div>
                </div>
                <div class="box-body">
                    <div class="chart">
                        <canvas id="regiStat" style="height:250px"></canvas>
                    </div>
                </div>
            </div>
            <div class="box box-danger">
                <div class="box-header with-border">
                    <h3 class="box-title">Donut Chart</h3>
                    <div class="box-tools pull-right">
                        <button type="button" class="btn btn-box-tool" data-widget="collapse"><i
                                class="fa fa-minus"></i>
                        </button>
                        <button type="button" class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i>
                        </button>
                    </div>
                </div>
                <div class="box-body">
                    <canvas id="pieChart" style="height:250px"></canvas>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="box box-info">
                <div class="box-header with-border">
                    <h3 class="box-title">充值</h3>
                    <div class="box-tools pull-right">
                        <button type="button" class="btn btn-box-tool" data-widget="collapse"><i
                                class="fa fa-minus"></i>
                        </button>
                        <button type="button" class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i>
                        </button>
                    </div>
                </div>
                <div class="box-body">
                    <div class="chart">
                        <canvas id="depositWStat" style="height:250px"></canvas>
                    </div>
                </div>
            </div>
            <div class="box box-success">
                <div class="box-header with-border">
                    <h3 class="box-title">Bar Chart</h3>
                    <div class="box-tools pull-right">
                        <button type="button" class="btn btn-box-tool" data-widget="collapse"><i
                                class="fa fa-minus"></i>
                        </button>
                        <button type="button" class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i>
                        </button>
                    </div>
                </div>
                <div class="box-body">
                    <div class="chart">
                        <canvas id="barChart" style="height:250px"></canvas>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<script src="/static/bootstrap/js/Chart.min.js"></script>
<script src="/static/js/welcome.js"></script>
</body>
</html>