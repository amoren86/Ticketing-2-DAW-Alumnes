<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>

<div class="jumbotron">
	<h1>
		<spring:message code="header.title" />
	</h1>
	<p>
		<spring:message code="header.subtitle" />
	</p>
</div>
<spring:message var="currencySymbol" code="currency.symbol"
	scope="session" />
<fmt:setLocale value="${pageContext.response.locale}" />

<nav class="navbar navbar-default">
	<div class="container-fluid">
		<div class="navbar-header">
			<sec:authorize access="isAuthenticated()">
				<sec:authentication var="user" property="principal" />
				<a class="navbar-brand" href="<spring:url value="/users/edit"/>">
					${user.username} </a>
			</sec:authorize>
		</div>
		<ul class="nav navbar-nav">
			<sec:authorize access="isAuthenticated()">
				<li>
					<a href="#" data-toggle="dropdown">
						<span class="glyphicon glyphicon glyphicon glyphicon-th-list"></span>
						<spring:message code="header.navbar.tickets" />
						<span class="caret"></span>
					</a>
					<ul class="dropdown-menu">
						<sec:authorize access="!hasAuthority('EMPLOYEE')">
							<li class="disabled">
								<a>
									<spring:message code="header.navbar.tickets.new" />
								</a>
							</li>
						</sec:authorize>
						<sec:authorize access="hasAuthority('EMPLOYEE')">
							<li>
								<a href="<spring:url value="/tickets/new"/>">
									<spring:message code="header.navbar.tickets.new" />
								</a>
							</li>
						</sec:authorize>
				</li>
				<li>
					<a href="<spring:url value="/tickets/list/PENDING"/>">
						<spring:message code="header.navbar.tickets.pending" />
					</a>
				</li>
				<li>
					<a href="<spring:url value="/tickets/list/IN_PROCESS"/>">
						<span class="glyphicon glyphicon glyphicon-repeat"></span>
						<spring:message code="header.navbar.tickets.in_process" />
					</a>
				</li>
		</ul>
		</li>
		<li>
			<a href="#" data-toggle="dropdown">
				<spring:message code="header.navbar.users" />
				<span class="caret"></span>
			</a>
			<ul class="dropdown-menu">
				<sec:authorize access="hasAuthority('EMPLOYEE')">
					<li class="disabled">
						<a>
							<spring:message code="header.navbar.users.new" />
						</a>
					</li>
				</sec:authorize>
				<sec:authorize access="!hasAuthority('EMPLOYEE')">
					<li>
						<a href="<spring:url value="/users/new"/>">
							<spring:message code="header.navbar.users.new" />
						</a>
					</li>
				</sec:authorize>
				<li>
					<a href="<spring:url value="/users/list"/>">
						<spring:message code="header.navbar.users.list" />
					</a>
				</li>
			</ul>
		</li>
		<li>
			<a href="#" data-toggle="dropdown">
				<span class="glyphicon glyphicon-user"></span>
				<spring:message code="header.navbar.profile" />
				<span class="caret"></span>
			</a>
			<ul class="dropdown-menu">
				<li>
					<a href="<spring:url value="/users/edit"/>">
						<spring:message code="header.navbar.profile.edit" />
					</a>
				</li>
				<li>
					<a href="<spring:url value="/logout"/>">
						<span class="glyphicon glyphicon-log-out"></span>
						<spring:message code="header.navbar.profile.logout" />
					</a>
				</li>
			</ul>
		</li>
		</sec:authorize>
		</ul>
		<ul class="nav navbar-nav navbar-right">
			<li>
				<a href="#" data-toggle="dropdown">
					<span class="glyphicon glyphicon-flag"></span>
					<spring:message code="header.navbar.language" />
					<span class="caret"></span>
				</a>
				<ul class="dropdown-menu">
					<li
						class="${pageContext.response.locale.language eq 'ca'?'disabled':''}">
						<a href="?language=ca">
							<spring:message code="header.navbar.language.catalan" />
						</a>
					</li>
					<li
						class="${pageContext.response.locale.language eq 'en'?'disabled':''}">
						<a href="?language=en">
							<spring:message code="header.navbar.language.english" />
						</a>
					</li>
				</ul>
			</li>
		</ul>
	</div>
</nav>

<div class="panel panel-default">
	<div class="panel-heading">
		<h4>
			<span class="glyphicon glyphicon-th-large"></span>
			<c:choose>
				<c:when test="${not empty pageTitle}">
					<spring:message code="${pageTitle}" />
				</c:when>
				<c:otherwise>
					<spring:message code="header.welcome" />
				</c:otherwise>
			</c:choose>
		</h4>
	</div>
</div>

