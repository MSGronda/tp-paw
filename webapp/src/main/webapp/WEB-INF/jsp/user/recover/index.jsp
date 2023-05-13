<%--<%@ taglib prefix="spring" uri="http://java.sun.com/jsp/jstl/fmt" %>--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Recover Password</title>
    <jsp:include page="../../components/head_shared.jsp"/>

    <style>
        .card-basic {
            width: 100%;
        }
        .error {
            color: red;
        }
        .submit-button {
            display: flex;
            justify-content: center;
        }
        h5 {
            font-weight: normal;
            font-size: 1rem;
        }
        h3 {
            margin-top: 0;
        }
    </style>
</head>
<body>
<jsp:include page="../../components/navbar.jsp" />

<main>
    <div class="container-small">
        <h1><spring:message code="recover.title"/></h1>
    </div>
    <div class="container-small">
        <c:url value="/recover" var="sendEmail"/>
        <form:form modelAttribute="RecoverPasswordForm" action="${sendEmail}" method="post">
            <sl-card class="card-basic">
                <h3><spring:message code="recover.subtitle"/></h3>
                <h5><spring:message code="recover.description"/></h5>
                <c:if test="${invalidToken}">
                    <p class="error"><spring:message code="recover.invalidToken"/></p>
                </c:if>

                <form:errors path="email" cssClass="error" element="p"/>
                <spring:message code="reviewForm.email.placeholder" var="emailPlaceholder"/>
                <sl-input name="email" path="email" value="${RecoverPasswordForm.email}" placeholder="${emailPlaceholder}"></sl-input>

                <br/>
                <br/>
                <div class="submit-button">
                    <sl-button type="submit" variant="primary" outline onclick="this.disabled = true"><spring:message code="recover.sendEmail"/></sl-button>
                </div>
            </sl-card>
        </form:form>
    </div>
</main>

<jsp:include page="../../components/footer.jsp"/>
<jsp:include page="../../components/body_scripts.jsp"/>
</body>
</html>
