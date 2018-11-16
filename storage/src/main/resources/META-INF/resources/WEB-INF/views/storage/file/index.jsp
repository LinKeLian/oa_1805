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
				<a class="btn btn-default" data-toggle="modal" data-target=".file-upload-dialog">新增</a>
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
								<a href="${ctx}/storage/file/${i.id}">下载</a>
								<a href="javascript:deleteFile('${i.id }')">删除</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="5" style="text-align: center;">
							<%-- 前缀随便写，关键要跟taglib指令的前缀要一致，冒号后面的则直接使用JSP Tag文件的名称 --%>
							<fk:page url="/storage/file?keyword=${param.keyword }" page="${page }"/>
						</td>
					</tr>
				</tfoot>
			</table>
		</div>
	</div>
</div>
<div class="modal fade file-upload-dialog" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
        	<form action="" method="post" enctype="multipart/form-data">
        		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	            <div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	                	<span aria-hidden="true">&times;</span>
	               	</button>
	                <h4 class="modal-title">上传文件</h4>
	            </div>
	            <div class="modal-body">
	                <p>请选择要上传的文件，文件大小不能超过10M。</p>
	                <div class="form-group">
					    <label for="uploadFile">选择文件</label>
					    <%-- 这里需要使用JS读取文件大小，限制文件的大小不能超过10M。此乃坑也！ --%>
					    <input type="file" id="uploadFile" name="file" required="required"/>
					    <p class="help-block">自己上传的文件，只有自己能够看到。</p>
					</div>
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
	                <button type="submit" class="btn btn-primary">上传</button>
	            </div>
            </form>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<!-- 用ajax删除文件 -->
<script type="text/javascript">
	var deleteFile = function(id){
		$.ajax({
			url: "${ctx}/storage/file/" + id,
			method: "DELETE",
			success: function(){
				//重新加载此页
				window.location.reload();
				//window.location.href = "${ctx}/storage/file";
				// 还可以根据id删除一行记录
			}
			
		});
	};
</script>


</body>
</html>