<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
  <title><spring:message code="builder.title"/></title>
  <jsp:include page="../components/head_shared.jsp"/>
</head>
<jsp:include page="../components/table_style.jsp"/>
<style>
  <%-- DO NOT REMOVE "UNUSED" CSS --%>
  body {
      height: 100vh;
  }
  .builder-area {
      display: flex;
      flex-direction: row;
      align-items: stretch;
      height: 94vh;
      margin: auto 0;
  }
  .no-padding {
      padding: 0;
  }
  .icon {
    padding-top: .5rem;
    font-size: 1rem;
  }

  h4 {
    margin: 0 !important;
    padding: 0 !important;
  }

  h5 {
    margin: 0 !important;
    padding: 0 !important;
  }

  /* Layout */
  .chooser {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
  }
  .chooser-tab {
    padding-bottom: 0.5rem;
    width: 33rem;
  }

  /* Subject information */
  .subject-chooser-tab-area {
    padding: 0.5rem 0.2rem;
    flex: 1;
    min-height: 0;
  }
  .subject-chooser-tab-area::part(base) {
    height: 100%;
  }
  .subject-chooser-tab-area::part(body) {
    height: 100%;
    overflow-y: auto;
  }
  .subject-list {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
  }
  .subject-info-card {
    width: 100%;
    padding: 0.3rem;
  }
  .subject-info-card::part(base) {
    height: 8rem;
  }
  .subject-info-card-details{
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    height: 6rem;
  }

  /* Class information */
  .class-chooser-tab-area {
    display: none;
    padding: 0.5rem 0.2rem;
    flex: 1;
    height: 100%;
  }
  .class-chooser-tab-area::part(body) {
    height: 100%;
    overflow-y: auto;
  }
  .class-chooser-tab-area::part(base) {
    height: 100%;
  }
  .class-list{
    display: flex;
    flex-direction: column;
    height: 100%;
    padding-left: 1rem;
    padding-right: 1rem;
  }
  .class-subject-list {
    display: none;
    flex-direction: column;
    overflow-y: auto;
    max-height: 100%;
  }
  .class-card{
    padding-top: 0.5rem;
  }
  .class-card::part(base){
    width: 100%;
  }
  .class-card::part(body){
    padding: 0.1rem;
  }

  /* Selected subjects */
  .selected-tab-area{
    padding: 0.5rem 0.2rem;
    width: 33%;
    height: 100%;
  }
  .selected-tab{
    width: 100%;
  }
  .selected-tab::part(base){
    height: 100%;
    overflow-y: auto;
  }
  .selected-tab::part(body){
     padding: 0.5rem;
   }

  /* Semester overview */
  .semester-overview-tab-area{
    padding: 0.5rem 0.2rem;
    width: 25%;
    height: 100%;
  }
  .semester-overview-tab{
    width: 100%;
  }
  .semester-overview-tab::part(base){
    width: 100% !important;
    height: 100%;
  }
  .semester-overview-tab::part(body), .overview-item::part(body){
    padding: 0.5rem;
  }
  .overview-item{
    padding-top: 0.5rem;
    padding-bottom: 0.5rem;
  }
   .overview-item::part(body){
     padding: 0.5rem;
     display: flex;
     align-items: center;
   }

  /* Time table */
  table {
      margin-bottom: 0;
  }
  .time-table {
      padding: 0.5rem;
      --padding: 0.1rem;
      flex: 1;
      display: none;
  }

  .time-table::part(body), .time-table::part(base) {
      display: flex;
      flex-direction: row;
      align-items: stretch;
      flex: 1;
  }
  .table-scroll {
    overflow: auto;
    flex: 1;
    width: 100%;
  }

  tbody td {
    font-size: 0.9rem;
    padding: 0.25rem !important;
    border-left: 1px solid #e9ecef;
  }

  tbody th {
    background-color: #f8f9fa;
    margin-right: 0;
    padding-right: 0;
  }

  thead th {
    background-color: #f8f9fa;
    position: sticky;
    top: 0
  }

  /* Ordering */
  sl-dropdown {
    padding: 0;
    height: 1.5rem;
  }



  .order-menu {
      display: flex;
      flex-direction: row;
      justify-content: space-around;
  }


  /* Unused */
  .class-selection {
      display: flex;
      flex-direction: row;
      align-items: center;
      justify-content: space-between;
      margin: 1rem;
      min-height: 0;
  }
  .button-section {
      display: flex;
      align-items: center;
      justify-content: start;
  }
</style>

<body>
</body>
<jsp:include page="../components/navbar.jsp"/>
<main class="no-padding container-90">
  <div class="builder-area">

    <sl-card class="time-table">
      <div class="table-scroll">
        <table>
          <thead>
          <tr>
            <th></th>
            <th><spring:message code="subject.classDay1"/></th>
            <th><spring:message code="subject.classDay2"/></th>
            <th><spring:message code="subject.classDay3"/></th>
            <th><spring:message code="subject.classDay4"/></th>
            <th><spring:message code="subject.classDay5"/></th>
            <th><spring:message code="subject.classDay6"/></th>
          </tr>
          </thead>
          <tbody id="weekly-schedule">
          </tbody>
        </table>
      </div>
    </sl-card>

    <div class="column chooser-tab">
      <sl-card id="choose-subject" class="subject-chooser-tab-area">
        <div slot="header">
          <div class="row-space-between ">
            <h4><spring:message code="builder.available"/></h4>
            <sl-dropdown>
              <sl-button class="dropdown-button" variant="text" slot="trigger" size="medium" caret>
                <span class="dropdown-text"><spring:message code="subject.sort"/></span>
                <sl-icon slot="prefix" name="sort-down"></sl-icon>
              </sl-button>

              <sl-menu>
                <sl-menu-item id="credit-orderby">
                  <div class="order-menu">
                    <spring:message code="search.credits"/>
                    <sl-icon id="credits-down" class="icon" slot="suffix" name="arrow-down"></sl-icon>
                    <sl-icon style="display: none" id="credits-up" class="icon" slot="suffix" name="arrow-up"></sl-icon>
                  </div>
                </sl-menu-item>
              </sl-menu>
            </sl-dropdown>
          </div>
        </div>
        <div id="subject-list" class="subject-list">
          <c:forEach var="subject" items="${availableSubjects}">
            <sl-card id="subject-card-${subject.id}" class="subject-info-card">
              <div class="chooser">
                <div class="subject-info-card-details">
                  <h5><c:out value="${subject.name}"/></h5>
                  <spring:message code="subject.credits"/> <c:out value="${subject.credits}"/>
                  <c:choose>
                    <c:when test="${availableSubjectsStatistics[subject.id].difficulty == 0}">
                      <sl-badge size="medium" variant="success" pill><spring:message code="form.easy"/></sl-badge>
                    </c:when>
                    <c:when test="${availableSubjectsStatistics[subject.id].difficulty == 1}">
                      <sl-badge size="medium" variant="primary" pill><spring:message code="form.normal"/></sl-badge>
                    </c:when>
                    <c:when test="${availableSubjectsStatistics[subject.id].difficulty == 2}">
                      <sl-badge size="medium" variant="danger" pill><spring:message code="form.hard"/></sl-badge>
                    </c:when>
                    <c:otherwise><sl-badge size="medium" variant="neutral" pill><spring:message code="form.noDif"/></sl-badge></c:otherwise>
                  </c:choose>
                </div>
                <div class="column">
                  <sl-button id="select-${subject.id}" variant="default" size="small" circle>
                    <sl-icon class="icon" name="check2" label="Select Subject"></sl-icon>
                  </sl-button>
                  <sl-button id="deselect-subject-${subject.id}" style="display: none; align-self: end" variant="default" size="small" circle>
                    <sl-icon class="icon" name="x-lg" label="Remove subject"></sl-icon>
                  </sl-button>
                  <span id="selected-${subject.id}" style="display: none; color: #7db6f8; padding-top:0.5rem">Selected</span>
                </div>
              </div>
            </sl-card>
          </c:forEach>
        </div>
      </sl-card>
      <sl-card id="choose-class" class="class-chooser-tab-area">
        <div slot="header">
          <div class="row" style="justify-content: space-between">
            <h4><spring:message code="builder.selectClass"/></h4>
            <sl-button style="padding-top: 0.64rem; padding-bottom: 0.64rem" id="exit-class-selector" variant="default"
                       size="small" circle>
              <sl-icon name="x-lg" label="Exit" class="icon"></sl-icon>
            </sl-button>
          </div>
        </div>
        <div id="class-list" class="class-list">

          <c:forEach var="subject" items="${availableSubjects}">
          <div class="class-subject-list" id="classes-${subject.id}">

            <c:forEach var="subClass" items="${subject.subjectClasses.values()}">
            <sl-card id="class-card-${subClass.getIdSub()}-${subClass.getIdClass()}" class="class-card">
              <div class="chooser" slot="header">
                <h5>${subClass.getIdClass()}</h5>
                <sl-button id="select-class-${subClass.getIdSub()}-${subClass.getIdClass()}" variant="default" size="small" circle>
                  <sl-icon class="icon" name="check2" label="Select Subject"></sl-icon>
                </sl-button>
                <sl-button style="display: none;" id="deselect-class-${subClass.getIdSub()}-${subClass.getIdClass()}"
                           variant="default" size="small" circle>
                  <sl-icon class="icon" name="x-lg" label="Select Subject"></sl-icon>
                </sl-button>
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
            </c:forEach>
          </div>
          </c:forEach>
        </div>
      </sl-card>
    </div>

    <div class="selected-tab-area">
      <sl-card class="selected-tab">
        <div slot="header">
          <div class="row-space-between ">
            <h4>Selected classes</h4>
          </div>
        </div>
        <div id="selected-subject-info-list">
        </div>
      </sl-card>
    </div>
    <div class="semester-overview-tab-area">
      <sl-card class="semester-overview-tab">
        <div slot="header">
          <div class="row-space-between">
            <h4>Your semester overview</h4>
          </div>
        </div>
        <div class="column">
          <sl-card class="overview-item">
            <span>Number of credits</span>
            <sl-divider vertical style="height: 1rem"></sl-divider>
            <span id="number-of-credits">0</span>
          </sl-card>
          <sl-card  class="overview-item">
            <span>Time Demand</span>
            <sl-divider vertical style="height:  1rem; margin: 0.5rem"></sl-divider>
            <sl-badge id="time-difficulty-none"  size="medium" variant="neutral" pill>No reviews</sl-badge>
            <sl-badge id="time-difficulty-easy" style="display: none;" size="medium" variant="success" pill><spring:message code="form.NotTimeDemanding" /></sl-badge>
            <sl-badge id="time-difficulty-medium" style="display: none;" size="medium" variant="primary" pill><spring:message code="form.averageTimeDemand" /></sl-badge>
            <sl-badge id="time-difficulty-hard" style="display: none;" size="medium" variant="warning" pill><spring:message code="form.timeDemanding" /></sl-badge>
          </sl-card>
          <sl-card  class="overview-item">
            <span>Overall Difficulty</span>
            <sl-divider vertical style="height:  1rem; margin: 0.5rem"></sl-divider>
            <sl-badge id="overall-difficulty-none"  size="medium" variant="neutral" pill>No reviews</sl-badge>
            <sl-badge id="overall-difficulty-easy" style="display: none;" size="medium" variant="success" pill><spring:message code="form.easy"/></sl-badge>
            <sl-badge id="overall-difficulty-medium" style="display: none;" size="medium" variant="primary" pill><spring:message code="form.normal"/></sl-badge>
            <sl-badge id="overall-difficulty-hard" style="display: none;" size="medium" variant="danger" pill><spring:message code="form.hard"/></sl-badge>
          </sl-card>

        </div>
      </sl-card>
    </div>

  </div>
</main>
<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>

<script src="${pageContext.request.contextPath}/js/schedule.js"></script>
<script src="${pageContext.request.contextPath}/js/builder-view.js"></script>
<script>
    const schedule = new Schedule(29, 7);

    const selectedColor = '#aad1ff'
    const incompatibleColor = '#d2d2d2'
    const normalColor = '#000000'
    const normalBorderColor = '#e0e0e0'

    const overviewStats = {'totalCredits': 0, 'overallDifficulty': 0, 'timeDemand': 0}


    const daysOfWeek = [
        '<spring:message code="subject.classDay1"/>', '<spring:message code="subject.classDay2"/>', '<spring:message code="subject.classDay3"/>',
        '<spring:message code="subject.classDay4"/>', '<spring:message code="subject.classDay5"/>', '<spring:message code="subject.classDay6"/>'
    ]
    const creditsText = '<spring:message code="subject.credits"/>'
    const selectedText = '<spring:message code="builder.selected"/>'
    const classTimeTableNames = {
        'day': '<spring:message code="subject.classDay"/>', 'time': '<spring:message code="builder.time"/>',
        'class': '<spring:message code="builder.class"/>', 'building': '<spring:message code="builder.building"/>',
        'mode': '<spring:message code="builder.mode"/>'
    }
    let currentOrder = 'none'

    const subjectClasses = [
        <c:forEach var="sub" items="${availableSubjects}">
        {
            'id': '${sub.id}', 'name': '${sub.name}', 'department': '${sub.department}', 'credits': ${sub.credits},
            'classes': [
                <c:forEach var="subClass" items="${sub.subjectClasses.values()}">
                {
                    'idClass': '${subClass.getIdClass()}', 'classTimes': [
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
                    ]
                },
                </c:forEach>
            ],
          'difficulty': ${availableSubjectsStatistics[sub.id].difficulty},
          'timeDemand': ${availableSubjectsStatistics[sub.id].timeDifficulty}
        },
        </c:forEach>
    ]
    for (let subjectNum in subjectClasses) {
      document.getElementById('select-'+subjectClasses[subjectNum].id).addEventListener('click',
              createSubjectSelectAction(subjectClasses[subjectNum].id)
      )

      for(let classNum in subjectClasses[subjectNum].classes){
        document.getElementById('select-class-'+subjectClasses[subjectNum].id + '-' + subjectClasses[subjectNum].classes[classNum].idClass).addEventListener('click',
                createClassSelectionAction(
                        subjectClasses[subjectNum],
                        subjectClasses[subjectNum].classes[classNum]
                )
        );
      }
    }

    // set exit class selection action
    document.getElementById('exit-class-selector').addEventListener('click', exitClassSelectionAction);

    // set order by action for credits
    document.getElementById('credit-orderby').addEventListener('click', orderByCreditAction)

</script>

</html>
