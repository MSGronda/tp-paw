<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
  <title><spring:message code="navbar.login" /></title>
  <jsp:include page="../components/head_shared.jsp"/>

  <style>
    main {
      background-color: #efefef;
      flex: 1 0 auto;
      display: flex;
      flex-direction: column;
      padding: 8px;
    }
    .error {
      color: red;
    }
    a{
      color: #0369a1;
    }
  </style>
</head>
<body>
<jsp:include page="../components/navbar.jsp"/>
<main class="container-50 pusher">
  <h1><spring:message code="navbar.login" /></h1>
  <c:if test="${error == true}">
    <p class="error"><spring:message code="userform.invalidcredentials" /></p>
  </c:if>
  <c:url value="/login" var="postPath" />
  <form action="${postPath}" method="post">
    <div>
      <spring:message code="reviewForm.email.placeholder" var="EmailPlaceholder"/>
      <sl-input name="email" id="email" placeholder="${EmailPlaceholder}"></sl-input>
    </div>
    <br />
    <div>
      <spring:message code="userform.password" var="PasswordPlaceholder"/>
      <sl-input type="password" name="password" placeholder="${PasswordPlaceholder}" password-toggle></sl-input>
    </div>
    <br />
    <div>
      <label><sl-checkbox name="rememberMe" id="rememberMe" type="checkbox" /><spring:message code="userform.rememberme" /></label>
    </div>
    <br />
    <div>
      <sl-button type="submit" variant="success" ><spring:message code="userform.login" /></sl-button>
    </div>
    <br/>
    <div>
      <span><spring:message code="login.noAccount"/><a href="<c:url value="/register"/>"><spring:message code="login.register"/></a></span>
    </div>
    <br/>
    <br/>
    <div>
      <span><spring:message code="login.forgotPassword"/></span>
      <span><a href="/recover"><spring:message code="login.recover"/></a></span>
    </div>
  </form>
</main >
<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>
</body>
</html>
