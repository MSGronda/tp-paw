var degreeId;

var subjectList = [];

function nextStep() {
    var currentStepValue = getCurrentStep();
    var currentStep = document.getElementById("step" + currentStepValue);
    currentStep.style.display = "none";

    var nextStep = document.getElementById("step" + ( currentStepValue + 1));
    nextStep.style.display = "block";
}

function previousStep() {
    var currentStepValue = getCurrentStep();
    // Hide the current step
    var currentStep = document.getElementById("step" + currentStepValue);
    currentStep.style.display = "none";

    // Show the previous step
    var previousStep = document.getElementById("step" + (currentStepValue - 1));
    previousStep.style.display = "block";
}

function getCurrentStep() {
    // Determine the current step based on the displayed component
    for (var i = 1; i <= 3; i++) {
        var step = document.getElementById("step" + i);
        if (step.style.display !== "none") {
            return i;
        }
    }
}

document.getElementById('select-degree').addEventListener('sl-change', updateDegreeSelection);

function updateDegreeSelection() {
    degreeId = document.getElementById('select-degree').value;

    var nextButton = document.getElementById('nextButton');
    nextButton.disabled = false;

    document.getElementById('degreeIdHiddenInput').value = degreeId;

    var degreeTitle = document.getElementById("degree-"+degreeId);
    degreeTitle.style.display = "block"
    var degreeTree = document.getElementById("tree-" + degreeId);
    degreeTree.style.display = "block";
    var electiveTree = document.getElementById("elective-tree-"+degreeId);
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
                console.log(subjectList);
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
        if( subjectProgress && subjectProgress[checkbox.id.split('-')[5]] != null ){
            checkbox.checked = true;
            if( subjectList.findIndex(element => element === checkbox.id.split('-')[5]) === -1 ){
                subjectList.push(checkbox.id.split('-')[5])
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