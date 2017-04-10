<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="cl"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@include file="messageModals.jsp"%>
<c:set var="ctx" value="<%=request.getContextPath()%>"/>
 <link rel="shortcut icon" href="${ctx }/static/assets/img/favicon.png" type="image/x-icon">


    <script src="${ctx }/static/jedate/jedate.js"></script>
    <script src="${ctx }/static/assets/js/jquery-2.0.3.min.js"></script>
    <script src="${ctx }/static/assets/js/jquery.form.js"></script>
    
    <!--Basic Styles-->
    <link href="${ctx }/static/assets/css/bootstrap.min.css" rel="stylesheet" />
    <link id="bootstrap-rtl-link" href="" rel="stylesheet" />
    <link href="${ctx }/static/assets/css/font-awesome.min.css" rel="stylesheet" />
    <link href="${ctx }/static/assets/css/weather-icons.min.css" rel="stylesheet" />

    <!--Fonts-->
    <link href="https://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,400,600,700,300" rel="stylesheet" type="text/css">
 	
 	<!--Bootstrap Date Picker-->
 	<script src="${ctx }/static/assets/js/bootstrap.min.js"></script>
	<script src="${ctx }/static/assets/js/datetime/bootstrap-datepicker.js"></script>
	<script src="${ctx }/static/assets/js/validation/bootstrapValidator.js"></script>
	
    <!--Beyond styles-->
    <link href="${ctx }/static/assets/css/beyond.min.css" rel="stylesheet" />
    <link href="${ctx }/static/assets/css/demo.min.css" rel="stylesheet" />
    <link href="${ctx }/static/assets/css/typicons.min.css" rel="stylesheet" />
    <link href="${ctx }/static/assets/css/animate.min.css" rel="stylesheet" />
    <link id="skin-link" href="" rel="stylesheet" type="text/css" />

    <!--Page Related styles-->
    <link href="${ctx }/static/assets/css/dataTables.bootstrap.css" rel="stylesheet" />

    <!--Skin Script: Place this script in head to load scripts for skins and rtl support-->
    <script src="${ctx }/static/assets/js/skins.min.js"></script>
    <script src="${ctx }/static/assets/js/jquery.form.js"></script>
    <!-- 图片上传 -->
    <script src="${ctx }/static/assets/js/upload/uploadPic.js"></script>
    
    <script src="${ctx }/static/assets/js/bootbox/bootbox.js"></script>
    <style>
    	.page-body{
    		padding-top: 0px;
    		padding-left: 0px;
    	}
    	.widget{
    		padding-left: 0px;
    	}
    </style>
