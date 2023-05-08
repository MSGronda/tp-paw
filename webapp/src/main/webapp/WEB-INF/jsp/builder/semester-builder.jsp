<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <jsp:include page="../components/head_shared.jsp"/>
</head>
<jsp:include page="../components/table_style.jsp"/>
<style>
    .builder-height{
        max-height: 90%;
        height: 90%;
    }
    .builder-area{
        display: flex;
        flex-direction: row;
    }
    .subject-list{
        display: flex;
        flex-direction: column;
        overflow: scroll;
        max-height: 600px;
    }
    .subject-card{
        width: 19rem;
        padding: 0.3rem;
    }
    .time-table{
        min-width: 70%;
        padding: 0.5rem;
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
    .subject-card-choose{
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
        height: 650px;
        width: 100%;
    }
    tbody td {
        font-size: 0.9rem;
        padding-top: 0.25rem !important;
        padding-bottom: 0.25rem !important;
    }
    tbody th {
        font-size: 0.9rem;
        padding-top: 0.25rem !important;
        padding-bottom: 0.25rem !important;
    }

</style>

<body>
</body>
<jsp:include page="../components/navbar.jsp"/>
<main class="container-80 builder-area builder-height">
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


    <sl-card id="choose-subject" class="choose-subject">
        <div slot="header"><h4>Available Subjects</h4></div>
        <div id="subject-list" class="subject-list">

            <c:forEach var="subject" items="${availableSubjects}">
                <sl-card id="subject-card-${subject.id}" class="subject-card">
                    <div slot="header"><h5><c:out value="${subject.name}"/></h5> </div>
                    <div class="subject-card-choose">
                        <c:out value="Credits: ${subject.credits}"/>
                        <sl-button id="select-${subject.id}" variant="default" size="small" circle>
                            <sl-icon class="icon" name="check2" label="Select Subject"></sl-icon>
                        </sl-button>
                    </div>
                </sl-card>
            </c:forEach>
        </div>

    </sl-card>

    <sl-card id="choose-class" class="choose-class">
        <div id="class-list" class="subject-list">

            <c:forEach var="subject" items="${availableSubjects}">
                <div class="subject-class-info column" id="classes-${subject.id}">
                    <h4>${subject.name}</h4>

                    <c:forEach var="subClass" items="${subject.subjectClasses.values()}">
                        <sl-card id="class-card-${subClass.getIdSub()}-${subClass.getIdClass()}" class="subject-card">
                            <div class="subject-card-choose" slot="header">
                                <h5>${subClass.getIdClass()}</h5>
                                <sl-button id="select-class-${subClass.getIdSub()}-${subClass.getIdClass()}" variant="default" size="small" circle>
                                    <sl-icon class="icon" name="check2" label="Select Subject"></sl-icon>
                                </sl-button>
                            </div>

                            <div class="column">
                                <c:forEach varStatus="status" var="classTime" items="${subClass.getClassTimes()}">
                                    <span>Day: ${classTime.getDay()}</span>
                                    <span>Time: ${classTime.getStartTime()} - ${classTime.getEndTime()}</span>
                                    <span>Class: ${classTime.getClassLoc()}</span>
                                    <span>Building: ${classTime.getBuilding()}</span>
                                    <span>Mode: ${classTime.getMode()}</span>
                                    <c:if test="${status.last != true}">
                                        <sl-divider></sl-divider>
                                    </c:if>
                                </c:forEach>
                            </div>

                        </sl-card>
                    </c:forEach>

                </div>
            </c:forEach>
        </div>
    </sl-card>

</main>
<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>

<script src="${pageContext.request.contextPath}/js/schedule.js"></script>
<script>
    const schedule = new Schedule(29,7);

    const subjectClasses = [
        <c:forEach var="sub" items="${availableSubjects}">
        {
            'id': '${sub.id}','name': '${sub.name}', 'department':  '${sub.department}',
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

    function switchSelector(chooseClassVisibility, chooseSubjectVisibility, classNumber, classVisibility){
        document.getElementById('choose-class').style.display = chooseClassVisibility;
        document.getElementById('choose-subject').style.display = chooseSubjectVisibility;
        document.getElementById('classes-' + classNumber).style.display = classVisibility;
    }

    function disbleSubjectCard(subjectId){
        const card = document.getElementById('subject-card-'+subjectId);
        card.style.color = '#d2d2d2'
        const select = document.getElementById('select-'+subjectId);
        select.disabled = true;
    }
    function disableClassCard(subjectId, classId){
        const card = document.getElementById('class-card-'+subjectId+'-'+classId);
        card.style.color = '#d2d2d2'
        const select = document.getElementById('select-class-'+subjectId+'-'+classId);
        select.disabled = true;
    }

    function disableIncompatibleSubjects(){
        for(let subNum in subjectClasses){
            // already signed up for that class
            if(!schedule.canAddSubject(subjectClasses[subNum].id)){
                disbleSubjectCard(subjectClasses[subNum].id);
                continue;
            }

            let anyClassCompatible = false;
            for(let clNum in subjectClasses[subNum].classes){
                const classCompatibility = schedule.canAddClass(subjectClasses[subNum].classes[clNum].classTimes);

                if(classCompatibility === false){
                    // class isn't compatible with timetable
                    disableClassCard( subjectClasses[subNum].id,subjectClasses[subNum].classes[clNum].idClass)
                }
                anyClassCompatible = anyClassCompatible || classCompatibility;
            }

            // none of the classes are compatible with timetable => disable subject as well
            if(!anyClassCompatible){
                disbleSubjectCard(subjectClasses[subNum].id);
            }
        }
    }

    // set actions for select subject and select class buttons
    for(let subjectNum in subjectClasses){
        document.getElementById('select-' + subjectClasses[subjectNum].id).addEventListener('click',
            function() {
                // go to class selection
                switchSelector('flex','none',subjectClasses[subjectNum].id,'flex')
            }
        );
        for(let subjectClassNum in subjectClasses[subjectNum].classes){
            document.getElementById(
                'select-class-' + subjectClasses[subjectNum].id + '-' + subjectClasses[subjectNum].classes[subjectClassNum].idClass
            ).addEventListener('click',
                function() {
                    // go back to subject selection
                    switchSelector('none','flex',subjectClasses[subjectNum].id,'none')

                    // modify schedule table
                    schedule.addClass(subjectClasses[subjectNum].id, subjectClasses[subjectNum].classes[subjectClassNum].classTimes)

                    // disable all incompatible classes (already signed up to that subject or it doesn't fit in your schedule)
                    disableIncompatibleSubjects();
                }
            );
        }
    }

</script>

</html>
