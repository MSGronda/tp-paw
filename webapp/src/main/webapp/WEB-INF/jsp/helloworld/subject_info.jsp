<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
  <title>${subject.name}</title>
  <jsp:include page="../components/head_shared.jsp"/>
  <style>
      .card-basic {
          width: 100%;
      }

      .info {
          padding-bottom: 2rem;
      }

      .review_bt {
          width: 15rem;
          align-self: center;
          justify-self: center;
      }

      .bread_tag {
          margin-top: 2%;
          margin-left: 10%;
      }

      .title {
          margin-left: 10%;
      }

      .filter {
          display: flex;
          flex-direction: row-reverse;
          margin-right: 20%;
      }

      h1 {
          font-size: 44px;
          font-weight: bold;
      }

      .review-column {
          display: flex;
          flex-direction: column;
          justify-content: space-around;
          align-items: center;
          width: 100%;
          padding-bottom: 3rem;

      }

      .card-header {
          width: 45%;
          margin: 15px;
          /*max-width: 2000px;*/
      }

      .card-header [slot='header'] {
          display: flex;
          align-items: center;
          justify-content: space-between;
      }

      .card-header h3 {
          margin: 0;
      }

      .break-text {
          overflow-wrap: break-word;
          margin-bottom: 2%;
      }

      a {
          color: #0369a1;
          background-color: transparent;
          text-decoration: none;
      }
  </style>

</head>
<body>

<jsp:include page="../components/navbar.jsp"/>

<main>
  <div class="info container-70">
    <h1>
      <c:out value="${subject.name}"/>
    </h1>
    <sl-card class="card-basic">
      <spring:message code="subject.department"/> <c:out value="${subject.department}"/>
      <sl-divider></sl-divider>
      <spring:message code="subject.credits"/> <c:out value="${subject.credits}"/>
      <sl-divider></sl-divider>
      <spring:message code="subject.prerequisites"/>
      <c:if test="${empty prereqNames}">
        <spring:message code="subject.prerequisites?"/>
      </c:if>
      <c:forEach var="prerec" items="${prereqNames}" varStatus="status">
        <a href='<c:url value="/subject/${prerec.key}"/>'><c:out value="${prerec.value}"/></a>
        <c:if test="${not status.last}">
          ,
        </c:if>
      </c:forEach>
      <sl-divider></sl-divider>
      <spring:message code="subject.professors"/>
      <c:forEach var="proffesor" items="${professors}" varStatus="status">
        <c:out value="${proffesor.name}"/>
        <c:if test="${not status.last}">;
        </c:if>
      </c:forEach>
      <sl-divider></sl-divider>
      <spring:message code="subject.difficulty"/>
      <c:choose>
        <c:when test="${difficulty == 0}">
          <sl-badge size="medium" variant="success"><spring:message code="form.easy"/></sl-badge>
        </c:when>
        <c:when test="${difficulty == 1}">
          <sl-badge size="medium" variant="primary"><spring:message code="form.normal"/></sl-badge>
        </c:when>
        <c:when test="${difficulty == 2}">
          <sl-badge size="medium" variant="danger"><spring:message code="form.hard"/></sl-badge>
        </c:when>
        <c:otherwise>
          <sl-badge size="medium" variant="neutral"><spring:message code="form.noDif"/></sl-badge>
        </c:otherwise>
      </c:choose>
    </sl-card>
  </div>
  <sl-button href='<c:url value="/review/${subject.id}"/>' variant="primary" size="large" pill class="review_bt">
    <spring:message code="subject.review"/></sl-button>
  <br/>
  <hr/>
  <div class="filter">
    <sl-tooltip content="Open viewing sorts" placement="bottom">
      <sl-button variant="text" size="large">
        <sl-icon slot="suffix" name="sort-down"></sl-icon>
        <spring:message code="subject.sort"/></sl-button>
    </sl-tooltip>
  </div>

  <div class="review-column">
    <c:if test="${empty reviews}">
      <h3><spring:message code="subject.noreviews"/></h3>
    </c:if>
    <c:forEach var="review" items="${reviews}">
      <sl-card class="card-header">
        <div slot="header">
          <c:out value="${review.userEmail}"/>
        </div>

        <div class="break-text">
          <c:out value="${review.text}"/>
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
            <c:when test="${review.timeDemanding}">
              <sl-badge size="medium" variant="warning"><spring:message code="form.timeDemanding"/></sl-badge>
            </c:when>
            <c:otherwise>
              <sl-badge size="medium" ariant="primary"><spring:message code="form.NotTimeDemanding"/></sl-badge>
            </c:otherwise>
          </c:choose>
        </div>
      </sl-card>
    </c:forEach>
  </div>
</main>
<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>
</body>
</html>
