<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
    <title>Subject Review</title>
    <jsp:include page="../components/head_shared.jsp"/>
    <!-- CSS  -->
<%--    <link href="${pageContext.request.contextPath}/css/style.css" type="text/css" rel="stylesheet" media="screen,projection"/>--%>
    <style>
        .general-area {
            background-color: #efefef
        }
        .container{
            margin-left: 15%;
            margin-right: 15%;
        }
        .row{
            display: flex;
        }
        .error{
            color: red;
        }
    </style>
</head>
<body class="general-area">
<jsp:include page="../components/navbar.jsp"/>
<div class="container">
    <div class="row">
        <h1><spring:message code="review.header" arguments="${subject.name}"/></h1>
    </div>
    <c:url var="CreateReview" value="/review/${subject.id}"/>
    <form:form modelAttribute="ReviewForm" class="col s12" method="post" action="${CreateReview}">
        <div class="tags-removable">
            <div>
                <form:errors path="email" cssClass="error" element="p"/>
<%--            <form:input path="email" placeholder='<spring:message code="reviewForm.email.placeholder"/>'/>--%>
                <spring:message code="reviewForm.email.placeholder" var="EmailPlaceholder"/>
                <sl-input name="email" path="email" placeholder="${EmailPlaceholder}" value="${ReviewForm.email}"></sl-input>
            </div>

            <br/>

            <form:errors path="text" cssClass="error" element="p"/>
<%--        <sl-textarea path="text" label="Write your review"/>--%>
            <sl-textarea name="text" label="Write your review" value="${ReviewForm.text}"></sl-textarea>
            <br/>

            <form:errors path="easy" cssClass="error" element="p"/>
            <sl-radio-group label="Select an option" value="${ReviewForm.easy}" name="easy" help-text="Were the topics in the subject complex to understand?">
                <sl-radio-button value="false">Hard</sl-radio-button>
                <sl-radio-button value="true">Easy</sl-radio-button>
            </sl-radio-group>
            <br />

            <form:errors path="timeDemanding" cssClass="error" element="p"/>
            <sl-radio-group name="timeDemanding" value="${ReviewForm.timeDemanding}" help-text="Were you required to put a lot of time every week to keep up with the topics?">
                <sl-radio-button value="true">Time demanding</sl-radio-button>
                <sl-radio-button value="false">Not very time demanding</sl-radio-button>
            </sl-radio-group>
            <br />
            <sl-button type="submit" variant="success">Submit</sl-button>
        </div>
    </form:form>
</div>
<%-- INCLUDE FOR SHOELACE --%>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@shoelace-style/shoelace@2.3.0/dist/themes/light.css" />
<script type="module" src="https://cdn.jsdelivr.net/npm/@shoelace-style/shoelace@2.3.0/dist/shoelace-autoloader.js"></script>
</body>
</html>
