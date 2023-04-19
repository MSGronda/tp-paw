<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<!-- Params -->
<c:set var="subject" value="${requestScope.subject}"/>
<c:set var="subProfs" value="${requestScope.subProfs}"/>
<c:set var="reviewCount" value="${requestScope.reviewCount}"/>
<c:set var="prereqNames" value="${requestScope.prereqNames}"/>





<a href='<c:out value="${pageContext.request.contextPath}/subject/${subject.id}"/>'>
<sl-card class="card-header subject-card" >
  <div slot="header">
    <b><c:out value="${subject.name}" /> - <c:out value="${subject.id}"/></b>
  </div>
<%--  <c:choose>--%>
<%--    <c:when test="${empty subProfs}">--%>
<%--      <spring:message code="card.noProfessor"/>--%>
<%--    </c:when>--%>
<%--    <c:otherwise>--%>
<%--      <spring:message code="card.headProfessor"/><br><c:out value="${subProfs[0].name}"/>--%>
<%--    </c:otherwise>--%>
<%--  </c:choose>--%>
  <div class="chip-row">
    <c:choose>
      <c:when test="${reviewCount == 0}">
        <sl-badge variant="warning" pill>
          <spring:message code="form.noDif" />
        </sl-badge>
      </c:when>
      <c:when test="${reviewCount == 1}">
        <sl-badge variant="primary" pill>
          <spring:message code="card.oneReview" arguments="${reviewCount}"/>
        </sl-badge>
      </c:when>
      <c:otherwise>
        <sl-badge variant="primary" pill>
          <spring:message code="card.moreReviews" arguments="${reviewCount}"/>
        </sl-badge>
      </c:otherwise>
    </c:choose>
  </div>

  <div slot="footer" class="chip-row">
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
    <c:if test="${subject.prerequisites.isEmpty()}">
      <sl-badge variant="warning" pill>
        <spring:message code="card.noPrerequisites"/>
      </sl-badge>
    </c:if>
    <c:if test="${!subject.prerequisites.isEmpty()}">
<%--      <c:forEach items="${prereqNames}" var="prereqName">--%>
<%--        <p>${prereqName}</p>--%>
<%--        <p> hola</p>--%>
<%--      </c:forEach>--%>
      <sl-tooltip content="
            <c:forEach items="${prereqNames}" var="name">
              <c:out value="${name}"/>
              ,
            </c:forEach>">
        <sl-badge variant="warning" pill>
          <c:choose>
            <c:when test="${subject.prerequisites.size() != 1}">
              <spring:message code="card.prerequisites" arguments="${subject.prerequisites.size()}"/>
            </c:when>
            <c:otherwise>
              <spring:message code="card.onePrerequisite" arguments="${subject.prerequisites.size()}"/>
            </c:otherwise>
          </c:choose>
        </sl-badge>
      </sl-tooltip>
    </c:if>
<%--    <c:if test="${!empty subject.department}">--%>
<%--      <sl-badge variant="success" pill>--%>
<%--        <c:out value="${subject.department}" />--%>
<%--      </sl-badge>--%>
<%--    </c:if>--%>
  </div>
</sl-card>
</a>