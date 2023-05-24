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
        for(let classNum in subjectClasses[subjectNum].classes){
            subjectClone.children[0].children[1].children[1].addEventListener('click',
                createSubjectDeselectAction(subjectClasses[subjectNum], subjectClasses[subjectNum].classes[classNum])
            )
        }


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

function createSubjectDeselectAction(subject,idClass){
    return function() {
        // modify schedule table
        schedule.removeClass(subject.id)

        // enable subjects that can are now compatible after removing this subject
        enableCompatibleSubjects()

        // unhide subject from subject list
        document.getElementById('subject-card-'+subject.id).style.display = 'block';

        // remove selected subject from selected subject list
        document.getElementById('selected-subject-info-list').removeChild(document.getElementById('selected-class-card-'+subject.id + '-'+idClass))

        // switch selection buttons
        document.getElementById('select-class-'+ subject.id + '-' + idClass).style.display = 'block'

        //update
        updateCreditCounter(-subject.credits)
        updateTimeDemand(-(subject.timeDemand+1))
        updateOverallDifficulty(-(subject.difficulty+1))
    }
}
function _modifyBanners(delta, prefix, scriptVariable){

    overviewStats[scriptVariable] += delta;

    let average = 0;
    if(Object.keys(schedule.chosenSubjectMap).length !== 0){
        average = (overviewStats[scriptVariable] *  (overviewStats.totalCredits/24) ) / Object.keys(schedule.chosenSubjectMap).length
    }

    if(average===0){
        document.getElementById(prefix+'-difficulty-none').style.display = 'flex';
        document.getElementById(prefix+'-difficulty-easy').style.display = 'none';
        document.getElementById(prefix+'-difficulty-medium').style.display = 'none';
        document.getElementById(prefix+'-difficulty-hard').style.display = 'none';
    }
    else if(average > 0 && average <= 1){
        document.getElementById(prefix+'-difficulty-none').style.display = 'none';
        document.getElementById(prefix+'-difficulty-easy').style.display = 'flex';
        document.getElementById(prefix+'-difficulty-medium').style.display = 'none';
        document.getElementById(prefix+'-difficulty-hard').style.display = 'none';
    }
    else if(average > 1 && average <= 2){
        document.getElementById(prefix+'-difficulty-none').style.display = 'none';
        document.getElementById(prefix+'-difficulty-easy').style.display = 'none';
        document.getElementById(prefix+'-difficulty-medium').style.display = 'flex';
        document.getElementById(prefix+'-difficulty-hard').style.display = 'none';
    }
    else if(average > 2 && average <= 3){
        document.getElementById(prefix+'-difficulty-none').style.display = 'none';
        document.getElementById(prefix+'-difficulty-easy').style.display = 'none';
        document.getElementById(prefix+'-difficulty-medium').style.display = 'none';
        document.getElementById(prefix+'-difficulty-hard').style.display = 'flex';
    }
}

function updateCreditCounter(delta){
    overviewStats.totalCredits += delta;
    document.getElementById('number-of-credits').innerText = overviewStats.totalCredits.toString();
}
function updateTimeDemand(delta){
    _modifyBanners(delta,'time','timeDemand');
}

function updateOverallDifficulty(delta){
    _modifyBanners(delta,'overall','overallDifficulty');
}

function createSubjectSelectAction(subId){
    return function() {
        // go to class selection
        switchSelector('flex','none')
        document.getElementById('classes-' + subId).style.display = 'flex';
    }
}
function addSelectedClassToList(subject,classSubject){
    const selected = document.getElementById('selected-subject-info-list');
    const selectedSubjectClass = document.getElementById('class-card-' +subject.id + '-'+classSubject.idClass).cloneNode(true);
    selectedSubjectClass.id = 'selected-class-card-' +subject.id + '-'+classSubject.idClass
    selectedSubjectClass.firstElementChild.firstElementChild.textContent = subject.name + ' - ' + classSubject.idClass

    selectedSubjectClass.firstElementChild.children[1].style.display= 'none'
    selectedSubjectClass.firstElementChild.children[2].style.display= 'flex'

    selectedSubjectClass.firstElementChild.children[2].addEventListener('click', createSubjectDeselectAction(subject, classSubject.idClass));

    selected.appendChild(selectedSubjectClass)
}


function createClassSelectionAction(subject,classSubject){
    return function() {
        // go back to subject selection
        switchSelector('none','flex')
        document.getElementById('classes-' + subject.id).style.display = 'none';

        addSelectedClassToList(subject,classSubject)

        // modify schedule table
        schedule.addClass(subject.id, subject.name, classSubject.classTimes);

        // disable all incompatible classes (already signed up to that subject or it doesn't fit in your schedule)
        disableIncompatibleSubjects();

        // hide subject from subject list
        document.getElementById('subject-card-'+subject.id).style.display = 'none';

        // update
        updateCreditCounter(subject.credits)
        updateTimeDemand((subject.timeDemand+1))
        updateOverallDifficulty((subject.difficulty+1))
    }
}

function exitClassSelectionAction(){
    // go to class selection
    switchSelector('none','flex')
    hideAllClasses();
}

function switchToTableView(){
    switchToView('none','flex');
}
function switchToListView(){
    switchToView('flex','none');
}

function switchToView(a,b){
    // switch selector buttons
    document.getElementById('switch-to-table-button').style.display = a;
    document.getElementById('switch-to-list-button').style.display = b;

    // document.getElementById('choosing-tab').style.display = a;
    document.getElementById('chosen-tab').style.display = a;
    document.getElementById('overview-tab').style.display = a;

    document.getElementById('time-table').style.display = b;
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