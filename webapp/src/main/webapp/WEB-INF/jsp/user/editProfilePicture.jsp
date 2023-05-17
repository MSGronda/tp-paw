<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title><spring:message code="profile.editing" /></title>
    <jsp:include page="../components/head_shared.jsp"/>
    <script>
        function previewImage(event) {
            var reader = new FileReader();
            reader.onload = function () {
                var output = document.getElementById('imagePreview');
                output.src = reader.result;
            }
            reader.readAsDataURL(event.target.files[0]);
        }
    </script>
    <style>
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
            border-radius: 100%;
            object-fit: cover;
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
        .images {
            display: flex;
            flex-direction: row;
            width: 100%;
            justify-content: space-around;
        }
        h5 {
            color: red;
            font-weight: normal;
            font-size: 1rem;
            display: flex;
            justify-content: center;
        }
    </style>
</head>
<body>
<jsp:include page="../components/navbar.jsp" />
<main class="container-account">
    <div class="title container-50">
        <h1><spring:message code="profile.editing"/></h1>
        <div class="editButton">
            <sl-tooltip content="<spring:message code="profile.cancel" />">
                <sl-icon-button name="x-lg" label="Return" href="<c:url value="/profile" />"></sl-icon-button>
            </sl-tooltip>
        </div>
    </div>
    <div class="container-50" >
        <sl-card class="card-basic">
            <c:if test="${invalidImageSize}">
                <h5><spring:message code="profile.editing.picture.error"/></h5>
            </c:if>
            <div class="images">
                <div class="tower-picture">
                    <spring:message code="profile.editing.picture" />
                    <br/>
                    <br/>
                    <spring:message code="profile.picture.alt" var="pic" arguments="${user.username}"/>
                    <img class="profile-image" src="<c:url value="/image/${user.imageId}"/>" alt="${pic}" >
                </div>
                <div class="tower-picture">
                    <spring:message code="profile.new_image" />
                    <br/>
                    <br/>
                    <img id="imagePreview" src="<c:url value="/image/${user.imageId}"/>" class="profile-image">
                </div>
            </div>

            <br />
            <c:url var="editProfilePicture" value="/profile/editprofilepicture" />
            <form:form modelAttribute="editProfilePictureForm" acceptCharset="utf-8" action="${editProfilePicture}" method="post" enctype="multipart/form-data">
                <form:errors path="profilePicture" cssClass="error" element="p"/>
                <spring:message code="profile.upload.placeholder" var="uploadPlaceholder"/>
                <span class="upload-button">
                    <input type="file" name="profilePicture" placeholder="${uploadPlaceholder}" accept="image/gif, image/png, image/jpeg" onchange="previewImage(event)"/>
                </span>
                <br />
                <span class="submit-button">
                    <sl-button type="submit" variant="primary" outline onclick="this.disabled = true"><spring:message code="profile.update.picture"/></sl-button>
                </span>
            </form:form>
        </sl-card>
    </div>

</main>
<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>
</body>
</html>
