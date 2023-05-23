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
      max-height: 94vh;
      margin: auto 0;
  }

  .no-padding {
      padding: 0;
  }

  .subject-list {
      display: flex;
      flex-direction: column;
      overflow: auto;
  }

  table {
      margin-bottom: 0;
  }

  .subject-card {
      width: 16rem;
      padding: 0.3rem;
  }

  .subject-card table {
      margin-bottom: 0.5rem;
  }

  .time-table {
      padding: 0.5rem;
      --padding: 0.1rem;
      flex: 1;
      display: flex;
  }

  .time-table::part(body), .time-table::part(base) {
      display: flex;
      flex-direction: row;
      align-items: stretch;
      flex: 1;
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

  .chooser {
      display: flex;
      flex-direction: row;
      align-items: center;
      justify-content: space-between;
  }

  .choose-class {
      display: none;
      padding: 0.5rem;
      flex: 1;
      min-height: 0;
  }

  .choose-class::part(body) {
      overflow-y: auto;
  }

  .builder-right-pane {
      padding-bottom: 0.5rem;
      width: 20.5rem;
  }

  .choose-subject {
      padding: 0.5rem;
      flex: 1;
      min-height: 0;
  }

  .choose-subject::part(base) {
      height: 100%;
  }

  .choose-subject::part(body) {
      height: 100%;
      overflow-y: auto;
  }

  .subject-class-info {
      display: none;
      flex-direction: column;
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

  .order-menu {
      display: flex;
      flex-direction: row;
      justify-content: space-around;
  }

  .class-selection {
      display: flex;
      flex-direction: row;
      align-items: center;
      justify-content: space-between;
      margin: 1rem;
      min-height: 0;
  }

  sl-dropdown {
      padding: 0;
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
<main class="no-padding container-80">
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

    <div class="column builder-right-pane">
      <sl-card id="choose-subject" class="choose-subject">
        <div slot="header">
          <div class="row ">
            <h4><spring:message code="builder.available"/></h4>
            <sl-dropdown style="padding-left: 1rem">
              <sl-button variant="text" slot="trigger" size="large" caret>
                <spring:message code="subject.sort"/>
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
            <sl-card id="subject-card-${subject.id}" class="subject-card">
              <div class="chooser">
                <div class="column">
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

      <sl-card id="choose-class" class="choose-class">
        <div slot="header">
          <div class="row" style="justify-content: space-between">
            <h4><spring:message code="builder.selectClass"/></h4>
            <sl-button style="padding-top: 0.64rem; padding-bottom: 0.64rem" id="exit-class-selector" variant="default"
                       size="small" circle>
              <sl-icon name="x-lg" label="Exit" class="icon"></sl-icon>
            </sl-button>
          </div>
        </div>
        <div id="class-list" class="subject-list">

          <c:forEach var="subject" items="${availableSubjects}">
          <div class="subject-class-info column" id="classes-${subject.id}">

            <c:forEach var="subClass" items="${subject.subjectClasses.values()}">
            <sl-card id="class-card-${subClass.getIdSub()}-${subClass.getIdClass()}" class="subject-card">
              <div class="chooser" slot="header">
                <h5>${subClass.getIdClass()}</h5>
                <sl-button id="select-class-${subClass.getIdSub()}-${subClass.getIdClass()}" variant="default" size="small" circle>
                  <sl-icon class="icon" name="check2" label="Select Subject"></sl-icon>
                </sl-button>
              </div>

              <div class="column">
                <c:forEach varStatus="status" var="classTime" items="${subClass.getClassTimes()}">
                  <table>
                    <tbody>
                    <c:choose>
                      <c:when test="${classTime.getDay() != 0}">
                        <tr><th><spring:message code="subject.classDay"/></th><td><spring:message code="subject.classDay${classTime.getDay()}"/></td></tr>
                      </c:when>
                      <c:otherwise><tr><th><spring:message code="subject.classDay"/></th><td>-</td></tr></c:otherwise>
                    </c:choose>
                    <tr><th><spring:message code="builder.time"/></th><td>${classTime.getStartTime()} - ${classTime.getEndTime()}</td></tr>
                    <tr><th><spring:message code="builder.class"/></th><td>${classTime.getClassLoc()}</td></tr>
                    <tr><th><spring:message code="builder.building"/></th><td>${classTime.getBuilding()}</td></tr>
                    <tr><th><spring:message code="builder.mode"/></th><td>${classTime.getMode()}</td></tr>
                    </tbody>
                  </table>
                </c:forEach>
              </div>

            </sl-card>
            </c:forEach>
          </div>
          </c:forEach>



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
            'id': '${sub.id}', 'name': '${sub.name}', 'department': '${sub.department}', 'credits': '${sub.credits}',
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
          'difficulty': '${availableSubjectsStatistics[sub.id].difficulty}'
        },
        </c:forEach>
    ]
    for (let subjectNum in subjectClasses) {
      document.getElementById('select-'+subjectClasses[subjectNum].id).addEventListener('click',
              createSubjectSelectAction(subjectClasses[subjectNum].id)
      )
      document.getElementById('deselect-subject-'+subjectClasses[subjectNum].id).addEventListener('click',
              createSubjectDeselectAction(subjectClasses[subjectNum].id)
      );
      for(let classNum in subjectClasses[subjectNum].classes){
        document.getElementById('select-class-'+subjectClasses[subjectNum].id + '-' + subjectClasses[subjectNum].classes[classNum].idClass).addEventListener('click',
                createClassSelectionAction(subjectClasses[subjectNum].id,subjectClasses[subjectNum].name,subjectClasses[subjectNum].classes[classNum]));
      }
    }

    // set exit class selection action
    document.getElementById('exit-class-selector').addEventListener('click', exitClassSelectionAction);

    // set order by action for credits
    document.getElementById('credit-orderby').addEventListener('click', orderByCreditAction)

    document.getElementById('download-button').addEventListener('click', function () {
        downloadTable('<spring:message code="builder.csvName"/>' + '.csv')
    })

</script>

</html>
