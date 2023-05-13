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
          <%-- insert with js subject list into here --%>
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
          <%-- insert with js  clases list into here--%>
        </div>
      </sl-card>

      <sl-card style="padding-left: 0.5rem;padding-right: 0.5rem; align-items: center">
        <div class="button-section">
          <sl-button id="download-button">
            <spring:message code="builder.download"/>
            <sl-icon slot="suffix" name="download"></sl-icon>
          </sl-button>
          <sl-divider vertical style="height: 3rem"></sl-divider>
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
            ]
        },
        </c:forEach>
    ]

    subjectClasses.sort(sortByCreditsDesc)
    let currentOrder = 'creditsDesc'

    // create subject list and class list and set actions
    const subjectList = document.getElementById('subject-list')
    const classList = document.getElementById('class-list')
    for (let subjectNum in subjectClasses) {
        const card = createSubjectCard(subjectList, subjectClasses[subjectNum]);
        subjectList.appendChild(card)

        const subjectClassCards = createSubjectClassInfo(subjectClasses[subjectNum])
        classList.appendChild(subjectClassCards)
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
