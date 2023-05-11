<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <title><spring:message code="review.delete_confirmation.title"/></title>
    <jsp:include page="../components/head_shared.jsp"/>

    <style>
        .center{
            margin-top: 2rem;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .center2{
            display: flex;
            flex-direction: column;
            width: 100%;
            justify-content: center;
            align-items: center;
        }
        h3{
            font-size: 1.7rem;
        }
        .success{
            font-size: 5rem;
            color: limegreen;
        }
        .failure{
            font-size: 5rem;
            color: red;
        }
        .row{
            display: flex;
            justify-content: space-evenly;
        }
    </style>
</head>
<body>
<jsp:include page="../components/navbar.jsp"/>
<main class="container-70">
    <sl-card class="center">
        <div class="center2">
            <c:choose>
                <c:when test="${successful}">
                    <sl-icon class="success" name="check-circle"></sl-icon>
                    <h3><spring:message code="review.delete_confirmation.success"/></h3>
                </c:when>
                <c:otherwise>
                    <sl-icon class="failure" name="x-circle"></sl-icon>
                    <h3><spring:message code="review.delete_confirmation.failure"/></h3>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="row">
            <sl-button href="<c:url value="/"/>" variant="primary" outline><spring:message code="home.returnHome"/></sl-button>
            <sl-button href="<c:url value="/subject/${subjectId}"/>" variant="primary" outline><spring:message code="subject.returnToSubject"/></sl-button>
        </div>
    </sl-card>
</main>
<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>
</body>
</html>