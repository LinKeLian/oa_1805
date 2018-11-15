<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- 引用JSP Tag文件 --%>
<%@ taglib prefix="fk" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>文件管理</title>
</head>
<body>
<div class="container-fluid">
	<div class="panel panel-default">
		<!-- Default panel contents -->
		<div class="panel-heading">
			文件管理
			<div class="close">
				<a class="btn btn-default">新增</a>
			</div>
		</div>
		<div class="panel-body">
			<!-- Table -->
			<table class="table table-hover table-striped">
				<thead>
					<tr>
						<th>文件名</th>
						<th>文件类型</th>
						<th>文件大小</th>
						<th>上传时间</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${page.content }" var="i">
						<tr>
							<td>${i.name }</td>
							<td>${i.contentType }</td>
							<td>${i.fileSize }</td>
							<td>${i.uploadTime }</td>
							<td>
								下载
								删除
							</td>
						</tr>
					</c:forEach>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="5" style="text-align: center;"	
						<%-- 前缀随便写，关键要跟taglib指令的前缀要一致，冒号后面的则直接使用JSP Tag文件的名称 --%>
						<fk:page url="/storage/file"  page="${page }"/>
						</td>
					</tr>
				</tfoot>
			</table>
		</div>
	</div>
</div>

	<form action="" method="post" enctype="multipart/form-data">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		  <div class="form-group">
		    <label for="exampleInputEmail1">Email address</label>
		    <input type="email" class="form-control" id="exampleInputEmail1" placeholder="Enter email">
		  </div>
		  <div class="form-group">
		    <label for="exampleInputPassword1">Password</label>
		    <input type="password" class="form-control" id="exampleInputPassword1" placeholder="Password">
		  </div>
		  <div class="form-group">
		    <label for="exampleInputFile">File input</label>
		    <input type="file" id="exampleInputFile">
		    <p class="help-block">自己的文件</p>
		  </div>
		  
		  <button type="submit" class="btn btn-default">上传</button>
	</form>

</body>
</html>