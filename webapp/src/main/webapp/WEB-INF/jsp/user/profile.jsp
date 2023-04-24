<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html>
<head>
    <title>Hello</title>
    <jsp:include page="../components/head_shared.jsp"/>

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
        h1 {
            font-size: 36px;
            font-weight: bold;
        }
        .info {
            padding-bottom: 2rem;
        }
        .review-column {
            display: flex;
            flex-direction: column;
            justify-content: space-around;
            align-items: center;
            padding-bottom: 3rem;
        }
    </style>
</head>
<body>
<jsp:include page="../components/navbar.jsp" />
<main class="container-50">
    <h1><spring:message code="profile.header" /></h1>
    <sl-card class="card-basic">
        <spring:message code="profile.username" /> <c:out value="${user.username}" />
        <sl-divider></sl-divider>
        <spring:message code="profile.email" /> <c:out value="${user.email}" />
    </sl-card>
    <br/>
    <hr />
    <h3><spring:message code="profile.reviews"/></h3>
    <c:if test="${empty reviews}">
        <h3><spring:message code="subject.noreviews"/></h3>
    </c:if>
    <c:forEach var="review" items="${reviews}">
        <sl-card class="card-header">
            <div slot="header">
                <c:out value="${review.subjectId}" /> - <c:out value="${review.subjectName}"/>
            </div>

            <div class="break-text">
                <c:out value="${review.text}"/>
            </div>
            <div>
                <c:choose>
                    <c:when test="${review.easy == 0}">
                        <sl-badge size="medium" variant="success"><spring:message code="form.easy"/></sl-badge>
                    </c:when>
                    <c:when test="${review.easy == 1}">
                        <sl-badge size="medium" variant="primary"><spring:message code="form.normal"/></sl-badge>
                    </c:when>
                    <c:otherwise>
                        <sl-badge size="medium" variant="danger"><spring:message code="form.hard"/></sl-badge>
                    </c:otherwise>
                </c:choose>

                <c:choose>
                    <c:when test="${review.timeDemanding == 1}">
                        <sl-badge size="medium" variant="warning"><spring:message code="form.timeDemanding"/></sl-badge>
                    </c:when>
                    <c:otherwise>
                        <sl-badge size="medium" ariant="primary"><spring:message code="form.NotTimeDemanding"/></sl-badge>
                    </c:otherwise>
                </c:choose>
            </div>
        </sl-card>
        <br />
    </c:forEach>
</main>
<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>
</body>
</html>