<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<html>
<head>
  <title><c:out value="${user.username}"/> - <spring:message code="profile.profile"/></title>
  <jsp:include page="../components/head_shared.jsp"/>

  <style>
    hr {
      width: 40rem;
    }
    h1 {
      font-size: 2rem;
      font-weight: bold;
    }
    .title {
      display: flex;
      flex-direction: row;
      justify-content: space-between;
      margin-left: 2rem;
      width: 100%;
    }

    h3 {
      display: flex;
      justify-content: center;
    }

    #more {
      display: none;
    }

    .review-container {
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        gap: 2rem;
        padding: 1.5rem 0 1.5rem 0;
    }

    .profile-image {
      height:5rem;
      width:5rem;
      border-radius: 100%;
      object-fit: cover;
    }
    .header {
      display: flex;
      flex-direction: row;
    }
    sl-icon {
      padding-top: 0.5rem;
    }
    .image-container {
      position: relative;
      display: inline-block;
    }
    .editor-text {
      margin-left: 1rem;
      color: #4e90e2;
      font-weight: normal;
    }
    a {
      color: #0369a1;
      background-color: transparent;
      text-decoration: none;
    }
    .moderator-tag {
      display: flex;
      flex-direction: row;
      align-items: center;
    }
    .pag-buttons{
      display: flex;
      flex-direction: column;
      justify-content: space-around;
      align-items: center;
      padding-bottom: 3rem;
    }
    <jsp:include page="../components/component-style/table_style.jsp"/>
  </style>
</head>
<body>
<jsp:include page="../components/navbar.jsp" />
<main class="container-50 pusher container-account" >
  <sl-card>
  <div class="header">
    <div class="image-container">
      <spring:message code="profile.picture.alt" var="pic" arguments="${user.username}" argumentSeparator="\0"/>
      <img class="profile-image" src="<c:url value="/image/${user.imageId}"/>" alt="${pic}" >
    </div>
    <div class="title">
      <div class="moderator-tag">
        <h1><c:out value="${user.username}"/></h1>
        <c:if test="${user.editor}">
          <h1 class="editor-text"> <spring:message code="profile.editor" /> </h1>
        </c:if>
      </div>
      <sec:authorize access="hasRole('EDITOR')">
        <div class="moderator-tag">
          <c:if test="${!user.editor}">
            <sl-button variant="primary" outline href="<c:url value="/user/${user.id}/moderator"/>"><spring:message code="profile.make_moderator" /> </sl-button>
          </c:if>
        </div>
      </sec:authorize>
    </div>
    <div>
    </div>
  </div>
  <br>
  <table>
    <tbody>
      <tr>
        <td><spring:message code="degree"/></td>
        <c:if test="${user.degree != null}" >
          <td><c:out value="${user.degree.name}"/></td>
        </c:if>
        <c:if test="${user.degree == null}" >
          <td><spring:message code="profile.no_degree"/></td>
        </c:if>
      </tr>

      <c:forEach var="subject" items="${user.userSemester}" varStatus="status">
      <tr>
        <td>
          <c:if test="${status.first}">
            <spring:message code="dashboard.currentSemester"/>
          </c:if>
        </td>
        <td>
          <a href="<c:url value="/subject/${subject.subject.id}"/>"><c:out value="${subject.subject.name}"/></a>
        </td>
      </tr>
      </c:forEach>
      <c:if test="${user.degree != null}">
        <tr>
          <td><spring:message code="dashboard.overview.completedCredits"/></td>
          <td><c:out value="${user.creditsDone}"/> <spring:message code="profile.outOf"/> <c:out value="${user.degree.totalCredits}"/> </td>
        </tr>
      </c:if>
    </tbody>
  </table>
  </sl-card>
  <br>
  <hr />
  <c:if test="${not empty reviews}">
    <c:set var="order" value="${order}" scope="request"/>
    <c:set var="dir" value="${dir}" scope="request"/>
    <c:import url="../components/order_dropdown.jsp"/>
  </c:if>
  <c:if test="${empty reviews}">
    <h3><spring:message code="subject.noreviews"/></h3>
  </c:if>
  <div class="review-container">
    <c:forEach var="review" items="${reviews}">
      <c:if test="${!review.anonymous}">
        <c:set var="review" value="${review}" scope="request"/>
        <c:set var="fromProfile" value="${true}" scope="request"/>
        <c:set var="user" value="${null}" scope="request"/>
        <c:import url="../components/review_card.jsp"/>
      </c:if>
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