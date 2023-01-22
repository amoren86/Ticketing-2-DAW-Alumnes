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
<title><spring:message code="login.title" /></title>
<jsp:include page="sections/head.jsp" />
</head>
<body>
	<section class="container">
		<jsp:include page="sections/header.jsp" />

		<form action="<c:url value="/login"/>" method="post">
			<div class="row">
				<div class="col-md-4 col-md-offset-4">
					<div class="panel panel-default">
						<div class="panel-heading">
							<h4 class="panel-title">
								<spring:message code="login.give.credentials" />
							</h4>
						</div>

						<div class="panel-body">
							<c:if test="${not empty error}">
								<div class="alert alert-danger">
									<a href="#" class="close" data-dismiss="alert"
										aria-label="close">&times;</a>
									<spring:message code="login.credentials.failure" />
								</div>
							</c:if>
							<div class="input-group">
								<span class="input-group-addon"><em
									class="glyphicon glyphicon-user"></em></span> <input
									class="form-control"
									placeholder="<spring:message code="login.username"/>"
									name="username" type="text">
							</div>
							<div class="input-group">
								<span class="input-group-addon"><em
									class="glyphicon glyphicon-lock"></em></span> <input
									class="form-control"
									placeholder="<spring:message code="login.password"/>"
									name="password" type="password">
							</div>
						</div>
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" />
						<div class="panel-footer">
							<input class="btn btn-default btn-block" type="submit"
								value="<spring:message code="login.login"/>">
						</div>
					</div>
				</div>
			</div>
		</form>
	</section>
</body>
</html>