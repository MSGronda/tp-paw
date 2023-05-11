<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <title>Title</title>
    <jsp:include page="../components/head_shared.jsp"/>
</head>
<jsp:include page="../components/table_style.jsp"/>
<style>

    <%-- DO NOT REMOVE "UNUSED" CSS --%>
    .builder-area{
        display: flex;
        flex-direction: row;
    }
    .subject-list{
        display: flex;
        flex-direction: column;
        overflow: auto;
        max-height: 31rem;
        min-height: 31rem;
    }

    .subject-card{
        width: 16rem;
        padding: 0.3rem;
    }
    .time-table{
        min-width: 75%;
        padding: 0.5rem;
        --padding: 0.1rem;
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
    .chooser{
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: space-between;
    }
    .choose-class{
        display: none;
        padding: 0.5rem;
    }
    .choose-subject{
        padding: 0.5rem;
    }
    .subject-class-info{
        display: none;
        flex-direction: column;
    }
    .table-scroll{
        overflow: auto;
        height: 38rem;
        width: 100%;
    }
    tbody td {
        font-size: 0.9rem;
        padding: 0.25rem !important;
        border-left: 1px solid #e9ecef;
    }
    thead th{
        background-color: #f8f9fa;
        position: sticky;
        top: 0
    }
    .order-menu{
        display: flex;
        flex-direction: row;
        justify-content: space-around;
    }
    .class-selection{
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: space-between;
        margin: 1rem;
    }
    sl-dropdown{
        padding: 0;
    }
</style>

<body>
</body>
<jsp:include page="../components/navbar.jsp"/>
<main class="container-80 builder-area">
    <sl-card class="time-table">
        <div class="table-scroll">
            <table>
                <thead>
                <tr>
                    <th></th>
                    <th>Monday</th>
                    <th>Tuesday</th>
                    <th>Wednesday</th>
                    <th>Thursday</th>
                    <th>Friday</th>
                    <th>Saturday</th>
                </tr>
                </thead>
                <tbody id="weekly-schedule">
                </tbody>
            </table>
        </div>
    </sl-card>

    <div class="column">
        <sl-card id="choose-subject" class="choose-subject">
            <div slot="header">
                <div class="row ">
                    <h4>Available Subjects</h4>
                    <sl-dropdown style="padding-left: 1rem">
                        <sl-button variant="text" slot="trigger" size="large" caret>
                            <spring:message code="subject.sort"/>
                            <sl-icon slot="prefix" name="sort-down"></sl-icon>
                        </sl-button>
                        <sl-menu>
                            <sl-menu-item id="credit-orderby">
                                <div class="order-menu">
                                    Credits
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
                    <h4>Select a class</h4>
                    <sl-button style="padding-top: 0.64rem; padding-bottom: 0.64rem" id="exit-class-selector" variant="default" size="small" circle>
                        <sl-icon name="x-lg" label="Exit" class="icon"></sl-icon>
                    </sl-button>
                </div>
            </div>
            <div id="class-list" class="subject-list">
                <%-- insert with js  clases list into here--%>
            </div>
        </sl-card>
    </div>

</main>
<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>

<script src="${pageContext.request.contextPath}/js/schedule.js"></script>
<script src="${pageContext.request.contextPath}/js/builder-view.js"></script>
<script>
    const schedule = new Schedule(29,7);

    const selectedColor = '#aad1ff'
    const incompatibleColor = '#d2d2d2'
    const normalColor = '#000000'
    const normalBorderColor = '#e0e0e0'

    const daysOfWeek = [
        '<spring:message code="subject.classDay1"/>','<spring:message code="subject.classDay2"/>','<spring:message code="subject.classDay3"/>',
        '<spring:message code="subject.classDay4"/>','<spring:message code="subject.classDay5"/>','<spring:message code="subject.classDay6"/>'
    ]

    const subjectClasses = [
        <c:forEach var="sub" items="${availableSubjects}">
        {
            'id': '${sub.id}','name': '${sub.name}', 'department':  '${sub.department}', 'credits': '${sub.credits}',
            'classes': [
                <c:forEach var="subClass" items="${sub.subjectClasses.values()}">
                {
                    'idClass': '${subClass.getIdClass()}', 'classTimes': [
                        <c:forEach var="classTime" items="${subClass.getClassTimes()}">
                        {

                            'day': '${classTime.getDay()}', 'start': '${classTime.getStartTime()}', 'end': '${classTime.getEndTime()}',
                            'loc': '${classTime.getClassLoc()}','building': '${classTime.getBuilding()}', 'mode': '${classTime.getMode()}'
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
    for(let subjectNum in subjectClasses){
        const card = createSubjectCard(subjectList, subjectClasses[subjectNum]);
        subjectList.appendChild(card)

        const subjectClassCards =  createSubjectClassInfo(subjectClasses[subjectNum])
        classList.appendChild(subjectClassCards)
    }

    // set exit class selection action
    document.getElementById('exit-class-selector').addEventListener('click', exitClassSelectionAction);

    // set order by action for credits
    document.getElementById('credit-orderby').addEventListener('click', orderByCreditAction)

</script>

</html>
