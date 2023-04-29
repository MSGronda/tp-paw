<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html>
<head>
    <title><c:out value="${user.username}"/> - <spring:message code="profile.profile"/></title>
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
            width: 40rem;
        }
        h1 {
            font-size: 36px;
            font-weight: bold;
        }
        .title {
            display: flex;
            flex-direction: row;
            justify-content: space-between;
            margin-left: 2rem;
            width: 100%;
        }
        .edit-button {
            display: flex;
            justify-content: space-around;
        }
        h4{
            display: flex;
            justify-content: center;
        }
        .logout-button{
            display: flex;
            align-items: center;
        }

        #more {
            display: none;
        }

        .profile-image {
            height:5rem ;
            width:5rem;
        }
        .header {
            display: flex;
            flex-direction: row;
        }
        .table-area{
            width: 100%;
            padding-bottom: 2rem;
        }
        .icon-area{
            width: 80%;
            display: flex;
            justify-content: end;
        }
        sl-icon{
            padding-top: 0.5rem;
        }
        .image-container{
            position: relative;
            display: inline-block;
        }
        .edit-picture{
            position: absolute;
            bottom: 0;
            right: 0;
            padding-bottom: 0.65rem;
        }
        <jsp:include page="../components/table_style.jsp"/>
    </style>
</head>
<body>
<jsp:include page="../components/navbar.jsp" />
<main class="container-50 pusher">
    <div class="header">
        <div class="image-container">
            <spring:message code="profile.picture.alt" var="pic" arguments="${user.username}"/>
            <img class="profile-image" src="<c:url value="/profile/${user.id}"/>" alt="${pic}" >
            <sl-button class="edit-picture" variant="primary" size="small" outline  href="<c:out value="/profile/editprofilepicture"/>">
                <sl-icon name="pen" label="Edit"></sl-icon>
            </sl-button>
        </div>
        <div class="title">
            <c:if test="${user.id != loggedUser.id}">
                <h1><spring:message code="profile.header" arguments="${user.username}" /></h1>
            </c:if>
            <c:if test="${user.id == loggedUser.id}" >
                <h1><spring:message code="profile.loggeduser" /></h1>
                <c:url value="/logout" var="logout"/>
                <div class="logout-button">
                    <sl-button variant="primary" href="${logout}"><spring:message code="profile.logout"/></sl-button>
                </div>
            </c:if>
        </div>
        </div>
    <c:if test="${user.id == loggedUser.id}">
        <sl-card class="card-basic">
            <div class="table-area">
                <table width="100% !important">
                    <tbody>
                    <tr>
                        <th><spring:message code="profile.username" /></th>
                        <td><c:out value="${user.username}" /></td>
                        <td>
                            <div class="icon-area">
                                <sl-button variant="primary" size="small" outline  href="<c:url value="/profile/editdata"/>">
                                    <sl-icon name="pen" label="Edit"></sl-icon>
                                </sl-button>
                            </div>

                        </td>
                    </tr>
                    <tr>
                        <th><spring:message code="profile.email" /></th>
                        <td><c:out value="${user.email}" /></td>
                        <td></td>
                    </tr>
                    </tbody>
                </table>
            </div>

            <div class="edit-button">
<%--                <sl-button variant="primary" outline href=""><spring:message code="profile.update.picture"/></sl-button>--%>
<%--                <sl-button variant="primary" outline href="<c:url value="/profile/editdata"/>"><spring:message code="profile.update.username"/></sl-button>--%>
                <sl-button variant="primary" outline href="<c:url value="/profile/editpassword"/>"><spring:message code="profile.update.password"/></sl-button>
            </div>
        </sl-card>
    </c:if>

    <br/>
    <br>
    <hr />
    <h3><spring:message code="profile.reviews"/></h3>
    <c:if test="${empty reviews}">
        <h4><spring:message code="subject.noreviews"/></h4>
    </c:if>
    <c:forEach var="review" items="${reviews}">
        <c:if test="${!review.anonymous || loggedUser.id == review.userId}">
            <c:set var="review" value="${review}" scope="request"/>
            <c:set var="fromProfile" value="${true}" scope="request"/>
            <c:import url="../components/review_card.jsp"/>
        </c:if>
    </c:forEach>
</main>
<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>
<script>
    const showMore = document.querySelector('.showMore');

    if(showMore !== null){
        showMore.addEventListener('click',() => {
            const dots = document.getElementById("dots");
            const moreText = document.getElementById("more");

            if (dots.style.display === "none") {
                dots.style.display = "inline";
                showMore.innerHTML = "<spring:message code="subject.showMore" /><sl-icon name=\"chevron-down\"></sl-icon>";
                moreText.style.display = "none";
            } else {
                dots.style.display = "none";
                showMore.innerHTML = "<spring:message code="subject.showLess" /><sl-icon name=\"chevron-up\"></sl-icon>";
                moreText.style.display = "inline";
            }
        });
    }

</script>
</body>
</html>