<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <jsp:include page="../components/head_shared.jsp"/>
    <title>Dashboard</title>
    <style>
        /* General */
        .dashboard-area {
            display: flex;
            flex-direction: row;
            align-items: stretch;
            height: 94vh;
            margin: auto 0;
        }
        .choosing-area{
            width: 100%;
            padding: 1rem;
            height: 100%;
        }
        .tabs::part(base){
            height: 100%;
        }
        .tabs::part(body){
            width: 100%;
            height: 100%;
            display: flex;
            flex-direction: column;
            justify-content: start;
            align-items: center;
            margin: 1rem
        }
        .tabs::part(tabs){
            padding:0;
            --padding:0;
            margin: 0;
        }
        .tab-panel{
            width: 90%;
            height: 100%;
            --padding: 0;
        }

        /* Overview */
        .overview-area{
            display: flex;
            flex-direction: column;
            justify-content: space-around;
            height: 100%;
            width: 100%;
        }
        .stat-row{
            display: flex;
            flex-direction: row;
            align-items: center;
            justify-content: space-evenly;
        }
        .general-info-card{
            padding-left: 2rem;
            padding-right: 2rem;
        }
        .progress-card{
            min-width: 35%;
            padding-left: 2rem;
            padding-right: 2rem;
        }
        .progress-card::part(base){
            height: 25rem;
        }
        .progress-card::part(header){
            padding-top: 0;
            padding-bottom: 0;
        }
        .progress-card::part(body){
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100%;
         }

        /* Current semester */
        .current-semester-area{
            display: flex;
            flex-direction: row;
            justify-content: center;
            align-items: center;
            height: 100%;
            width: 100%;
        }
        .time-table-area{
            height: 100%;
            width: 100%;
        }
        .current-semester-class-area{
            padding: 0.5rem 0.2rem;
            width: 45%;
            height: 100%;
        }
        .current-semester-card{
            width: 100%;
        }
        .current-semester-card::part(header){
            padding-top: 0;
            padding-bottom: 0;
        }
        .current-semester-card::part(base), .current-semester-card::part(body){
            height: 100%;
        }
        .current-semester-card::part(body){
            padding: 0.5rem;
        }
        .current-semester-subject-info-list{
            height: 100%;
            overflow-y: scroll;
            flex: 1;
        }
        .no-active-semester-info-area{
            align-self: center;
            justify-self: start;
            justify-content: center ;
            align-items: center;
            max-width: 30rem;
            padding-bottom: 1rem;
        }
        .no-active-semester-info{
            font-weight: normal;
            font-size: 1.7rem;
        }
        .builder-button{
            color: #79b2fc;
        }

       /*  Future subjects  */
        .future-subjects-area {
            height: 100%;
            width: 100%;
            /*display: grid;*/
            /*grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));*/
            /*overflow-y: scroll;*/
        }
        .subject-card{    /* We overwrite previous attributres */
            max-width: 30% !important;
            min-width: 30% !important;
            padding: 0.5rem;
        }
        .subject-card::part(base){
            max-height: 25% !important;
            min-height: 25% !important;
        }
    </style>
    <jsp:include page="../components/component-style/class_info_card_style.jsp"/>
</head>
<body>
<jsp:include page="../components/navbar.jsp"/>
    <main class="container-90">
        <div class="dashboard-area">
            <div class="choosing-area">
                <sl-tab-group class="tabs" >
                    <sl-tab  slot="nav" panel="overview">Overview</sl-tab>
                    <sl-tab slot="nav" panel="current-semester">Current Semester</sl-tab>
                    <sl-tab slot="nav" panel="future-subjects">Future Subjects</sl-tab>
                    <sl-tab slot="nav" panel="past-subjects">Past Subjects</sl-tab>


                    <sl-tab-panel class="tab-panel" name="overview">
                        <div class="overview-area">
                            <sl-card class="general-info-card">
                                <div class="row">
                                    <h3>Completed Credits</h3>
                                    <sl-divider style="height: 1rem" vertical></sl-divider>
                                    <h5>${userCreditsDone}</h5>
                                </div>
                                <div class="row">
                                    <h3>Total Credits In Your Degree</h3>
                                    <sl-divider style="height: 1rem" vertical></sl-divider>
                                    <h5>${totalCredits}</h5>
                                </div>
                            </sl-card>
                            <div class="stat-row">
                                <sl-card class="progress-card">
                                    <div slot="header">
                                        <h3>Overall Progress</h3>
                                    </div>

                                    <div class="column-center">
                                        <sl-progress-ring value="${userProgressPercentage}"></sl-progress-ring>
                                        <h4>${userProgressPercentage} %</h4>
                                    </div>
                                </sl-card>
                                <sl-card class="progress-card">
                                    <div slot="header">
                                        <h3>Completed Credits By Year</h3>
                                    </div>
                                    <div style="width: 600px;"><canvas id="progress-by-year"></canvas></div>
                                </sl-card>
                            </div>

                        </div>
                    </sl-tab-panel>


                    <sl-tab-panel class="tab-panel" name="current-semester">
                        <div class="current-semester-area">
                            <c:if test="${currentUserSemester.size() != 0}">
                                <div class="time-table-area">
                                    <jsp:include page="../components/time_table.jsp"/>
                                </div>
                                <div id="chosen-tab" class="current-semester-class-area">
                                    <sl-card class="current-semester-card">
                                        <div slot="header">
                                            <div class="row-space-between">
                                                <h4 style="margin: 0.5rem">This semester you are taking</h4>
                                            </div>
                                        </div>
                                        <div class="current-semester-subject-info-list">
                                            <c:forEach var="subject" items="${currentUserSemester}">
                                                <c:set var="name" value="${subject.name}" scope="request"/>
                                                <c:set var="subClass" value="${subject.subjectClasses.values().stream().findFirst().get()}" scope="request"/>
                                                <a href='<c:out value="${pageContext.request.contextPath}/subject/${subject.id}"/>'>
                                                    <jsp:include page="../components/class_info_card.jsp"/>
                                                </a>
                                            </c:forEach>
                                        </div>
                                    </sl-card>
                                </div>
                            </c:if>
                            <c:if test="${currentUserSemester.size() == 0}">
                                <div class="no-active-semester-info-area">
                                    <h3 class="no-active-semester-info">
                                        It seems like you don't have an active semester. You can build one using the
                                        <a class="builder-button" href="<c:out value="${pageContext.request.contextPath}/builder"/>" >
                                            semester builder.
                                        </a>
                                    </h3>
                                </div>
                            </c:if>
                        </div>
                    </sl-tab-panel>
                    <sl-tab-panel  class="tab-panel" name="future-subjects">
                        <div class="future-subjects-area">
                            <c:forEach var="subject" items="${futureSubjects}">
                                <c:set var="subject" value="${subject}" scope="request"/>
<%--                                <c:set var="reviewCount" value="${reviewStats[subject.id].reviewCount}" scope="request"/>--%>
<%--                                <c:set var="difficulty" value="${reviewStats[subject.id].difficulty}" scope="request"/>--%>
<%--                                <c:set var="time" value="${reviewStats[subject.id].timeDifficulty}" scope="request"/>--%>
<%--                                <c:set var="progress" value="${subjectProgress.getOrDefault(subject.id, 0)}" scope="request"/>--%>
                                <c:import url="../components/subject_card.jsp"/>
                            </c:forEach>
                        </div>
                    </sl-tab-panel>
                    <sl-tab-panel name="past-subjects">

                    </sl-tab-panel>
                </sl-tab-group>
            </div>
        </div>
    </main>
<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>
<script src="${pageContext.request.contextPath}/js/schedule.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.3.0/chart.min.js" integrity="sha512-mlz/Fs1VtBou2TrUkGzX4VoGvybkD9nkeXWJm3rle0DPHssYYx4j+8kIS15T78ttGfmOjH0lLaBXGcShaVkdkg==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.3.0/chart.umd.js" integrity="sha512-CMF3tQtjOoOJoOKlsS7/2loJlkyctwzSoDK/S40iAB+MqWSaf50uObGQSk5Ny/gfRhRCjNLvoxuCvdnERU4WGg==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.3.0/helpers.js" integrity="sha512-BQJ3AP+pvkpSDEexjv6OYwGVCVIFo507d09S8pFPTp63+d7YZDrvjoB+4cSPTThQVfQjP6yybIZ6P29ZQGcPvQ==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script defer>

    const daysOfWeek = [
        '<spring:message code="subject.classDay1"/>', '<spring:message code="subject.classDay2"/>', '<spring:message code="subject.classDay3"/>',
        '<spring:message code="subject.classDay4"/>', '<spring:message code="subject.classDay5"/>', '<spring:message code="subject.classDay6"/>'
    ]

    <c:if test="${currentUserSemester.size() != 0}">
        //  - - - - - - - Create time table - - - - - - -
        const schedule = new Schedule(29, 7);

        //  - - - - - - - Inflate time table - - - - - - -
        <c:forEach var="subject" items="${currentUserSemester}">
        <c:forEach var="subClass" items="${subject.subjectClasses.values()}">
        schedule.addClass('${subject.id}','${subject.name}',
            [
                <c:forEach var="classTime" items="${subClass.getClassTimes()}">
                {

                    'day': '${classTime.getDay()}',
                    'start': '${classTime.getStartTime()}',
                    'end': '${classTime.getEndTime()}',
                    'loc': '${classTime.getClassLoc()}',
                    'building': '${classTime.getBuilding()}',
                    'mode': '${classTime.getMode()}'
                },
                </c:forEach>
            ],
            </c:forEach>
        )
        </c:forEach>
    </c:if>
    // - - - - - - - Inflate chart - - - - - - -
    const chart = new Chart(document.getElementById('progress-by-year'),
        {type: 'bar', options: {animation: false, plugins: {legend: {display: false}, tooltip: {enabled: false}}},
            data: {
                labels: [
                    <c:forEach var="year" items="${creditsDoneByUserPerYear.keySet()}">
                        <c:if test="${year != -1}">'Year ${year}',</c:if>
                    </c:forEach>
                    'Electives'
                ],
                datasets: [
                    {
                        barThickness: 50,
                        data: [
                            <c:forEach varStatus="status" var="year" items="${creditsDoneByUserPerYear.values()}">
                                <c:if test="${!status.first}">${year},</c:if>
                            </c:forEach>
                            ${creditsDoneByUserPerYear.values().stream().findFirst().get()}
                        ]
                    }
                ]
            }
        }
    );

</script>
</body>

</html>
