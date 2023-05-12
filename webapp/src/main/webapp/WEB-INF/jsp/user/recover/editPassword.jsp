<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="string" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
  <title>Change password</title>
  <jsp:include page="../../components/head_shared.jsp"/>

  <style>
      main {
          background-color: #efefef;
          flex: 1 0 auto;
          display: flex;
          flex-direction: column;
          padding: 8px;
      }
      hr {
          width: 30rem;
      }
      .title {
          display: flex;
          flex-direction: row;
          justify-content: space-between;
      }
      .editButton {
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 1.3rem;
      }
      .card-basic {
          width: 100%;
      }
      .error{
          color: red;
      }
      h3 {
        margin-top: 0;
      }
  </style>
</head>
<jsp:include page="../../components/navbar.jsp" />

<body>

<main>
  <div class="title container-small">
    <h1><spring:message code="recover.title"/></h1>
  </div>
  <div class="container-small" >
    <c:url var="formUrl" value="/recover/${token}" />
    <form:form modelAttribute="RecoverPasswordEditForm" action="${formUrl}" method="post">
      <sl-card class="card-basic">
        <h3><spring:message code="recover.edit.title"/></h3>

        <form:errors path="password" cssClass="error" element="p"/>
        <spring:message code="profile.newpassword.placeholder" var="PasswordPlaceholder"/>
        <sl-input name="password" type="password" path="password" value="${RecoverPasswordEditForm.password}" placeholder="${PasswordPlaceholder}" password-toggle></sl-input>
        <br/>


        <form:errors path="passwordConfirmation" cssClass="error" element="p"/>
        <spring:message code="profile.newpasswordconfirm.placeholder" var="PasswordConfirmationPlaceholder"/>
        <sl-input name="passwordConfirmation" type="password" path="passwordEditConfirmation" value="${RecoverPasswordEditForm.passwordConfirmation}" placeholder="${PasswordConfirmationPlaceholder}" password-toggle></sl-input>

        <br/>
        <sl-button type="submit" variant="primary" outline><spring:message code="profile.update.password"/></sl-button>
      </sl-card>
    </form:form>
  </div>
</main>

<jsp:include page="../../components/footer.jsp"/>
<jsp:include page="../../components/body_scripts.jsp"/>
</body>
</html>
