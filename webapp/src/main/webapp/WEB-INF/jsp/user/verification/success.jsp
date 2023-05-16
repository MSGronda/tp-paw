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
    .card-basic {
      width: 100%;
    }
    .card-basic::part(body) {
      padding: 2rem;
    }
    .center2 {
      display: flex;
      flex-direction: column;
      width: 100%;
      justify-content: center;
      align-items: center;
    }
    h3 {
      font-size: 1.7rem;
      margin-bottom: 0;
      text-align: center;
    }
    .success {
      font-size: 5rem;
      color: limegreen;
    }
    h5 {
      font-weight: normal;
      font-size: 1rem;
      text-align: center;
    }
  </style>
</head>
<body>
<jsp:include page="../../components/navbar.jsp"/>

<main class="container-50">
  <div class="title container-small">
    <h1><spring:message code="user.confirm.hello"/></h1>
  </div>
  <div class="container-small">
    <sl-card class="card-basic">
      <div class="center2">
        <sl-icon class="success" name="person-fill-check"></sl-icon>
        <h3><spring:message code="user.confirm.welcome"/></h3>
        <h5><spring:message code="user.confirm.presentation"/></h5>
        <p><spring:message code="user.confirm.team"/></p>
        <sl-button href="<c:url value="/"/> " variant="primary" outline>
          <spring:message code="user.confirm.start"/>
        </sl-button>
      </div>
    </sl-card>
  </div>
</main>

<jsp:include page="../../components/footer.jsp"/>
</body>
</html>
