<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title><spring:message code="subject.edit" /></title>
    <jsp:include page="../components/head_shared.jsp"/>

    <style>
        .add-button {
            display: flex;
            justify-content: center;
            align-items: center;
            margin-top: 2rem;
        }
        .table {
            display: flex;
            justify-content: center;
            align-items: center;
        }
        td {
            font-size: 1.2rem;
            padding-left: 1rem;
            padding-right: 1rem;
        }
        table {
            width: 100%;
        }
        .selection {
            width: 80%;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        li {
            list-style-type: none;
            font-size: 0.9rem;
        }
        .list-rm {
            display: flex;
            flex-direction: row;
            align-items: center;
            border: 0;
            padding: 0;
            margin: 0;
        }
        .error-message {
            color: red;
        }
        .professors-cell-width {
            max-width:20rem;
            overflow: hidden;
            white-space: normal;
            word-wrap: break-word;
            max-height:5rem;
        }
        .optional{
            color: grey;
            margin: 0;
        }
        .correlatives-div{
            display: flex;
            flex-direction: column;
        }
        .bottom-buttons{
            margin-left: 0.5rem;
            margin-right: 0.5rem;
        }
        p{
            margin: 0;
            font-size: medium;
        }
    </style>
</head>
<body>
<jsp:include page="../components/navbar.jsp" />

<sl-dialog label="<spring:message code="subject.create.professor"/>" class="dialog-header-actions" style="--width: 40rem">
    <div class="table">
        <table>
            <tbody>
            <tr class="table-row">
                <td><spring:message code="professor.name"/></td>
                <td>
                    <sl-input autofocus id="new-prof-name"></sl-input>
                </td>
            </tr>
            <tr class="table-row">
                <td><spring:message code="professor.surname"/></td>
                <td>
                    <sl-input id="new-prof-surname"></sl-input>
                </td>
            </tr>
        </table>
    </div>
    <sl-button slot="footer" variant="success" onclick="createProfessor()"><spring:message code="subject.create"/></sl-button>
</sl-dialog>

<sl-dialog label="<spring:message code="subject.create.class.title"/>" class="dialog-header-actions2" style="--width: 45rem">
    <p slot="footer"><spring:message code="subject.create.class.hint"/></p>
    <div id="error-message">

    </div>
    <div class="table">
        <table>
            <tbody>
            <tr>
                <td><spring:message code="subject.classCode"/></td>
                <td><sl-input autofocus id="class-code"></sl-input></td>
            </tr>
            <tr>
                <td>
                    <spring:message code="subject.professors"/>
                </td>
                <td>
                    <sl-select multiple clearable id="class-professors">

                    </sl-select>
                </td>
            </tr>
            <tr>
                <td><spring:message code="subject.classDay"/></td>
                <td>
                    <sl-select id="class-day">
                        <sl-option value="<c:out value="1"/>"><spring:message code="subject.classDay1"/></sl-option>
                        <sl-option value="<c:out value="2"/>"><spring:message code="subject.classDay2"/></sl-option>
                        <sl-option value="<c:out value="3"/>"><spring:message code="subject.classDay3"/></sl-option>
                        <sl-option value="<c:out value="4"/>"><spring:message code="subject.classDay4"/></sl-option>
                        <sl-option value="<c:out value="5"/>"><spring:message code="subject.classDay5"/></sl-option>
                        <sl-option value="<c:out value="6"/>"><spring:message code="subject.classDay6"/></sl-option>
                        <sl-option value="<c:out value="7"/>"><spring:message code="subject.classDay7"/></sl-option>
                    </sl-select>
                </td>
            </tr>
            <tr>
                <td><spring:message code="subject.time.start"/></td>
                <td><sl-input id="class-start-time" type="time"></sl-input></td>
            </tr>
            <tr>
                <td><spring:message code="subject.time.end"/></td>
                <td><sl-input id="class-end-time" type="time"></sl-input></td>
            </tr>
            <tr>
                <td><spring:message code="subject.classMode"/></td>
                <td>
                    <sl-select id="class-mode">
                        <sl-option value="<c:out value="1"/>"><spring:message code="subject.mode.inperson"/></sl-option>
                        <sl-option value="<c:out value="2"/>"><spring:message code="subject.mode.virtual"/></sl-option>
                        <sl-option value="<c:out value="3"/>"><spring:message code="subject.mode.lab"/></sl-option>
                    </sl-select>
                </td>
            </tr>
            <tr>
                <td><spring:message code="builder.building"/></td>
                <td>
                    <sl-select id="class-building">
                        <sl-option value="<c:out value="1"/>"><spring:message code="subject.building.rectorado"/></sl-option>
                        <sl-option value="<c:out value="2"/>"><spring:message code="subject.building.tecnologico"/></sl-option>
                        <sl-option value="<c:out value="3"/>"><spring:message code="subject.building.financiero"/></sl-option>
                        <sl-option value="<c:out value="4"/>"><spring:message code="subject.mode.virtual"/></sl-option>
                    </sl-select>
                </td>
            </tr>
            <tr>
                <td><spring:message code="subject.classNumber"/></td>
                <td><sl-input id="classroom"></sl-input></td>
            </tr>
            </tbody>
        </table>
    </div>
    <sl-button slot="footer" variant="success" onclick="createClass()"><spring:message code="subject.add.short"/></sl-button>
</sl-dialog>

<sl-dialog label="<spring:message code="subject.add.degree"/>" class="dialog-header-actions3" >

    <div class="table degree-dialog">
        <table>
            <tbody>
            <tr>
                <td><spring:message code="degree"/></td>
                <td>
                    <sl-select id="select-degree" class="select-degree" hoist>
                        <c:forEach var="degree" items="${degrees}">
                            <sl-option value="${degree.id}"><c:out value="${degree.name}"/></sl-option>
                        </c:forEach>
                    </sl-select>
                </td>
            </tr>
            <tr>
                <td><spring:message code="subject.semester"/></td>
                <td>
                    <sl-select id="select-semester" class="select-semester" hoist>
                        <sl-option value="<c:out value="1"/>"><spring:message code="semester" arguments="1"/></sl-option>
                        <sl-option value="<c:out value="2"/>"><spring:message code="semester" arguments="2"/></sl-option>
                        <sl-option value="<c:out value="3"/>"><spring:message code="semester" arguments="3"/></sl-option>
                        <sl-option value="<c:out value="4"/>"><spring:message code="semester" arguments="4"/></sl-option>
                        <sl-option value="<c:out value="5"/>"><spring:message code="semester" arguments="5"/></sl-option>
                        <sl-option value="<c:out value="6"/>"><spring:message code="semester" arguments="6"/></sl-option>
                        <sl-option value="<c:out value="7"/>"><spring:message code="semester" arguments="7"/></sl-option>
                        <sl-option value="<c:out value="8"/>"><spring:message code="semester" arguments="8"/></sl-option>
                        <sl-option value="<c:out value="9"/>"><spring:message code="semester" arguments="9"/></sl-option>
                        <sl-option value="<c:out value="10"/>"><spring:message code="semester" arguments="10"/></sl-option>
                        <sl-option value="<c:out value="-1"/>"><spring:message code="elective"/></sl-option>
                    </sl-select>
                </td>
            </tr>
            </tbody>
        </table>
        <sl-button slot="footer" variant="success" onclick="addDegreeSemester()"><spring:message code="subject.add.short"/></sl-button>
    </div>
</sl-dialog>

<main class="container-50">
    <c:url value="/subject/${subject.id}/edit" var="EditSubject"/>
    <form:form modelAttribute="editSubjectForm" class="col s12" method="post" action="${EditSubject}">
        <div id="step1">
            <div class="table">
                <table>
                    <thead>
                    <th colspan="2"><h1><spring:message code="subject.current.edit" arguments="${subject.name}\0${subject.id}" argumentSeparator="\0"/></h1></th>
                    </thead>
                    <tbody>
                    <tr>
                        <td>
                            <form:errors path="credits" cssClass="error" element="p"/>
                            <spring:message code="subject.credits"/>
                        </td>
                        <td><sl-input name="credits" path="credits" value="${subject.credits}" type="number" id="subject-credits" onkeydown="return event.key !== 'Enter';"></sl-input></td>
                    </tr>
                    <tr>
                        <td>
                            <form:errors path="degreeIds" cssClass="error" element="p"/>
                            <form:errors path="semesters" cssClass="error" element="p"/>
                            <spring:message code="subject.degrees"/>
                        </td>
                        <td>
                            <sl-button id="open-button3"><spring:message code="subject.add.degree"/></sl-button>
                            <input name="degreeIds" type="hidden" id="degreeIds-hiddenInput"/>
                            <input name="semesters" type="hidden" id="semesters-hiddenInput"/>
                        </td>
                    </tr>
                    <tr>
                        <td/>
                        <td>
                            <ul id="degreeSemesters">
                            </ul>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div class="correlatives-div">
                                <form:errors path="requirementIds" cssClass="error" element="p"/>
                                <spring:message code="subject.prerequisites"/>
                                <h6 class="optional"><spring:message code="subject.optional"/></h6>
                            </div>
                        </td>
                        <td style="display: flex; flex-direction: row">

                            <input id="requirement" list="requirements" class="selection">
                            <datalist id="requirements">
                                <c:forEach items="${allSubjects}" var="subject">
                                    <option value="${subject.id} - ${subject.name}" id="${subject.id}"></option>
                                </c:forEach>
                            </datalist>
                            <sl-icon-button name="plus-lg" onclick="addRequirement()"></sl-icon-button>
                        </td>
                        <input type="hidden" name="requirementIds" id="hiddenInput"/>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                            <ul id="prerequisiteItems">

                            </ul>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <form:errors path="professors" cssClass="error" element="p"/>
                            <spring:message code="subject.professors"/>
                        </td>
                        <td style="display: flex; flex-direction: row">
                            <input id="professor" list="professors" class="selection">
                            <datalist id="professors">
                                <c:forEach items="${professors}" var="professor">
                                    <option value="${professor.name}" id="${professor.id}"></option>
                                </c:forEach>
                            </datalist>
                            <sl-icon-button name="plus-lg" onclick="addProfessor()"></sl-icon-button>
                            <input name="professors" type="hidden" id="professors-hiddenInput"/>
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                            <ul id="professorItems">

                            </ul>
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                            <sl-button id="open-button"><spring:message code="subject.create.professor"/></sl-button>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="add-button">
                <sl-button variant="success" onclick="nextStep()" id="nextStep1"><spring:message code="register.next"/></sl-button>
            </div>
        </div>
        <div id="step2" style="display: none">
            <div>
                <table>
                    <thead>
                    <th colspan="2"><h1><spring:message code="subject.current.edit" arguments="${subject.name}\0${subject.id}" argumentSeparator="\0"/></h1></th>
                    </thead>
                    <tbody>
                    <tr>
                        <td><spring:message code="subject.class"/></td>
                        <td><sl-button id="open-button2"><spring:message code="subject.create.class"/></sl-button></td>
                    </tr>
                    </tbody>
                </table>
                <br/>
                <div>

                    <table>
                        <thead>
                        <tr>
                            <th>
                                <form:errors path="classCodes" cssClass="error" element="p"/>
                                <spring:message code="builder.class"/>
                            </th>
                            <th>
                                <form:errors path="classProfessors" cssClass="error" element="p"/>
                                <spring:message code="subject.add.professors"/>
                            </th>
                            <th>
                                <form:errors path="classDays" cssClass="error" element="p"/>
                                <spring:message code="subject.classDay"/>
                            </th>
                            <th>
                                <form:errors path="classStartTimes" cssClass="error" element="p"/>
                                <spring:message code="subject.time.start"/>
                            </th>
                            <th>
                                <form:errors path="classEndTimes" cssClass="error" element="p"/>
                                <spring:message code="subject.time.end"/>
                            </th>
                            <th>
                                <form:errors path="classBuildings" cssClass="error" element="p"/>
                                <spring:message code="builder.mode"/>
                            </th>
                            <th>
                                <form:errors path="classRooms" cssClass="error" element="p"/>
                                <spring:message code="builder.building"/>
                            </th>
                            <th>
                                <form:errors path="classModes" cssClass="error" element="p"/>
                                <spring:message code="subject.classroom"/>
                            </th>
                            <th> </th>
                        </tr>
                        </thead>
                        <tbody id="classItems">

                        </tbody>
                    </table>

                </div>
                <input type="hidden" name="classCodes" id="classCodes-hiddenInput"/>
                <input type="hidden" name="classProfessors" id="classProfessors-hiddenInput"/>
                <input type="hidden" name="classDays" id="classDays-hiddenInput"/>
                <input type="hidden" name="classStartTimes" id="classStartTimes-hiddenInput"/>
                <input type="hidden" name="classEndTimes" id="classEndTimes-hiddenInput"/>
                <input type="hidden" name="classBuildings" id="classBuildings-hiddenInput"/>
                <input type="hidden" name="classRooms" id="classRooms-hiddenInput"/>
                <input type="hidden" name="classModes" id="classModes-hiddenInput"/>
            </div>

            <div class="add-button">
                <sl-button variant="success" onclick="previousStep()" class="bottom-buttons"><spring:message code="register.previous"/></sl-button>
                <sl-button type="submit" variant="success" class="bottom-buttons" id="submit-button"><spring:message code="subject.edit.submit"/></sl-button>
            </div>
        </div>
        </div>
    </form:form>

</main>
<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>
</body>
<script src="${pageContext.request.contextPath}/js/create-edit-subject.js"></script>
<script>

    window.onload = function () {
        loadStartingFields();
    };

    function loadStartingFields() {
        <c:forEach var="degree" items="${subject.degrees}">
            <c:forEach var="semester" items="${degree.degreeSubjects}">
                <c:if test="${semester.subject.id == subject.id}">
                    semesterArray.push("${semester.semester}");
                </c:if>
            </c:forEach>
            degreeArray.push("${degree.id}");
        </c:forEach>
        <c:forEach var="prereq" items="${subject.prerequisites}">
            requirementList.push("${prereq.id}");
            reqNameList.push("${prereq.id} - ${prereq.name}");
        </c:forEach>
        <c:forEach var="prof" items="${subject.professors}">
            professorList.push("${prof.name}");
            professorMap[index] = "${prof.name}";
            index++;
        </c:forEach>
        let classProfessorsList;
        <c:forEach var="classes" items="${subject.classes}">
            <c:forEach var="subjectTimes" items="${classes.classTimes}">
                classProfessorsList = [];
                <c:forEach var="subjectProfessors" items="${classes.professors}">
                    classProfessorsList.push("${subjectProfessors.name}");
                </c:forEach>
                classCodeList.push("${classes.classId}");
                classProfList.push(classProfessorsList);
                classDayList.push("${subjectTimes.day}");
                classStartTimeList.push("${subjectTimes.startTime}");
                classEndTimeList.push("${subjectTimes.endTime}");
                classBuildingList.push("${subjectTimes.building}");
                classRoomList.push("${subjectTimes.classLoc}");
                classModeList.push("${subjectTimes.mode}");
            </c:forEach>
        </c:forEach>

        updateDegreeSemesterItems();
        updatePrerequisiteItems();
        updateProfessorItems();
        updateClassItems();

        document.getElementById('degreeIds-hiddenInput').value = JSON.stringify(degreeArray);
        document.getElementById('semesters-hiddenInput').value = JSON.stringify(semesterArray);
        document.getElementById('professors-hiddenInput').value = JSON.stringify(professorList);
        document.getElementById('classCodes-hiddenInput').value = JSON.stringify(classCodeList);
        document.getElementById('classProfessors-hiddenInput').value = JSON.stringify(classProfList);
        document.getElementById('classDays-hiddenInput').value = JSON.stringify(classDayList);
        document.getElementById('classStartTimes-hiddenInput').value = JSON.stringify(classStartTimeList);
        document.getElementById('classEndTimes-hiddenInput').value = JSON.stringify(classEndTimeList);
        document.getElementById('classBuildings-hiddenInput').value = JSON.stringify(classBuildingList);
        document.getElementById('classRooms-hiddenInput').value = JSON.stringify(classRoomList);
        document.getElementById('classModes-hiddenInput').value = JSON.stringify(classModeList);
    }

    function addRequirement() {
        const requirement = document.getElementById("requirement");
        const id = requirement.value.split(" - ")[0];
        const name = requirement.value;
        if(id === "" || id === "<spring:message code="subject.create.subject.error"/>" ) {
            return;
        }
        if(!requirementList.includes(id)) {
            requirementList.push(id);
            reqNameList.push(name);
            document.getElementById('hiddenInput').value = JSON.stringify(requirementList);
            requirement.value = "";
        } else {
            requirement.value = "<spring:message code="subject.create.subject.error"/>";
        }
        updatePrerequisiteItems();
    }
    function updatePrerequisiteItems() {
        let selectedPrequisitesItems = document.getElementById('prerequisiteItems');
        selectedPrequisitesItems.innerHTML = '';
        reqNameList.forEach((item) => {
            let div = document.createElement('div');
            div.className = 'list-rm';
            let prereqItem = document.createElement('li');
            prereqItem.textContent = item;

            let removeBtn = document.createElement('sl-icon-button');
            removeBtn.name = "x";
            removeBtn.addEventListener('click', () => {
                let index = reqNameList.indexOf(item);
                reqNameList.splice(index, 1);
                requirementList.splice(index, 1);
                updatePrerequisiteItems();
                document.getElementById('hiddenInput').value = JSON.stringify(requirementList);
            });
            div.appendChild(prereqItem);
            div.appendChild(removeBtn);
            degreeArray.forEach((degreeID) => {
                if( !degreesSubjectMap[degreeID].includes(requirementList[reqNameList.indexOf(item)])){
                    let warning = document.createElement('li');
                    warning.textContent = "<spring:message code="subject.create.notInDegree"/>";
                    warning.style.color = "red";
                    div.appendChild(warning);
                }
            });
            selectedPrequisitesItems.appendChild(div);
        });
    }

    function addProfessor() {
        const professor = document.getElementById("professor");
        const id = professor.value;
        if(id === "" || id === "<spring:message code="subject.create.professor.error1"/>"){
            return;
        }
        if(!professorList.includes(id)) {
            professorList.push(id);
            professorMap[index] = id;

            document.getElementById('professors-hiddenInput').value = JSON.stringify(professorList);
            professor.value = "";
            index++;
        } else {
            professor.value = "<spring:message code="subject.create.professor.error1"/>";
        }
        updateProfessorItems();
        //checkCompleteFields();
    }

    function createProfessor(){
        const profName = document.getElementById("new-prof-name");
        const profSurname = document.getElementById("new-prof-surname");
        const professorName = profSurname.value + ", " + profName.value;
        if(professorName === "") {
            dialog.hide();
            return;
        }
        if(professorName === "<spring:message code="subject.create.professor.error2"/>"){
            return;
        }
        if(professorList.includes(professorName)){
            professor.value = "<spring:message code="subject.create.professor.error2"/>";
            return;
        }
        professorList.push(professorName);
        document.getElementById('professors-hiddenInput').value = JSON.stringify(professorList);
        professorMap[index] = professorName;

        index++;
        //checkCompleteFields();
        profName.value = "";
        profSurname.value = "";
        updateProfessorItems();
        dialog.hide();
    }

    let degreeMap = {
        <c:forEach var="degree" items="${degrees}">
        "${degree.id}":"${degree.name}",
        </c:forEach>
    }

    let semesterMap = {
        "1":"<spring:message code="semester" arguments="1"/>",
        "2":"<spring:message code="semester" arguments="2"/>",
        "3":"<spring:message code="semester" arguments="3"/>",
        "4":"<spring:message code="semester" arguments="4"/>",
        "5":"<spring:message code="semester" arguments="5"/>",
        "6":"<spring:message code="semester" arguments="6"/>",
        "7":"<spring:message code="semester" arguments="7"/>",
        "8":"<spring:message code="semester" arguments="8"/>",
        "9":"<spring:message code="semester" arguments="9"/>",
        "10":"<spring:message code="semester" arguments="10"/>",
        "-1":"<spring:message code="elective"/>",
    }

    const degreesSubjectMap = {
        <c:forEach var="degree" items="${degrees}">
        "${degree.id}":[
            <c:forEach var="subject" items="${degree.subjects}">
            "${subject.id}",
            </c:forEach>
        ],
        </c:forEach>
    }

    function updateDegreeSemesterItems(){
        let selectedDegreeSemester = document.getElementById('degreeSemesters');
        selectedDegreeSemester.innerHTML = '';
        degreeArray.forEach((degreeId) => {
            let div = document.createElement('div');
            div.className = 'list-rm';
            let degreeItem = document.createElement('li');
            let removeBtn = document.createElement('sl-icon-button');
            removeBtn.name = "x";
            removeBtn.addEventListener('click', () => {
                let index = degreeArray.indexOf(degreeId);
                degreeArray.splice(index, 1);
                semesterArray.splice(index, 1);
                document.getElementById('degreeIds-hiddenInput').value = JSON.stringify(degreeArray);
                document.getElementById('semesters-hiddenInput').value = JSON.stringify(semesterArray);

                updateDegreeSemesterItems();
                //checkCompleteFields();
                checkForCorrelatives();
            });
            degreeItem.textContent = degreeMap[degreeId] + " - " + semesterMap[semesterArray[degreeArray.indexOf(degreeId)]]
            selectedDegreeSemester.appendChild(div);
            div.appendChild(degreeItem);
            div.appendChild(removeBtn);
        });
    }

    const modeMap = {
        "1":"Presencial",
        "2":"Virtual",
        "3":"Laboratorio"
    }

    const buildingMap = {
        "1":"Rectorado",
        "2":"Tecnológico",
        "3":"Financiero",
        "4":"Virtual"
    }

    const dayMap = {
        "1":"<spring:message code="subject.classDay1"/>",
        "2":"<spring:message code="subject.classDay2"/>",
        "3":"<spring:message code="subject.classDay3"/>",
        "4":"<spring:message code="subject.classDay4"/>",
        "5":"<spring:message code="subject.classDay5"/>",
        "6":"<spring:message code="subject.classDay6"/>",
        "7":"<spring:message code="subject.classDay7"/>",
    }

    function updateErrorMessage(id) {
        let errorMessage = document.getElementById('error-message');
        errorMessage.innerHTML = "";
        let message = document.createElement('p');
        message.className="error-message";
        if(id === 1) message.textContent = "<spring:message code="subject.create.class.error1"/>";
        if(id === 2) message.textContent = "<spring:message code="subject.create.class.error2"/>";
        errorMessage.appendChild(message);
    }

    function createClass() {
        const classCode = document.getElementById("class-code");
        const classProf = document.getElementById("class-professors");
        const classDay = document.getElementById("class-day");
        const classStartTime = document.getElementById("class-start-time");
        const classEndTime = document.getElementById("class-end-time");
        const classBuilding = document.getElementById("class-building");
        const classRoom = document.getElementById("classroom");
        const classMode = document.getElementById("class-mode");

        if (classCode.value === "" || classProf.value.length === 0 || classDay.value === "" || classStartTime.value === "" || classEndTime.value === "" || classBuilding.value === "" || classRoom.value === "" || classMode.value === "") {
            updateErrorMessage(2)
            return;
        }
        if (classCodeList.includes(classCode.value) && classProfList.includes(classProf.value) && classDayList.includes(dayMap[classDay.value]) && classStartTimeList.includes(classStartTime.value) && classEndTimeList.includes(classEndTime.value) && classBuildingList.includes(buildingMap[classBuilding.value]) && classRoomList.includes(classRoom.value) && classModeList.includes(modeMap[classMode.value])) {
            classCode.value = "<spring:message code="subject.create.class.error1"/>";
            updateErrorMessage(1)
            return;
        }

        classCodeList.push(classCode.value);
        let profIndex = 0;
        let professors = [];
        if (classProf.value.length > 1){
            while (profIndex < classProf.value.length) {
                professors.push(professorMap[classProf.value[profIndex++]]);
            }
        }
        else {
            professors.push(professorMap[classProf.value]);
        }
        classProfList.push(professors);
        classDayList.push(classDay.value);
        classStartTimeList.push(classStartTime.value);
        classEndTimeList.push(classEndTime.value);
        classBuildingList.push(buildingMap[classBuilding.value]);
        classRoomList.push(classRoom.value);
        classModeList.push(modeMap[classMode.value]);

        document.getElementById('classCodes-hiddenInput').value = JSON.stringify(classCodeList);
        document.getElementById('classProfessors-hiddenInput').value = JSON.stringify(classProfList);
        document.getElementById('classDays-hiddenInput').value = JSON.stringify(classDayList);
        document.getElementById('classStartTimes-hiddenInput').value = JSON.stringify(classStartTimeList);
        document.getElementById('classEndTimes-hiddenInput').value = JSON.stringify(classEndTimeList);

        document.getElementById('classBuildings-hiddenInput').value = JSON.stringify(classBuildingList);
        document.getElementById('classRooms-hiddenInput').value = JSON.stringify(classRoomList);
        document.getElementById('classModes-hiddenInput').value = JSON.stringify(classModeList);

        updateClassItems();
        checkCompleteFieldsForSubmit();

        classCode.value = "";
        classProf.value = "";
        classDay.value = "";
        classStartTime.value = "";
        classEndTime.value = "";
        classBuilding.value = "";
        classRoom.value = "";
        classMode.value = "";

        let errorMessage = document.getElementById('error-message');
        errorMessage.innerHTML = "";


        dialog2.hide();
    }

    function checkCompleteFields(){
        const credits = document.getElementById('subject-credits').value;
        const professors = document.getElementById('professors-hiddenInput').value;
        const degrees = document.getElementById('degreeIds-hiddenInput').value;

        document.getElementById('nextStep1').disabled = !(
            credits > 0 && credits <= 12 &&
            professors.length > 2 &&
            degrees.length > 2
        );
    }


</script>
