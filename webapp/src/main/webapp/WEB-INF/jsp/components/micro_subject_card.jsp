<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<!-- Params -->
<c:set var="subject" value="${requestScope.subject}"/>

<a href='<c:out value="${pageContext.request.contextPath}/subject/${subject.id}"/>'>
    <sl-card class="micro-subject-card" >
        <div>
            <h3 class="card-title"><c:out value="${subject.name}" /> - <c:out value="${subject.id}"/></h3>
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
            </sl-badge>
        </div>
    </sl-card>
</a>