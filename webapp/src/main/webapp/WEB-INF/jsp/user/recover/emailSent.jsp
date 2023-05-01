<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <jsp:include page="../../components/head_shared.jsp"/>
  <title>Uni</title>
</head>
<body>
<jsp:include page="../../components/navbar.jsp"/>

<main class="container-50">
  <h1><spring:message code="recover.sent.title"/></h1>
  <p><spring:message code="recover.sent.body" /></p>
</main>

<jsp:include page="../../components/footer.jsp"/>
<jsp:include page="../../components/body_scripts.jsp"/>
</body>
</html>
