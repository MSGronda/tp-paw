
function nextStep() {
    let currentStepValue = getCurrentStep();
    let currentStep = document.getElementById("step" + currentStepValue);
    currentStep.style.display = "none";

    let nextStep = document.getElementById("step" + ( currentStepValue + 1));
    nextStep.style.display = "block";
}

function previousStep() {
    let currentStepValue = getCurrentStep();
    // Hide the current step
    let currentStep = document.getElementById("step" + currentStepValue);
    currentStep.style.display = "none";

    // Show the previous step
    let previousStep = document.getElementById("step" + (currentStepValue - 1));
    previousStep.style.display = "block";
}

function getCurrentStep() {
    for (let i = 1; i <= 2; i++) {
        let step = document.getElementById("step" + i);
        if (step.style.display !== "none") {
            return i;
        }
    }
}

const dialog = document.querySelector('.dialog-header-actions');
const openButton = document.querySelector('#open-button')
const closeButton = dialog.querySelector('sl-button[slot="footer"]');

openButton.addEventListener('click', () => dialog.show());

const dialog2 = document.querySelector('.dialog-header-actions2');
const openButton2 = document.querySelector('#open-button2');

openButton2.addEventListener('click', () => dialog2.show());

const dialog3 = document.querySelector('.dialog-header-actions3');
const openButton3 = document.querySelector('#open-button3');
const closeButton3 = dialog3.querySelector('sl-button[slot="footer"]');

openButton3.addEventListener('click', () => dialog3.show());
closeButton3.addEventListener('click', () => dialog3.hide());

const dialog4 = document.querySelector('.dialog-header-actions4');

dialog.addEventListener('click', (event) => {
    event.stopPropagation();
});

let requirementList = [];
let reqNameList = [];

let professorList = [];
let professorMap = {

}
let index = 0;

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

let degreeArray = [];
let semesterArray = [];

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

function checkForCorrelatives(){
    const requirementInput = document.getElementById('requirement');

    const degrees = document.getElementById('degreeIds-hiddenInput').value;
    requirementInput.disabled = !(
        degrees.length > 2
    )

}

let classCodeList = [];
let classProfList = [];
let classDayList = [];
let classStartTimeList = [];
let classEndTimeList = [];
let classBuildingList = [];
let classRoomList = [];
let classModeList = [];

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

let classToUpdate = -1;
function editClass(classItem, index){
    let options = 0;
    document.getElementById('class-professors4').innerHTML = '';
    let classProfessorsSlSelect =  document.getElementById('class-professors4');
    professorList.forEach( (item) => {
        let classProf = document.createElement('sl-option');
        classProf.value=options++;
        classProf.textContent=item;

        classProfessorsSlSelect.appendChild(classProf);
    });


    document.getElementById('class-day4').value = classDayList[index];
    document.getElementById('class-start-time4').value = classStartTimeList[index];
    document.getElementById('class-end-time4').value = classEndTimeList[index];
    document.getElementById('class-mode4').value = classModeList[index];
    document.getElementById('class-building4').value = classBuildingList[index];
    document.getElementById('classroom4').value = classRoomList[index];
    dialog4.show();
    classToUpdate = index;
}

function checkCompleteFieldsForSubmit(){
    const ClassCode = document.getElementById('classCodes-hiddenInput').value;
    document.getElementById('submit-button').disabled = !(
        ClassCode.length > 2
    )
}
