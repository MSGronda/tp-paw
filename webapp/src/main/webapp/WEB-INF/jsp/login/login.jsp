<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
  <title>Login</title>
  <jsp:include page="../components/head_shared.jsp"/>

  <style>
    main {
      background-color: #efefef;
      flex: 1 0 auto;
      display: flex;
      flex-direction: column;
      padding: 8px;
    }
  </style>
</head>
<body>
<jsp:include page="../components/navbar.jsp"/>
<main>
  <h2>Login</h2>
  <c:url value="/login" var="postPath" />
  <form action="${postPath}" method="post">
    <div>
      <label><spring:message code="reviewForm.email.placeholder" />
        <spring:message code="reviewForm.email.placeholder" var="EmailPlaceholder"/>
        <sl-input name="email" name="email" placeholder="${EmailPlaceholder}"></sl-input>
      </label>
    </div>
    <div>
      <label><spring:message code="userform.password" />
        <spring:message code="userform.password" var="PasswordPlaceholder"/>
        <sl-input type="password" name="password" placeholder="${PasswordPlaceholder}" ></sl-input>
      </label>
    </div>
    <div>
      <sl-button type="submit" variant="success" ><spring:message code="form.submit" /></sl-button>
    </div>
  </form>
  <jsp:include page="../components/footer.jsp"/>
  <jsp:include page="../components/body_scripts.jsp"/>
</main >
</body>
</html>
