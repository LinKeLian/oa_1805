<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- 引用JSP Tag文件 --%>
<%@ taglib prefix="fk" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="application"></c:set>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>角色管理</title>
<style type="text/css">
.role:hover
{
	background-color: #aaa;
	cursor: pointer;
}
.role-form
{
	border-left: 1px solid #faa;
	padding-bottom: 10px;
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
	/* $(".role").bind("click", function(){
	}); */
	$(".role").click(function(event){
		//阻止事件传播
		event.stopPropagation();
		// 发生事件的div
		var div = $(this);
		var id = div.attr("data-id");
		var name = div.attr("data-name");
		var roleKey = div.attr("data-roleKey");
		
		$(".role-form [name='id']").val(id);
		$(".role-form #inputName").val(name);
		$(".role-form #inputRoleKey").val(roleKey);
	});
	
	$(".role").hover(function(){
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
		var url = "${ctx}/identity/role/" + id;
		// 发生DELETE请求删除数据
		// DELETE请求，表示删除URL对应的资源，浏览器不能直接发送
		// 有两种方式发送：使用AJAX发送、使用POST表单模拟发送（Spring MVC扩展的，需要一个名为_method的参数，值为DELETE）
		$.ajax({
			url: url,
			method: "DELETE",// 发生DELETE请求
			success: function(data, status, xhr){
				// 相当于是重定向
				document.location.href="${ctx}/identity/role";
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
			角色管理
			<div class="close">
				<a class="btn btn-default">新增</a>
			</div>
		</div>
		<div class="panel-body">
			<div class="col-sm-12 col-md-4">
				<%-- 角色列表 --%>
				<c:forEach items="${roles }" var="r">
				<div class="col-xs-12 role"
					data-id="${r.id }"
					data-name="${r.name }"
					data-roleKey="${r.roleKey }">
					${r.name }(${r.roleKey })
					<span class="pull-right hide remove-btn">X</span>
				</div>
				</c:forEach>
			</div>
			<div class="col-sm-12 col-md-8 role-form">
				<%-- 角色修改的表单 --%>
				<form action="" method="post" class="form-horizontal">
					<input type="hidden" name="id" />
					
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
					
					<div class="col-sm-12">
						<div class="form-group">
						    <label for="inputName" class="col-sm-2 control-label">角色名称</label>
						    <div class="col-sm-10">
						        <input type="text" 
						        	class="form-control" 
						        	id="inputName" 
						        	name="name"
						        	required="required"
						        	placeholder="角色名称"/>
						    </div>
						</div>
					</div>
					<div class="col-sm-12">
						<div class="form-group">
						    <label for="inputRoleKey" class="col-sm-2 control-label">角色KEY</label>
						    <div class="col-sm-10">
						        <input type="text" 
						        	class="form-control" 
						        	id="inputRoleKey" 
						        	name="roleKey"
						        	required="required"
						        	placeholder="用于角色判断的KEY，唯一"/>
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