function uploadPic() {
	// 构建jquery.form.js需要参数信息
	var options = {

		url : "../upload/upload.do",
		type : "post",
		dataType : "json",
		success : function(data) {
			// 1、图片回显
			$("#allUrl").attr("src", data.allUrl);
			// 2、设置图片的url的隐藏域用于保存到数据库imgUrl
			$("#showImg").val(data.showImg);
		}
	}
	// 通过jquery.form.js构建表单提交
	$("#picUpload").ajaxSubmit(options);
}