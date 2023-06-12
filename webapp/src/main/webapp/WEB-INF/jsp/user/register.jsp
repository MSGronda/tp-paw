<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title><spring:message code="userform.header" /></title>
    <jsp:include page="../components/head_shared.jsp"/>

    <style>
        .select-button{
            margin-bottom: 1rem;
        }
    </style>
</head>
<body>
<jsp:include page="../components/default_navbar.jsp"/>
<main class="container-small pusher container-account">


        <c:url var="registerUrl" value="/register"/>
        <form:form modelAttribute="UserForm" action="${registerUrl}" method="post" >
            <div id="step1">
                <h1><spring:message code="userform.header" /></h1>
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

    <%--            <sl-button type="submit" variant="success" onclick="this.disabled = true"><spring:message code="userform.submit"/></sl-button>--%>
                <sl-button variant="success" onclick="nextStep()"><spring:message code="register.next"/></sl-button>
            </div>
            <div id="step2" style="display: none">
                <h1><spring:message code="register.whatAreYouStudying"/></h1>

                <spring:message code="register.selectOne" var="SelectPlaceHolder"/>
                <sl-select id="select-degree" placeholder="${SelectPlaceHolder}" class="select-button">
                    <c:forEach var="degree" items="${degrees}">
                        <sl-option value="${degree.id}"><c:out value="${degree.name}"/></sl-option>
                    </c:forEach>
                </sl-select>
                <br/>
                <input type="hidden" name="degreeId" id="degreeIdHiddenInput"/>
                <sl-button variant="success" onclick="previousStep()"><spring:message code="register.previous"/></sl-button>
                <sl-button variant="success" onclick="nextStep()" id="nextButton" disabled><spring:message code="register.next"/></sl-button>
            </div>
            <div id="step3" style="display: none">
                <c:set var="degrees" value="${degrees}" scope="request"/>
                <c:import url="../components/degrees_tree.jsp"/>
                <br/>
                <input name="subjectIds" type="hidden" id="hiddenInput"/>
                <sl-button variant="success" onclick="previousStep()"><spring:message code="register.previous"/></sl-button>
                <sl-button id="submit-button" type="submit" variant="success" onclick="this.disabled = true; updateSubjectList()"><spring:message code="userform.submit"/></sl-button>
            </div>
        </form:form>
</main>
<jsp:include page="../components/footer.jsp"/>
<script>    //se define este mapa para que funcione los metodos del js compartido
    const subjectProgress = {
    <c:forEach var="entry" items="${user.subjectProgress}">
        <c:out value="${entry.key}"/>: 1,
    </c:forEach>
    }
</script>
<script src="${pageContext.request.contextPath}/js/select-degree.js"></script>

</body>
</html>
