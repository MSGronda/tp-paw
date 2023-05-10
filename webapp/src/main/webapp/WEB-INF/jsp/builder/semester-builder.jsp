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
    .builder-area{
        display: flex;
        flex-direction: row;
    }
    .subject-list{
        display: flex;
        flex-direction: column;
        overflow: auto;
        max-height: 31rem;
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
        height: 36rem;
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

    .class-selection{
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: space-between;
        margin: 1rem;
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
            <div slot="header"><h4>Available Subjects</h4></div>
            <div id="subject-list" class="subject-list">
                <%-- insert with js subject list into here --%>
            </div>
        </sl-card>

        <sl-card id="choose-class" class="choose-class">
            <div id="class-list" class="subject-list">
                <%-- insert with js  clases list into here--%>
            </div>
        </sl-card>
    </div>

</main>
<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>

<script src="${pageContext.request.contextPath}/js/schedule.js"></script>
<script>
    const schedule = new Schedule(29,7);

    const selectedColor = '#aad1ff'
    const incompatibleColor = '#d2d2d2'
    const normalColor = '#4f4f4f'
    const normalBorderColor = '#cccccc'

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

    function sortByCredits(a,b){
        return b.credits - a.credits
    }
    subjectClasses.sort(sortByCredits)

    function switchSelector(chooseClassVisibility, chooseSubjectVisibility, classNumber, classVisibility){
        document.getElementById('choose-class').style.display = chooseClassVisibility;
        document.getElementById('choose-subject').style.display = chooseSubjectVisibility;
        document.getElementById('classes-' + classNumber).style.display = classVisibility;
    }

    function alterSubjectCard(subjectId,color,colorBorder, disabled){
        const card = document.getElementById('subject-card-'+subjectId);
        card.style.color = color
        card.style.setProperty('--border-color', colorBorder)
        const select = document.getElementById('select-'+subjectId);
        select.disabled = disabled;
    }
    function alterClassCard(subjectId, classId, color,disabled){
        const card = document.getElementById('class-card-'+subjectId+'-'+classId);
        card.style.color = color
        const select = document.getElementById('select-class-'+subjectId+'-'+classId);
        select.disabled = disabled;
    }

    function disableIncompatibleSubjects(){
        for(let subNum in subjectClasses){
            // already signed up for that class
            if(!schedule.canAddSubject(subjectClasses[subNum].id)){
                alterSubjectCard(subjectClasses[subNum].id,selectedColor, selectedColor,true);
                continue;
            }

            let anyClassCompatible = false;
            for(let clNum in subjectClasses[subNum].classes){
                const classCompatibility = schedule.canAddClass(subjectClasses[subNum].classes[clNum].classTimes);

                if(classCompatibility === false){
                    // class isn't compatible with timetable
                    alterClassCard( subjectClasses[subNum].id,subjectClasses[subNum].classes[clNum].idClass, incompatibleColor,normalBorderColor,true)
                }
                anyClassCompatible = anyClassCompatible || classCompatibility;
            }

            // none of the classes are compatible with timetable => disable subject as well
            if(!anyClassCompatible){
                alterSubjectCard(subjectClasses[subNum].id,incompatibleColor,normalBorderColor,true);
            }
        }
    }

    function enableCompatibleSubjects(){
        for(let subNum in subjectClasses){
            // enable card if now is compatible
            if(schedule.canAddSubject(subjectClasses[subNum].id)){
                alterSubjectCard(subjectClasses[subNum].id,normalColor,normalBorderColor,false );
                continue;
            }

            let anyClassCompatible = false;
            for(let clNum in subjectClasses[subNum].classes){
                const classCompatibility = schedule.canAddClass(subjectClasses[subNum].classes[clNum].classTimes);

                if(classCompatibility === true){
                    // class is now compatible with timetable
                    alterClassCard( subjectClasses[subNum].id,subjectClasses[subNum].classes[clNum].idClass, normalColor,normalBorderColor,false )
                }
                anyClassCompatible = anyClassCompatible || classCompatibility;
            }

            // at least one of the classes is compatible with timetable => enable subject as well
            if(anyClassCompatible){
                alterSubjectCard(subjectClasses[subNum].id,normalColor,normalBorderColor,false );
            }
        }
    }


    function createSubjectCard(subjectList, subject){
        const card = document.createElement('sl-card');
        card.setAttribute('id', 'subject-card-'+subject.id);
        card.setAttribute('class', 'subject-card');

        const chooser = document.createElement('div');
        chooser.setAttribute('class', 'chooser');

        const column1 = document.createElement('div');
        column1.setAttribute('class', 'column');

        const subjectName = document.createElement('h5');
        subjectName.textContent = subject.name;
        column1.appendChild(subjectName);

        const credits = document.createElement('span');
        credits.textContent = 'Credits: '+subject.credits;
        column1.appendChild(credits);

        const column2 = document.createElement('div');
        column2.setAttribute('class', 'column');

        const selectButton = document.createElement('sl-button');
        selectButton.setAttribute('id', 'select-'+subject.id);
        selectButton.setAttribute('variant', 'default');
        selectButton.setAttribute('size', 'small');
        selectButton.setAttribute('circle', '');
        column2.appendChild(selectButton);
        selectButton.addEventListener('click',
            function() {
                // go to class selection
                switchSelector('flex','none',subject.id,'flex')
            }
        );

        const selectIcon = document.createElement('sl-icon');
        selectIcon.setAttribute('class', 'icon');
        selectIcon.setAttribute('name', 'check2');
        selectIcon.setAttribute('label', 'Select Subject');
        selectButton.appendChild(selectIcon);

        const deselectButton = document.createElement('sl-button');
        deselectButton.setAttribute('id', 'deselect-subject-'+subject.id);
        deselectButton.setAttribute('style', 'display: none; align-self: end');
        deselectButton.setAttribute('variant', 'default');
        deselectButton.setAttribute('size', 'small');
        deselectButton.setAttribute('circle', '');

        deselectButton.addEventListener('click',
            function() {
                // modify schedule table
                schedule.removeClass(subject.id)

                // enable subjects that can are now compatible after removing this subject
                enableCompatibleSubjects()

                // disable deselect button
                document.getElementById('select-'+ subject.id).style.display = 'block'
                document.getElementById('deselect-subject-'+ subject.id).style.display = 'none'
                document.getElementById('selected-'+ subject.id).style.display = 'none'
            }
        );



        column2.appendChild(deselectButton);

        const deselectIcon = document.createElement('sl-icon');
        deselectIcon.setAttribute('class', 'icon');
        deselectIcon.setAttribute('name', 'x-lg');
        deselectIcon.setAttribute('label', 'Remove subject');
        deselectButton.appendChild(deselectIcon);

        const selectedSpan = document.createElement('span');
        selectedSpan.setAttribute('id', 'selected-'+subject.id);
        selectedSpan.setAttribute('style', 'display: none; color: #7db6f8; padding-top:0.5rem');
        selectedSpan.textContent = 'Selected';
        column2.appendChild(selectedSpan);

        chooser.appendChild(column1);
        chooser.appendChild(column2);
        card.appendChild(chooser);

        return card;
    }


    function createClassInfoTable(classTime) {
        // create table element
        const table = document.createElement("table");

        // create table body
        const tbody = document.createElement("tbody");

        // create table rows and cells
        const dayRow = document.createElement("tr");
        const dayHeader = document.createElement("th");
        dayHeader.innerHTML = "Day";
        const dayData = document.createElement("td");

        // check if class has a day
        console.log(classTime.day)
        if (classTime.day !== 0) {
            dayData.innerHTML = daysOfWeek[classTime.day-1]
        } else {
            dayData.innerHTML = "No day";
        }

        dayRow.appendChild(dayHeader);
        dayRow.appendChild(dayData);

        const timeRow = document.createElement("tr");
        const timeHeader = document.createElement("th");
        timeHeader.innerHTML = "Time";
        const timeData = document.createElement("td");
        timeData.innerHTML = classTime.start + " - " + classTime.end;

        timeRow.appendChild(timeHeader);
        timeRow.appendChild(timeData);

        const classRow = document.createElement("tr");
        const classHeader = document.createElement("th");
        classHeader.innerHTML = "Class";
        const classData = document.createElement("td");
        classData.innerHTML = classTime.loc;

        classRow.appendChild(classHeader);
        classRow.appendChild(classData);

        const buildingRow = document.createElement("tr");
        const buildingHeader = document.createElement("th");
        buildingHeader.innerHTML = "Building";
        const buildingData = document.createElement("td");
        buildingData.innerHTML = classTime.building;

        buildingRow.appendChild(buildingHeader);
        buildingRow.appendChild(buildingData);

        const modeRow = document.createElement("tr");
        const modeHeader = document.createElement("th");
        modeHeader.innerHTML = "Mode";
        const modeData = document.createElement("td");
        modeData.innerHTML = classTime.mode;

        modeRow.appendChild(modeHeader);
        modeRow.appendChild(modeData);

        // append rows to table body
        tbody.appendChild(dayRow);
        tbody.appendChild(timeRow);
        tbody.appendChild(classRow);
        tbody.appendChild(buildingRow);
        tbody.appendChild(modeRow);

        // append table body to table
        table.appendChild(tbody);

        return table;
    }

    function createClassCard(subId, subName, subClass) {
        // create card element
        const card = document.createElement("sl-card");
        card.id = "class-card-" + subId + "-" + subClass.idClass;
        card.classList.add("subject-card");

        // create card header
        const header = document.createElement("div");
        header.classList.add("chooser");
        header.setAttribute("slot", "header");

        const title = document.createElement("h5");
        title.innerHTML = subClass.idClass;

        const selectButton = document.createElement("sl-button");
        selectButton.id = "select-class-" + subId + "-" + subClass.idClass;
        selectButton.variant = "default";
        selectButton.size = "small";
        selectButton.circle = true;

        selectButton.addEventListener('click',
            function() {
                // go back to subject selection
                switchSelector('none','flex',subId,'none')

                // modify schedule table
                schedule.addClass(subId, subName, subClass.classTimes)

                // disable all incompatible classes (already signed up to that subject or it doesn't fit in your schedule)
                disableIncompatibleSubjects();

                // enable deselect button
                document.getElementById('deselect-subject-'+ subId).style.display = 'block'
                document.getElementById('select-'+ subId).style.display = 'none'
                document.getElementById('selected-'+ subId).style.display = 'block'
            }
        );

        const icon = document.createElement("sl-icon");
        icon.classList.add("icon");
        icon.name = "check2";
        icon.label = "Select Subject";

        selectButton.appendChild(icon);

        header.appendChild(title);
        header.appendChild(selectButton);

        // create card content column
        const content = document.createElement("div");
        content.classList.add("column");

        // add all the class times information to card
        for(let classNum in subClass.classTimes){
            const table = createClassInfoTable(subClass.classTimes[classNum])
            content.append(table);
        }

        // append header and content to card
        card.appendChild(header);
        card.appendChild(content);

        return card;
    }

    function createSubjectClassInfo(subject) {
        // create subject class info container
        const container = document.createElement("div");
        container.classList.add("subject-class-info", "column");
        container.id = "classes-" + subject.id;

        // create class selection header
        const selectionHeader = document.createElement("div");
        selectionHeader.classList.add("class-selection");

        const title = document.createElement("h4");
        title.innerHTML = "Select a class";

        const exitButton = document.createElement("sl-button");
        exitButton.id = "exit-class-selector-" + subject.id;
        exitButton.variant = "default";
        exitButton.size = "small";
        exitButton.circle = true;

        exitButton.addEventListener('click',
            function() {
                // go to class selection
                switchSelector('none','flex',subject.id,'none')
        });

        const icon = document.createElement("sl-icon");
        icon.classList.add("icon");
        icon.name = "x-lg";
        icon.label = "Exit";

        exitButton.appendChild(icon);

        selectionHeader.appendChild(title);
        selectionHeader.appendChild(exitButton);

        // create column for class info
        const classInfoColumn = document.createElement("div");
        classInfoColumn.classList.add("column");

        for(let classNum in subject.classes){
            const classCard = createClassCard(subject.id ,subject.name, subject.classes[classNum])
            classInfoColumn.append(classCard)
        }


        // append selection header and class info column to container
        container.appendChild(selectionHeader);
        container.appendChild(classInfoColumn);

        return container;
    }

    const subjectList = document.getElementById('subject-list')
    const classList = document.getElementById('class-list')

    // set actions for select subject and select class buttons
    for(let subjectNum in subjectClasses){
        const card = createSubjectCard(subjectList, subjectClasses[subjectNum]);
        subjectList.appendChild(card)

        const subjectClassCards =  createSubjectClassInfo(subjectClasses[subjectNum])
        classList.appendChild(subjectClassCards)
    }

</script>

</html>
