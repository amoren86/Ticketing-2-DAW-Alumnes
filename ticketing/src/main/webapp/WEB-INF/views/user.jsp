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
<c:url value="/resources/js/user.js" var="userJs" />
<script type="text/javascript" src="${userJs}">
	
</script>
</head>
<body onload="roleFields()">
	<section class="container">
		<jsp:include page="sections/header.jsp" />
		TODO
	</section>
</body>
</html>