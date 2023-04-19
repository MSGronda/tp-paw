<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--<%@ taglib prefix="spring" uri="http://java.sun.com/jsp/jstl/fmt" %>--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html lang="en">
<head>
    <title>Uni</title>

    <jsp:include page="../components/head_shared.jsp"/>

    <style>
        main {
            background-color: #efefef;
            flex: 1 0 auto;
            display: flex;
            flex-direction: column;
            padding: 8px;
        }

        sl-tab-panel.year-panel::part(base) {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));

            padding: 1.25rem;
            gap: 1rem;
        }

        sl-tab-group::part(base) {
            overflow: hidden;
        }
    </style>
</head>
<body>
<jsp:include page="../components/navbar.jsp"/>
<main>
    <sl-tab-group class="year-group container-70">
        <c:forEach var="year" items="${years}">
            <%--          <sl-tab slot="nav" panel="semester-${semester.number}">--%>
            <sl-tab slot="nav" panel="year-${year}">

                    <%--            <spring:message code="home.semester" arguments="${semester.number}"/>--%>
                <spring:message code="home.year" arguments="${year}"/>
                    <%--            <c:out value="${year}° Year"/>--%>
                    <%--            <c:out value="${semester.number / 2}° Year"/>--%>
            </sl-tab>
        </c:forEach>
        <sl-tab slot="nav" panel="electivas">
            <spring:message code="home.electives"/>
        </sl-tab>

        <c:forEach var="year" items="${years}">
            <sl-tab-panel class="year-panel" name="year-${year}">
                <c:forEach var="subject" items="${infSubsByYear[year]}">
                    <c:set var="subject" value="${subject}" scope="request"/>
                    <c:set var="subProfs" value="${profsBySubId[subject.id]}" scope="request"/>
                    <c:set var="reviewCount" value="${subjectReviewCount[subject.id]}" scope="request"/>
                    <c:set var="prereqNames" value="${prereqNames.get(subject.id)}" scope="request"/>
                    <c:import url="../components/subject_card.jsp"/>
                </c:forEach>

            </sl-tab-panel>
        </c:forEach>
        <sl-tab-panel class="year-panel" name="electivas">
            <%--            TODO - pasar lista de materias electivas--%>
        </sl-tab-panel>
    </sl-tab-group>
</main>
<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>
</body>
</html>
