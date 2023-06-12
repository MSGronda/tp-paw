<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <title>Edit</title>
    <jsp:include page="../components/head_shared.jsp"/>

</head>
<body>
<jsp:include page="../components/navbar.jsp"/>
<main class="container-50">
    <div class="row">
        <h1><spring:message code="review.edit.header" arguments="${subject.name}" argumentSeparator=""/></h1>
    </div>
    <c:url var="EditReview" value="/review/${subject.id}/edit/${review.id}"/>
    <form:form modelAttribute="ReviewForm" class="col s12" method="post" action="${EditReview}">
        <div class="tags-removable">
<%--            <form:errors path="text" cssClass="error" element="p"/>--%>
            <spring:message code="reviewForm.text.label" var="TextLabel"/>
            <sl-textarea name="text" label="${TextLabel}" value="${review.text}"></sl-textarea>
            <br/>

<%--            <form:errors path="easy" cssClass="error" element="p"/>--%>
            <spring:message code="form.label" var="ButtonLabel"/>
            <spring:message code="form.easy.help" var="EasyHelp"/>
            <sl-radio-group label="${ButtonLabel}" value="${review.difficulty.value}" name="difficulty" help-text="${EasyHelp}">
                <sl-radio-button value="0"><spring:message code="form.easy"/></sl-radio-button>
                <sl-radio-button value="1"><spring:message code="form.normal"/></sl-radio-button>
                <sl-radio-button value="2"><spring:message code="form.hard"/></sl-radio-button>
            </sl-radio-group>
            <br />

<%--            <form:errors path="timeDemanding" cssClass="error" element="p"/>--%>
            <spring:message code="form.timeDemanding.help" var="TimeDemandingHelp"/>
            <sl-radio-group name="timeDemanding" value="${review.timeDemanding.value}" help-text="${TimeDemandingHelp}">
                <sl-radio-button value="0"><spring:message code="form.NotTimeDemanding"/></sl-radio-button>
                <sl-radio-button value="1"><spring:message code="form.averageTimeDemand"/></sl-radio-button>
                <sl-radio-button value="2"><spring:message code="form.timeDemanding"/></sl-radio-button>
            </sl-radio-group>
            <br />
<%--            <div>--%>
<%--                <sl-checkbox name="anonymous" path="anonymous" value="${review.anonymous}"><spring:message code="form.anonymous"/></sl-checkbox>--%>
<%--            </div>--%>
            <div>
                <sl-radio-group name="anonymous" value="${review.anonymous}">
                    <sl-radio-button value="false"><spring:message code="form.public"/></sl-radio-button>
                    <sl-radio-button value="true"><spring:message code="form.anonymous"/></sl-radio-button>
                </sl-radio-group>
            </div>
            <br />
            <sl-button type="submit" variant="success" onclick="this.disabled = true"><spring:message code="review.edit"/></sl-button>
        </div>
    </form:form>
</main>
<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>


</body>
</html>
