<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../commons/commons.jsp"%>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<!--Head-->
<script type="text/javascript">
	//如果不是顶级窗口，则先获取顶级窗口，再跳转到登录界面
	if(!(window.parent == window)){
		var topWin= (function (p,c){
		    while(p!=c){
		        c = p        
		        p = p.parent
		    }
		    return c
		})(window.parent,window);
		topWin.location.href='${ctx}' + "/welcome.do";
	}
</script>
<head>
<title>登录</title>
</head>
<!--Head Ends-->
<!--Body-->
<body>
	<div class="login-container animated fadeInDown">
		<div class="loginbox bg-white" style="height: 220px!important;">
			<div class="loginbox-title">登录</div>
			<div class="loginbox-textbox">
				<input type="text" class="form-control" id="phone"
					placeholder="passport" />
			</div>
			<div class="loginbox-textbox">
				<input type="password" class="form-control" id="password"
					placeholder="Password" />
			</div>
			<div class="loginbox-forgot">
				<span class="error_msg" style="color:red"> </span>
			</div>
			<div class="loginbox-submit" >
				<input type="button" class="btn btn-primary btn-block" value="Login">
			</div>
<!-- 			<div class="loginbox-signup"> -->
<!-- 				<a href="register.html">Forgot Password?</a> -->
<!-- 			</div> -->
		</div>
	</div>

	<!--Google Analytics::Demo Only-->
	<script>
		$("body").keydown(function() {
				    if (event.keyCode == "13") {//keyCode=13是回车键
				        $('.btn-block').click();
				    }
				});  
        (function (i, s, o, g, r, a, m) {
            i['GoogleAnalyticsObject'] = r; i[r] = i[r] || function () {
                (i[r].q = i[r].q || []).push(arguments)
            }, i[r].l = 1 * new Date(); a = s.createElement(o),
            m = s.getElementsByTagName(o)[0]; a.async = 1; a.src = g; m.parentNode.insertBefore(a, m)
        })(window, document, 'script', 'http://www.google-analytics.com/analytics.js', 'ga');

        ga('create', 'UA-52103994-1', 'auto');
        ga('send', 'pageview');
		
        
        $('.btn-block').click(function(){
        	var phone = $('#phone').val();
        	var password = $('#password').val();
        	if(!phone){
        		$('.error_msg').html('帐号不能为空！');
        		return false;
        	}
        	if(!password){
        		$('.error_msg').html('密码不能为空！');
        		return false;
        	}
        	var sendData = {
    				"passport": phone,
    				"passwd": password
    			};
    			$.post("getLogin.do",sendData, function(backData) {
    				if(backData.status == 1){
    					window.location.href = "home.do";
    				} else{
    					$('.error_msg').html(backData.message[0].msg);
    				}
    			});
    		  
        })
    </script>
</body>
<!--Body Ends-->
</html>
