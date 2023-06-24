<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html>
<head>
    <title><c:out value="${user.username}"/> - <spring:message code="profile.profile"/></title>
    <jsp:include page="../components/head_shared.jsp"/>

    <style>
        .review-container {
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            gap: 2rem;
            padding: 1rem 0 1.5rem;
        }
        table {
            width: 100% !important;
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
        .editor-text {
            margin-left: 1rem;
            color: #4e90e2;
            font-weight: normal;
        }
        .edit-button {
            display: flex;
            justify-content: space-around;
        }
        h4 {
            display: flex;
            justify-content: center;
        }
        .logout-button {
            display: flex;
            align-items: center;
        }

        #more {
            display: none;
        }

        .profile-image {
            height:5rem ;
            width:5rem;
            border-radius: 100%;
            object-fit: cover;
        }
        .header {
            display: flex;
            flex-direction: row;
        }
        .table-area {
            width: 100%;
            padding-bottom: 2rem;
        }
        .icon-area{
            width: 80%;
            display: flex;
            justify-content: end;
        }
        .image-container {
            position: relative;
            display: inline-block;
        }
        .edit-picture-button {
            position: absolute;
            bottom: 0;
            right: 0;
            padding-bottom: 0.65rem;
            opacity: 85%;
        }
        .edit-picture-button:hover {
            opacity: 100%;
        }
        .edit-icon {
            padding-top: 0.5rem;
        }

        .moderator-tag {
            display: flex;
            flex-direction: row;
        }
        <jsp:include page="../components/component-style/table_style.jsp"/>
    </style>
</head>
<body>
<jsp:include page="../components/navbar.jsp" />
<main class="container-50 pusher container-account">
    <div class="header">
        <div class="image-container">
            <spring:message code="profile.picture.alt" var="pic" arguments="${user.username}" argumentSeparator="\0"/>
            <img class="profile-image" src="<c:url value="/image/${user.imageId}"/>" alt="${pic}" >
            <sl-button class="edit-picture-button" variant="primary" size="small" href="<c:url value="/profile/editprofilepicture"/>">
                <sl-icon class="edit-icon" name="pen" label="Edit"></sl-icon>
            </sl-button>
        </div>
        <div class="title">
            <div class="moderator-tag">
                <h1><spring:message code="profile.loggeduser" /></h1>
                <c:if test="${user.editor}">
                    <h1 class="editor-text"> <spring:message code="profile.editor" /> </h1>
                </c:if>
            </div>
            <c:url value="/logout" var="logout"/>
            <div class="logout-button">
                <sl-button variant="primary" href="${logout}"><spring:message code="profile.logout"/></sl-button>
            </div>
        </div>
        </div>
        <sl-card class="card-basic">
            <div class="table-area">
                <table>
                    <tbody>
                    <tr>
                        <th><spring:message code="profile.username" /></th>
                        <td><c:out value="${user.username}" /></td>
                        <td>
                            <div class="icon-area">
                                <sl-button variant="primary" size="small" outline  href="<c:url value="/profile/editdata"/>">
                                    <sl-icon class="edit-icon" name="pen" label="Edit"></sl-icon>
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
                <sl-button variant="primary" outline href="<c:url value="/profile/editpassword"/>"><spring:message code="profile.update.password"/></sl-button>
            </div>
        </sl-card>
    <br/>
    <br/>
    <hr />
    <c:if test="${not empty reviews}">
        <c:set var="order" value="${order}" scope="request"/>
        <c:set var="dir" value="${dir}" scope="request"/>
        <c:import url="../components/order_dropdown.jsp"/>
    </c:if>
    <c:if test="${empty reviews}">
        <h4><spring:message code="subject.noreviews"/></h4>
    </c:if>
    <div class="review-container">
        <c:forEach var="review" items="${reviews}">
            <c:set var="review" value="${review}" scope="request"/>
            <c:set var="fromProfile" value="${true}" scope="request"/>
            <c:set var="user" value="${user}" scope="request"/>
            <c:import url="../components/review_card.jsp"/>
        </c:forEach>
    </div>

    <c:set value="${totalPages}" var="totalPages" scope="request"/>
    <c:set value="${currentPage}" var="currentPage" scope="request"/>
    <c:import url="../components/page_buttons.jsp"/>
</main>
<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>
<script src="${pageContext.request.contextPath}/js/review_card.js"></script>
<script src="${pageContext.request.contextPath}/js/url-param-utils.js"></script>
<script>
    $(document).ready(function() {
        $('.vote-button').click(function() {
            const formId = $(this).data('form-id');
            const newVote = $(this).data('form-value');

            const prevVote = parseInt($('#' + formId + ' input[name=vote]').val())

            if(newVote !== prevVote)
                submitReviewVoteForm('${pageContext.request.contextPath}/voteReview',formId, prevVote ,newVote);
            else
                submitReviewVoteForm('${pageContext.request.contextPath}/voteReview',formId, prevVote ,0);
        })
    });
</script>
</body>
</html>