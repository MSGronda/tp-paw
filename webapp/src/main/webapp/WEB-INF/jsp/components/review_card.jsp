<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%--Params--%>
<c:set var="review" value="${requestScope.review}"/>
<c:set var="fromProfile" value="${requestScope.fromProfile}"/>

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
    text-decoration: underline;
    text-underline-color: black;
    text-decoration-thickness: 0.05rem;
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
</style>

<sl-card class="card-header">
  <div slot="header" class="header">
    <c:choose>
      <c:when test="${fromProfile}">
        <c:out value="${review.subjectId}" /> - <c:out value="${review.subjectName}"/>
      </c:when>
      <c:otherwise>
        <c:choose>
          <c:when test="${review.anonymous}">
            <spring:message code="form.anonymous"/>
          </c:when>
          <c:when test="${!review.anonymous}">
                <a class="username-redirect" href="<c:url value="/profile/${review.userId}"/>"><c:out value="${review.username}"/></a>
          </c:when>
        </c:choose>
      </c:otherwise>
    </c:choose>

    <c:if test="${review.userId == loggedUser.id}">
      <sl-icon-button name="pencil-square" label="edit" href="<c:url value="/review/${review.subjectId}/edit/${review.id}"/>"></sl-icon-button>
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
      <c:when test="${review.timeDemanding == 1}">
        <sl-badge size="medium" variant="warning"><spring:message code="form.timeDemanding"/></sl-badge>
      </c:when>
      <c:otherwise>
        <sl-badge size="medium" ariant="primary"><spring:message code="form.NotTimeDemanding"/></sl-badge>
      </c:otherwise>
    </c:choose>
  </div>
</sl-card>


