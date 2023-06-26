<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
<head>
  <title><spring:message code="builder.title"/></title>
  <jsp:include page="../components/head_shared.jsp"/>
</head>
<jsp:include page="../components/component-style/table_style.jsp"/>
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
  .icon-lg{
    padding-top: .4rem;
    padding-right: 0.025rem;
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
    padding: 0;
  }
  .class-chooser-tab-area::part(base) {
    height: 100%;
    width: 100%;
  }
  .class-list{
    display: flex;
    flex-direction: column;
    height: 100%;
    width: 100%;
    padding-left: 0.3rem;
    padding-right: 0.3rem;
  }
  .class-subject-list {
    display: none;
    flex-direction: column;
    overflow-y: auto;
    max-height: 100%;
    width: 100%;
  }
  .class-card{
    padding-top: 0.5rem;
    width: 100%;
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
  }
  .selected-tab::part(body){
     padding: 0.5rem;
     overflow-y: auto;
   }
  form{
    margin-block-end: 0;
  }


  /* Semester overview */
  .semester-overview-tab-area{
    padding: 0.5rem 0.2rem;
    width: 30%;
    height: 100%;
  }
  .semester-overview-tab{
    width: 100%;
  }
  .semester-overview-tab::part(header){
    height: 3.3rem;
    display: flex;
    align-items: center;
  }
  .semester-overview-tab::part(base){
    width: 100% !important;
    height: 100%;
  }
  .semester-overview-tab::part(body){
    height: 100%;
    padding: 0.5rem;
  }
  .overview-item::part(body){
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
    height: 100%;
  }
  .unlock-card{
    height: 100%;
  }
  .unlock-card::part(base){
    height: 70%;
  }
  .unlock-card::part(body){
    height: 90%;
  }
  .unlockable-list{
    height: 100%;
    width: 100%;
    overflow-y: auto;
  }

  /* Time table */
  table {
      margin-bottom: 0;
  }
  .time-table {
      padding: 0.5rem;
      flex: 1;
      display: none;
  }
  .time-table::part(base){
    width: 100%;
  }
  .time-table::part(header){
    height: 3.3rem;
    /*display: flex;*/
    /*align-items: center;*/
    /*justify-content: center;*/
  }

  .time-table::part(body) {
      display: flex;
    --padding: 0.1rem;
      align-items: stretch;
      flex: 1;
      max-height: 93%;
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
    height: 2rem;
  }
  .order-menu {
      display: flex;
      flex-direction: row;
      justify-content: space-around;
  }
  .clickable {
    pointer-events: all !important;
  }
  sl-tooltip::part(body) {
    color: black;
    background-color: white;
    margin: 0;
    padding: 1rem;
  }
  sl-tooltip::part(base__arrow) {
    background-color: white;
  }
  h5:hover {
    cursor: pointer;
  }

  .close-button {
    display: flex;
    flex-direction: row-reverse;
    font-size: 1rem;
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
  .clickable{
    pointer-events: all !important;
  }

  sl-tooltip::part(body) {
    padding: 0.25rem 0.5rem;
    background-color: black;
    color: white;
  }

  .subject-info-card-details sl-tooltip::part(body) {
    padding: 0;
    background-color: transparent;
  }

  .row-space-between sl-tooltip::part(body) {
    padding: 0;
    background-color: transparent;
    color: black;
  }

  .subject-name-title{
    display: none;
  }
  .dialog-header-actions {
    --width: 45rem;
  }
  .done-fab-button {
    position: fixed;
    bottom: 1.5rem;
    right: 1.5rem;
    display:flex;
    align-items: baseline;
  }
  .done-check {
    font-size: 1.5rem;
  }
</style>

<body>
</body>
<jsp:include page="../components/navbar.jsp"/>
<c:forEach var="subject" items="${availableSubjects}">
  <sl-dialog label="<c:out value="${subject.name}"/> - <c:out value="${subject.id}"/>" id="dialogue-${subject.id.hashCode()}" class="dialog-header-actions">
    <table>
      <tbody>
      <tr>
        <td><h3><spring:message code="subject.department"/></h3></td>
        <td><p><c:out value="${subject.department}"/></p></td>
      </tr>
      <tr>
        <th><spring:message code="subject.prerequisites"/></th>
        <td>
          <c:if test="${empty subject.prerequisites}">
            <spring:message code="subject.prerequisites?"/>
          </c:if>
          <c:forEach var="prereq" items="${subject.prerequisites}" varStatus="status">
            <a href='<c:url value="/subject/${prereq.id}"/>'><c:out value="${prereq.name}"/></a>
            <c:if test="${not status.last}">
              ,
            </c:if>
          </c:forEach>
        </td>
      </tr>
      <tr>
        <th><spring:message code="subject.professors"/></th>
        <td>
          <c:forEach var="professor" items="${subject.professors}" varStatus="status">
            <sl-badge variant="primary">
              <c:out value="${professor.name}"/>
            </sl-badge>

          </c:forEach>
        </td>
      </tr>
      <tr>
        <th><spring:message code="subject.difficulty" /></th>
        <td>
          <c:choose>
            <c:when test="${subject.reviewStats.difficulty eq 'EASY'}">
              <sl-badge size="medium" variant="success"><spring:message code="form.easy" /></sl-badge>
            </c:when>
            <c:when test="${subject.reviewStats.difficulty eq 'MEDIUM'}">
              <sl-badge size="medium" variant="primary"><spring:message code="form.normal" /></sl-badge>
            </c:when>
            <c:when test="${subject.reviewStats.difficulty eq 'HARD'}">
              <sl-badge size="medium" variant="warning"><spring:message code="form.hard" /></sl-badge>
            </c:when>
            <c:otherwise>
              <sl-badge size="medium" variant="neutral"><spring:message code="form.noDif" /></sl-badge>
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
      <tr>
        <th><spring:message code="subject.time" /></th>
        <td>
          <c:choose>
            <c:when test="${subject.reviewStats.timeDemanding eq 'LOW'}">
              <sl-badge size="medium" variant="success"><spring:message code="form.NotTimeDemanding" /></sl-badge>
            </c:when>
            <c:when test="${subject.reviewStats.timeDemanding eq 'MEDIUM'}">
              <sl-badge size="medium" variant="primary"><spring:message code="form.averageTimeDemand" /></sl-badge>
            </c:when>
            <c:when test="${subject.reviewStats.timeDemanding eq 'HIGH'}">
              <sl-badge size="medium" variant="warning"><spring:message code="form.timeDemanding" /></sl-badge>
            </c:when>
            <c:otherwise>
              <sl-badge size="medium" variant="neutral"><spring:message code="form.noDif" /></sl-badge>
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
      </tbody>
    </table>
    <div class="column-center">
      <sl-button variant="primary" outline href="<c:url value="/subject/${subject.id}"/>" target="_blank" rel="noopener noreferrer"><spring:message code="builder.fullSubject"/></sl-button>
    </div>
  </sl-dialog>
</c:forEach>

<main class="no-padding container-95">
  <div class="builder-area">
    <div id="choosing-tab" class="column chooser-tab">
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
                <sl-menu-item id="name-orderby">
                  <div class="order-menu">
                    <spring:message code="search.name"/>
                    <sl-icon style="display: none" id="order-name-down" class="icon" slot="suffix" name="arrow-down"></sl-icon>
                    <sl-icon style="display: none" id="order-name-up" class="icon" slot="suffix" name="arrow-up"></sl-icon>
                  </div>
                </sl-menu-item>
                <sl-menu-item id="credit-orderby">
                  <div class="order-menu">
                    <spring:message code="search.credits"/>
                    <sl-icon style="display: none" id="order-credits-down" class="icon" slot="suffix" name="arrow-down"></sl-icon>
                    <sl-icon style="display: none" id="order-credits-up" class="icon" slot="suffix" name="arrow-up"></sl-icon>
                  </div>
                </sl-menu-item>
                <sl-menu-item id="difficulty-orderby">
                  <div class="order-menu">
                    <spring:message code="search.difficulty"/>
                    <sl-icon style="display: none" id="order-difficulty-down" class="icon" slot="suffix" name="arrow-down"></sl-icon>
                    <sl-icon style="display: none" id="order-difficulty-up" class="icon" slot="suffix" name="arrow-up"></sl-icon>
                  </div>
                </sl-menu-item>
                <sl-menu-item id="timedemand-orderby">
                  <div class="order-menu">
                    <spring:message code="search.timedemand"/>
                    <sl-icon style="display: none" id="order-timedemand-down" class="icon" slot="suffix" name="arrow-down"></sl-icon>
                    <sl-icon style="display: none" id="order-timedemand-up" class="icon" slot="suffix" name="arrow-up"></sl-icon>
                  </div>
                </sl-menu-item>
              </sl-menu>
            </sl-dropdown>
          </div>
        </div>
        <div id="subject-list" class="subject-list">
          <c:forEach var="subject" items="${availableSubjects}">
            <c:if test="${subject.credits != 0}">
            <sl-card id="subject-card-${subject.id.hashCode()}" class="subject-info-card">
              <div class="chooser">
                <div class="subject-info-card-details">
                  <h5 id="open-button-${subject.id.hashCode()}" class="open-button"><c:out value="${subject.name}"/></h5>
                  <spring:message code="subject.credits"/> <c:out value="${subject.credits}"/>
                  <c:choose>
                    <c:when test="${subject.reviewStats.difficulty eq 'EASY'}">
                      <sl-badge size="medium" variant="success" pill><spring:message code="form.easy"/></sl-badge>
                    </c:when>
                    <c:when test="${subject.reviewStats.difficulty eq 'MEDIUM'}">
                      <sl-badge size="medium" variant="primary" pill><spring:message code="form.normal"/></sl-badge>
                    </c:when>
                    <c:when test="${subject.reviewStats.difficulty eq 'HARD'}">
                      <sl-badge size="medium" variant="danger" pill><spring:message code="form.hard"/></sl-badge>
                    </c:when>
                    <c:otherwise><sl-badge size="medium" variant="neutral" pill><spring:message code="form.noDif"/></sl-badge></c:otherwise>
                  </c:choose>
                </div>
                <div class="column">
                  <sl-button id="select-${subject.id.hashCode()}" variant="default" size="small" circle>
                    <sl-icon class="icon" name="check2" label="Select Subject"></sl-icon>
                  </sl-button>
                  <sl-button id="deselect-subject-${subject.id.hashCode()}" style="display: none; align-self: end" variant="default" size="small" circle>
                    <sl-icon class="icon-lg" name="x-lg" label="Remove subject"></sl-icon>
                  </sl-button>
                  <span id="selected-${subject.id.hashCode()}" style="display: none; color: #7db6f8; padding-top:0.5rem"><spring:message code="builder.selected"/></span>
                </div>
              </div>
            </sl-card>
            </c:if>
          </c:forEach>
        </div>
      </sl-card>
      <sl-card id="choose-class" class="class-chooser-tab-area">
        <div slot="header">
          <div class="row-space-between">
            <div>
              <h4><spring:message code="builder.selectClass"/></h4>
              <c:forEach var="subject" items="${availableSubjects}">
                <h4 class="subject-name-title" id="subject-name-title-${subject.id.hashCode()}"><c:out value="${subject.name}"/></h4>
              </c:forEach>
            </div>
            <sl-button style="" id="exit-class-selector" variant="default"
                       size="small" circle>
              <sl-icon name="x-lg" label="Exit" class="icon-lg"></sl-icon>
            </sl-button>
          </div>
        </div>
        <div id="class-list" class="class-list">

          <c:forEach var="subject" items="${availableSubjects}">
          <div class="class-subject-list" id="classes-${subject.id.hashCode()}">
            <c:forEach var="subClass" items="${subject.classes}">
            <sl-card id="class-card-${subject.id.hashCode()}-${subClass.classId.hashCode()}" class="class-card">
              <div class="chooser" slot="header">
                <h5><c:out value="${subClass.classId}"/></h5>
                <form id="form-select-class-${subject.id.hashCode()}-${subClass.classId.hashCode()}" method="post" action="${pageContext.request.contextPath}/builder/add">
                  <input type="hidden" id="idSub-add-${subject.id.hashCode()}" name="idSub" value="<c:out value="${subject.id}"/>">
                  <input type="hidden" id="idClass-add-${subject.id.hashCode()}" name="idClass" value="<c:out value="${subClass.classId}"/>">

                  <sl-button type="submit" id="select-class-${subject.id.hashCode()}-${subClass.classId.hashCode()}" variant="default" size="small" circle>
                    <sl-icon class="icon" name="check2" label="Select Subject"></sl-icon>
                  </sl-button>
                </form>

                <form style="display: none" id="form-deselect-class-${subject.id.hashCode()}-${subClass.classId.hashCode()}" method="post" action="${pageContext.request.contextPath}/builder/remove">
                  <input type="hidden" id="idSub-remove-${subject.id.hashCode()}" name="idSub" value="<c:out value="${subject.id}"/>">
                  <input type="hidden" id="idClass-remove-${subject.id.hashCode()}" name="idClass" value="<c:out value="${subClass.classId}"/>">

                <sl-button  type="submit" id="deselect-class-${subject.id.hashCode()}-${subClass.classId.hashCode()}"
                           variant="default" size="small" circle>
                  <sl-icon class="icon-lg" name="x-lg" label="Select Subject"></sl-icon>
                </sl-button>
                </form>
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
                        <c:when test="${classTime.day >= 1 && classTime.day <= 7}">
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
            </c:forEach>

            <c:if test="${subject.classes.size() == 0}">
            <sl-card id="class-card-${subject.id.hashCode()}-0" class="class-card">
              <div class="chooser" slot="header">
                <h5><c:out value="${subject.name}"/></h5>
                <form id="form-select-class-${subject.id.hashCode()}-${0}" method="post" action="${pageContext.request.contextPath}/builder/add">
                    <input type="hidden" id="idSub-add-${subject.id.hashCode()}" name="idSub" value="<c:out value="${subject.id}"/>">
                    <input type="hidden" id="idClass-add-${subject.id.hashCode()}" name="idClass" value="${0}">
                  <sl-button id="select-class-${subject.id.hashCode()}-0" variant="default" size="small" type="submit" circle>
                    <sl-icon class="icon" name="check2" label="Select Subject"></sl-icon>
                  </sl-button>
                </form>

                <form style="display: none" id="form-deselect-class-${subject.id.hashCode()}-${0}" method="post" action="${pageContext.request.contextPath}/builder/remove">
                  <input type="hidden" id="idSub-remove-${subject.id.hashCode()}" name="idSub" value="<c:out value="${subject.id}"/>">
                  <input type="hidden" id="idClass-remove-${subject.id.hashCode()}" name="idClass" value="${0}">
                  <sl-button style="display: none;" id="deselect-class-${subject.id.hashCode()}-0"
                             variant="default" size="small" circle>
                    <sl-icon class="icon-lg" name="x-lg" label="Select Subject"></sl-icon>
                  </sl-button>
                </form>
              </div>
            </sl-card>
            </c:if>

          </div>
          </c:forEach>
        </div>
      </sl-card>
    </div>

    <sl-card id="time-table" class="time-table">
      <div slot="header">
        <div  class="row-space-between">
          <h4><spring:message code="builder.timeTable"/></h4>
          <hr>
          <sl-tooltip content="<spring:message code="builder.selectedListToolTip"/>">
            <sl-icon-button id="switch-to-list-button" name="view-list" label="Switch to list view"></sl-icon-button>
          </sl-tooltip>
        </div>
      </div>
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

    <div id="chosen-tab" class="selected-tab-area">
      <sl-card class="selected-tab">
        <div slot="header">
          <div class="row-space-between">
            <h4><spring:message code="builder.selectedList"/></h4>
            <sl-tooltip content="<spring:message code="builder.timeTableToolTip"/>">
              <sl-icon-button id="switch-to-table-button" name="table" label="Switch to timetable view"></sl-icon-button>
            </sl-tooltip>
          </div>
        </div>
        <div id="selected-subject-info-list">
        </div>
      </sl-card>
    </div>

    <div id="overview-tab" class="semester-overview-tab-area">
      <sl-card class="semester-overview-tab">
        <div slot="header">
          <div class="row-space-between">
            <h4><spring:message code="builder.semesterOverview.title"/></h4>
            <sl-tooltip trigger="click">
              <div class="clickable" slot="content">
                <sl-card>
                  <div class="column-center">
                    <h4><spring:message code="builder.help1"/></h4>
                    <p>
                      &#x2022; <spring:message code="builder.help2"/>
                      <br/>
                      &#x2022; <spring:message code="builder.help3"/>
                      <br/>
                      &#x2022; <spring:message code="builder.help4"/>
                    </p>
                  </div>
                </sl-card>
              </div>
              <sl-icon-button name="info-circle" label="info"></sl-icon-button>
            </sl-tooltip>
          </div>
        </div>
        <div style="height: 100%" class="column">
          <sl-card class="overview-item">
            <span><spring:message code="builder.semesterOverview.credits"/></span>
            <sl-divider vertical style="height: 1rem"></sl-divider>
            <span id="number-of-credits">0</span>
          </sl-card>
          <sl-tooltip content="<spring:message code="builder.timeDemanding"/>">
            <sl-card  class="overview-item">
              <span><spring:message code="builder.semesterOverview.timeDemand"/></span>
              <sl-divider vertical style="height:  1rem; margin: 0.5rem"></sl-divider>
              <sl-badge id="time-difficulty-none"  size="medium" variant="neutral" pill><spring:message code="builder.semesterOverview.noReviews"/></sl-badge>
              <sl-badge id="time-difficulty-easy" style="display: none;" size="medium" variant="success" pill><spring:message code="form.NotTimeDemanding" /></sl-badge>
              <sl-badge id="time-difficulty-medium" style="display: none;" size="medium" variant="primary" pill><spring:message code="form.averageTimeDemand" /></sl-badge>
              <sl-badge id="time-difficulty-hard" style="display: none;" size="medium" variant="warning" pill><spring:message code="form.timeDemanding" /></sl-badge>
            </sl-card>
          </sl-tooltip>
          <sl-tooltip  content="<spring:message code="builder.difficulty"/>">
            <sl-card  class="overview-item">
              <span><spring:message code="builder.semesterOverview.overallDifficulty"/></span>
              <sl-divider vertical style="height:  1rem; margin: 0.5rem"></sl-divider>
              <sl-badge id="overall-difficulty-none"  size="medium" variant="neutral" pill><spring:message code="builder.semesterOverview.noReviews"/></sl-badge>
              <sl-badge id="overall-difficulty-easy" style="display: none;" size="medium" variant="success" pill><spring:message code="form.easy"/></sl-badge>
              <sl-badge id="overall-difficulty-medium" style="display: none;" size="medium" variant="primary" pill><spring:message code="form.normal"/></sl-badge>
              <sl-badge id="overall-difficulty-hard" style="display: none;" size="medium" variant="danger" pill><spring:message code="form.hard"/></sl-badge>
            </sl-card>
          </sl-tooltip>
          <sl-card class="overview-item unlock-card">
            <div slot="header">
              <span><spring:message code="builder.semesterOverview.unlock"/></span>
            </div>
            <div class="unlockable-list">
              <c:forEach var="subject" items="${unlockableSubjects}">
                <sl-button style="display: none" id="unlock-${subject.id.hashCode()}" variant="text" href="${pageContext.request.contextPath}/subject/<c:out value="${subject.id}"/>">

                  <c:out value="${subject.name}"/>
                </sl-button>
              </c:forEach>
            </div>

          </sl-card>

        </div>
      </sl-card>
    </div>

  </div>
  <sl-button href="<c:url value="/?tab=current-semester"/>" variant="success" size="large" class="done-fab-button">
    <spring:message code="builder.done"/>
    <sl-icon slot="suffix" class="done-check" name="check-lg"></sl-icon>
  </sl-button>
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
      <c:if test="${sub.credits != 0}">
      {
        'id': '${sub.id.hashCode()}', 'name': '<c:out value="${sub.name}"/>', 'department': '<c:out value="${sub.department}"/>', 'credits': <c:out value="${sub.credits}"/>,
        'classes': [
          <c:forEach var="subClass" items="${sub.classes}">
          {
            'idClass': '${subClass.classId.hashCode()}',
            'stringIdClass' : '<c:out value="${subClass.classId}" />',
            'classTimes': [
              <c:forEach var="classTime" items="${subClass.classTimes}">
              {

                'day': '<c:out value="${classTime.day}"/>',
                'start': '<c:out value="${classTime.startTime}"/>',
                'end': '<c:out value="${classTime.endTime}"/>',
                'loc': '<c:out value="${classTime.classLoc}"/>',
                'building': '<c:out value="${classTime.building}"/>',
                'mode': '<c:out value="${classTime.mode}"/>'
              },
              </c:forEach>
            ]
          },
          </c:forEach>
        ],
        'difficulty': <c:out value="${sub.reviewStats.difficulty.value}"/>,
        'timeDemand': <c:out value="${sub.reviewStats.timeDemanding.intValue}"/>
      },
      </c:if>
      </c:forEach>
    ]

    <c:if test="${user.userSemester.size() != 0}">
      <c:forEach var="subClass" varStatus="status" items="${user.userSemester}">
          const sub_<c:out value="${status.index}"/> = subjectClasses.find(elem => elem.id === '${subClass.subject.id.hashCode()}')
          const sub_<c:out value="${status.index}"/>_class = sub_<c:out value="${status.index}"/>.classes.find(elem => elem.idClass === '${subClass.classId.hashCode()}');

          addSelectedClassToList(sub_<c:out value="${status.index}"/>, sub_<c:out value="${status.index}"/>_class);
          schedule.addClass(sub_<c:out value="${status.index}"/>.id, sub_<c:out value="${status.index}"/>.name, sub_<c:out value="${status.index}"/>_class.classTimes);
          document.getElementById('subject-card-'+sub_<c:out value="${status.index}"/>.id).style.display = 'none';
          updateCreditCounter(sub_<c:out value="${status.index}"/>.credits)
          updateTimeDemand((sub_<c:out value="${status.index}"/>.timeDemand+1))
          updateOverallDifficulty((sub_<c:out value="${status.index}"/>.difficulty+1))
          disableIncompatibleSubjects(sub_<c:out value="${status.index}"/>);
      </c:forEach>
    </c:if>

    const unlockables = [
      <c:forEach var="subject" items="${unlockableSubjects}">
        {
          'id': '${subject.id.hashCode()}', 'prereqs':[
            <c:forEach var="prereq" items="${subject.prerequisites}">
                  '${prereq.id.hashCode()}',
            </c:forEach>
          ]
        },
      </c:forEach>
    ]

    const doneSubjects = [
      <c:forEach var="subject" items="${doneSubjects}">
        '${subject.id.hashCode()}',
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
      if(subjectClasses[subjectNum].classes.length === 0){
        document.getElementById('select-class-'+subjectClasses[subjectNum].id + '-' + 0).addEventListener('click',
                createClassSelectionAction(
                        subjectClasses[subjectNum],
                        {'idClass': 0, 'classTimes': []}
                )
        );
      }
    }

    // set exit class selection action
    document.getElementById('exit-class-selector').addEventListener('click', exitClassSelectionAction);

    // set order by action for all sorters
    document.getElementById('name-orderby').addEventListener('click', orderByNameAction)
    document.getElementById('credit-orderby').addEventListener('click', orderByCreditAction)
    document.getElementById('difficulty-orderby').addEventListener('click', orderByDifficultyAction)
    document.getElementById('timedemand-orderby').addEventListener('click', orderByTimeDemandAction)


    // set switch to table view
    document.getElementById('switch-to-table-button').addEventListener('click',switchToTableView);

    // set switch to table view
    document.getElementById('switch-to-list-button').addEventListener('click',switchToListView);

    const tooltip = document.querySelectorAll('.manual-tooltip');
    const toggle = tooltip.lastElementChild;
    const close = tooltip.firstElementChild;



    const buttons = document.querySelectorAll('.open-button');
    buttons.forEach(button => {
        button.addEventListener('click', () => {
            const id = button.id.split('-')[2];
            const dialog = document.getElementById('dialogue-'+id);
            dialog.show();
        });
    });

    const dialog = document.querySelector('.dialog-header-actions');
    const openButton = document.querySelector('open-button-');
    const closeButton = dialog.querySelector('sl-button[slot="footer"]');


    alterUnlockables();
</script>

</html>
