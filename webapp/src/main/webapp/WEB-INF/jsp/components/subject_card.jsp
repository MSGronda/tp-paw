<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!-- Params -->
<c:set var="subject" value="${requestScope.subject}"/>
<c:set var="subProfs" value="${requestScope.subProfs}"/>

<sl-card class="card-header subject-card">
  <div slot="header">
    <b><c:out value="${subject.name}" /></b>
  </div>
  Head Professor: <br><c:out value="${subProfs[0].name}"/>
  <div slot="footer" class="chip-row">
    <sl-badge variant="primary" pill>
      <c:out value="${subject.credits}"/> Credit<c:if test="${subject.credits != 1}">s</c:if>
    </sl-badge>
    <c:if test="${subject.prerequisites.isEmpty()}">
      <sl-badge variant="warning" pill>No Prerequisites</sl-badge>
    </c:if>
    <c:if test="${!subject.prerequisites.isEmpty()}">
      <sl-badge variant="warning" pill>
        <c:out value="${subject.prerequisites.size()}"/>
        Prerequisite<c:if test="${subject.prerequisites.size() != 1}">s</c:if>
      </sl-badge>
    </c:if>
    <sl-badge variant="success" pill>
      <c:out value="${subject.department}" />
    </sl-badge>
  </div>
</sl-card>
