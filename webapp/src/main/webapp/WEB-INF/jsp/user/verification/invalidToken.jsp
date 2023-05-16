<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title><spring:message code="user.confirm.invalidToken.title" /></title>
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
    <spring:message code="user.confirm.invalidToken.title"/>
  </h1>
  <sl-button href="${pageContext.request.contextPath}/" variant="primary">
    <spring:message code="user.confirm.invalidToken.button"/>
  </sl-button>
</main>

<jsp:include page="../../components/footer.jsp"/>
</body>
</html>
