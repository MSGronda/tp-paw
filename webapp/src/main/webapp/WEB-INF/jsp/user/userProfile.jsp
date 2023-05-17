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
    .pag-buttons{
      display: flex;
      flex-direction: column;
      justify-content: space-around;
      align-items: center;
      padding-bottom: 3rem;
    }
    <jsp:include page="../components/table_style.jsp"/>
  </style>
</head>
<body>
<jsp:include page="../components/navbar.jsp" />
<main class="container-50 pusher container-account" >
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
  <c:if test="${not empty reviews}">
    <c:set var="order" value="${order}" scope="request"/>
    <c:set var="dir" value="${dir}" scope="request"/>
    <c:import url="../components/order_dropdown.jsp"/>
  </c:if>
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
  <c:if test="${not empty reviews}">
    <div class="pag-buttons">
      <sl-radio-group name="pagination-radio" value="${actualPage}">
        <sl-radio-button id="prevPage" value="-1" <c:if test="${actualPage-1 <= 0}">disabled</c:if>>
          <sl-icon slot="prefix" name="chevron-left"></sl-icon>
          <spring:message code="subject.previousPage" />
        </sl-radio-button>
        <c:forEach var="pageNum" begin="1" end="${totalPages+1}">
          <c:choose>
            <c:when test="${(pageNum > actualPage -2 and pageNum < actualPage + 2) or (pageNum == 1 or pageNum == totalPages+1)}">
              <div class="active-pag-buttons">
                <sl-radio-button class="pageNumButton" value="${pageNum}">${pageNum}</sl-radio-button>
              </div>
            </c:when>
            <c:when test="${pageNum < totalPages + 1 and pageNum == actualPage + 2}">
              <div class="active-pag-buttons">
                <sl-radio-button class="pageNumButton" value="${pageNum}">${pageNum}</sl-radio-button>
                <sl-radio-button value="..." disabled>...</sl-radio-button>
              </div>
            </c:when>
            <c:when test="${pageNum > 1 and pageNum == actualPage - 2}">
              <div class="active-pag-buttons">
                <sl-radio-button value="..." disabled>...</sl-radio-button>
                <sl-radio-button class="pageNumButton" value="${pageNum}">${pageNum}</sl-radio-button>
              </div>
            </c:when>
            <c:otherwise>
              <div class="hidden-pag-buttons">
                <sl-radio-button class="pageNumButton" value="${pageNum}">${pageNum}</sl-radio-button>
              </div>
            </c:otherwise>
          </c:choose>

        </c:forEach>
        <sl-radio-button id="nextPage" value="0" <c:if test="${actualPage-1 >= totalPages}">disabled</c:if>>
          <sl-icon slot="suffix" name="chevron-right"></sl-icon>
          <spring:message code="subject.nextPage" />
        </sl-radio-button>
      </sl-radio-group>
    </div>
  </c:if>
</main>
<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/url-param-utils.js"></script>
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

  let urlParams = new URLSearchParams(window.location.search);

  let prevButton = document.getElementById("prevPage");
  prevButton.addEventListener('click',
          function(event) {
            event.preventDefault();
            let url = window.location.href;
            let pageNum = Number(urlParams.get('pageNum')) -1;
            if(pageNum >= 0){
              url = addOrUpdateParam(url,"pageNum",pageNum.toString());
            } else {
              url = addOrUpdateParam(url,"pageNum","0");
            }
            window.location.href = url;
          });
  let nextButton = document.getElementById("nextPage");
  nextButton.addEventListener('click',
          function(event) {
            event.preventDefault();
            let url = window.location.href;
            let pageNum = Number(urlParams.get('pageNum')) +1;
            if(pageNum >= 0){
              url = addOrUpdateParam(url,"pageNum",pageNum.toString());
            } else {
              url = addOrUpdateParam(url,"pageNum","0");
            }
            window.location.href = url;
          });

  let elements = document.getElementsByClassName("pageNumButton");
  for (let i = 0, len = elements.length; i < len; i++) {
    elements[i].addEventListener('click',
            function(event) {
              event.preventDefault();
              let url = window.location.href;
              let pageNum = Number(elements[i].value) -1;
              url = addOrUpdateParam(url,"pageNum",pageNum.toString());
              window.location.href = url;
            });
  }
  let activePaginationButtons = document.getElementsByClassName("active-pag-buttons");
  for (let i = 0, len = activePaginationButtons.length; i < len; i++) {
    activePaginationButtons[i].style.display = 'block';
  }
  let hiddenPaginationButtons = document.getElementsByClassName("hidden-pag-buttons");
  for (let i = 0, len = hiddenPaginationButtons.length; i < len; i++) {
    hiddenPaginationButtons[i].style.display = 'none';
  }

</script>
</body>
</html>