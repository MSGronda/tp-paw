<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title><spring:message code="user.confirm.invalidToken.title" /></title>
  <jsp:include page="../../components/head_shared.jsp"/>
  <style>
      .card{
        padding-top: 4rem;
      }
      .center-content{
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
      }

      .removeBold{
        font-weight: normal;
      }
  </style>
</head>
<body>
<jsp:include page="../../components/default_navbar.jsp"/>

<main class="container-small">
  <sl-card class="card">
    <div class="center-content">
      <h2>
        <spring:message code="recover.invalidToken.title"/>
      </h2>
      <h3 class="removeBold">
        <spring:message code="recover.invalidToken.checkEmail"/>
      </h3>
      <sl-button href="${pageContext.request.contextPath}/" variant="primary">
        <spring:message code="recover.invalidToken.button"/>
      </sl-button>
    </div>
  </sl-card>
</main>

<jsp:include page="../../components/footer.jsp"/>
</body>
</html>
