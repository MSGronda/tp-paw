<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <jsp:include page="../../components/head_shared.jsp"/>
  <title>Uni</title>

  <style>
    .center{
      margin-top: 2rem;
      display: flex;
      justify-content: center;
      align-items: center;
    }
    .center2{
      display: flex;
      flex-direction: column;
      width: 100%;
      justify-content: center;
      align-items: center;
    }
    h3{
      font-size: 1rem;
      font-weight: normal;
    }
    .success{
      font-size: 5rem;
      color: limegreen;
    }
  </style>
</head>
<body>
<jsp:include page="../../components/default_navbar.jsp"/>

<main class="container-70">
  <sl-card class="center">
    <div class="center2">
      <sl-icon class="success" name="check-circle"></sl-icon>
      <h1><spring:message code="recover.success.title"/></h1>
      <h3><spring:message code="recover.success.body"/></h3>
      <p></p>
      <sl-button href="<c:url value="/"/>" variant="primary" outline><spring:message code="home.returnHome"/></sl-button>
    </div>
  </sl-card>
</main>

<jsp:include page="../../components/footer.jsp"/>
<jsp:include page="../../components/body_scripts.jsp"/>
</body>
</html>
