<%@ page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@ taglib
	prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html lang="EN">
<head>
<jsp:include page="sections/head.jsp" />
<script>
	var csrfParameterName = "${_csrf.parameterName}";
	var csrfToken = "${_csrf.token}";
	var employeeMsg = "<spring:message code="user.role.EMPLOYEE" />";
	var technicianMsg = "<spring:message code="user.role.TECHNICIAN" />";
	var supervisorMsg = "<spring:message code="user.role.SUPERVISOR" />";
	var externalMsg = "<spring:message code="users.table.body.external" />&nbsp;";

	var ajaxUrl = "<c:url value="/users/ajax/list"/>";
</script>
<c:url value="/resources/js/usersFilter.js" var="usersFilterJs" />
<script type="text/javascript" src="${usersFilterJs}">
	
</script>
<c:url value="/resources/js/users.js" var="usersJs" />
<script type="text/javascript" src="${usersJs}">
	
</script>
</head>
<body>
	<section class="container">
		<jsp:include page="sections/header.jsp" />

		<jsp:include page="sections/users-filter.jsp" />


		<table id="userTable"
			class="table table-striped table-bordered table-condensed table-responsive">
			<thead>
				<tr>
					<th>
						<spring:message code="users.table.head.num" />
					</th>
					<th>
						<spring:message code="users.table.head.role" />
					</th>
					<th>
						<spring:message code="users.table.head.username" />
					</th>
					<th>
						<spring:message code="users.table.head.full.name" />
					</th>
					<th>
						<spring:message code="users.table.head.ext" />
					</th>
					<th>
						<spring:message code="users.table.head.location" />
					</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="user" items="${usersList}" varStatus="status">
					<tr>
						<td>${status.index+1}</td>
						<td>
							<spring:message code="user.role.${user.role}" />
						</td>
						<td>${user.username}</td>
						<td>${user.fullName}</td>
						<td>${user.extension}</td>
						<c:choose>
							<c:when test="${user.role eq 'EMPLOYEE'}">
								<td>${user.location}</td>
							</c:when>
							<c:otherwise>
								<td>
									<spring:message code="users.table.body.external" />
									&nbsp;${user.location}
								</td>
							</c:otherwise>
						</c:choose>
						<td class="text-center">
							<sec:authorize access="hasAnyAuthority('SUPERVISOR' )">
								<c:url value="/users/remove" var="removeHref">
									<c:param name="username" value="${user.username}" />
								</c:url>
								<spring:message code="users.confirm.remove"
									arguments="${user.username}" var="confirmRemoveMsg" />
								<button type="button" class="btn btn-danger btn-sm"
									data-toggle="modal" data-target="#confirm"
									onclick="changeDni('${confirmRemoveMsg}','${removeHref}')"
									data-backdrop="true">
									<span class="glyphicon glyphicon-trash"> </span>
								</button>
							</sec:authorize>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</section>

	<!-- Remove Modal -->
	<div id="confirm" class="modal fade" role="dialog">
		<div class="modal-dialog">

			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header" style="padding: 35px 50px;">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">
						<spring:message code="users.confirm.remove.title" />
					</h4>
				</div>
				<div class="modal-body" style="padding: 40px 50px;">
					<p id="confirmRemoveMessage">confirmRemoveMessage</p>
				</div>
				<div class="modal-footer">
					<a id="removeHref" class="btn btn-danger" href="#">
						<span class="glyphicon glyphicon-trash"> </span>
						<spring:message code="users.confirm.remove.button" />
					</a>
					<button type="button" class="btn btn-default" data-dismiss="modal">
						<spring:message code="users.confirm.cancel.button" />
					</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>