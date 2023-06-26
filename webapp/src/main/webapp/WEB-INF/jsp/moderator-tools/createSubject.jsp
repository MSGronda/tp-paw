<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title><spring:message code="subject.new" /></title>
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
        td{

        }
    </style>
</head>
<body>
<jsp:include page="../components/navbar.jsp" />

<jsp:include page="../components/common-dialogues.jsp"/>

<main class="container-50">
    <c:url value="/create-subject" var="CreateSubject"/>
    <form:form modelAttribute="subjectForm" class="col s12" method="post" action="${CreateSubject}">
        <div id="step1">
            <div class="table">
                <table>
                    <thead>
                    <th colspan="2"><h1><spring:message code="subject.new"/></h1></th>
                    </thead>
                    <tbody>
                    <tr>
                        <td>
                            <form:errors path="id" cssClass="error" element="p"/>
                            <c:if test="${subjectCodeRepeated == true}">
                                <p class="error"><spring:message code="subject.create.code-repeated"/></p>
                            </c:if>
                            <spring:message code="subject.id"/>
                        </td>
                        <td><sl-input name="id" path="id" value="<c:out value="${subjectForm.id}"/>" id="subject-id" onkeydown="return event.key !== 'Enter';"></sl-input></td>
                    </tr>
                    <tr>

                        <td>
                            <form:errors path="name" cssClass="error" element="p"/>
                            <spring:message code="subject.name"/>
                        </td>
                        <td><sl-input name="name" path="name" value="<c:out value="${subjectForm.name}"/>" id="subject-name" onkeydown="return event.key !== 'Enter';"></sl-input></td>
                    </tr>
                    <tr>

                        <td>
                            <form:errors path="department" cssClass="error" element="p"/>
                            <spring:message code="subject.department"/>
                        </td>
                        <td>
                            <sl-select id="department-select">
                                <sl-option id="department-select-1" value="<c:out value="1"/>"><c:out value="Ambiente y Movilidad"/></sl-option>
                                <sl-option id="department-select-2" value="<c:out value="2"/>"><c:out value="Ciencias Exactas y Naturales"/></sl-option>
                                <sl-option id="department-select-3" value="<c:out value="3"/>"><c:out value="Ciencias de la Vida"/></sl-option>
                                <sl-option id="department-select-4" value="<c:out value="4"/>"><c:out value="Economia y Negocios"/></sl-option>
                                <sl-option id="department-select-5" value="<c:out value="5"/>"><c:out value="Sistemas Complejos y Energía"/></sl-option>
                                <sl-option id="department-select-6" value="<c:out value="6"/>"><c:out value="Sistemas Digitales y Datos"/></sl-option>
                            </sl-select>
                            <input type="hidden" name="department" id="department-hiddenInput"/>
                        </td>
                    </tr>
                    <tr>

                        <td>
                            <form:errors path="credits" cssClass="error" element="p"/>
                            <spring:message code="subject.credits"/>
                        </td>
                        <td><sl-input name="credits" path="credits" value="<c:out value="${subjectForm.credits}"/>" type="number" id="subject-credits" onkeydown="return event.key !== 'Enter';"></sl-input></td>
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
                            <ul id="degreeSemesters"></ul>
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
                                <c:forEach items="${subjects}" var="subject">
                                    <option value="<c:out value="${subject.id}"/> - <c:out value="${subject.name}"/>" id="<c:out value="${subject.id}"/>"></option>
                                </c:forEach>
                            </datalist>
                            <sl-icon-button name="plus-lg" onclick="addRequirement()"></sl-icon-button>
                        </td>
                        <input type="hidden" name="requirementIds" id="hiddenInput"/>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                            <ul id="prerequisiteItems"></ul>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <form:errors path="professors" cssClass="error" element="p"/>
                            <spring:message code="subject.professors.noPoints"/>
                        </td>
                        <td style="display: flex; flex-direction: row">
                            <input id="professor" list="professors" class="selection">
                            <datalist id="professors">
                                <c:forEach items="${professors}" var="professor">
                                    <option value="<c:out value="${professor.name}"/>" id="<c:out value="${professor.id}"/>"></option>
                                </c:forEach>
                            </datalist>
                            <sl-icon-button name="plus-lg" onclick="addProfessor()"></sl-icon-button>
                            <input name="professors" type="hidden" id="professors-hiddenInput"/>
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                            <ul id="professorItems"></ul>
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
                    <th colspan="2"><h1><spring:message code="subject.new"/></h1></th>
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
                                <spring:message code="subject.classCode"/>
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
                            <th></th>
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
                <sl-button type="submit" variant="success" class="bottom-buttons" id="submit-button"><spring:message code="subject.create"/></sl-button>
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

    function addRequirement() {
        const requirement = document.getElementById("requirement");
        const id = requirement.value.split(" - ")[0];
        const name = requirement.value;
        if(id === "" || id === "<spring:message code="subject.create.subject.error" htmlEscape="false" javaScriptEscape="true"/>") {
            return;
        }
        if(!requirementList.includes(id)) {
            requirementList.push(id);
            reqNameList.push(name);
            document.getElementById('hiddenInput').value = JSON.stringify(requirementList);
            requirement.value = "";
        } else {
            requirement.value = "<spring:message code="subject.create.subject.error" htmlEscape="false" javaScriptEscape="true"/>";
        }
        updatePrerequisiteItems();
    }

    function addProfessor() {
        const professor = document.getElementById("professor");
        const id = professor.value;
        if(id === "" || id === "<spring:message code="subject.create.professor.error1" htmlEscape="false" javaScriptEscape="true"/>"){
            return;
        }
        if(!professorList.includes(id)) {
            professorList.push(id);
            professorMap[index] = id;

            document.getElementById('professors-hiddenInput').value = JSON.stringify(professorList);
            professor.value = "";
            index++;
        } else {
            professor.value = "<spring:message code="subject.create.professor.error1" htmlEscape="false" javaScriptEscape="true"/>";
        }
        updateProfessorItems();
        checkCompleteFields();
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
                    warning.textContent = "<spring:message code="subject.create.notInDegree" htmlEscape="false" javaScriptEscape="true"/>";
                    warning.style.color = "red";
                    div.appendChild(warning);
                }
            });
            selectedPrequisitesItems.appendChild(div);
        });
    }

    const dayMap = {
        "1":"<spring:message code="subject.classDay1" htmlEscape="false" javaScriptEscape="true"/>",
        "2":"<spring:message code="subject.classDay2" htmlEscape="false" javaScriptEscape="true"/>",
        "3":"<spring:message code="subject.classDay3" htmlEscape="false" javaScriptEscape="true"/>",
        "4":"<spring:message code="subject.classDay4" htmlEscape="false" javaScriptEscape="true"/>",
        "5":"<spring:message code="subject.classDay5" htmlEscape="false" javaScriptEscape="true"/>",
        "6":"<spring:message code="subject.classDay6" htmlEscape="false" javaScriptEscape="true"/>",
        "7":"<spring:message code="subject.classDay7" htmlEscape="false" javaScriptEscape="true"/>",
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
            updateErrorMessage(2,"")
            return;
        }
        if (classCodeList.includes(classCode.value) && classProfList.includes(classProf.value) && classDayList.includes(dayMap[classDay.value]) && classStartTimeList.includes(classStartTime.value) && classEndTimeList.includes(classEndTime.value) && classBuildingList.includes(classBuilding.value) && classRoomList.includes(classRoom.value) && classModeList.includes(classMode.value)) {
            classCode.value = "<spring:message code="subject.create.class.error1" htmlEscape="false" javaScriptEscape="true"/>";
            updateErrorMessage(1,"")
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
        classBuildingList.push(classBuilding.value);
        classRoomList.push(classRoom.value);
        classModeList.push(classMode.value);

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

    function updateClass() {
        let updatedClassProf = document.getElementById('class-professors4');
        let profIndex = 0;
        let updatedProfessors = [];
        if( updatedClassProf.value.length > 1){
            while( profIndex < updatedClassProf.value.length ){
                updatedProfessors.push(professorMap[updatedClassProf.value[profIndex]]);
                profIndex++;
            }
        }else{
            updatedProfessors.push(professorMap[updatedClassProf.value]);
        }

        if( updatedClassProf.value.length === 0 || document.getElementById('class-day4').value === "" || document.getElementById('class-start-time4').value === "" || document.getElementById('class-end-time4').value === "" || document.getElementById('class-building4').value === "" || document.getElementById('classroom4').value === "" || document.getElementById('class-mode4').value === ""){
            updateErrorMessage(2, 4);
            return;
        }

        classProfList[updatedClassIndex] = updatedProfessors;
        classDayList[updatedClassIndex] = document.getElementById('class-day4').value;
        classStartTimeList[updatedClassIndex] = document.getElementById('class-start-time4').value;
        classEndTimeList[updatedClassIndex] = document.getElementById('class-end-time4').value;
        classModeList[updatedClassIndex] = document.getElementById('class-mode4').value;
        classBuildingList[updatedClassIndex] = document.getElementById('class-building4').value;
        classRoomList[updatedClassIndex] = document.getElementById('classroom4').value;

        updateClassItems();

        dialog4.hide();
        document.getElementById('classCodes-hiddenInput').value = JSON.stringify(classCodeList);
        document.getElementById('classProfessors-hiddenInput').value = JSON.stringify(classProfList);
        document.getElementById('classDays-hiddenInput').value = JSON.stringify(classDayList);
        document.getElementById('classStartTimes-hiddenInput').value = JSON.stringify(classStartTimeList);
        document.getElementById('classEndTimes-hiddenInput').value = JSON.stringify(classEndTimeList);
        document.getElementById('classBuildings-hiddenInput').value = JSON.stringify(classBuildingList);
        document.getElementById('classRooms-hiddenInput').value = JSON.stringify(classRoomList);
        document.getElementById('classModes-hiddenInput').value = JSON.stringify(classModeList);
    }

    function updateErrorMessage(id, dialogN) {
        let errorMessage = document.getElementById('error-message'+dialogN);
        errorMessage.innerHTML = "";
        let message = document.createElement('p');
        message.className="error-message";
        if(id === 1) message.textContent = "<spring:message code="subject.create.class.error1" htmlEscape="false" javaScriptEscape="true"/>";
        if(id === 2) message.textContent = "<spring:message code="subject.create.class.error2" htmlEscape="false" javaScriptEscape="true"/>";
        if(id === 3) message.textContent = "<spring:message code="subject.create.professor.error2" htmlEscape="false" javaScriptEscape="true"/>";
        errorMessage.appendChild(message);
    }

    let degreeMap = {
        <c:forEach var="degree" items="${degrees}">
        "<c:out value="${degree.id}"/>":"<c:out value="${degree.name}"/>",
        </c:forEach>
    }

    let semesterMap = {
        "1":"<spring:message code="semester" arguments="1" htmlEscape="false" javaScriptEscape="true"/>",
        "2":"<spring:message code="semester" arguments="2" htmlEscape="false" javaScriptEscape="true"/>",
        "3":"<spring:message code="semester" arguments="3" htmlEscape="false" javaScriptEscape="true"/>",
        "4":"<spring:message code="semester" arguments="4" htmlEscape="false" javaScriptEscape="true"/>",
        "5":"<spring:message code="semester" arguments="5" htmlEscape="false" javaScriptEscape="true"/>",
        "6":"<spring:message code="semester" arguments="6" htmlEscape="false" javaScriptEscape="true"/>",
        "7":"<spring:message code="semester" arguments="7" htmlEscape="false" javaScriptEscape="true"/>",
        "8":"<spring:message code="semester" arguments="8" htmlEscape="false" javaScriptEscape="true"/>",
        "9":"<spring:message code="semester" arguments="9" htmlEscape="false" javaScriptEscape="true"/>",
        "10":"<spring:message code="semester" arguments="10" htmlEscape="false" javaScriptEscape="true"/>",
        "-1":"<spring:message code="elective" htmlEscape="false" javaScriptEscape="true"/>",
    }

    // listener for department
    const departmentSelect = document.getElementById('department-select');
    departmentSelect.addEventListener('sl-change', setDepartment);

    function setDepartment(){
        const departmentValue = document.getElementById('department-select').value;
        const departmentString = document.getElementById('department-select-' + departmentValue).textContent
        document.getElementById('department-hiddenInput').value = departmentString
        checkCompleteFields();
    }

    //que aparezca disabled el boton de next si no estan los campos completos
    window.onload = function() {
        checkCompleteFields();
        checkCompleteFieldsForSubmit();
        checkForCorrelatives();
    };

    function checkCompleteFields(){
        const code = document.getElementById('subject-id').value;
        const subjectCodePattern = /^\d{2}\.\d{2}$/;

        const name = document.getElementById('subject-name').value;
        const department = document.getElementById('department-hiddenInput').value;
        const credits = document.getElementById('subject-credits').value;
        const professors = document.getElementById('professors-hiddenInput').value;
        const degrees = document.getElementById('degreeIds-hiddenInput').value;

        document.getElementById('nextStep1').disabled = !(
            subjectCodePattern.test(code) &&
            name.length > 0 &&
            department.length > 0 &&
            credits > 0 && credits <= 12 &&
            professors.length > 2 &&
            degrees.length > 2
        );
    }

    const degreesSubjectMap = {
        <c:forEach var="degree" items="${degrees}">
        "<c:out value="${degree.id}"/>":[
            <c:forEach var="subject" items="${degree.subjects}">
            "<c:out value="${subject.id}"/>",
            </c:forEach>
        ],
        </c:forEach>
    }

    let professorMap = {

    }

    function updateProfessorItems() {
        let options = 0;
        let selectedProfItems = document.getElementById('professorItems');
        let classProfessors = document.getElementById('class-professors');
        selectedProfItems.innerHTML = '';
        classProfessors.innerHTML = '';
        professorList.forEach((item) => {
            let div = document.createElement('div');
            div.className = 'list-rm';
            let profItem = document.createElement('li');
            let removeBtn = document.createElement('sl-icon-button');
            removeBtn.name = "x";
            removeBtn.addEventListener('click', () => {
                let iter = professorList.indexOf(item);
                professorList.splice(iter, 1);
                let lastProfMap = -1;
                let indexProfMap = -1;
                Object.entries(professorMap).forEach(([key, value]) => {
                    if ( indexProfMap !== -1){
                        professorMap[key-1] = professorMap[key];
                    }
                    if(value === item){
                        indexProfMap = key;
                        delete professorMap[key];
                        index--;
                    }
                    lastProfMap = key;
                });
                if( lastProfMap !== indexProfMap){
                    delete professorMap[lastProfMap];
                }
                updateProfessorItems();
                document.getElementById('professors-hiddenInput').value = JSON.stringify(professorList);
                checkCompleteFields();
            });
            profItem.textContent = item;
            selectedProfItems.appendChild(div);
            div.appendChild(profItem);
            div.appendChild(removeBtn);
            let classProf = document.createElement('sl-option');

            classProf.value=options++;
            classProf.textContent=item;
            classProfessors.appendChild(classProf);
        });
    }

    function addDegreeSemester(){
        const degree = document.getElementById("select-degree");
        const semester = document.getElementById("select-semester");

        if( degreeArray.includes(degree.value) || degree.value === "" || semester.value === ""){
            degree.value = "";
            semester.value = "";
            return;
        }

        degreeArray.push(degree.value);
        semesterArray.push(semester.value);
        document.getElementById('degreeIds-hiddenInput').value = JSON.stringify(degreeArray);
        document.getElementById('semesters-hiddenInput').value = JSON.stringify(semesterArray);
        checkCompleteFields();
        checkForCorrelatives();
        updateDegreeSemesterItems();
        degree.value = "";
        semester.value = "";
        dialog3.hide();
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
                checkCompleteFields();
                checkForCorrelatives();
            });
            degreeItem.textContent = degreeMap[degreeId] + " - " + semesterMap[semesterArray[degreeArray.indexOf(degreeId)]]
            selectedDegreeSemester.appendChild(div);
            div.appendChild(degreeItem);
            div.appendChild(removeBtn);
        });
    }

    function updateClassItems() {
        let selectedClassItems = document.getElementById('classItems');
        selectedClassItems.innerHTML = '';

        classCodeList.forEach((item, index) => {
            let breakLine = document.createElement('br');
            let tableRow = document.createElement('tr');
            let classTitle = document.createElement('td');
            let classProf = document.createElement('td');
            let classDay = document.createElement('td');
            let classStartTime = document.createElement('td');
            let classEndTime = document.createElement('td');
            let classMode = document.createElement('td');
            let classBuilding = document.createElement('td');
            let classRoom = document.createElement('td');
            let classEdit = document.createElement('td');
            let classRemove = document.createElement('td');
            let editBtn = document.createElement('sl-icon-button');
            let removeBtn = document.createElement('sl-icon-button');
            classProf.className = "professors-cell-width";
            editBtn.name = "pencil";
            editBtn.addEventListener('click', () => {
                editClass(item, index);
            });
            removeBtn.className = 'list-rm';
            removeBtn.name = "x";
            removeBtn.addEventListener('click', () => {
                classCodeList.splice(index, 1);
                classProfList.splice(index, 1);
                classDayList.splice(index, 1);
                classStartTimeList.splice(index, 1);
                classEndTimeList.splice(index, 1);
                classBuildingList.splice(index, 1);
                classRoomList.splice(index, 1);
                classModeList.splice(index, 1);
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
            });
            classTitle.textContent = item;
            classProf.textContent= classProfList[index] ;
            classDay.textContent= dayMap[classDayList[index]] ;
            classStartTime.textContent= classStartTimeList[index] ;
            classEndTime.textContent= classEndTimeList[index] ;
            classMode.textContent= classModeList[index] ;
            classBuilding.textContent= classBuildingList[index] ;
            classRoom.textContent= classRoomList[index] ;
            classRemove.appendChild(removeBtn);
            classEdit.appendChild(editBtn);
            tableRow.appendChild(classTitle);
            tableRow.appendChild(classProf);
            tableRow.appendChild(classDay);
            tableRow.appendChild(classStartTime);
            tableRow.appendChild(classEndTime);
            tableRow.appendChild(classMode);
            tableRow.appendChild(classBuilding);
            tableRow.appendChild(classRoom);
            tableRow.appendChild(classEdit);
            tableRow.appendChild(classRemove);
            selectedClassItems.appendChild(tableRow);
            selectedClassItems.appendChild(breakLine);
        });
    }

    function createProfessor(){
        const profName = document.getElementById("new-prof-name");
        const profSurname = document.getElementById("new-prof-surname");

        if(profName.value === "" || profSurname.value === "") {
            dialog.hide();
            return;
        }
        const professorName = profSurname.value + ", " + profName.value;
        if(professorList.includes(professorName)) {
            updateErrorMessage(3,"2");
            return;
        }
        professorList.push(professorName);
        document.getElementById('professors-hiddenInput').value = JSON.stringify(professorList);
        professorMap[index] = professorName;

        index++;
        checkCompleteFields();
        profName.value = "";
        profSurname.value = "";
        updateProfessorItems();
        dialog.hide();
    }

</script>
</html>

