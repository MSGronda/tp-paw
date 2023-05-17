<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="string" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title><spring:message code="profile.editing" /></title>
  <jsp:include page="../components/head_shared.jsp"/>

  <style>
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

  </style>
</head>
<jsp:include page="../components/navbar.jsp" />

<body>

<main class="container-account">
  <div class="title container-50">
    <h1><spring:message code="profile.editing"/></h1>
    <div class="editButton">
      <sl-tooltip content="<spring:message code="profile.cancel" />">
        <sl-icon-button name="x-lg" label="Return" href="<c:url value="/profile" />"></sl-icon-button>
      </sl-tooltip>
    </div>
  </div>
  <div class="container-50" >
    <c:url var="editPassword" value="/profile/editpassword" />
    <form:form modelAttribute="EditUserPasswordForm" action="${editPassword}" method="post">
      <sl-card class="card-basic">

        <c:if test="${oldPasswordDoesNotMatch}">
          <p class="error"><spring:message code="profile.editing.oldPasswordError"/></p>
        </c:if>
        <spring:message code="profile.oldpassword.placeholder" var="OldPasswordPlaceholder"/>
        <sl-input name="oldPassword" path="oldPassword" type="password" value="${EditUserPasswordForm.oldPassword}" placeholder="${OldPasswordPlaceholder}" password-toggle></sl-input>
        <br/>

        <form:errors path="editPassword" cssClass="error" element="p"/>
        <spring:message code="profile.newpassword.placeholder" var="PasswordPlaceholder"/>
        <sl-input name="editPassword" type="password" path="password" value="${EditUserPasswordForm.editPassword}" placeholder="${PasswordPlaceholder}" password-toggle></sl-input>
        <br/>


        <form:errors path="passwordEditConfirmation" cssClass="error" element="p"/>
        <spring:message code="profile.newpasswordconfirm.placeholder" var="PasswordConfirmationPlaceholder"/>
        <sl-input name="passwordEditConfirmation" type="password" path="passwordEditConfirmation" value="${EditUserPasswordForm.passwordEditConfirmation}" placeholder="${PasswordConfirmationPlaceholder}" password-toggle></sl-input>

        <br/>
        <sl-button type="submit" variant="primary" outline onclick="this.disabled = true"><spring:message code="profile.update.password"/></sl-button>
      </sl-card>
    </form:form>
  </div>
</main>

<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>
</body>
</html>
