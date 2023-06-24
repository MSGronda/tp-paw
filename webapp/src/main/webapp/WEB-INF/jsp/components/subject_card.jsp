<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<!-- Params -->
<c:set var="subject" value="${requestScope.subject}"/>
<c:set var="progress" value="${requestScope.progress}"/>

<a href='<c:url value="/subject/${subject.id}"/>'>
<sl-card class="review-card subject-card" >
  <div>
    <b class="class-info"><c:out value="${subject.name}" /> - <c:out value="${subject.id}"/></b>
    <div class="chip-row class-info">
      <sl-badge variant="primary" pill>
        <c:choose>
          <c:when test="${subject.credits != 1}">
            <spring:message code="card.credits" arguments="${subject.credits}"/>
          </c:when>
          <c:otherwise>
            <spring:message code="card.credit" arguments="${subject.credits}"/>
          </c:otherwise>
        </c:choose>

        <%--      <c:out value="${subject.credits}"/> Credit<c:if test="${subject.credits != 1}">s</c:if>--%>
      </sl-badge>
      <c:if test="${not empty subject.prerequisites}">
        <sl-badge variant="warning" pill>
          <spring:message code="card.noPrerequisites"/>
        </sl-badge>
      </c:if>
      <c:if test="${not empty subject.prerequisites}">
        <sl-tooltip content="<c:out value="${subject.prerequisites.toString()}" />">
          <sl-badge variant="warning" pill>
            <c:choose>
              <c:when test="${subject.prerequisites.size() > 1}">
                <spring:message code="card.prerequisites" arguments="${subject.prerequisites.size()}"/>
              </c:when>
              <c:otherwise>
                <spring:message code="card.onePrerequisite" arguments="${subject.prerequisites.size()}"/>
              </c:otherwise>
            </c:choose>
          </sl-badge>
        </sl-tooltip>
      </c:if>

      <sec:authorize access="isAuthenticated()">
        <c:choose>
          <c:when test="${progress eq 'DONE'}">
            <sl-tooltip class="progress-icon" content="<spring:message code="card.progress.tooltip.passed"/>">
                  <sl-icon name="check2-circle"></sl-icon>
            </sl-tooltip>
          </c:when>
        </c:choose>
      </sec:authorize>
    </div>
  </div>
    <div slot="footer" class="chip-row ">
      <c:choose>
        <c:when test="${subject.reviewStats.difficulty eq 'EASY'}">
          <sl-badge size="medium" variant="success" pill><spring:message code="form.easy"/></sl-badge>
        </c:when>
        <c:when test="${subject.reviewStats.difficulty eq 'MEDIUM'}">
          <sl-badge size="medium" variant="primary" pill><spring:message code="form.normal"/></sl-badge>
        </c:when>
        <c:when test="${subject.reviewStats.difficulty eq 'HARD'}">
          <sl-badge size="medium" variant="danger" pill><spring:message code="form.hard"/></sl-badge>
        </c:when>
      </c:choose>

      <c:choose>
        <c:when test="${subject.reviewStats.timeDemanding eq 'LOW'}">
          <sl-badge size="medium" variant="success" pill><spring:message code="form.NotTimeDemanding" /></sl-badge>
        </c:when>
        <c:when test="${subject.reviewStats.timeDemanding eq 'MEDIUM'}">
          <sl-badge size="medium" variant="primary" pill><spring:message code="form.averageTimeDemand" /></sl-badge>
        </c:when>
        <c:when test="${subject.reviewStats.timeDemanding eq 'HIGH'}">
          <sl-badge size="medium" variant="warning" pill><spring:message code="form.timeDemanding" /></sl-badge>
        </c:when>
      </c:choose>

      <c:choose>
        <c:when test="${subject.reviewStats.reviewCount == 0}">
          <sl-badge variant="neutral" pill>
            <spring:message code="form.noDif" />
          </sl-badge>
        </c:when>
        <c:when test="${subject.reviewStats.reviewCount == 1}">
        <span class="review-count">
          <spring:message code="card.oneReview" arguments="${subject.reviewStats.reviewCount}"/>
        </span>
        </c:when>
        <c:otherwise>
        <span class="review-count">
          <spring:message code="card.moreReviews" arguments="${subject.reviewStats.reviewCount}"/>
        </span>
        </c:otherwise>
      </c:choose>
    </div>
</sl-card>
</a>