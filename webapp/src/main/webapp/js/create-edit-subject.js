
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

let classCodeList = [];
let classProfList = [];
let classDayList = [];
let classStartTimeList = [];
let classEndTimeList = [];
let classBuildingList = [];
let classRoomList = [];
let classModeList = [];
let requirementList = [];
let reqNameList = [];
let professorList = [];
let degreeArray = [];
let semesterArray = [];
let index = 0;
let updatedClassIndex = -1;

function checkForCorrelatives(){
    const requirementInput = document.getElementById('requirement');

    const degrees = document.getElementById('degreeIds-hiddenInput').value;
    requirementInput.disabled = !(
        degrees.length > 2
    )

}

function editClass(classItem, index){
    updatedClassIndex = index;

    let options = 0;
    document.getElementById('class-professors4').innerHTML = '';

    document.getElementById('error-message4').innerHTML = '';

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
}

function checkCompleteFieldsForSubmit(){
    const ClassCode = document.getElementById('classCodes-hiddenInput').value;
    document.getElementById('submit-button').disabled = !(
        ClassCode.length > 2
    )
}
