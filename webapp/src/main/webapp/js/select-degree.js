let degreeId;

let subjectList = [];

function resetCheckBox(){
    const checkBoxes = document.querySelectorAll('[id^="degree-'  + degreeId + '-year-"]')
    checkBoxes.forEach( e => {e.checked = false; e.selected = false})

    const electiveCheckBox = document.querySelectorAll('[id^="elective-'  + degreeId + '"]')
    electiveCheckBox.forEach( e => {e.checked = false; e.selected = false})
}

function nextStep() {
    const currentStepValue = getCurrentStep();
    const currentStep = document.getElementById("step" + currentStepValue);
    currentStep.style.display = "none";

    const nextStep = document.getElementById("step" + (currentStepValue + 1));
    nextStep.style.display = "block";
}

function previousStep() {
    const currentStepValue = getCurrentStep();
    // Hide the current step
    const currentStep = document.getElementById("step" + currentStepValue);
    currentStep.style.display = "none";

    // Show the previous step
    const previousStep = document.getElementById("step" + (currentStepValue - 1));
    previousStep.style.display = "block";

    resetCheckBox()
}

function getCurrentStep() {
    // Determine the current step based on the displayed component
    for (let i = 1; i <= 3; i++) {
        const step = document.getElementById("step" + i);
        if (step.style.display !== "none") {
            return i;
        }
    }
}

document.getElementById('select-degree').addEventListener('sl-change', updateDegreeSelection);

function createCloseAction(elemId){
    return function (){
        const trees = document.querySelectorAll('[id^="year-tree-"]')
        trees.forEach(e => {
            if(e.id !== elemId){
                e.expanded = false;
                e.selected = false;
            }
        });
    }
}

const trees = document.querySelectorAll('[id^="year-tree-"]')

trees.forEach( e => {
    e.addEventListener('sl-expand', createCloseAction(e.id));
})


function updateDegreeSelection() {
    degreeId = document.getElementById('select-degree').value;

    //es necesario no mostrar todos los otros arboles
    const degreeTitles = document.querySelectorAll('.degree-title');
    degreeTitles.forEach((title) => {
        title.style.display = "none";
    });
    const degreeTrees = document.querySelectorAll('.degree-tree');
    degreeTrees.forEach((tree) => {
        tree.style.display = "none";
    });
    //reiniciar el subjectList
    subjectList = [];

    const nextButton = document.getElementById('nextButton');
    nextButton.disabled = false;

    document.getElementById('degreeIdHiddenInput').value = degreeId;

    const degreeTitle = document.getElementById("degree-" + degreeId);
    degreeTitle.style.display = "block"
    const degreeTree = document.getElementById("tree-" + degreeId);
    degreeTree.style.display = "block";
    const electiveTree = document.getElementById("elective-tree-" + degreeId);
    electiveTree.style.display = "block";


    // Get all year checkboxes
    const yearCheckboxes = document.querySelectorAll('.degree-' + degreeId + '-year-checkbox');

    // Attach event listeners to each year checkbox
    yearCheckboxes.forEach((checkbox) => {
        const year = checkbox.id.split('-')[4]; // Extract the year value from the checkbox ID
        const yearSubjectCheckboxes = document.querySelectorAll('.degree-' + degreeId + '-year-' + year + '-subject');
        yearSubjectCheckboxes.forEach((subjectCheckbox) => {
            //marcar checkbox a materias que ya tenia seleccionadas (usuario viejo)
            if( subjectProgress && subjectProgress[subjectCheckbox.id.split('-')[5]] != null ){
                subjectCheckbox.checked = true;
                if( subjectList.findIndex(element => element === subjectCheckbox.id.split('-')[5]) === -1 ){
                    subjectList.push(subjectCheckbox.id.split('-')[5])
                    document.getElementById('hiddenInput').value = JSON.stringify(subjectList);
                }
                //chequear que estan todas prendidas
                let allSelected = true;
                const yearSubjectCheckboxes = document.querySelectorAll('.degree-' + degreeId + '-year-' + year + '-subject');
                yearSubjectCheckboxes.forEach((subjectCheckbox2) => {
                    if ( !subjectCheckbox2.checked ){
                        allSelected = false;
                    }
                })
                checkbox.checked = allSelected;
            }
            subjectCheckbox.addEventListener('sl-change', function () {
                const checked = document.getElementById(subjectCheckbox.id).checked;
                const subjectID = subjectCheckbox.id.split('-')[5];
                if( !checked ){
                    checkbox.checked = false;
                    let index = subjectList.findIndex(element => element === subjectID);
                    subjectList.splice(index, 1);
                    document.getElementById('hiddenInput').value = JSON.stringify(subjectList);
                }else{
                    if( subjectList.findIndex(element => element === subjectID) === -1 ){
                        subjectList.push(subjectID)
                        document.getElementById('hiddenInput').value = JSON.stringify(subjectList);
                    }
                    //chequear que estan todas prendidas
                    let allSelected = true;
                    const yearSubjectCheckboxes = document.querySelectorAll('.degree-' + degreeId + '-year-' + year + '-subject');
                    yearSubjectCheckboxes.forEach((subjectCheckbox2) => {
                        if ( !subjectCheckbox2.checked ){
                            allSelected = false;
                        }
                    })
                    checkbox.checked = allSelected;
                }
            });
        });

        checkbox.addEventListener('sl-change', function () {
            const year = this.id.split('-')[4]; // Extract the year value from the checkbox ID
            const checked = document.getElementById('degree-' + degreeId + '-year-checkbox-'+year).checked;

            createCloseAction('year-tree-' + degreeId +  '-' + year)();

            // Get all checkboxes within the corresponding year
            const yearSubjectCheckboxes = document.querySelectorAll('.degree-' + degreeId + '-year-' + year + '-subject');

            // Set the checked property of each year subject checkbox
            yearSubjectCheckboxes.forEach((subjectCheckbox) => {
                subjectCheckbox.checked = checked;
                const subjectID = subjectCheckbox.id.split('-')[5];
                if( checked ){
                    if( subjectList.findIndex(element => element === subjectID) === -1 ){
                        subjectList.push(subjectID)
                        document.getElementById('hiddenInput').value = JSON.stringify(subjectList);
                    }

                }else{
                    let index = subjectList.findIndex(element => element === subjectID);
                    subjectList.splice(index, 1);
                    document.getElementById('hiddenInput').value = JSON.stringify(subjectList);
                }
            });
        });
    });

    const electiveCheckboxes = document.querySelectorAll('.elective-' + degreeId +'-subject');

    // Attach event listeners to each year checkbox
    electiveCheckboxes.forEach((checkbox) => {
        if( subjectProgress && subjectProgress[checkbox.id.split('-')[3]] != null ){
            checkbox.checked = true;
            if( subjectList.findIndex(element => element === checkbox.id.split('-')[3]) === -1 ){
                subjectList.push(checkbox.id.split('-')[3])
                document.getElementById('hiddenInput').value = JSON.stringify(subjectList);
            }
        }
        checkbox.addEventListener('sl-change', function () {
            const checked = document.getElementById(checkbox.id).checked;
            const subjectID = checkbox.id.split('-')[3];

            if( !checked ){
                let index = subjectList.findIndex(element => element === subjectID);
                subjectList.splice(index, 1);
                document.getElementById('hiddenInput').value = JSON.stringify(subjectList);
            }else{
                if( subjectList.findIndex(element => element === subjectID) === -1 ){
                    subjectList.push(subjectID)
                    document.getElementById('hiddenInput').value = JSON.stringify(subjectList);
                }
            }
        });
    });
}