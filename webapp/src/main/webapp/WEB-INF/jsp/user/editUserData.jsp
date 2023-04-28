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
        <div class="container-50">
            <c:url var="editProfile" value="/profile/editdata"/>
            <form:form modelAttribute="EditUserDataForm" action="${editProfile}" method="post" >
                <sl-card class="card-basic">
                    <spring:message code="profile.editing.username" /> <c:out value="${loggedUser.username}" />
                    <br/>
                    <br />
                    <form:errors path="userName" cssClass="error" element="p"/>
                    <spring:message code="profile.newusername.placeholder" var="NamePlaceholder"/>
                    <sl-input name="userName" path="userName" placeholder="${NamePlaceholder}" value="${EditUserDataForm.userName}"></sl-input>
                    <br/>
                    <sl-button type="submit" variant="primary" outline><spring:message code="profile.update.username"/></sl-button>
                </sl-card>
            </form:form>
        </div>

    </main>
</body>
<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>
</html>
