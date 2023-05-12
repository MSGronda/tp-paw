<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
  <jsp:include page="../../components/head_shared.jsp"/>
  <title>Uni</title>
  <style>
    .card-basic{
      width: 100%;
    }
    .center2{
      display: flex;
      flex-direction: column;
      width: 100%;
      justify-content: center;
      align-items: center;
    }
    h3{
      font-size: 1.7rem;
      margin-bottom: 0;
    }
    .success{
      font-size: 5rem;
      color: limegreen;
    }
    .row{
      display: flex;
      justify-content: space-evenly;
    }
    h5 {
      font-weight: normal;
      font-size: 1rem;
    }
  </style>
</head>
<body>
<jsp:include page="../../components/navbar.jsp"/>

<main class="container-50">
    <div class="title container-small">
        <h1><spring:message code="recover.title"/></h1>
    </div>
    <div class="container-small" >
        <sl-card class="card-basic">
            <div class="center2">
                <sl-icon class="success" name="check-circle"></sl-icon>
                <h3><spring:message code="recover.email_sent"/></h3>
                <h5><spring:message code="recover.sent.body"/></h5>
            </div>
        </sl-card>
    </div>
</main>

<jsp:include page="../../components/footer.jsp"/>
<jsp:include page="../../components/body_scripts.jsp"/>
</body>
</html>
