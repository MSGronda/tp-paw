<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%--Params--%>
<c:set var="review" value="${requestScope.review}"/>
<c:set var="fromProfile" value="${requestScope.fromProfile}"/>
<c:set var="user" value="${requestScope.user}"/>

<style>
  .card-header {
    width: 100%;
    margin: 15px;
  }
  .header{
    display: flex;
    justify-content: space-between;
    font-size: 1.2rem;
  }
  .username-redirect{
    color: black;
  }
  .username-redirect:hover{
    color: #0369a1;
  }
  .break-text {
    overflow-wrap: break-word;
    margin-bottom: 2%;
  }
  #more {
    display: none;
  }

  .showMore{
    display: flex;
  }
  .showMore::part(base) {
    border: 0;
  }
  .showMore::part(base):hover{
    background: 0;
  }
  .showMore::part(base):active {
    background: rgba(255, 99, 71, 0);
  }
  .delete-button{
    color: red;
  }
  .vote-button{
    padding: 0;
  }
  sl-button.vote-button::part(base){
    border-color: white;
  }
  .vote-button-icon{
    font-size: 20px;
    padding-top: 0.3rem;
  }
</style>

<sl-card class="card-header">
  <div slot="header" class="header">
    <c:choose>
      <c:when test="${fromProfile}">

        <a class="username-redirect" href="/subject/${review.subjectId}"><c:out value="${review.subjectId}" /> - <c:out value="${review.subjectName}"/></a>
      </c:when>
      <c:otherwise>
        <c:choose>
          <c:when test="${review.anonymous}">
            <spring:message code="form.anonymous"/>
          </c:when>
          <c:when test="${!review.anonymous}">
                <a class="username-redirect" href="<c:url value="/user/${review.userId}"/>"><c:out value="${review.username}"/></a>
          </c:when>
        </c:choose>
      </c:otherwise>
    </c:choose>

    <c:if test="${review.userId == user.id}">
      <div>
        <sl-icon-button name="pencil-square" label="edit" href="<c:url value="/review/${review.subjectId}/edit/${review.id}"/>"></sl-icon-button>
        <sl-icon-button name="trash3" class="delete-button" label="delete" href="<c:url value="/review/${review.subjectId}/delete/${review.id}"/>"></sl-icon-button>
      </div>
    </c:if>
  </div>

  <div class="break-text">
    <c:if test="${review.requiresShowMore}">
      <c:out value="${review.previewText}"/><span id="dots">...</span><span id="more"><c:out value="${review.showMoreText}"/></span>

      <br />
      <sl-button size="small" class="showMore"><spring:message code="subject.showMore" />  <sl-icon name="chevron-down"></sl-icon></sl-button>
    </c:if>
    <c:if test="${!review.requiresShowMore}">
      <c:out value="${review.text}"/>
    </c:if>
  </div>
  <div>
    <c:choose>
      <c:when test="${review.easy == 0}">
        <sl-badge size="medium" variant="success"><spring:message code="form.easy"/></sl-badge>
      </c:when>
      <c:when test="${review.easy == 1}">
        <sl-badge size="medium" variant="primary"><spring:message code="form.normal"/></sl-badge>
      </c:when>
      <c:otherwise>
        <sl-badge size="medium" variant="danger"><spring:message code="form.hard"/></sl-badge>
      </c:otherwise>
    </c:choose>

    <c:choose>
      <c:when test="${review.timeDemanding == 0}">
        <sl-badge size="medium" variant="success"><spring:message code="form.NotTimeDemanding"/></sl-badge>
      </c:when>
      <c:when test="${review.timeDemanding == 1}">
        <sl-badge size="medium" variant="primary"><spring:message code="form.averageTimeDemand"/></sl-badge>
      </c:when>
      <c:otherwise>
        <sl-badge size="medium" variant="warning"><spring:message code="form.timeDemanding"/></sl-badge>
      </c:otherwise>
    </c:choose>
  </div>
  <div slot="footer" >
    <form style="margin: 0" id="form-${review.id}">
      <input type="hidden" name="reviewId" id="reviewId" value="${review.id}">

      <input type="hidden" name="vote" id="vote" value="${userVotes.getOrDefault(review.id, 0)}">

      <sec:authorize access="isAuthenticated()">
        <sl-button class="vote-button" variant="default" size="small" circle
                   data-form-id="form-${review.id}" data-form-value="1">
          <sl-icon id="like-icon-form-${review.id}" class="vote-button-icon" name="hand-thumbs-up" label="Upvote"
                  <c:choose>
                    <c:when test="${ userVotes[review.id] == 1}">
                      style="color: #f5a623;"
                    </c:when>
                    <c:otherwise>
                      style="color: #4a90e2;"
                    </c:otherwise>
                  </c:choose>
          ></sl-icon>
        </sl-button>
      </sec:authorize>
      <sec:authorize access="!isAuthenticated()">
        <sl-button class="vote-button" variant="default" size="small" circle href="${pageContext.request.contextPath}/login">
          <sl-icon class="vote-button-icon" name="hand-thumbs-up" label="Upvote"
                      style="color: #4a90e2;"
          ></sl-icon>
        </sl-button>
      </sec:authorize>


      <span id="like-number-form-${review.id}"><c:out value="${review.upvotes}"/></span>

    <sec:authorize access="isAuthenticated()">
      <sl-button class="vote-button" variant="default" size="small" circle
                 data-form-id="form-${review.id}" data-form-value="-1">
        <sl-icon id="dislike-icon-form-${review.id}" class="vote-button-icon" name="hand-thumbs-down" label="Downvote"
                <c:choose>
                  <c:when test="${ userVotes[review.id] == -1}">
                    style="color: #f5a623;"
                  </c:when>
                  <c:otherwise>
                    style="color: #4a90e2;"
                  </c:otherwise>
                </c:choose>
        ></sl-icon>
      </sl-button>
    </sec:authorize>

    <sec:authorize access="!isAuthenticated()">
      <sl-button class="vote-button" variant="default" size="small" circle href="${pageContext.request.contextPath}/login">
        <sl-icon  class="vote-button-icon" name="hand-thumbs-down" label="Downvote"
                <c:choose>
                  <c:when test="${ userVotes[review.id] == -1}">
                    style="color: #f5a623;"
                  </c:when>
                  <c:otherwise>
                    style="color: #4a90e2;"
                  </c:otherwise>
                </c:choose>
        ></sl-icon>
      </sl-button>
    </sec:authorize>

      <span id="dislike-number-form-${review.id}"><c:out value="${review.downvotes}"/></span>
    </form>
  </div>
</sl-card>



