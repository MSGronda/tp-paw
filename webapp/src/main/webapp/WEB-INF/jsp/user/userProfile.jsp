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

    h4 {
      display: flex;
      justify-content: center;
    }

    #more {
      display: none;
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
    .moderator-tag {
      display: flex;
      flex-direction: row;
      align-items: center;
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
      <img class="profile-image" src="<c:url value="/image/${user.imageId}"/>" alt="${pic}" >
    </div>
    <div class="title">
      <div class="moderator-tag">
        <h1><c:out value="${user.username}"/></h1>
        <c:if test="${editor}">
          <h1 class="editor-text"> <spring:message code="profile.editor" /> </h1>
        </c:if>
      </div>
      <sec:authorize access="hasRole('EDITOR')">
        <div class="moderator-tag">
          <c:if test="${!editor}">
            <sl-button variant="primary" outline href="<c:out value="/user/${user.id}/moderator"/>"><spring:message code="profile.make_moderator" /> </sl-button>
          </c:if>
        </div>
      </sec:authorize>
    </div>
    <div>
    </div>
  </div>

  <br/>
  <br>
  <hr />
  <h3><spring:message code="profile.reviews"/></h3>
  <c:if test="${empty reviews}">
    <h4><spring:message code="subject.noreviews"/></h4>
  </c:if>
  <c:forEach var="review" items="${reviews}">
    <c:if test="${!review.anonymous}">
      <c:set var="review" value="${review}" scope="request"/>
      <c:set var="fromProfile" value="${true}" scope="request"/>
      <c:set var="userVotes" value="${userVotes}" scope="request"/>
      <c:set var="user" value="${null}" scope="request"/>
      <c:import url="../components/review_card.jsp"/>
    </c:if>
  </c:forEach>
</main>
<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/review_card.js"></script>
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