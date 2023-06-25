<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="name" value="${requestScope.name}"/>
<c:set var="subClass" value="${requestScope.subClass}"/>

<sl-card class="class-card">
    <div class="chooser" slot="header">
        <h5 class="class-card-name"><c:out value="${name}"/> - <c:out value="${subClass.classId}"/></h5>
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
            <c:forEach varStatus="status" var="classTime" items="${subClass.classTimes}">
                <tr>
                    <c:choose>
                        <c:when test="${classTime.day != 0}">
                            <td><spring:message code="subject.classDay${classTime.day}"/>
                        </c:when>
                        <c:otherwise><<td>-</td></c:otherwise>
                    </c:choose>
                    <td><c:out value="${classTime.startTime}"/> - <c:out value="${classTime.endTime}"/></td>
                    <td><c:out value="${classTime.classLoc}"/></td>
                    <td><c:out value="${classTime.building}"/></td>
                    <td><c:out value="${classTime.mode}"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

    </div>
</sl-card>