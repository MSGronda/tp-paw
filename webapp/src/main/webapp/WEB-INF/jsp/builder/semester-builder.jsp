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
        min-width: 60%;
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
    }
    .subject-class-info{
        display: none;
        flex-direction: column;
    }
    .table-scroll{
        overflow: auto;
        height: 650px;
        width: 650px;
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
        <div class="subject-list">
            <c:forEach var="subject" items="${availableSubjects}">
                <sl-card id="card-${subject.id}" class="subject-card">
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

        <div class="subject-list">
            <c:forEach var="subject" items="${availableSubjects}">
                <div class="subject-class-info column" id="classes-${subject.id}">
                    <h4>${subject.name}</h4>
                    <c:forEach var="subClass" items="${subject.subjectClasses.values()}">
                        <sl-card class="subject-card">
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

<script>

    const scheduleHTML = document.getElementById('weekly-schedule')
    const ROWS = 24
    const COLS = 7
    const scheduleArray = Array(ROWS).fill(Array(COLS))

    for(let i=0; i<ROWS; i++ ){
        let tr = document.createElement("tr");

        let td = document.createElement('th');
        let hours = (8 + Math.floor(i / 2)) + ""
        let minutes = i % 2 === 0 ? "00" : "30";
        td.textContent = hours + ":" + minutes;
        tr.appendChild(td);

        for(let j=0; j<COLS-1; j++ ){
            let td = document.createElement('td');
            td.id='r'+i+'c'+j;
            scheduleArray[i][j] = "";
            td.textContent = "";
            tr.appendChild(td);
        }
        scheduleHTML.appendChild(tr);
    }

    const chosenClasses = []

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

    for(let subjectNum in subjectClasses){
        document.getElementById('select-' + subjectClasses[subjectNum].id).addEventListener('click',
            function() {
                document.getElementById('choose-class').style.display = 'block';
                document.getElementById('choose-subject').style.display = 'none';
                document.getElementById('classes-' + subjectClasses[subjectNum].id).style.display = 'flex';
            }
        );
        for(let subjectClassNum in subjectClasses[subjectNum].classes){
            document.getElementById(
                'select-class-' + subjectClasses[subjectNum].id + '-' + subjectClasses[subjectNum].classes[subjectClassNum].idClass
            ).addEventListener('click',
                function() {
                    document.getElementById('choose-class').style.display = 'none';
                    document.getElementById('choose-subject').style.display = 'flex';
                    document.getElementById('classes-' + subjectClasses[subjectNum].id).style.display = 'none';
                    chosenClasses.push(subjectClasses[subjectNum].classes[subjectClassNum].classTimes)

                    for(let classNum in subjectClasses[subjectNum].classes[subjectClassNum].classTimes){
                        const day = subjectClasses[subjectNum].classes[subjectClassNum].classTimes[classNum].day
                        const start = subjectClasses[subjectNum].classes[subjectClassNum].classTimes[classNum].start
                        const end = subjectClasses[subjectNum].classes[subjectClassNum].classTimes[classNum].end

                        const rowStart = parseInt(start.split(":")[0]) + (parseInt(start.split(":")[1]) === 30 ? 1 : 0)
                        const rowEnd = parseInt(end.split(":")[0]) + (parseInt(end.split(":")[1]) === 30 ? 1 : 0)

                        for(let i=rowStart; i<rowEnd; i++){
                            document.getElementById('r'+i+'c'+day).style.backgroundColor = '#8d2f2f';
                        }
                    }

                }
            );
        }
    }


</script>

</html>
