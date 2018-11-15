<%@ tag language="java" pageEncoding="UTF-8"%>
<%--这里可以写JSP中的任何代码，并且可以定义接收参数！ --%>
<%--tag指令是JSP2.0的时候提供的一个标签指令，把HTML内容作为标签库来使用，避免在java代码中嵌入HTML --%>
<%--attirbute指令用于在JSP Tag文件中定义参数 --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--引入jsp --%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="application"></c:set>
<%@ attribute name="url" required="ture" type="java.lang.String"%>
<%@ attribute name="page" required="ture" type="org.springframework.data.domain.Page"%>

<c:if test="${not empty page }">
<nav aria-label="Page navigation">
	<ul class="pagination">
		<li>
			<a href="${ctx }${url }?pageNumber=${page.number eq 0 ? 0 : page.number - 1}" aria-label="上一页">
				<span aria-hidden="true">&laquo;</span>
			</a>
		</li>
		<c:set var="begin" value="${page.number - 2 }"></c:set>
		<c:set var="end" value="${page.number+2 }"></c:set>
		<c:if test="${begin lt 0 }">
		<%--此时的begin是负数，在begin之前加上减号，就变成了正数 --%>
		<%--end+begin取反，其实就是把end往右挪一点 --%>
		<c:set var="end" value="${end + (-begin) }"></c:set>
			<c:set var="begin" value="0"></c:set>
		</c:if>
		<c:if test="${end gt (page.totalPages - 1) }">
			<%--不能大于最大页数 --%>
			<c:set var ="end" value="${page.totalPages - 1 }"></c:set>
			<c:set var="begin" value="${end - 4 }"></c:set>
		</c:if>
		<c:if test="${begin lt 0 }">
		 	<c:set var="begin" value="0"></c:set>
		 </c:if> 
		<c:forEach begin="${begin }" end="${end }" var="number">
			<li class="${page.number eq number ? 'active':'' }"><a href="${ctx }${url }?pageNumber=${number}">${number + 1  }</a></li>
		</c:forEach>
		<li>
		    <a href="${ctx }${url }?pageNumber=${page.number ge (page.totalPages-1) ? page.totalPages-1 : page.number+1}" aria-label="下一页">
		     	<span aria-hidden="true">&raquo;</span>
		    </a>
        </li>
    </ul> 
</nav>
</c:if>
<c:if test="${empty page }">
	没有数据
</c:if>