<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--<%@ taglib prefix="spring" uri="http://java.sun.com/jsp/jstl/fmt" %>--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="ar.edu.itba.paw.models.enums.SubjectProgress" %>
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
            padding: 0.5rem;
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
        .title{
            font-weight: normal;
        }
    </style>
</head>
<body>
<jsp:include page="../components/navbar.jsp"/>
<main class="container-70">
    <h1 class="title"><c:out value="${degree.name}"/></h1>
    <sl-tab-group class="year-group ">
        <c:forEach var="year" items="${degree.years}">
            <%--          <sl-tab slot="nav" panel="semester-${semester.number}">--%>
            <sl-tab slot="nav" panel="year-${year.number}">
                <spring:message code="home.year" arguments="${year.number}"/>
            </sl-tab>
        </c:forEach>
        <sl-tab slot="nav" panel="electives">
            <spring:message code="home.electives"/>
        </sl-tab>

        <c:forEach var="year" items="${degree.years}">
            <sl-tab-panel class="year-panel" name="year-${year.number}">
                <c:forEach var="subject" items="${year.subjects}">
                    <c:set var="subject" value="${subject}" scope="request"/>
                    <c:set var="progress" value="${subjectProgress.getOrDefault(subject.id, SubjectProgress.PENDING)}" scope="request"/>
                    <c:import url="../components/subject_card.jsp"/>
                </c:forEach>

            </sl-tab-panel>
        </c:forEach>
        <sl-tab-panel class="year-panel" name="electives">
            <c:forEach var="elective" items="${degree.electives}">
                <c:set var="subject" value="${elective}" scope="request"/>
                <c:set var="progress" value="${subjectProgress.getOrDefault(elective.id, SubjectProgress.PENDING)}" scope="request"/>
                <c:import url="../components/subject_card.jsp"/>
            </c:forEach>
        </sl-tab-panel>
    </sl-tab-group>
</main>
<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>
</body>
</html>

<script>
    window.onload = function(){
        const urlParams = new URLSearchParams(window.location.search);
        const tab = urlParams.get('tab')
        const tabGroup = document.querySelector('.year-group');

        if(tab ==='1' ||tab ==='2' ||tab ==='3' ||tab ==='4' ||tab ==='5'){
            tabGroup.show('year-'+tab);
        }
        else if(tab ==='electives'){
            tabGroup.show('electives');
        }
    };

</script>
