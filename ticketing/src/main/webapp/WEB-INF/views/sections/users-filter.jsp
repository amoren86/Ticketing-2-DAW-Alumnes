<%@ page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@ taglib
	prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<div class="panel-body">
	<div class="col-md-5">
		<div class="form-group">
			<form:form modelAttribute="usersFilter" cssClass="form-horizontal">
				<label for="role" class="col-sm-3 control-label"> <spring:message
						code="users.filter.role" />
				</label>
				<div class="col-sm-8">
					<spring:message code="users.filter.role.all" var="roleAll" />
					<form:select id="role" path="role" cssClass="form-control"
						cssErrorClass="form-control form-control-error">
						<form:option value="" label="${roleAll}" />
						<c:forEach var="role" items="${roles}">
							<option value="${role}"
								${usersFilter.role==role?'selected="selected"':''}><spring:message
									code="user.role.${role}" /></option>
						</c:forEach>
					</form:select>
				</div>
			</form:form>
		</div>
		<div class="form-group">
			<form:form modelAttribute="usersFilter" cssClass="form-horizontal">
				<label for="name" class="col-sm-3 control-label"> <spring:message
						code="users.filter.name" />
				</label>
				<div class="col-sm-8">
					<spring:message code="users.filter.name.all" var="nameAll" />
					<form:input id="name" path="fullName" placeholder="${nameAll}"
						cssClass="form-control"
						cssErrorClass="form-control form-control-error" />
				</div>
			</form:form>
		</div>
	</div>
</div>