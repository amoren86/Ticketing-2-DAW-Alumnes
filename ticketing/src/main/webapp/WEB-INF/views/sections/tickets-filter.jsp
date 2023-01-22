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
			<form:form modelAttribute="ticketsFilter" cssClass="form-horizontal">
				<label for="performer" class="col-sm-3 control-label"> <spring:message
						code="tickets.filter.employee" />
				</label>
				<div class="col-sm-8">
					<sec:authorize access="hasAnyAuthority('SUPERVISOR','TECHNICIAN')">
						<spring:message code="tickets.filter.employee.all"
							var="employeeAll" />
						<form:select id="performer" path="performer"
							cssClass="form-control"
							cssErrorClass="form-control form-control-error">
							<form:option value="" label="${employeeAll}" />
							<form:options items="${employeeList}" itemLabel="fullName"
								itemValue="username" />
						</form:select>
					</sec:authorize>
					<sec:authorize access="hasAuthority('EMPLOYEE')">
						<form:select id="performer" path="performer"
							cssClass="form-control"
							cssErrorClass="form-control form-control-error" disabled="true">
							<form:option value="" label="${employeeAll}" />
							<form:options items="${employeeList}" itemLabel="fullName"
								itemValue="username" />
						</form:select>
					</sec:authorize>
				</div>
			</form:form>
		</div>
		<div class="form-group">
			<form:form modelAttribute="ticketsFilter" cssClass="form-horizontal">
				<label for="category" class="col-sm-3 control-label"> <spring:message
						code="tickets.filter.category" />
				</label>
				<div class="col-sm-8">
					<spring:message code="tickets.filter.category.all"
						var="categoryAll" />
					<form:select id="category" path="category" cssClass="form-control"
						cssErrorClass="form-control form-control-error">
						<option value="">${categoryAll}</option>
						<c:forEach var="category" items="${categories}">
							<option value="${category}"
								${ticketsFilter.category==category?'selected="selected"':''}><spring:message
									code="ticket.category.${category}" /></option>
						</c:forEach>
					</form:select>
				</div>
			</form:form>
		</div>
		<div class="form-group">
			<form:form modelAttribute="ticketsFilter" cssClass="form-horizontal">
				<form:label cssClass="col-sm-3 control-label"
					cssErrorClass="col-sm-3 control-label control-label-error"
					path="from">
					<spring:message code="tickets.filter.date.from" />
				</form:label>
				<div class="col-sm-8">
					<spring:message code="tickets.filter.date.placeholder"
						var="fromPlaceholder" />
					<form:input id="from" path="from" class="form-control"
						type="datetime-local" />
				</div>
			</form:form>
		</div>
		<div class="form-group">
			<form:form modelAttribute="ticketsFilter" cssClass="form-horizontal">
				<form:label cssClass="col-sm-3 control-label"
					cssErrorClass="col-sm-3 control-label control-label-error"
					path="to">
					<spring:message code="tickets.filter.date.to" />
				</form:label>
				<div class="col-sm-8">
					<spring:message code="tickets.filter.date.placeholder"
						var="toPlaceholder" />
					<form:input id="to" path="to" class="form-control"
						type="datetime-local" />
				</div>
			</form:form>
		</div>
	</div>
</div>