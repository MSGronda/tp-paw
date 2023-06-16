<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
  <jsp:include page="../components/head_shared.jsp"/>
  <title>Dashboard</title>
  <style>
      sl-tab-panel {
          width: 100%;
          min-height: 70vh;
          overflow-y: hidden;
      }

      /* General */
      .dashboard-area {
          display: flex;
          flex-direction: row;
          align-items: stretch;
          min-height: 94vh;
          margin: auto 0;
      }

      .choosing-area {
          width: 100%;
          padding: 0.5rem;
          min-height: 100%;
      }

      .tabs::part(base) {
          height: 100%;
      }

      .tabs::part(body) {
          width: 100%;
          height: 100%;
          display: flex;
          flex-direction: column;
          justify-content: start;
          align-items: center;
          margin: 1rem
      }

      .tabs::part(tabs) {
          padding: 0;
          --padding: 0;
          margin: 0;
      }

      .tabs::part(nav) {
          position: sticky;
          top: 0;
          background: #efefef;
          padding-bottom: 0.05rem;
      }

      /* Overview */
      sl-progress-ring {
          --size: 175px;
      }

      .overview-area {
          display: flex;
          flex-direction: column;
          justify-content: center;
          gap: 1rem;
          min-height: 100%;
          margin: auto;
      }

      .stat-row {
          flex: 1 0 auto;
          display: flex;
          flex-direction: row;
          gap: 1rem;
      }

      .general-info-card {
          width: 100%;
      }

      .general-info-card::part(body) {
          padding: 1rem 2rem;
      }

      .chart-container {
          position: relative;
          margin: auto;
          height: 90%;
          width: 90%;
      }

      .progress-card {
          flex: auto;
          min-width: 25vw;
      }

      .progress-card::part(base) {
          height: 100%;
      }

      .progress-card::part(header) {
          padding-top: 0;
          padding-bottom: 0;
      }

      .progress-card::part(body) {
          display: flex;
          justify-content: center;
          align-items: center;
          height: 100%;
      }

      /* Current semester */
      .current-semester-area {
          display: flex;
          flex-direction: row;
          justify-content: center;
          align-items: center;
          height: 90vh;
          width: 100%;
      }

      .time-table-area {
          height: 100%;
          width: 100%;
          overflow-y: auto;
      }
      .current-semester-class-area {
          padding: 0.5rem 0.2rem;
          width: 45%;
          height: 100%;
      }
      .current-semester-card {
          width: 100%;
      }
      .current-semester-card::part(header) {
          padding-top: 0;
          padding-bottom: 0;
      }
      .current-semester-card::part(base), .current-semester-card::part(body) {
          height: 100%;
      }
      .current-semester-card::part(body) {
          padding: 0.5rem;
      }
      .current-semester-subject-info-list {
          height: 95%;
          overflow-y: auto;
          flex: 1;
      }
      .builder-button {
          color: #79b2fc;
      }
      .semester-edit-area{
        position: fixed;
        bottom: 1.5rem;
        right: 1.5rem;
        display: flex;
        flex-direction: column;
      }
      .finish-button-icon{
        font-size: 2rem;
        padding-top: 0.15rem;
      }
      .semester-edit-button{
        padding: 0.2rem;
      }

      /*  Future subjects  */
      .future-subjects-area {
          display: grid;
          grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));

          padding: 0 1rem 2rem 1rem;
          gap: 1rem;
      }

      /*.subject-card{    !* We overwrite previous attributres *!*/
      /*    max-width: 30% !important;*/
      /*    min-width: 30% !important;*/
      /*    padding: 0.5rem;*/
      /*}*/
      /*.subject-card::part(base){*/
      /*    max-height: 25% !important;*/
      /*    min-height: 25% !important;*/
      /*}*/

      /* Past subjects */
      .past-subjects-area {
          display: grid;
          grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));

          padding: 0 1rem 2rem 1rem;
          gap: 1rem;
      }


      /*  Common style */
      .empty-tab-area {
          align-self: center;
          justify-self: center;
          justify-content: center;
          align-items: center;
          max-width: 30rem;
          padding-bottom: 1rem;
      }

      .empty-tab-info {
          font-weight: normal;
          font-size: 1.7rem;
      }
  </style>
  <jsp:include page="../components/component-style/class_info_card_style.jsp"/>
  <jsp:include page="../components/component-style/micro_subject_card_style.jsp"/>
</head>
<body>
<jsp:include page="../components/navbar.jsp"/>
<main class="container-90">
  <div class="dashboard-area">
    <div class="choosing-area">
      <sl-tab-group class="tabs">
        <sl-tab slot="nav" panel="overview"><spring:message code="dashboard.overview"/></sl-tab>
        <sl-tab slot="nav" panel="current-semester"><spring:message code="dashboard.currentSemester"/></sl-tab>
        <sl-tab slot="nav" panel="future-subjects"><spring:message code="dashboard.futureSubjects"/></sl-tab>
        <sl-tab slot="nav" panel="past-subjects"><spring:message code="dashboard.pastSubjects"/></sl-tab>

        <sl-tab-panel name="overview">
          <div class="container-90 overview-area">
            <sl-card class="general-info-card">
              <div class="row">
                <h3><spring:message code="dashboard.overview.completedCredits"/></h3>
                <sl-divider style="height: 1rem" vertical></sl-divider>
                <h5>${user.creditsDone}</h5>
              </div>
              <div class="row">
                <h3><spring:message code="dashboard.overview.totalCredits"/></h3>
                <sl-divider style="height: 1rem" vertical></sl-divider>
                <h5>${degree.totalCredits}</h5>
              </div>
            </sl-card>
            <div class="stat-row">
              <sl-card class="progress-card">
                <div slot="header">
                  <h3><spring:message code="dashboard.overview.overallProgress"/></h3>
                </div>

                <div class="column-center">
                  <sl-progress-ring value="${userProgressPercentage}"></sl-progress-ring>
                  <h4>${userProgressPercentage} %</h4>
                </div>
              </sl-card>
              <sl-card class="progress-card">
                <div slot="header">
                  <h3><spring:message code="dashboard.overview.completedCreditsByYear"/></h3>
                </div>
                <div class="chart-container">
                  <canvas id="progress-by-year"></canvas>
                </div>
              </sl-card>
            </div>
          </div>
        </sl-tab-panel>


        <sl-tab-panel name="current-semester">
          <div class="current-semester-area">
            <c:if test="${user.userSemester.size() != 0}">
              <div class="time-table-area">
                <jsp:include page="../components/time_table.jsp"/>
              </div>
              <div id="chosen-tab" class="current-semester-class-area">
                <sl-card class="current-semester-card">
                  <div slot="header">
                    <div class="row-space-between">
                      <h4 style="margin: 0.5rem"><spring:message code="dashboard.currentSemester.thisSemester"/></h4>
                    </div>
                  </div>
                  <div class="current-semester-subject-info-list">
                    <c:forEach var="subjectClass" items="${user.userSemester}">
                      <c:set var="name" value="${subjectClass.subject.name}" scope="request"/>
                      <c:set var="subClass" value="${subjectClass}" scope="request"/>
                      <a href='<c:out value="${pageContext.request.contextPath}/subject/${subjectClass.subject.id}"/>'>
                        <jsp:include page="../components/class_info_card.jsp"/>
                      </a>
                    </c:forEach>
                  </div>
                </sl-card>
              </div>
            </c:if>
            <c:if test="${user.userSemester.size() == 0}">
              <div class="empty-tab-area">
                <h3 class="empty-tab-info">
                  <spring:message code="dashboard.currentSemester.emptySemester"/>
                  <a class="builder-button" href="<c:out value="${pageContext.request.contextPath}/builder"/>">
                    <spring:message code="dashboard.currentSemester.emptySemesterLink"/>
                  </a>
                </h3>
              </div>
            </c:if>
          </div>
            <c:if test="${user.userSemester.size() != 0}">
              <div class="semester-edit-area">
                <sl-button href="${pageContext.request.contextPath}/builder/finish" variant="success" size="large" class="semester-edit-button">
                  <spring:message code="dashboard.currentSemester.finish"/>
                  <sl-icon slot="suffix" class="finish-button-icon" name="check"></sl-icon>
                </sl-button>
                <sl-button href="${pageContext.request.contextPath}/builder" size="large" class="semester-edit-button">
                  <spring:message code="dashboard.currentSemester.edit"/>
                  <sl-icon slot="suffix" class="finish-button-icon" style="font-size: 1.2rem" name="pencil"></sl-icon>
                </sl-button>
              </div>
            </c:if>
        </sl-tab-panel>


        <sl-tab-panel name="future-subjects">
          <div class="future-subjects-area container-90">
            <c:forEach var="subject" items="${futureSubjects}">
              <c:set var="subject" value="${subject}" scope="request"/>
              <c:import url="../components/subject_card.jsp"/>
            </c:forEach>
          </div>
        </sl-tab-panel>
        <sl-tab-panel name="past-subjects">
          <c:if test="${pastSubjects.size() != 0}">
            <div class="past-subjects-area container-90">
              <c:forEach var="subject" items="${pastSubjects}">
                <c:set var="subject" value="${subject}" scope="request"/>
                <c:import url="../components/micro_subject_card.jsp"/>
              </c:forEach>
            </div>
          </c:if>
          <c:if test="${pastSubjects.size() == 0}">
            <div style="padding-top: 15rem" class="empty-tab-area">
              <h3 class="empty-tab-info">
                <spring:message code="dashboard.pastSubjects.emptySubjects"/>
              </h3>
            </div>
          </c:if>
        </sl-tab-panel>
      </sl-tab-group>
    </div>
  </div>
</main>
<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>
<script src="${pageContext.request.contextPath}/js/schedule.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.3.0/chart.min.js"
        integrity="sha512-mlz/Fs1VtBou2TrUkGzX4VoGvybkD9nkeXWJm3rle0DPHssYYx4j+8kIS15T78ttGfmOjH0lLaBXGcShaVkdkg=="
        crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.3.0/chart.umd.js"
        integrity="sha512-CMF3tQtjOoOJoOKlsS7/2loJlkyctwzSoDK/S40iAB+MqWSaf50uObGQSk5Ny/gfRhRCjNLvoxuCvdnERU4WGg=="
        crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.3.0/helpers.js"
        integrity="sha512-BQJ3AP+pvkpSDEexjv6OYwGVCVIFo507d09S8pFPTp63+d7YZDrvjoB+4cSPTThQVfQjP6yybIZ6P29ZQGcPvQ=="
        crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
<script>
  window.onload = function(){
    const urlParams = new URLSearchParams(window.location.search);
    const tab = urlParams.get('tab');
    const tabGroup = document.querySelector('.tabs');
    if(tab === 'current-semester') {
      tabGroup.show('current-semester');
    }
  };
</script>
<script defer>



    const daysOfWeek = [
        '<spring:message code="subject.classDay1"/>', '<spring:message code="subject.classDay2"/>', '<spring:message code="subject.classDay3"/>',
        '<spring:message code="subject.classDay4"/>', '<spring:message code="subject.classDay5"/>', '<spring:message code="subject.classDay6"/>'
    ]

    <c:if test="${user.userSemester.size() != 0}">
      //  - - - - - - - Create time table - - - - - - -
      const schedule = new Schedule(29, 7);

      //  - - - - - - - Inflate time table - - - - - - -
      <c:forEach var="subClass" items="${user.userSemester}">
        schedule.addClass('${subClass.subject.id}', '${subClass.subject.name}',
            [
                <c:forEach var="classTime" items="${subClass.classTimes}">
                {
                    'day': '${classTime.day}',
                    'start': '${classTime.startTime}',
                    'end': '${classTime.endTime}',
                    'loc': '${classTime.classLoc}',
                    'building': '${classTime.building}',
                    'mode': '${classTime.mode}'
                },
                </c:forEach>
            ],
        )
        </c:forEach>
    </c:if>

    <c:if test="${user.userSemester.size() == 0}">
      $('document').ready(function () {
          const tabGroup = document.querySelector('.tabs');
          tabGroup.show('current-semester');
      });
    </c:if>


    // - - - - - - - Inflate chart - - - - - - -
    const chartElem = document.getElementById('progress-by-year');
    const chart = new Chart(chartElem,
        {
            type: 'bar', options: {responsive: true, maintainAspectRatio: false, animation: false, plugins: {legend: {display: false}, tooltip: {enabled: false}}},
            data: {
                labels: [
                    <c:forEach var="year" varStatus="status" items="${user.totalProgressPercentagePerYear.keySet()}">
                    '<spring:message code="home.year" arguments="${year}" htmlEscape="false" javaScriptEscape="true"/>',
                    </c:forEach>
                    '<spring:message code="home.electives"/>'
                ],
                datasets: [
                    {
                        barThickness: 50,
                        data: [
                            <c:forEach varStatus="status" var="year" items="${user.totalProgressPercentagePerYear.values()}">
                            ${year},
                            </c:forEach>
                            ${user.electiveProgressPercentage}
                        ]
                    }
                ]
            }
        }
    );

</script>
</body>

</html>
