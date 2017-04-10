<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="cl"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@include file="../commons/messageModals.jsp"%>
<%@include file="../commons/commons.jsp"%>
<!DOCTYPE html>
<!--
BeyondAdmin - Responsive Admin Dashboard Template build with Twitter Bootstrap 3.2.0
Version: 1.0.0
Purchase: http://wrapbootstrap.com
-->
<html xmlns="http://www.w3.org/1999/xhtml">
<!-- Head -->
<head>
    <meta charset="utf-8" />
    <title>首页</title>

    <meta name="description" content="Dashboard" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    
    <!--Basic Styles-->
	<link href="static/assets/css/bootstrap.min.css" rel="stylesheet" />
	<link id="bootstrap-rtl-link" href="" rel="stylesheet" />
	<link href="static/assets/css/font-awesome.min.css" rel="stylesheet" />
	<link href="static/assets/css/weather-icons.min.css" rel="stylesheet" />
	
	<!--Fonts-->
	<link
		href="https://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,400,600,700,300"
		rel="stylesheet" type="text/css">
	
	<!--Beyond styles-->
	<link href="static/assets/css/beyond.min.css" rel="stylesheet"
		type="text/css" />
	<link href="static/assets/css/demo.min.css" rel="stylesheet" />
	<link href="static/assets/css/typicons.min.css" rel="stylesheet" />
	<link href="static/assets/css/animate.min.css" rel="stylesheet" />
	<link id="skin-link" href="" rel="stylesheet" type="text/css" />
	
	<!--Skin Script: Place this script in head to load scripts for skins and rtl support-->
	<script src="static/assets/js/skins.min.js"></script>
    
    
     <!--Google Analytics::Demo Only-->
    <!--Basic Scripts-->
	<script type="text/javascript">
		function pageTo(iClass,title,url){
			if(url!=null && url!=''){
				$("#imgI").addClass(iClass)
				$("#titleH1").html(title);
				$("#iframepage").attr("src",url);
			}else{
				warningAlert("功能完善中...");
			}
    	}
    
    	//解决当窗口改变大小的时候执行js事件，以让iframe自适就高度
    	function changeFrameHeight(){
		    var ifm = document.getElementById("iframepage"); 
		    ifm.height=document.documentElement.clientHeight;
		    var pageHeaderDiv = $("#pageHeaderDiv").css("width").replace("px","");
		    ifm.width= parseInt(pageHeaderDiv);
		}
		//window.onresize的作用就是当窗口大小改变的时候会触发这个事件。
		window.onresize=function(){  
		     changeFrameHeight();  
		} 
	</script>
</head>
<!-- /Head -->
<!-- Body -->
<body>
    <!-- Loading Container -->
    <div class="loading-container">
        <div class="loading-progress">
            <div class="rotator">
                <div class="rotator">
                    <div class="rotator colored">
                        <div class="rotator">
                            <div class="rotator colored">
                                <div class="rotator colored"></div>
                                <div class="rotator"></div>
                            </div>
                            <div class="rotator colored"></div>
                        </div>
                        <div class="rotator"></div>
                    </div>
                    <div class="rotator"></div>
                </div>
                <div class="rotator"></div>
            </div>
            <div class="rotator"></div>
        </div>
    </div>
    <!--  /Loading Container -->
    <!-- Navbar -->
    <div class="navbar">
        <div class="navbar-inner">
            <div class="navbar-container">
                <!-- Navbar Barnd -->
                <div class="navbar-header pull-left">
                    <a href="#" class="navbar-brand">
                        <small>
                            <img src="static/assets/img/logo.png" alt="" />
                        </small>
                    </a>
                </div>
                <!-- /Navbar Barnd -->

                <!-- Sidebar Collapse -->
                <div class="sidebar-collapse" id="sidebar-collapse">
                    <i class="collapse-icon fa fa-bars"></i>
                </div>
                <!-- /Sidebar Collapse -->
                <!-- Account Area and Settings --->
                <div class="navbar-header pull-right">
                    <div class="navbar-account">
                        <ul class="account-area">
                            <li>
                                <a class="login-area dropdown-toggle" data-toggle="dropdown">
                                    <div class="avatar" title="View your public profile">
                                        <img src="static/assets/img/avatars/adam-jansen.jpg">
                                    </div>
                                    <section>
                                        <h2><span class="profile"><span>您好，${USER.userName }</span></span></h2>
                                    </section>
                                </a>
                                <!--Login Area Dropdown-->
                                <ul class="pull-right dropdown-menu dropdown-arrow dropdown-login-area">
                                    <li class="username"><a>${USER.userName }</a></li>
                                    <!--Avatar Area-->
                                    <li>
                                        <div class="avatar-area">
                                            <img src="static/assets/img/avatars/adam-jansen.jpg" class="avatar">
                                            <span class="caption">${USER.userName }</span>
                                        </div>
                                    </li>
                                    <li>
                                        <a href="${ctx }/logout.do">退出</a>
                                    </li>
                                </ul>
                            </li>
                        </ul>
                    </div>
                </div>
                <!-- /Account Area and Settings -->
            </div>
        </div>
    </div>
    <!-- /Navbar -->
    <!-- Main Container -->
    <div class="main-container container-fluid">
        <!-- Page Container -->
        <div class="page-container">
            <!-- Page Sidebar -->
            <div class="page-sidebar" id="sidebar">
			    <!-- Page Sidebar Header-->
			    <div class="sidebar-header-wrapper">
			        <input type="text" class="searchinput" />
			        <i class="searchicon fa fa-search"></i>
			        <div class="searchhelper">Search Reports, Charts, Emails or Notifications</div>
			    </div>
			    <ul class="nav sidebar-menu">
			        <li>
			            <a href="javascript:pageTo('menu-icon glyphicon glyphicon-home','首页','')"> <i class="menu-icon glyphicon glyphicon-home"></i> <span class="menu-text"> 首页 </span> </a>
			        </li>
			        <li>
			            <a href="javascript:pageTo('menu-icon fa fa-user-o','用户管理','adminMgt/list.do')">
			            	<i class="menu-icon fa fa-picture-o"></i>
			                <span class="menu-text">用户管理 </span>
		                </a>
			        </li>
			    </ul>
			    <!-- /Sidebar Menu -->
			</div>

            <!-- /Page Sidebar -->
            <!-- Page Content -->
            <div class="page-content">
                <!-- Page Breadcrumb -->
                <%-- 
                <div class="page-breadcrumbs">
                    <ul class="breadcrumb">
                        <li>
                            <i class="fa fa-home"></i>
                            <a href="#">Home</a>
                        </li>
                        <li class="active">Dashboard</li>
                    </ul>
                </div>
                --%>
                <!-- /Page Breadcrumb -->
                <!-- Page Header -->
                <div id="pageHeaderDiv" class="page-header position-relative">
                    <div class="header-title">
                    	<i id="imgI" class="menu-icon glyphicon glyphicon-home"></i>
                        <h1 id="titleH1">首页</h1>
                    </div>
                    <!--Header Buttons-->
                    <div class="header-buttons">
                        <a class="sidebar-toggler" href="#">
                            <i class="fa fa-arrows-h"></i>
                        </a>
                        <a class="refresh" id="refresh-toggler" href="">
                            <i class="glyphicon glyphicon-refresh"></i>
                        </a>
                        <a class="fullscreen" id="fullscreen-toggler" href="#">
                            <i class="glyphicon glyphicon-fullscreen"></i>
                        </a>
                    </div>
                    <!--Header Buttons End-->
                </div>
                <!-- /Page Header -->
                <!-- Page Body -->
                 <div class="page-body"  style="padding-left: 10px;padding-top: 10px;">
                    <div class="row">
	                    <div class="col-lg-12 col-sm-12 col-xs-12">
	                    	<iframe src="" id="iframepage" scrolling="no" onload="changeFrameHeight()" frameborder="0"></iframe>
	                    </div>
                    </div>
                 </div>
                <!-- /Page Body -->
            </div>
            <!-- /Page Content -->
        </div>
        <!-- /Page Container -->
        <!-- Main Container -->

    </div>

	<!--Beyond Scripts-->
	<script src="static/assets/js/beyond.min.js"></script>
	
	
	<!--Page Related Scripts-->
	<!--Sparkline Charts Needed Scripts-->
	<script src="static/assets/js/charts/sparkline/jquery.sparkline.js"></script>
	<script src="static/assets/js/charts/sparkline/sparkline-init.js"></script>
	
	<!--Easy Pie Charts Needed Scripts-->
	<script src="static/assets/js/charts/easypiechart/jquery.easypiechart.js"></script>
	<script src="static/assets/js/charts/easypiechart/easypiechart-init.js"></script>
	
	<!--Flot Charts Needed Scripts-->
	<script src="static/assets/js/charts/flot/jquery.flot.js"></script>
	<script src="static/assets/js/charts/flot/jquery.flot.resize.js"></script>
	<script src="static/assets/js/charts/flot/jquery.flot.pie.js"></script>
	<script src="static/assets/js/charts/flot/jquery.flot.tooltip.js"></script>
	<script src="static/assets/js/charts/flot/jquery.flot.orderBars.js"></script>
    <script>
        (function (i, s, o, g, r, a, m) {
            i['GoogleAnalyticsObject'] = r; i[r] = i[r] || function () {
                (i[r].q = i[r].q || []).push(arguments)
            }, i[r].l = 1 * new Date(); a = s.createElement(o),
            m = s.getElementsByTagName(o)[0]; a.async = 1; a.src = g; m.parentNode.insertBefore(a, m)
        })(window, document, 'script', 'http://www.google-analytics.com/analytics.js', 'ga');

        ga('create', 'UA-52103994-1', 'auto');
        ga('send', 'pageview');

    </script>

</body>
<!--  /Body -->
</html>
