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
            justify-content: center;
            align-items: center;
        }
        .tab-panel{
            width: 90%;
            height: 90%;
            --padding: 0;
        }

        /* Overview */
        .overview-area{
            display: flex;
            flex-direction: column;
            justify-content: space-around;
            height: 100%;
            width: 90%;
        }
        .stat-row{
            display: flex;
            flex-direction: row;
            align-items: center;
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
            width: 40%;
            height: 100%;
        }
        .current-semester-card{
            width: 100%;
        }
        .current-semester-card::part(header){
            padding-top: 0;
            padding-bottom: 0;
        }
        .current-semester-card::part(base){
            height: 100%;
        }
        .current-semester-subject-info-list{
            height: 100%;
            overflow-y: auto;
            flex: 1;
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
<%--                                        <c:forEach var="subject" items="${currentUserSemester}">--%>
<%--                                            <c:set var="sub" value="${subject}" scope="request"/>--%>
<%--                                            <c:set var="subClass" value="${subject.subjectClasses}" scope="request"/>--%>
<%--                                            <jsp:include page="../components/class_info_card.jsp"/>--%>
<%--                                        </c:forEach>--%>
                                    </div>
                                </sl-card>
                            </div>
                        </div>
                    </sl-tab-panel>
                    <sl-tab-panel name="future-subjects">

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

    //  - - - - - - - Create time table - - - - - - -
    const daysOfWeek = [
        '<spring:message code="subject.classDay1"/>', '<spring:message code="subject.classDay2"/>', '<spring:message code="subject.classDay3"/>',
        '<spring:message code="subject.classDay4"/>', '<spring:message code="subject.classDay5"/>', '<spring:message code="subject.classDay6"/>'
    ]
    const schedule = new Schedule(29, 7);

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
