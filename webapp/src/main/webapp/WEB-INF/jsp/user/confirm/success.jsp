<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Confirmation successful</title>
  <jsp:include page="../../components/head_shared.jsp"/>
  <style>
    sl-button::part(base) {
      width: fit-content;
    }
  </style>
</head>
<body>
<jsp:include page="../../components/navbar.jsp"/>

<main class="container-50">
  <h1>
    <spring:message code="user.confirm.success.title"/>
  </h1>
  <c:url var="loginUrl" value="/login" />
  <sl-button href="${loginUrl}" variant="primary">
    <spring:message code="user.confirm.success.button"/>
  </sl-button>
</main>

<jsp:include page="../../components/footer.jsp"/>
</body>
</html>
