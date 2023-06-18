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
                        <sl-input autofocus id="new-prof" help-text="<spring:message code="professor.name.help" />"></sl-input>
                    </td>
                </tr>
            </table>
        </div>
        <sl-button slot="footer" variant="success" onclick="createProfessor()"><spring:message code="subject.create"/></sl-button>
    </sl-dialog>

    <sl-dialog label="<spring:message code="subject.create.class"/>" class="dialog-header-actions2" style="--width: 45rem">
        <div class="table">
            <table>
                <tbody>
                <tr>
                    <td><spring:message code="subject.classCode"/></td>
                    <td><sl-input autofocus name="code"></sl-input></td>
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
                            <sl-option value="1"><spring:message code="subject.classDay1"/></sl-option>
                            <sl-option value="2"><spring:message code="subject.classDay2"/></sl-option>
                            <sl-option value="3"><spring:message code="subject.classDay3"/></sl-option>
                            <sl-option value="4"><spring:message code="subject.classDay4"/></sl-option>
                            <sl-option value="5"><spring:message code="subject.classDay5"/></sl-option>
                            <sl-option value="6"><spring:message code="subject.classDay6"/></sl-option>
                            <sl-option value="7"><spring:message code="subject.classDay7"/></sl-option>
                        </sl-select>
                    </td>
                </tr>
                <tr>
                    <td><spring:message code="subject.classTimes"/></td>
                    <td><sl-input name="time"></sl-input></td>
                </tr>
                <tr>
                    <td><spring:message code="subject.classMode"/></td>
                    <td><sl-input name="class"></sl-input></td>
                </tr>
                <tr>
                    <td><spring:message code="subject.classBuilding"/></td>
                    <td><sl-input name="building"></sl-input></td>
                </tr>
                <tr>
                    <td><spring:message code="subject.classNumber"/></td>
                    <td><sl-input name="room"></sl-input></td>
                </tr>
                </tbody>
            </table>
        </div>
        <sl-button slot="footer" variant="success"><spring:message code="subject.prerequisites.add.short"/></sl-button>
    </sl-dialog>

    <sl-dialog label="<spring:message code="subject.add.degree"/>" class="dialog-header-actions3" >
        <div class="table degree-dialog">
            <table>
                <tbody>
                <tr class="table-row">
                    <td><spring:message code="degree"/></td>
                    <td>
                        <sl-select id="select-degree" class="select-degree">
                            <c:forEach var="degree" items="${degrees}">
                                <sl-option value="${degree.id}"><c:out value="${degree.name}"/></sl-option>
                            </c:forEach>
                        </sl-select>
                    </td>
                </tr>
                <tr>
                    <td><spring:message code="subject.semester"/></td>
                    <td>
                        <sl-select id="select-semester" class="select-semester">
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

            </table>
            <sl-button slot="footer" variant="success" onclick="addDegreeSemester()"><spring:message code="subject.create"/></sl-button>
        </div>
    </sl-dialog>

    <main class="container-50">
    <form:form modelAttribute="subjectForm" class="col s12" method="post" action="${CreateSubject}">
        <div class="table">
            <table>
                <thead>
                <th colspan="2"><h1><spring:message code="subject.new"/></h1></th>
                </thead>
                <tbody>
                <tr>
                    <form:errors path="id" cssClass="error" element="p"/>
                    <td><spring:message code="subject.id"/></td>
                    <td><sl-input name="id" path="id" value="${SubjectForm.id}"></sl-input></td>
                </tr>
                <tr>
                    <td><spring:message code="subject.name"/></td>
                    <td><sl-input name="name" path="name" value="${SubjectForm.name}"></sl-input></td>
                </tr>
                <tr>
                    <td><spring:message code="subject.department"/></td>
                    <td><sl-input name="department" path="department" value="${SubjectForm.department}"></sl-input></td>
                </tr>
                <tr>
                    <td><spring:message code="subject.credits"/></td>
                    <td><sl-input name="credit" path="credit" value="${SubjectForm.credits}"></sl-input></td>
                </tr>
                <tr>
                    <td><spring:message code="subject.prerequisites"/></td>
                    <td style="display: flex; flex-direction: row">
                        <input id="requirement" list="requirements" class="selection">
                        <datalist id="requirements">
                            <c:forEach items="${subjects}" var="subject">
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
                        <ul id="prerequisiteItems"></ul>
                    </td>
                </tr>
                <tr>
                    <td><spring:message code="subject.professors"/></td>
                    <td style="display: flex; flex-direction: row">
                        <input id="professor" list="professors" class="selection">
                        <datalist id="professors">
                            <c:forEach items="${professors}" var="professor">
                                <option value="${professor.name}" id="${professor.id}"></option>
                            </c:forEach>
                        </datalist>
                        <sl-icon-button name="plus-lg" onclick="addProfessor()"></sl-icon-button>
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
                <tr>
                    <td><spring:message code="subject.class"/></td>
                    <td><sl-button id="open-button2"><spring:message code="subject.create.class"/></sl-button></td>
                </tr>
                <tr>
                    <td><spring:message code="subject.degrees"/></td>
                    <td>
                        <sl-button id="open-button3"><spring:message code="subject.add.degree"/></sl-button>
                    </td>
                </tr>
                <tr>
                    <td/>
                    <td>
                        <ul id="degreeSemesters"></ul>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="add-button">
            <sl-button type="submit" variant="success"><spring:message code="subject.create"/></sl-button>
        </div>
    </form:form>
    </main>
    <jsp:include page="../components/footer.jsp"/>
    <jsp:include page="../components/body_scripts.jsp"/>
</body>

<script>

    const dialog = document.querySelector('.dialog-header-actions');
    const openButton = document.querySelector('#open-button')
    const closeButton = dialog.querySelector('sl-button[slot="footer"]');
    //const newWindowButton = dialog.querySelector('.new-window');

    openButton.addEventListener('click', () => dialog.show());
    //closeButton.addEventListener('click', () => dialog.hide());

    const dialog2 = document.querySelector('.dialog-header-actions2');
    const openButton2 = document.querySelector('#open-button2');
    const closeButton2 = dialog2.querySelector('sl-button[slot="footer"]');

    openButton2.addEventListener('click', () => dialog2.show());
    closeButton2.addEventListener('click', () => dialog2.hide());

    const dialog3 = document.querySelector('.dialog-header-actions3');
    const openButton3 = document.querySelector('#open-button3');
    const closeButton3 = dialog3.querySelector('sl-button[slot="footer"]');

    openButton3.addEventListener('click', () => dialog3.show());
    closeButton3.addEventListener('click', () => dialog3.hide());


    dialog.addEventListener('click', (event) => {
        event.stopPropagation();
    });

    let requirementList = [];
    let reqNameList = [];

    function addRequirement() {
        const requirement = document.getElementById("requirement");
        const id = requirement.value.split(" - ")[0];
        const name = requirement.value;
        if(id === "" || id === "Error materia ya agregada") {
            return;
        }
        if(!requirementList.includes(id)) {
            requirementList.push(id);
            reqNameList.push(name);
            document.getElementById('hiddenInput').value = JSON.stringify(requirementList);
            requirement.value = "";
        } else {
            requirement.value = "Error materia ya agregada";
        }
        updatePrerequisiteItems();
        console.log(requirementList);
    }

    var professorList = [];

    function addProfessor() {
        const professor = document.getElementById("professor");
        const id = professor.value;
        if(id === "" || id === "Error profesor ya agregado"){
            return;
        }
        if(!professorList.includes(id)) {
            professorList.push(id);
            //document.getElementById(); stringify
            professor.value = "";
        } else {
            professor.value = "Error profesor ya agregado"
        }
        updateProfessorItems();
        console.log(professorList);
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
            selectedPrequisitesItems.appendChild(div);
        });
    }

    function updateProfessorItems() {
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
                let index = professorList.indexOf(item);
                professorList.splice(index, 1);
                updateProfessorItems();
                document.getElementById('hiddenInput').value = JSON.stringify(requirementList);
            });
            profItem.textContent = item;
            selectedProfItems.appendChild(div);
            div.appendChild(profItem);
            div.appendChild(removeBtn);
            let classProf = document.createElement('sl-option');
            classProf.value=item;
            classProf.textContent=item;
            classProfessors.appendChild(classProf);
        });
    }

    function createProfessor(){
        const professor = document.getElementById("new-prof");
        const professorName = professor.value;
        if(professorName === "") {
            dialog.hide();
            return;
        }
        if(professorName === "Error profesor ya agregado"){
            return;
        }
        if(professorList.includes(professorName)){
            professor.value = "Error profesor ya agregado";
            return;
        }
        professorList.push(professorName);
        professor.value = "";
        updateProfessorItems();
        dialog.hide();
    }

    let classList = [];

    function createClass() {
        const classCode = document.getElementById("code");
        const classProf = document.getElementById("class-professors");
        const classDay = document.getElementById("class-day");
    }

    let degreeArray = [];
    let semesterArray = [];

    function addDegreeSemester(){
        const degree = document.getElementById("select-degree");
        const semester = document.getElementById("select-semester");

        if( degreeArray.includes(degree.value)){
            return;
        }

        degreeArray.push(degree.value);
        semesterArray.push(semester.value);
        updateDegreeSemesterItems();
        degree.value = "";
        semester.value = "";
        dialog3.hide();

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
                    updateDegreeSemesterItems();
                });
                degreeItem.textContent = degreeMap[degreeId] + " - " + semesterMap[semesterArray[degreeArray.indexOf(degreeId)]]
                selectedDegreeSemester.appendChild(div);
                div.appendChild(degreeItem);
                div.appendChild(removeBtn);
            });
    }

</script>
</html>

