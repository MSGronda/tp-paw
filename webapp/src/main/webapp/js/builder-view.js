function sortByCreditsDesc(a,b){
    return b.credits - a.credits
}
function sortByCreditsAsc(a,b){
    return  a.credits-b.credits
}
function orderByCreditAction() {
    let sorter;
    if (currentOrder === 'creditsDesc') {
        sorter = sortByCreditsAsc
        document.getElementById('credits-down').style.display = 'none'
        document.getElementById('credits-up').style.display = 'flex'
        currentOrder = 'creditsAsc'
    } else {
        sorter = sortByCreditsDesc
        document.getElementById('credits-down').style.display = 'flex'
        document.getElementById('credits-up').style.display = 'none'
        currentOrder = 'creditsDesc'
    }
    subjectClasses.sort(sorter)
    rebuildSubjectList()
}
function rebuildSubjectList(){
    const subjectList = document.getElementById('subject-list');
    let elements = document.createDocumentFragment();

    for(let subjectNum in subjectClasses){
        // shallow clone the element
        const subjectClone = document.getElementById('subject-card-' + subjectClasses[subjectNum].id).cloneNode(true);

        // we must re add event listener functions as they are lost during the cloning
        subjectClone.children[0].children[1].children[0].addEventListener('click',
            createSubjectSelectAction(subjectClasses[subjectNum].id)
        )
        subjectClone.children[0].children[1].children[1].addEventListener('click',
            createSubjectDeselectAction(subjectClasses[subjectNum].id)
        )

        elements.appendChild(subjectClone);
    }

    subjectList.innerHTML = null;
    subjectList.appendChild(elements);
}

function switchSelector(chooseClassVisibility, chooseSubjectVisibility){
    document.getElementById('choose-class').style.display = chooseClassVisibility;
    document.getElementById('choose-subject').style.display = chooseSubjectVisibility;

}
function hideAllClasses(){
    for(let subNum in subjectClasses){
        document.getElementById('classes-' + subjectClasses[subNum].id).style.display = 'none';
    }
}

function alterSubjectCard(subjectId,color,colorBorder, disabled){
    const card = document.getElementById('subject-card-'+subjectId);
    card.style.color = color
    card.style.setProperty('--border-color', colorBorder)
    const select = document.getElementById('select-'+subjectId);
    select.disabled = disabled;
}
function alterClassCard(subjectId, classId, color,colorBorder, disabled){
    const card = document.getElementById('class-card-'+subjectId+'-'+classId);
    card.style.color = color
    card.style.setProperty('--border-color', colorBorder)
    const select = document.getElementById('select-class-'+subjectId+'-'+classId);
    select.disabled = disabled;
}

function disableIncompatibleSubjects(){
    for(let subNum in subjectClasses){
        if(schedule.signedUpToClass(subjectClasses[subNum].id)){
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
        if(schedule.signedUpToClass(subjectClasses[subNum].id))
            continue;

        let anyClassCompatible = false;
        for(let clNum in subjectClasses[subNum].classes){
            const classCompatibility = schedule.canAddClass(subjectClasses[subNum].classes[clNum].classTimes);

            if(classCompatibility === true){
                // class is now compatible with timetable
                alterClassCard( subjectClasses[subNum].id,subjectClasses[subNum].classes[clNum].idClass, normalColor,normalBorderColor, false )
            }
            anyClassCompatible = anyClassCompatible || classCompatibility;
        }

        // at least one of the classes is compatible with timetable => enable subject as well
        if(anyClassCompatible){
            alterSubjectCard(subjectClasses[subNum].id,normalColor,normalBorderColor,false );
        }
    }
}

function createSubjectDeselectAction(subId,idClass){
    return function() {
        // modify schedule table
        schedule.removeClass(subId)

        // enable subjects that can are now compatible after removing this subject
        enableCompatibleSubjects()

        // unhide subject from subject list
        document.getElementById('subject-card-'+subId).style.display = 'block';

        // remove selected subject from selected subject list
        document.getElementById('selected-subject-info-list').removeChild(document.getElementById('selected-class-card-'+subId + '-'+idClass))

        // switch selection buttons
        document.getElementById('select-class-'+ subId + '-' + idClass).style.display = 'block'
    }
}

function createSubjectSelectAction(subId){
    return function() {
        // go to class selection
        switchSelector('flex','none')
        document.getElementById('classes-' + subId).style.display = 'flex';
    }
}
function addSelectedClassToList(subId, subName,subClass){
    const selected = document.getElementById('selected-subject-info-list');
    const selectedSubjectClass = document.getElementById('class-card-' +subId + '-'+subClass.idClass).cloneNode(true);
    selectedSubjectClass.id = 'selected-class-card-' +subId + '-'+subClass.idClass
    selectedSubjectClass.firstElementChild.firstElementChild.textContent = subName + ' - ' + subClass.idClass

    selectedSubjectClass.firstElementChild.children[1].style.display= 'none'
    selectedSubjectClass.firstElementChild.children[2].style.display= 'flex'

    selectedSubjectClass.firstElementChild.children[2].addEventListener('click', createSubjectDeselectAction(subId, subClass.idClass));

    selected.appendChild(selectedSubjectClass)
}


function createClassSelectionAction(subId, subName,subClass){
    return function() {
        // go back to subject selection
        switchSelector('none','flex')
        document.getElementById('classes-' + subId).style.display = 'none';

        addSelectedClassToList(subId, subName,subClass)

        // modify schedule table
        schedule.addClass(subId, subName, subClass.classTimes);

        // disable all incompatible classes (already signed up to that subject or it doesn't fit in your schedule)
        disableIncompatibleSubjects();

        // hide subject from subject list
        document.getElementById('subject-card-'+subId).style.display = 'none';
    }
}

function exitClassSelectionAction(){
    // go to class selection
    switchSelector('none','flex')
    hideAllClasses();
}


function downloadTable(csvName) {
    const csv_data = schedule.generateCsv();

     const CSVFile = new Blob([csv_data], {
        type: "text/csv"
    });

    // Create to temporary link
    const temp_link = document.createElement('a');

    temp_link.download = csvName;
    temp_link.href = window.URL.createObjectURL(CSVFile);
    temp_link.style.display = "none";

    document.body.appendChild(temp_link);

    // download csv
    temp_link.click();

    document.body.removeChild(temp_link);
}