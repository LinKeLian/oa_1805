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
<title>用户管理</title>
</head>
<body>
<div class="container-fluid">
	<div class="panel panel-default">
		<!-- Default panel contents -->
		<div class="panel-heading">
			用户列表
			<div class="close">
				<a class="btn btn-default" href="${ctx }/identity/user/add">新增</a>
			</div>
		</div>
		<div class="panel-body">
			<!-- Table -->
			<table class="table table-hover table-striped">
				<thead>
					<tr>
						<th>姓名</th>
						<th>登录名</th>
						<th>身份</th>
						<th>状态</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${page.content }" var="u">
					<tr>
						<td>${u.name }</td>
						<td>${u.loginName }</td>
						<td>
							<c:forEach items="${u.roles }" var="r">
								<span class="label label-info">${r.name }</span>
							</c:forEach>
						</td>
						<td>
							<c:choose>
								<c:when test="${u.status eq 'DISABLED' }">
									已禁用
								</c:when>
								<c:when test="${u.status eq 'EXPIRED' }">
									已过期
								</c:when>
								<c:when test="${u.status eq 'NORMAL' }">
									正常
								</c:when>
								<c:otherwise>
									未知
								</c:otherwise>
							</c:choose>
						</td>
						<td>
							<a href="${ctx }/identity/user/${u.id}">修改</a>
							<c:choose>
								<%-- 枚举在EL表达式里面，直接当做字符串来使用 --%>
								<c:when test="${u.status eq 'DISABLED' }">
									<a href="${ctx }/identity/user/active/${u.id}">激活</a>
								</c:when>
								<c:when test="${u.status eq 'EXPIRED' }">
									<a href="${ctx }/identity/user/active/${u.id}">激活</a>
								</c:when>
								<c:otherwise>
									<a href="${ctx }/identity/user/disable/${u.id}">禁用</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					</c:forEach>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="5" style="text-align: center;">
							<%-- 前缀随便写，关键要跟taglib指令的前缀要一致，冒号后面的则直接使用JSP Tag文件的名称 --%>
							<fk:page url="/identity/user?keyword=${param.keyword }" page="${page }"/>
						</td>
					</tr>
				</tfoot>
			</table>
		</div>
	</div>
</div>
</body>
</html>