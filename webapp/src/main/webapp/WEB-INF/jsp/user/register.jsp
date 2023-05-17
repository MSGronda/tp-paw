<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title><spring:message code="userform.header" /></title>
    <jsp:include page="../components/head_shared.jsp"/>
</head>
<body>
<jsp:include page="../components/navbar.jsp"/>
<main class="container-50 pusher container-account">
    <h1><spring:message code="userform.header" /></h1>
    <c:url var="registerUrl" value="/register"/>
    <form:form modelAttribute="UserForm" action="${registerUrl}" method="post" >

        <form:errors path="email" cssClass="error" element="p"/>
        <c:if test="${EmailAlreadyUsed == true}">
            <p class="error"><spring:message code="register.emailAlreadyUsed"/></p>
        </c:if>
        <spring:message code="reviewForm.email.placeholder" var="EmailPlaceHolder"/>
        <sl-input name="email" path="email" placeholder="${EmailPlaceHolder}" value="${UserForm.email}"></sl-input>

        <br/>

        <form:errors path="name" cssClass="error" element="p"/>
        <spring:message code="userform.name" var="NamePlaceholder"/>
        <sl-input name="name" path="name" placeholder="${NamePlaceholder}" value="${UserForm.name}"></sl-input>

        <br/>

        <form:errors path="password" cssClass="error" element="p"/>
        <spring:message code="userform.password" var="PasswordPlaceholder"/>
        <sl-input name="password" type="password" path="password" placeholder="${PasswordPlaceholder}" value="${UserForm.password}" password-toggle></sl-input>

        <br/>

        <form:errors path="passwordConfirmation" cssClass="error" element="p"/>
        <spring:message code="userform.passwordConfirmation" var="PasswordConfirmationPlaceholder"/>
        <sl-input name="passwordConfirmation" type="password" path="passwordConfirmation" placeholder="${PasswordConfirmationPlaceholder}" value="${UserForm.passwordConfirmation}" password-toggle></sl-input>

        <br/>

        <sl-button type="submit" variant="success" onclick="this.disabled = true"><spring:message code="userform.submit"/></sl-button>
    </form:form>
</main>
<jsp:include page="../components/footer.jsp"/>
</body>
</html>
