<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <jsp:include page="../components/head_shared.jsp"/>
    <style>
        main {
            background-color: #efefef;
            flex: 1 0 auto;
            display: flex;
            flex-direction: column;
            padding: 8px;
        }
    </style>
</head>
<body>
<jsp:include page="../components/navbar.jsp" />
<main>
    <div class="title container-50">
        <h1><spring:message code="profile.editing"/></h1>
        <div class="editButton">
            <sl-tooltip content="<spring:message code="profile.cancel" />">
                <sl-icon-button name="x-lg" label="Return" href="<c:url value="/profile/${loggedUser.id}" />"></sl-icon-button>
            </sl-tooltip>
        </div>
    </div>

</main>
<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>
</body>
</html>
