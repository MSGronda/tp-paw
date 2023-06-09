<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title><spring:message code="onboarding.page-title"/></title>
    <jsp:include page="../components/head_shared.jsp"/>

    <style>
        .subheader {
            font-weight: normal;
        }
        .select-button{
            margin-bottom: 1rem;
        }


    </style>
</head>
<body>
<jsp:include page="../components/default_navbar.jsp"/>
<main class="container-small pusher container-account">
    <c:url var="onboardingUrl" value="/select-degree"/>
    <form:form modelAttribute="UserForm" action="${onboardingUrl}" method="post">
        <div id="step1">
            <h1><spring:message code="onboarding.welcome"/></h1>
            <h2 class="subheader"><spring:message code="onboarding.explanation"/></h2>

            <spring:message code="register.selectOne" var="SelectPlaceHolder"/>
            <sl-select id="select-degree" placeholder="${SelectPlaceHolder}" class="select-button">
                <c:forEach var="degree" items="${degrees}">
                    <sl-option value="${degree.id}"><c:out value="${degree.name}"/></sl-option>
                </c:forEach>
            </sl-select>
            <br/>
            <input type="hidden" name="degreeId" id="degreeIdHiddenInput"/>
            <sl-button variant="success" onclick="nextStep()" id="nextButton" disabled><spring:message code="register.next"/></sl-button>
        </div>
        <div id="step2" style="display: none">
            <c:set value="${degrees}" var="degrees" scope="request"/>
            <c:import url="../components/degrees_tree.jsp"/>
            <input name="subjectIds" type="hidden" id="hiddenInput"/>
            <sl-button variant="success" onclick="previousStep()"><spring:message code="register.previous"/></sl-button>
            <sl-button id="submit-button" type="submit" variant="success" onclick="this.disabled = true; updateSubjectList()"><spring:message code="userform.update"/></sl-button>
        </div>
    </form:form>

</main>
<jsp:include page="../components/footer.jsp"/>
<script>
    const subjectProgress = {
    <c:forEach var="entry" items="${user.subjectProgress}">
        ${entry.key}: 1,
    </c:forEach>
    }
</script>
<script src="${pageContext.request.contextPath}/js/select-degree.js"></script>
</body>
</html>
