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
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
</head>
<body>
<jsp:include page="../components/navbar.jsp"/>
<main>
    <sl-tab-group class="year-group container-70">
        <c:forEach var="year" items="${years}">
            <%--          <sl-tab slot="nav" panel="semester-${semester.number}">--%>
            <sl-tab slot="nav" panel="year-${year}">
                <spring:message code="home.year" arguments="${year}"/>
            </sl-tab>
        </c:forEach>
        <sl-tab slot="nav" panel="electives">
            <spring:message code="home.electives"/>
        </sl-tab>

        <c:forEach var="year" items="${years}">
            <sl-tab-panel class="year-panel" name="year-${year}">
                <c:forEach var="subject" items="${infSubsByYear[year]}">
                    <c:set var="subject" value="${subject}" scope="request"/>
                    <c:set var="reviewCount" value="${reviewStatistics[subject.id].reviewCount}" scope="request"/>
                    <c:set var="difficulty" value="${reviewStatistics[subject.id].difficulty}" scope="request"/>
                    <c:set var="time" value="${reviewStatistics[subject.id].timeDifficulty}" scope="request"/>
                    <c:set var="progress" value="${subjectProgress.getOrDefault(subject.id, 0)}" scope="request"/>
                    <c:import url="../components/subject_card.jsp"/>
                </c:forEach>

            </sl-tab-panel>
        </c:forEach>
        <sl-tab-panel class="year-panel" name="electives">
            <c:forEach var="elective" items="${electives}">
                <c:set var="subject" value="${elective}" scope="request"/>
                <c:set var="reviewCount" value="${electivesReviewStatistics[subject.id].reviewCount}" scope="request"/>
                <c:set var="difficulty" value="${electivesReviewStatistics[subject.id].difficulty}" scope="request"/>
                <c:set var="time" value="${electivesReviewStatistics[subject.id].timeDifficulty}" scope="request"/>
                <c:set var="progress" value="${subjectProgress.getOrDefault(elective.id, 0)}" scope="request"/>
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
    $('document').ready(function(){
        const urlParams = new URLSearchParams(window.location.search);
        const tab = urlParams.get('tab')
        const tabGroup = document.querySelector('.year-group');

        if(tab ==='1' ||tab ==='2' ||tab ==='3' ||tab ==='4' ||tab ==='5'){
            tabGroup.show('year-'+tab);
        }
        else if(tab ==='electives'){
            tabGroup.show('electives');
        }
    });

</script>
