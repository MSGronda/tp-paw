<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title><spring:message code="profile.editing" /></title>
    <jsp:include page="../components/head_shared.jsp"/>
    <style>
        main {
            background-color: #efefef;
            flex: 1 0 auto;
            display: flex;
            flex-direction: column;
            padding: 8px;
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
            height: 100%;
        }
        .profile-image {
            height:8rem ;
            width:8rem;
        }
        .tower-picture {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
        }
        .upload-button {
            display: flex;
            align-items: center;
            flex-direction: column;
            width: 100%;
        }
        .submit-button {
            display: flex;
            align-items: start;
            flex-direction: row;
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
    <div class="container-50" >
        <sl-card class="card-basic">
            <div class="tower-picture">
                <spring:message code="profile.editing.picture" />
                <br/>
                <br/>
                <spring:message code="profile.picture.alt" var="pic" arguments="${loggedUser.username}"/>
                <img class="profile-image" src="<c:url value="/profile/${loggedUser.id}"/>" alt="${pic}" >
            </div>

            <br />
            <c:url var="editProfilePicture" value="/profile/editprofilepicture" />
            <form:form modelAttribute="editProfilePictureForm" action="${editProfilePicture}" method="post" enctype="multipart/form-data">
                <form:errors path="profilePicture" cssClass="error" element="p"/>
                <spring:message code="profile.upload.placeholder" var="uploadPlaceholder"/>
                <span class="upload-button">
                    <input type="file" name="profilePicture" placeholder="${uploadPlaceholder}" accept="image/gif, image/png, image/jpeg"/>
                </span>
                <br />
                <span class="submit-button">
                    <sl-button type="submit" variant="primary" outline><spring:message code="profile.update.picture"/></sl-button>
                </span>
            </form:form>
        </sl-card>
    </div>

</main>
<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>
</body>
</html>
