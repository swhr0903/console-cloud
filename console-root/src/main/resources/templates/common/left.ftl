<aside class="main-sidebar">

    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">

        <!-- Sidebar user panel (optional) -->
        <div class="user-panel">
            <div class="pull-left image">
                <img src="<#if avatar??>${avatar}<#else>/static/bootstrap/img/user2-160x160.jpg</#if>"
                     class="img-circle" alt="User Image">
            </div>
            <div class="pull-left info">
                <p>${user}</p>
                <!-- Status -->
                <a id="isOnline" href="#">
                </a>
            </div>
        </div>

        <!-- search form (Optional) -->
        <!--<form action="#" method="get" class="sidebar-form">
            <div class="input-group">
                <input type="text" name="q" class="form-control" placeholder="Search...">
                <span class="input-group-btn">
                        <button type="submit" name="search" id="search-btn" class="btn btn-flat"><i
                                class="fa fa-search"></i>
                        </button>
                    </span>
            </div>
        </form>-->
        <!-- /.search form -->

        <!-- Sidebar Menu -->
        <ul class="sidebar-menu">
            <li class="header">导航栏</li>
            ${menus}
        </ul>
        <!-- /.sidebar-menu -->
    </section>
    <!-- /.sidebar -->
</aside>
<script>
    function isOnline() {
        if (navigator.onLine) {
            $('#isOnline').html('<i class="fa fa-circle text-success"></i> 在线 ');
        } else {
            $('#isOnline').html('<i class="fa fa-circle text-fail"></i> 离线 ');
        }
    }

    setInterval(isOnline, 1000);
</script>