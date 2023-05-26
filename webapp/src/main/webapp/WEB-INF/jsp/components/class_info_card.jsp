<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="sub" value="${requestScope.subject}"/>
<c:set var="subClass" value="${requestScope.subClass}"/>

<sl-card class="class-card">
    <div class="chooser" slot="header">
        <h5>${sub.name} - ${subClass.getIdClass()}</h5>
    </div>
    <div class="column">
        <table>
            <thead>
            <tr>
                <th><spring:message code="subject.classDay"/>
                <th><spring:message code="builder.time"/></th>
                <th><spring:message code="builder.class"/></th>
                <th><spring:message code="builder.building"/></th>
                <th><spring:message code="builder.mode"/></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach varStatus="status" var="classTime" items="${subClass.getClassTimes()}">
                <tr>
                    <c:choose>
                        <c:when test="${classTime.getDay() != 0}">
                            <td><spring:message code="subject.classDay${classTime.getDay()}"/>
                        </c:when>
                        <c:otherwise><<td>-</td></c:otherwise>
                    </c:choose>
                    <td>${classTime.getStartTime()} - ${classTime.getEndTime()}</td>
                    <td>${classTime.getClassLoc()}</td>
                    <td>${classTime.getBuilding()}</td>
                    <td>${classTime.getMode()}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

    </div>
</sl-card>