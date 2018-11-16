<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>公告类型管理</title>
<style type="text/css">
.type:hover
{
	background-color: #aaa;
	cursor: pointer;
}
.type-form
{
	border-left: 1px solid #faa;
	padding-bottom: 10px;
}
.type
{
	padding-right: 0px;
}
.remove-btn
{
	background-color: #fff;
	border: 1px solid green;
	padding-left: 5px;
	padding-right: 5px; 
}
</style>
<script type="text/javascript">
$(function(){
	/* $(".type").bind("click", function(){
	}); */
	$(".type").click(function(){
		// 发生事件的div
		var div = $(this);
		var id = div.attr("data-id");
		var name = div.attr("data-name");
		
		$(".type-form [name='id']").val(id);
		$(".type-form #inputName").val(name);
	});
	
	$(".type").hover(function(){
		var span = $("span", this);
		// 事件发生的时候，会删除hide类
		// 鼠标移开以后，会自动重新加上hide类
		span.toggleClass("hide");
	});
	
	$(".remove-btn").click(function(event){
		// 组织事件传播
		event.stopPropagation();
		var div = $(this).parent();
		var id = div.attr("data-id");
		var url = "${ctx}/notice/type/" + id;
		// 发生DELETE请求删除数据
		// DELETE请求，表示删除URL对应的资源，浏览器不能直接发送
		// 有两种方式发送：使用AJAX发送、使用POST表单模拟发送（Spring MVC扩展的，需要一个名为_method的参数，值为DELETE）
		$.ajax({
			url: url,
			method: "DELETE",// 发生DELETE请求
			success: function(data, status, xhr){
				// 相当于是重定向
				document.location.href="${ctx}/notice/type";
			},
			error: function(data, status, xhr){
				// responseJSON表示返回的JSON对象
				// message是错误信息
				alert(data.responseJSON.message);
			}
		});
	});
});
</script>
</head>
<body>
<div class="container-fluid">
	<div class="panel panel-default">
		<!-- Default panel contents -->
		<div class="panel-heading">
			公告类型管理
			<div class="close">
				<a class="btn btn-default">新增</a>
			</div>
		</div>
		<div class="panel-body">
			<div class="col-sm-12 col-md-4">
				<%-- 公告列表 --%>
				<c:forEach items="${types }" var="r">
				<div class="col-xs-12 type"
					data-id="${r.id }"
					data-name="${r.name }">
					${r.name }
					<span class="pull-right hide remove-btn">x</span>
				</div>
				</c:forEach>
			</div>
			<div class="col-sm-12 col-md-8 type-form">
				<%-- 公告修改的表单 --%>
				<form action="" method="post" class="form-horizontal">
					<input type="hidden" name="id" />
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
					<div class="col-sm-12">
						<div class="form-group">
						    <label for="inputName" class="col-sm-2 control-label">类型名称</label>
						    <div class="col-sm-10">
						        <input type="text" 
						        	class="form-control" 
						        	id="inputName" 
						        	name="name"
						        	required="required"
						        	placeholder="类型名称"/>
						    </div>
						</div>
					</div>
					<div class="col-sm-12 text-right">
						<button class="btn btn-primary">保存</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
</body>
</html> 