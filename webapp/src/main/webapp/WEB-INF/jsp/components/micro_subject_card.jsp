<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<!-- Params -->
<c:set var="subject" value="${requestScope.subject}"/>
<%--<c:set var="subProfs" value="${requestScope.subProfs}"/>--%>
<%--<c:set var="reviewCount" value="${requestScope.reviewCount}"/>--%>
<%--<c:set var="difficulty" value="${requestScope.difficulty}"/>--%>
<%--<c:set var="time" value="${requestScope.time}"/>--%>
<%--<c:set var="progress" value="${requestScope.progress}"/>--%>

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

                <%--      <c:out value="${subject.credits}"/> Credit<c:if test="${subject.credits != 1}">s</c:if>--%>
            </sl-badge>
<%--            <c:if test="${subject.prerequisites.isEmpty()}">--%>
<%--                <sl-badge variant="warning" pill>--%>
<%--                    <spring:message code="card.noPrerequisites"/>--%>
<%--                </sl-badge>--%>
<%--            </c:if>--%>
<%--            <c:if test="${!subject.prerequisites.isEmpty()}">--%>
<%--                <sl-tooltip content="<c:out value="${subject.prerequisites.toString()}" />">--%>
<%--                    <sl-badge variant="warning" pill>--%>
<%--                        <c:choose>--%>
<%--                            <c:when test="${subject.prerequisites.size() != 1}">--%>
<%--                                <spring:message code="card.prerequisites" arguments="${subject.prerequisites.size()}"/>--%>
<%--                            </c:when>--%>
<%--                            <c:otherwise>--%>
<%--                                <spring:message code="card.onePrerequisite" arguments="${subject.prerequisites.size()}"/>--%>
<%--                            </c:otherwise>--%>
<%--                        </c:choose>--%>
<%--                    </sl-badge>--%>
<%--                </sl-tooltip>--%>
<%--            </c:if>--%>
        </div>
    </sl-card>
</a>