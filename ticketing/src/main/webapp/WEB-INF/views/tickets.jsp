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

	var hardwareMsg = "<spring:message code="ticket.category.HARDWARE" />";
	var softwareMsg = "<spring:message code="ticket.category.SOFTWARE" />";
	var printerMsg = "<spring:message code="ticket.category.PRINTER" />";
	var networkMsg = "<spring:message code="ticket.category.NETWORK" />";
	var supportMsg = "<spring:message code="ticket.category.SUPPORT" />";
	var othersMsg = "<spring:message code="ticket.category.OTHERS" />";

	var filterStatus = "${ticketsFilter.status}";
	var ajaxUrl = "<c:url value="/tickets/ajax/list"/>";
	
	var ticketStatus="${ticketStatus}";
	var closeDialogMsg="<spring:message code="tickets.confirm.close" />";
	var closeDialogHref="<spring:url value="/tickets/close"><spring:param name="ticketId" value="" /><spring:param name="ticketStatus" value="" /></spring:url>";
</script>
<sec:authorize access="hasAuthority('EMPLOYEE')">
	<script>
	var role="EMPLOYEE";
	</script>
</sec:authorize>
<sec:authorize access="hasAuthority('TECHNICIAN')">
	<script>
	var role="TECHNICIAN";
	</script>
</sec:authorize>
<sec:authorize access="hasAuthority('SUPERVISOR')">
	<script>
	var role="SUPERVISOR";
	</script>
</sec:authorize>
<c:url value="/resources/js/ticketsFilter.js" var="ticketsFilter" />
<script type="text/javascript" src="${ticketsFilter}">
	
</script>
<c:url value="/resources/js/tickets.js" var="ticketsJs" />
<script type="text/javascript" src="${ticketsJs}">
	
</script>
</head>
<body>
	<section class="container">
		<jsp:include page="sections/header.jsp" />
		TODO
	</section>
</body>
</html>