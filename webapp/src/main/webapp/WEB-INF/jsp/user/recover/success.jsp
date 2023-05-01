<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <jsp:include page="../../components/head_shared.jsp"/>
  <title>Uni</title>

  <style>
    .loginButton {
        width: fit-content;
    }
  </style>
</head>
<body>
<jsp:include page="../../components/navbar.jsp"/>

<main class="container-50">
  <h1><spring:message code="recover.success.title"/></h1>
  <p><spring:message code="recover.success.body" /></p>

  <c:url value="/login" var="loginUrl"/>
  <sl-button class="loginButton" href="${loginUrl}" variant="success"><spring:message code="recover.success.loginbutton"/></sl-button>
</main>

<jsp:include page="../../components/footer.jsp"/>
<jsp:include page="../../components/body_scripts.jsp"/>
</body>
</html>
