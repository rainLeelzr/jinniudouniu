<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../commons/commons.jsp"%>
<!DOCTYPE html>
<head>
<meta charset="utf-8" />
<title>后台</title>
<script src="${ctx }/static/js/crud.js"></script>
</head>

<body>
	<form action="${ctx }/adminMgt/list.do" id="submit-form">
	<div class="page-body">
		<div class="row">
			<div class="col-xs-12 col-md-12">
				<div class="widget">
					<div class="widget-header ">
						<span class="widget-caption">后台用户</span>
					</div>
					<div class="widget-body">
                        <div class="controls">
                             <div class="input-group">
								<input type="text" class="form-control" style="width:200px;margin-right: 5px;" value="${bean.userName }" name="userName" placeholder="用户名">
								<button type="submit" class="btn btn-default" style="height:34px" onClick="search()">搜索</button>
                             </div>
                    	</div>
                    </div>
                     <div class="table-toolbar">
						<a id="editabledatatable_new" href="${ctx }/adminMgt/add.do"
							class="btn btn-default">添加用户 </a>
					</div>
						<table class="table table-striped table-bordered table-hover"
							id="simpledatatable">
							<thead>
								<tr>
									<th>邮箱</th>
									<th>用户名</th>
									<th>ip</th>
									<th>最后登录时间</th>
									<th>注册时间</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								 <c:forEach items="${pagination.rows }" var="item">
                                      	   <%--  <input type="hidden" name="id" value="${"lottery.openTime }"/> --%>
                                            <tr>
	                                      	    
                                                <td>${item.email }</td>
                                                <td>${item.userName }</td>
                                                <td>${item.lastIp }</td>
                                                <td>
	                                            	<fmt:formatDate value="${item.lastLogin }" type="date" pattern="yyyy-MM-dd HH:mm" />
	                                            </td>
                                                <td>
	                                            	<fmt:formatDate value="${item.addTime }" type="date" pattern="yyyy-MM-dd HH:mm" />
	                                            </td>
	                                            <td>
	                                            	<a href="javascript:void(0)" class="btn btn-info btn-xs edit" onclick="edit('adminMgt','${item.id}')"><i class="fa fa-edit"></i>编辑</a>
	                                            	<a href="javascript:void(0)" class="btn btn-danger btn-xs delete" onclick="del('adminMgt','${item.id}')"><i class="fa fa-trash-o"></i>删除</a>
	                                            </td>
                                            </tr>
                                      	  </c:forEach>
							</tbody>
						</table>
						<%@include file="../commons/page.jsp"%>
					</div>
				</div>
			</div>
		</div>
	</form>

</body>
<script type="text/javascript">
	function search(){
		$("#submit-form").submit();
	}
	var del = function(requesturl, id){
		bootbox.confirm("你确定删除吗?", function (result) {
	        if (result) {
	        	var url = '${ctx }' + "/" + requesturl + "/del.do";
	    		var sendData = {
	    				"id": id
	    			};
	    			$.post(url,sendData, function(backData) {
	    				if(backData.status == 1){
	    					$("#submit-form").submit();
	    				} else{
	    					warningAlert(backData.message[0].msg);
	    				}
	    			});
	        }
	    });
	}

	var edit = function(url, id){
		 window.location.href = '${ctx }' + "/" + url + "/edit.do?id="+id;
	}
</script>
</html>