<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>编辑公告</title>

</head>
<body>
<div class="container-fluid">
	<div class="panel panel-default">
		<!-- Default panel contents -->
		<div class="panel-heading">
			编辑公告
		</div>
		<form action="${ctx }/notice" onsubmit="return checkContent();" method="post">
			<div class="panel-body">
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				<input type="hidden" name="id" value="${notice.id}"/>
				<div class="form-group">
				    <label for="inputTitle" class="col-sm-2 control-label">标题</label>
				    <div class="col-sm-10">
				        <input type="text" 
				        	class="form-control" 
				        	id="inputTitle" 
				        	name="title"
				        	required="required"
				        	placeholder="公告的标题"
				        	value="${notice.title }"/>
				    </div>
				</div>
				<div class="form-group">
				    <label for="inputTitle" class="col-sm-2 control-label">类型</label>
				    <div class="col-sm-10">
				    	<select name="type.id"
				        	required="required"
				        	class="form-control" >
				        	<option value="">-- 请选择类型 --</option>
				    		<c:forEach items="${types }" var="t">
				    		<option value="${t.id }" ${notice.type.id eq t.id ? 'selected="selected"' : '' }>${t.name }</option>
				    		</c:forEach>
				    	</select>
				    </div>
				</div>
				<div id="noticeContentEditor">${notice.content }</div>
				<textarea type="hidden" name="content" id="noticeContent" style="display: none;">${notice.content }</textarea>
			</div>
			<div class="panel-footer text-right">
				<button class="btn btn-primary">提交</button>
			</div>
		</form>
	</div>
</div>
<script type="text/javascript" src="${ctx }/webjars/wangEditor/3.1.1/release/wangEditor.min.js"></script>
<script type="text/javascript">
	$(function(){
		var E = window.wangEditor
		var editor = new E('#noticeContentEditor')
		//或者 var editor = new E( document.getElementById('editor') )
		// 隐藏“网络图片”tab
		//editor.customConfig.showLinkImg = false
		 // 上传图片到服务器
		editor.customConfig.uploadImgServer = '${ctx}/storage/file/wangEditor';
		//文件上传的字段名
		editor.customConfig.uploadFileName = 'file';
		//403自定义上传的时候请求头内容（被拦截是因为与post）
		editor.customConfig.uploadImgHeaders = {
				'${_csrf.headerName}': '${_csrf.token}'
		} 
		editor.customConfig.onchange = function (html) {
			// 接收改变后的内容，获取富文本编辑器里面的内容，放到#noticeContent里面
			$("#noticeContent").val(html);
		}
		
		// 粘贴图片
		editor.customConfig.pasteIgnoreImg = false;
		
		// 不要过滤复制内容的样式，保持原本的样式，可能有些时候不能完全得到样式，此时可以自定义外观（写CSS）
		editor.customConfig.pasteFilterStyle = false;
		
		//创建编辑器
		editor.create();
	});
	//检查公告的内容是否有输入
	var checkContent = function(){
		//.trim()去掉前后的空格
		var text = $("#noticeContentEditor").text.trim();
		if(text === ""){
			alert("公告内容必须填写");
			return false;
		}
		return true;
		
	};

</script>
</body>
</html> 