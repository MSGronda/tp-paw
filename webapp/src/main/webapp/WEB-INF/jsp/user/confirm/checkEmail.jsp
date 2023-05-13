<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title><spring:message code="user.confirm.checkEmail.title"/></title>
  <jsp:include page="../../components/head_shared.jsp"/>
</head>
<body>
<jsp:include page="../../components/navbar.jsp"/>

<main class="container-50">
  <h1>
    <spring:message code="user.confirm.checkEmail.title"/>
  </h1>
  <p>
    <spring:message code="user.confirm.checkEmail.message"/>
  </p>
</main>

<jsp:include page="../../components/footer.jsp"/>
</body>
</html>
