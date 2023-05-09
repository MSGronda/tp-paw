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
      <img class="profile-image" src="<c:url value="/image/${user.imageId}"/>" alt="${pic}" >
    </div>
    <div class="title">
      <h1><c:out value="${user.username}"/></h1>
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