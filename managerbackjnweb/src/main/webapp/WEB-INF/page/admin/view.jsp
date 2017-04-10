<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../commons/commons.jsp"%>
<!DOCTYPE html>

<head>
    <meta charset="utf-8" />
    <title>Data Tables</title>

    
</head>
<body>
<div class="page-body">
    <div class="row">
        <div class="col-xs-12 col-md-12">
            <div class="widget">
                <div class="widget-header ">
                    <span class="widget-caption">
        				<c:if test="${empty bean.id}">新增</c:if>            
                    	<c:if test="${!empty bean.id}">编辑</c:if>
                    </span>
                   
                </div>
                <div class="widget-body">
                    <form id="saveOrUpdate" class="form-horizontal form-bordered" role="form" enctype="multipart/form-data" method="post" action="${ ctx}/adminMgt/save.do">
                    	<input type="hidden" name="id" value="${bean.id }"/>
                    	<div class="form-group">
	                        <label for="inputEmail3" class="col-sm-2 control-label no-padding-right">用户名</label>
	                        <div class="col-sm-10">
	                        	<c:if test="${empty bean.id}"><input type="text" class="form-control" id="userName" name="userName" value="${bean.userName }" placeholder="用户名" data-bv-notempty="true"
	                                          data-bv-notempty-message="用户名不能为空"/></c:if>            
                    			<c:if test="${!empty bean.id}">${bean.userName }</c:if>
	                        	
	                        </div>
                        </div>
                    	<div class="form-group">
	                        <label for="inputEmail3" class="col-sm-2 control-label no-padding-right">邮箱</label>
	                        <div class="col-sm-10">
	                        	<input type="text" class="form-control" id="email" name="email" value="${bean.email }" placeholder="邮箱" data-bv-notempty="true"
	                                          data-bv-notempty-message="邮箱不能为空"/>
	                        </div>
                        </div>
                        
                        <div class="form-group">
	                         <label for="inputPassword3" class="col-sm-2 control-label no-padding-right">密码</label>
	                         <div class="col-sm-10">
	                             <input type="password" class="form-control" id="password" name="password" value="${bean.password }" placeholder="密码"
	                           			data-bv-notempty="true"
	                                    data-bv-notempty-message="密码不能为空"/>
                            </div>
                        </div>
                       <!--<div class="form-group">
	                         <label for="inputPassword3" class="col-sm-2 control-label no-padding-right">确认密码</label>
	                         <div class="col-sm-10">
	                             <input type="text" class="form-control" id="submitUserPwd"  placeholder="确认密码"
	                           			data-bv-notempty="true"
	                                    data-bv-notempty-message="确认密码不能为空"/>
                            </div>
                        </div>  -->
                            <div class="form-group">
                                <div class="col-sm-offset-2 col-sm-10">
                                    <a class="btn btn-palegreen" href="javascript:save()">提交</a>
                                    <a class="btn btn-palegreen" href="${ctx}/adminMgt/list.do">返回</a>
                                </div>
                     	</div>
                 	</form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script>
	$(document).ready(function () {
	    $('#picUpload').bootstrapValidator();
	});
	
	var save =function(){
		if(!$('#userName')){
			warningAlert("请输入用户名");
			return;
		}
		if(!$('#email')){
			warningAlert("请输入邮箱");
			return;
		}
		if(!$('#password')){
			warningAlert("请输入密码");
			return;
		}
		else{
			$("#saveOrUpdate").ajaxSubmit(function(result) {
				  if(result.status == 1){
					  window.location.href = '${ctx}' + "/adminMgt/list.do";
				  }else{
					  warningAlert(result.message[0].msg);
				  }
				});
			return false;
		}
	}

</script>
</html>
