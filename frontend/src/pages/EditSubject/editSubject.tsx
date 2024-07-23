import {useTranslation} from "react-i18next";
import {useNavigate, useParams} from "react-router-dom";
import {IconInfoCircle, IconPencil, IconX} from "@tabler/icons-react";
import {useEffect, useRef, useState} from "react";
import Class from "../../models/Class.ts";
import {Degree} from "../../models/Degree.ts";
import {Subject} from "../../models/Subject.ts";
import {Professor} from "../../models/Professor.ts";
import {degreeService, departmentService, professorService, subjectService} from "../../services";
import {handleService} from "../../handlers/serviceHandler.tsx";
import {
    ActionIcon,
    Alert,
    Button,
    Combobox,
    ComboboxOption,
    ComboboxOptions,
    Flex,
    Grid,
    InputBase,
    Modal, MultiSelect, NumberInput, Pagination, rem, Table, Tabs, Textarea, TextInput,
    useCombobox
} from "@mantine/core";
import ClassTime from "../../models/ClassTime.ts";
import classes from "../CreateSubject/createsubject.module.css";
import Title from "../../components/title/title.tsx";
import ChooseSubjectCard from "../../components/choose-subject-card/choose-subject-card.tsx";
import {TimeInput} from "@mantine/dates";
import {Navbar} from "../../components/navbar/navbar.tsx";


export function EditSubject() {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const params = useParams();

    const MINIMUM_CREDITS = 0;
    const MAXIMUM_CREDITS = 12;
    const SUBJECT_ID_REGEX = "[0-9]{2}\\.[0-9]{2}";
    const CLASS_NAME_REGEX = "[A-Za-z]+";
    const icon = <IconInfoCircle />;

    const classDays = [
        t("CreateSubject.day1"),
        t("CreateSubject.day2"),
        t("CreateSubject.day3"),
        t("CreateSubject.day4"),
        t("CreateSubject.day5"),
        t("CreateSubject.day6"),
        t("CreateSubject.day7"),
    ];
    const weekDaysMap = new Map<string,number>();
    classDays.forEach((clas, index) => weekDaysMap.set(clas, index));
    //const availableCreditsPerClass = new Map<string,number>();
    const [availableCreditsPerClass, setAvailableCreditsPerClass] = useState<Map<string,number>>(new Map());
    const [carreerSemester, setCarreerSemester] = useState<Map<string, string[]>>(new Map());
    const [fetchedSelectedDegrees, setFetchedSelectedDegrees] = useState<boolean>(false);

    // UI components states
    const [activeTab, setActiveTab] = useState<string | null>("general-info");
    const [openedDegreeModal, setOpenedDegreeModal] = useState(false);
    const [openedPrereqModal, setOpenedPrereqModal] = useState(false);
    const [openedProfessorModal, setOpenedProfessorModal] = useState(false);
    const [openedClassModal, setOpenedClassModal] = useState(false);
    const [openedClassEditModal, setOpenedClassEditModal] = useState(false);
    const [openedClassTimeModal, setOpenedClassTimeModal] = useState(false);
    const [currentPrereqPage, setCurrentPrereqPage] = useState<number>(1);
    const [maxPage, setMaxPage] = useState(2);

    // Error related
    const [missingClassTimeFields, setMissingClassTimeFields] = useState(false);
    const [missingClassFields, setMissingClassFields] = useState(false);
    const [wrongPatternInSubjectId, setWrongPatternInSubjectId] = useState(false);
    const [emptySubjectName, setEmptySubjectName] = useState(false);
    const [errorAlert, setErrorAlert] = useState(false);
    const [errorMessageTitle, setErrorMessageTitle] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [usedProfessorName, setUsedProfessorName] = useState(false);

    // Form related states
    const [department, setDepartment] = useState<string>("");
    const [subjectName, setSubjectName] = useState<string>("");
    const [subjectId, setSubjectId] = useState<string>("");
    const [credits, setCredits] = useState<number>(MINIMUM_CREDITS);
    const [selectedDegrees, setSelectedDegrees] = useState<number[]>([]);
    const [selectedSemesters, setSelectedSemesters] = useState<number[]>([]);
    const [selectedPrereqs, setSelectedPrereqs] = useState<number[]>([]);
    const [selectedProfessors, setSelectedProfessors] = useState<string[]>([]);
    const [createdProfessors, setCreatedProfessors] = useState<string[]>([]);
    const [selectedClasses, setSelectedClasses] = useState<Class[]>([]);

    // Fetched Values
    const [degrees, setDegrees] = useState<Degree[]>([]);
    const [subjects, setSubjects] = useState<Subject[]>([]);
    const [professors, setProfessors] = useState<Professor[]>([]);
    const [departments, setDepartments] = useState<string[]>([]);
    const [subject, setSubject] = useState<Subject>();

    // Current selected values
    const [currentDegree, setCurrentDegree] = useState<number>();
    const [currentSemester, setCurrentSemester] = useState<string>("");
    const [currentSemesterOptions, setCurrentSemesterOptions] = useState<JSX.Element[]>([]);
    const [currentProfessorCreation, setCurrentProfessorCreation] = useState<string>("");
    const [currentClassName, setCurrentClassName] = useState<string>("");
    const [currentClassProfessors, setCurrentClassProfessors] = useState<string[]>([]);
    const [currentClassEditName, setCurrentClassEditName] = useState<string>("");
    const [currentClassEditProfessors, setCurrentClassEditProfessors] = useState<string[]>([]);
    const [currentClassTimeDay, setCurrentClassTimeDay] = useState<string>("");
    const startTimeRef = useRef<HTMLInputElement>(null);
    const endTimeRef = useRef<HTMLInputElement>(null);
    const [currentClassTimeBuilding, setCurrentClassTimeBuilding] = useState<string>("");
    const [currentClassTimeMode, setCurrentClassTimeMode] = useState<string>("");
    const [currentClassTimeClassroom, setCurrentClassTimeClassroom] = useState<string>("");
    const [currentClassSelected, setCurrentClassSelected] = useState<Class>();

    const searchSubjects = async (page: number) => {
        const res = await subjectService.getSubjects(page);
        const data = handleService(res, navigate);
        if (res) {
            setSubjects(data);
            setMaxPage(res.maxPage || 1);
            
            try {
                setDepartments(await departmentService.getDepartments());
            } catch (e) { 
                console.error("Unable to get departments: ", e); 
            }
        }
    }

    const searchSubjectById = async (id: string) => {
        const res = await subjectService.getSubjectById(id);
        const data = handleService(res,navigate);
        if (res) {
            setSubject(data);
        }
    }

    const searchDegrees = async () => {
        const res = await degreeService.getDegrees();
        const data = handleService(res, navigate);
        if (res) {
            setDegrees(data);
        }
    }

    const searchDegreesForSubject = async (subjectId: string) => {
        const res = await degreeService.getDegrees(subjectId);
        const data = handleService(res, navigate);
        if (res) {
            setSelectedDegrees(data.map((degree: Degree) => degree.id));
        }
    }

    const searchSemesters = async (degreeIds: number[], subjectId: string) => {
        for await(const degreeId of degreeIds) {
            const res = await degreeService.getSemesters(degreeId, subjectId);
            const data = handleService(res, navigate);
            if(res && data) {
                setSelectedSemesters([...selectedSemesters, data.semester]);
            }
        }
    }

    const handleSearchProfessors = async (value: string) => {
        if(value.length > 1) {
            const res = await professorService.getProfessors(undefined, undefined, value);
            if(res.status == 200 && res.data && res.data != ""){
                setProfessors(res.data);
            }
        }
    }

    const editSubject = async(subjectId: string, subjectName: string, department: string, credits: number, selectedDegrees: number[],
                              selectedSemesters: number[], prereqs: string[], professors: string[], selectedClasses: any[]) => {
        const res = await subjectService.editSubject(subjectId, subjectName, department, credits, selectedDegrees, selectedSemesters, prereqs, professors, selectedClasses);
        if(res){
            if(res.status === 200){// 200 updated
                navigate('/subject/' + subjectId)
            } else {
                // Error message
            }
        }
    }

    const createProfessor = async (professorName: string) => {
        const res = await professorService.createProfessor(professorName);
        if(res.status == 201){
            //success message
            setOpenedProfessorModal(false);
            setUsedProfessorName(false);
            setCurrentProfessorCreation("");
        } else {
            // error message
            setUsedProfessorName(true);
        }
    }

    useEffect(() => {
        searchSubjects(currentPrereqPage);
    }, [selectedDegrees, currentPrereqPage]);

    useEffect(() => {
        searchDegrees();
    }, []);

    useEffect(() => {
        if(subjectId.length !== 0){
            searchDegreesForSubject(subjectId);
        }
    }, [subjectId]);

    useEffect(() => {
        if(subjectId.length !== 0 && selectedDegrees.length !== 0 && !fetchedSelectedDegrees){
            searchSemesters(selectedDegrees, subjectId);
        }
    }, [subjectId, selectedDegrees]);

    useEffect(() => {
        if(params.id !== undefined) {
            searchSubjectById(params.id);
        }
    }, [params.id]);

    useEffect(() => {
        if(subject !== undefined) {
            setDepartment(subject.department);
            setSubjectName(subject.name);
            setSubjectId(subject.id);
            setCredits(subject.credits);
            // Hacer request para obtener los cuatris degrees donde estan
            setSelectedPrereqs(subject.prerequisites.map((prereq) => Number(prereq)));
            const profs :string[] = [];
            for(const clas of subject.classes){
                for(const prof of clas.professors) {
                    if(!profs.includes(prof)){
                        profs.push(prof);
                    }
                }
            }
            setCreatedProfessors(profs);
            setSelectedProfessors(profs);
            setSelectedClasses(subject.classes);
        }
    }, [subject]);

    useEffect(() => {
        for (let i = 0; i < degrees.length; i++) {
            carreerSemester.set(degrees[i].id.toString(), []);
            if(degrees[i].semesterSubjects) {
                for (let j = 1; j < degrees[i].semesterSubjects.length; j++) {
                    carreerSemester.get(degrees[i].id.toString())?.push(t("CreateSubject.semesterOption", { number: j }));
                }
            }
            carreerSemester.get(degrees[i].id.toString())?.push(t("CreateSubject.elective"));
        }
        setCarreerSemester(new Map(carreerSemester));
    }, [degrees]);

    useEffect(() => {
        if(carreerSemester != undefined && currentDegree != undefined && carreerSemester.get(currentDegree.toString()) != undefined) {
            setCurrentSemesterOptions(carreerSemester.get(currentDegree.toString()).map((semester) => (
                <ComboboxOption key={semester} value={semester}>
                    {semester}
                </ComboboxOption>
            )));
        }
    }, [currentDegree, carreerSemester]);

    // Degrees/Semester Utils
    function searchForDegreeId(degreeName: string) {
        for(let i = 0; i < degrees.length; i++) {
            if(degrees[i].name === degreeName) {
                return degrees[i].id;
            }
        }
        return 0;
    }

    function searchForDegreeName(degreeId: number) {
        for(let i = 0; i < degrees.length; i++) {
            if(degrees[i].id === degreeId) {
                return degrees[i].name;
            }
        }
        return "";
    }

    function extractNumberFromSemesterName(semesterName: string) {
        return Number(semesterName.match(RegExp('[0-9][0-9]?'))) != 0 ? Number(semesterName.match(RegExp('[0-9][0-9]?'))) : -1
    }

    // Week day and time formats
    function extractNumberFromWeekDay(weekDay: string) {
        return weekDaysMap.get(weekDay) || 0;
    }

    function extractHoursFromTimeStamp(timestamp: string) {
        return Number(timestamp.slice(0,2));
    }

    function timeStringToMinutes(time: string): number {
        const [hours, minutes] = time.split(':').map(Number);
        return hours * 60 + minutes;
    }

    function calculateHoursDifference(startTime: string, endTime: string): number {
        const minutes1 = timeStringToMinutes(startTime);
        const minutes2 = timeStringToMinutes(endTime);
        return Math.abs(minutes2 - minutes1) / 60;
    }

    // Handlers
    function handleNewSubjectId(id: string){
        setSubjectId(id);
        if(id.match(SUBJECT_ID_REGEX)){
            setWrongPatternInSubjectId(false);
            return;
        }
        setWrongPatternInSubjectId(true);
    }

    function handleSelectedDegreeSemesters() {
        let index;
        if((index = selectedDegrees.indexOf(currentDegree)) !== -1) {
            selectedDegrees[index] = currentDegree;
            selectedSemesters[index] = extractNumberFromSemesterName(currentSemester);
        } else {
            setSelectedDegrees((selectedDegrees) => [...selectedDegrees, currentDegree]);
            setSelectedSemesters((selectedSemesters) => [...selectedSemesters, extractNumberFromSemesterName(currentSemester)]);
        }
        setOpenedDegreeModal(false);
    }

    function handleRemoveSelectedDegree(degreeId: number) {
        const index = selectedDegrees.indexOf(degreeId);
        setSelectedDegrees([...selectedDegrees.slice(0,index), ...selectedDegrees.slice(index + 1)]);
        setSelectedSemesters([...selectedSemesters.slice(0,index), ...selectedSemesters.slice(index + 1)]);
    }

    function handlePrereqSelection(subjectId: string) {
        if(!selectedPrereqs.includes(Number(subjectId))) {
            setSelectedPrereqs([...selectedPrereqs, Number(subjectId)]);
        }
    }

    function handlePrereqRemoval(subjectId: string) {
        if(selectedPrereqs.includes(Number(subjectId))) {
            const index = selectedPrereqs.indexOf(Number(subjectId));
            setSelectedPrereqs([...selectedPrereqs.slice(0,index), ...selectedPrereqs.slice(index + 1)]);
        }
    }

    function isPrereqSelected(subjectId: string) {
        return selectedPrereqs.includes(Number(subjectId));
    }

    function handleProfessorCreation() {
        createProfessor(currentProfessorCreation);
    }

    function handleRemoveCreatedProfessor(professor: string) {
        let index;
        if( ( index = createdProfessors.indexOf(professor)) != -1) {
            setCreatedProfessors([...createdProfessors.slice(0,index), ...createdProfessors.slice(index + 1)]);
        }
        let selectedIndex;
        if( (selectedIndex = selectedProfessors.indexOf(professor)) != -1) {
            setSelectedProfessors([...selectedProfessors.slice(0,selectedIndex), ...selectedProfessors.slice(index + 1)]);
        }
    }

    function handleProfessorRemove(professor: string) {
        let index;
        if( ( index = selectedProfessors.indexOf(professor)) != -1) {
            setSelectedProfessors([...selectedProfessors.slice(0,index), ...selectedProfessors.slice(index + 1)]);
        }
    }

    function handleProfessorAddition(professor: string) {
        if(!selectedProfessors.includes(professor)){
            setSelectedProfessors([...selectedProfessors, professor]);
        }
    }

    function handleClassCreation() {
        const newClass = {idClass: currentClassName, idSubject: subjectId, locations: [], professors: currentClassProfessors};

        if(currentClassName === "" || currentClassProfessors.length === 0 || !currentClassName.match(CLASS_NAME_REGEX)) {
            setMissingClassFields(true);
            return;
        }

        for(const clas of selectedClasses) {
            if(clas.idClass === currentClassName){
                setMissingClassFields(true);
                return;
            }
        }
        availableCreditsPerClass.set(newClass.idClass, credits);
        setAvailableCreditsPerClass(new Map<string,number>(availableCreditsPerClass));
        setSelectedClasses([...selectedClasses, newClass]);
        setCurrentClassName("");
        setCurrentClassProfessors([]);
        setMissingClassFields(false);
        setOpenedClassModal(false);
    }

    function handleRemoveClass(clas: Class) {
        let index;
        if( (index = selectedClasses.indexOf(clas) ) != -1) {
            availableCreditsPerClass.delete(clas.idClass);
            setAvailableCreditsPerClass(new Map<string,number>(availableCreditsPerClass))
            setSelectedClasses([...selectedClasses.slice(0,index), ...selectedClasses.slice(index + 1)]);
        }
    }

    function handleClassEditModal(clas: Class) {
        setCurrentClassEditName(clas.idClass);
        setCurrentClassEditProfessors(clas.professors);
        let index;
        if( (index = selectedClasses.indexOf(clas) ) != -1 ){
            setSelectedClasses([...selectedClasses.slice(0,index), ...selectedClasses.slice(index + 1)]);
        }
        setOpenedClassEditModal(true);
    }

    function handleClassEdit() {
        const newClass = {idClass: currentClassEditName, idSubject: subjectId, locations: [], professors: currentClassEditProfessors};
        if(currentClassEditProfessors.length === 0 || currentClassEditName === ""){
            setMissingClassFields(true);
            return;
        }
        let index;
        if( (index = selectedClasses.indexOf(newClass)) != -1) {
            setSelectedClasses([...selectedClasses, newClass]);
        } else {
            setSelectedClasses([...selectedClasses.slice(0,index), newClass, ...selectedClasses.slice(index + 1)]);
        }
        availableCreditsPerClass.set(newClass.idClass, credits);
        setAvailableCreditsPerClass(new Map<string,number>(availableCreditsPerClass));
        setCurrentClassEditName("");
        setCurrentClassEditProfessors([]);
        setMissingClassFields(false);
        setOpenedClassEditModal(false);
    }

    function handleOpenClassTimeModalCreation(clas: Class) {
        setCurrentClassSelected(clas);
        setOpenedClassTimeModal(true);
    }

    function handleClassTimeCreation() {
        let actualCredits;
        if (currentClassSelected){
            if(availableCreditsPerClass.get(currentClassSelected.idClass) === undefined){
                availableCreditsPerClass.set(currentClassSelected.idClass, credits);
                setAvailableCreditsPerClass(new Map<string,number>(availableCreditsPerClass));
            }
            actualCredits = availableCreditsPerClass.get(currentClassSelected.idClass);
        }
        if( currentClassTimeDay === "" || currentClassTimeClassroom === "" || currentClassTimeMode === "" || currentClassTimeBuilding === "" ||
            startTimeRef.current == undefined || endTimeRef.current == undefined || actualCredits == undefined ||
            calculateHoursDifference(startTimeRef.current.value, endTimeRef.current.value) > actualCredits) {
            setMissingClassTimeFields(true);
            return;
        }
        const newClassTime = {day: extractNumberFromWeekDay(currentClassTimeDay), startTime: startTimeRef.current.value,
            endTime: endTimeRef.current.value, mode:currentClassTimeMode, building: currentClassTimeBuilding, location: currentClassTimeClassroom}
        if(currentClassSelected && !currentClassSelected.locations.includes(newClassTime)){
            const creditsLeft = calculateHoursDifference(startTimeRef.current.value, endTimeRef.current.value);
            availableCreditsPerClass.set(currentClassSelected.idClass, actualCredits - creditsLeft);
            setAvailableCreditsPerClass(new Map<string,number>(availableCreditsPerClass))
            currentClassSelected?.locations.push(newClassTime);
        }
        setMissingClassTimeFields(false);
        setCurrentClassTimeDay("");
        setCurrentClassTimeBuilding("");
        setCurrentClassTimeMode("");
        setCurrentClassTimeClassroom("");
        startTimeRef.current.value = "";
        endTimeRef.current.value = "";
        setOpenedClassTimeModal(false);
    }

    function handleRemoveClassTime(classTime : ClassTime, clas: Class) {
        const creditsGained =  (extractHoursFromTimeStamp(classTime.endTime) - extractHoursFromTimeStamp(classTime.startTime));
        let actualCredits;
        if( (actualCredits = availableCreditsPerClass.get(clas.idClass)) !== undefined )
            availableCreditsPerClass.set(clas.idClass, actualCredits + creditsGained );
        setAvailableCreditsPerClass(new Map<string,number>(availableCreditsPerClass))
        const index = clas.locations.indexOf(classTime);
        if(index > -1){
            clas.locations.splice(index, 1);
            let classIndex;
            if((classIndex = selectedClasses.indexOf(clas)) != -1){
                setSelectedClasses([...selectedClasses.slice(0,classIndex), clas ,...selectedClasses.slice(classIndex + 1)]);
            }
        }
    }

    function handleCreditChange(credit: number){
        setCredits(credit);
    }

    function handleErrorAlert(errorMessage: string){
        setErrorAlert(true);
        setErrorMessageTitle(t("CreateSubject.missingFields"));
        setErrorMessage(errorMessage);
    }

    function handleSubjectEdit(){
        // Check that there are no missing values
        if(wrongPatternInSubjectId){
            handleErrorAlert(t("CreateSubject.wrongSubjectId"));
            return;
        }
        setWrongPatternInSubjectId(false);
        if(subjectName.length == 0){
            setEmptySubjectName(true);
            handleErrorAlert(t("CreateSubject.emptySubjectNameError"));
            return;
        }
        setEmptySubjectName(false);

        if(department.length == 0){
            handleErrorAlert(t("CreateSubject.emptyDepartment"));
            return;
        }
        if(selectedDegrees.length == 0 || selectedDegrees.length != selectedSemesters.length){
            handleErrorAlert(t("CreateSubject.emptyDegree"));
            return;
        }
        if(selectedClasses.length == 0){
            handleErrorAlert(t("CreateSubject.emptyClasses"));
        }
        for(const clas of selectedClasses){
            let actualCredits = 0;
            clas.locations.forEach((classTime: ClassTime) => {
                actualCredits += calculateHoursDifference(classTime.startTime, classTime.endTime);
            });
            if(actualCredits != credits){
                handleErrorAlert(t("CreateSubject.wrongCredits"))
                return;
            }
        }

        // Merge created professors
        setSelectedProfessors([...selectedProfessors, ...createdProfessors]);

        // Cast prereqs to strings
        const prereqs = selectedPrereqs.map((prereq: number) => prereq.toString());

        const subjectClasses = [];
        for(const clas of selectedClasses){
            subjectClasses.push({code: clas.idClass, professors: clas.professors,
                classTimes: clas.locations});
        }

        editSubject(subjectId, subjectName, department, credits, selectedDegrees, selectedSemesters, prereqs, selectedProfessors, subjectClasses);
    }

    // Combobox Options
    const deaprtmentOptions = departments.map((departament) => (
        <Combobox.Option key={departament} value={departament}>
            {departament}
        </Combobox.Option>
    ));
    const degreesOptions = degrees.map((degree) => (
        <ComboboxOption key={degree.id} value={degree.name}>
            {degree.name}
        </ComboboxOption>
    ));
    const classDayOptions = classDays.map((classDay) => (
        <ComboboxOption key={classDay} value={classDay}>
            {classDay}
        </ComboboxOption>
    ));

    const comboboxDepartment = useCombobox({
        onDropdownClose: () => comboboxDepartment.resetSelectedOption(),
    });
    const comboboxDegree = useCombobox({
        onDropdownClose: () => comboboxDegree.resetSelectedOption(),
    });
    const comboboxSemester = useCombobox({
        onDropdownClose: () => comboboxSemester.resetSelectedOption(),
    });
    const comboboxPrerequisiste = useCombobox({
        onDropdownClose: () => comboboxPrerequisiste.resetSelectedOption(),
    });
    const comboboxProfessor = useCombobox({
        onDropdownClose: () => comboboxProfessor.resetSelectedOption(),
    });
    const comboboxClassProfessor = useCombobox({
        onDropdownClose: () => comboboxClassProfessor.resetSelectedOption(),
    });
    const comboboxClassDays = useCombobox({
        onDropdownClose: () => comboboxClassDays.resetSelectedOption(),
    });
    return (
        <div className={classes.background}>
        <Title text={t("CreateSubject.editSubjectTitle")}/>

    { /* Degree Modal */}
    <Modal opened={openedDegreeModal} onClose={() => setOpenedDegreeModal(false)} title={t("CreateSubject.addDegree")} size="35%">
        <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
            {t("CreateSubject.degreeModalDegree")}
            <Combobox store={comboboxDegree} onOptionSubmit={(value) => {
                setCurrentDegree(searchForDegreeId(value));
                comboboxDegree.closeDropdown();
            }}>
                <Combobox.Target>
                    <InputBase className={classes.degreeDropdown} component="button" type="button" pointer rightSection={<Combobox.Chevron />}
                               rightSectionPointerEvents="none" onClick={() => comboboxDegree.toggleDropdown()}>
                        {searchForDegreeName(currentDegree)}
                    </InputBase>
                </Combobox.Target>
                <Combobox.Dropdown>
                    <ComboboxOptions>{degreesOptions}</ComboboxOptions>
                </Combobox.Dropdown>
            </Combobox>
        </Flex>
        <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
            {t("CreateSubject.semester")}
            <Combobox store={comboboxSemester}
                      onOptionSubmit={(value) => {
                          setCurrentSemester(value);
                          comboboxSemester.closeDropdown();
                      }}>
                <Combobox.Target>
                    <InputBase className={classes.degreeDropdown} component="button" type="button" pointer rightSection={<Combobox.Chevron />}
                               rightSectionPointerEvents="none" onClick={() => comboboxSemester.toggleDropdown()}>
                        { currentSemester }
                    </InputBase>
                </Combobox.Target>
                <Combobox.Dropdown>
                    <ComboboxOptions>{currentSemesterOptions}</ComboboxOptions>
                </Combobox.Dropdown>
            </Combobox>
        </Flex>
        <Flex mih={50} gap="xl" justify="center" align="center" direction="row" wrap="wrap">
            <Button color="green" onClick={() => handleSelectedDegreeSemesters()}>{t("CreateSubject.add")}</Button>
        </Flex>
    </Modal>

    {/* Prereqs Modal */}
    <Modal opened={openedPrereqModal} onClose={() => setOpenedPrereqModal(false)} title={t("CreateSubject.addPrereq")} centered size="80%">
        <Flex justify="center" direction="column" align="center" gap="md">
            <Grid >
                {subjects.map((subject) => (
                    <Grid.Col span={2}>
                        <ChooseSubjectCard subject={subject} selectionCallback={handlePrereqSelection} selected={isPrereqSelected(subject.id)} removalCallback={handlePrereqRemoval} />
                    </Grid.Col>
                ))}
            </Grid>
            <Flex>
                <Pagination total={maxPage} value={currentPrereqPage} onChange={setCurrentPrereqPage}/>
            </Flex>
        </Flex>
    </Modal>
    {/* Professor Modal */}
    <Modal opened={openedProfessorModal} onClose={() => setOpenedProfessorModal(false)} title={t("CreateSubject.createProfessor")} size="35%">
        {usedProfessorName && <Alert variant="light" color="yellow" title={t("CreateSubject.missingFields")} icon={icon}>
            <Flex direction="row" justify="space-between">
                {t("CreateSubject.userProfessorName")}
                <ActionIcon size={18} variant="transparent" color="gray" onClick={() => setUsedProfessorName(false)}>
                    <IconX style={{ width: rem(24), height: rem(24) }} />
                </ActionIcon>
            </Flex>
        </Alert>}
        <Flex mih={50} miw={500} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
            {t("CreateSubject.professorName")}
            <TextInput className={classes.degreeDropdown} value={currentProfessorCreation} onChange={(event) => setCurrentProfessorCreation(event.target.value)} />
        </Flex>
        <Flex direction="row" justify="center">
            <Button color="green" onClick={() => handleProfessorCreation()}>{t("CreateSubject.add")}</Button>
        </Flex>
    </Modal>

    {/* Create Class Modal */}
    <Modal opened={openedClassModal} onClose={() => setOpenedClassModal(false)} title={t("CreateSubject.addClasses")} size="30%">
        { missingClassFields && <Alert variant="light" color="yellow" title={t("CreateSubject.missingFields")} icon={<IconInfoCircle/>}>
            {t("CreateSubject.completeClassFields")}
        </Alert>}
        <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
            {t("CreateSubject.class")}
            <Textarea className={classes.degreeDropdown} autosize value={currentClassName}
                      onChange={(event) => setCurrentClassName(event.currentTarget.value)}/>
        </Flex>
        <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
            {t("CreateSubject.professors")}
            <Flex direction="column" justify="flex-end" maw={220}>
                <MultiSelect placeholder={t("CreateSubject.professorLabel")} data={selectedProfessors} value={currentClassProfessors} onChange={setCurrentClassProfessors}/>
            </Flex>
        </Flex>
        <Flex mih={50} gap="xl" justify="right" align="center" direction="row" wrap="wrap">
            <Button color="green" onClick={() => handleClassCreation()}>{t("CreateSubject.add")}</Button>
        </Flex>
    </Modal>

    { /* Edit Class Modal */ }
    <Modal opened={openedClassEditModal} onClose={() => handleClassEdit()} title={t("CreateSubject.editClasses")} size="30%">
        { missingClassFields && <Alert variant="light" color="yellow" title={t("CreateSubject.missingFields")} icon={<IconInfoCircle/>}>
            {t("CreateSubject.completeClassFields")}
        </Alert>}
        <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
            {t("CreateSubject.class")}
            <Textarea className={classes.degreeDropdown} autosize value={currentClassEditName}
                      onChange={(event) => setCurrentClassEditName(event.currentTarget.value)}/>
        </Flex>
        <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
            {t("CreateSubject.professors")}
            <Flex direction="column" justify="flex-end" maw={220}>
                <MultiSelect placeholder={t("CreateSubject.professorLabel")} data={selectedProfessors} value={currentClassEditProfessors} onChange={setCurrentClassEditProfessors}/>
            </Flex>
        </Flex>
        <Flex mih={50} gap="xl" justify="right" align="center" direction="row" wrap="wrap">
            <Button color="green" onClick={() => handleClassEdit()}>{t("CreateSubject.edit")}</Button>
        </Flex>
    </Modal>

    { /* Add Class Time Modal */ }
    <Modal opened={openedClassTimeModal} onClose={() => setOpenedClassTimeModal(false)} title={t("CreateSubject.addClassTimes")}>
        {missingClassTimeFields && <Alert variant="light" color="yellow" title={t("CreateSubject.missingFields")} icon={<IconInfoCircle />}>
            {t("CreateSubject.completeFields")}
        </Alert>}
        <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
            {t("CreateSubject.day")}
            <Combobox store={comboboxClassDays} onOptionSubmit={(value) => {
                setCurrentClassTimeDay(value);
                comboboxClassDays.closeDropdown();
            }}>
                <Combobox.Target>
                    <InputBase className={classes.degreeDropdown} component="button" type="button" pointer
                               rightSection={<Combobox.Chevron/>} rightSectionPointerEvents="none"
                               onClick={() => comboboxClassDays.toggleDropdown()}>
                        {currentClassTimeDay}
                    </InputBase>
                </Combobox.Target>
                <Combobox.Dropdown>
                    <ComboboxOptions>{classDayOptions}</ComboboxOptions>
                </Combobox.Dropdown>
            </Combobox>
        </Flex>
        <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
            {t("CreateSubject.timeStart")}
            <TimeInput className={classes.degreeDropdown} ref={startTimeRef} />
        </Flex>
        <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
            {t("CreateSubject.timeEnd")}
            <TimeInput className={classes.degreeDropdown} ref={endTimeRef}/>
        </Flex>
        <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
            {t("CreateSubject.mode")}
            <Textarea className={classes.degreeDropdown} autosize value={currentClassTimeMode}
                      onChange={(event) => setCurrentClassTimeMode(event.currentTarget.value)}/>
        </Flex>
        <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
            {t("CreateSubject.building")}
            <Textarea className={classes.degreeDropdown} autosize value={currentClassTimeBuilding}
                      onChange={(event) => setCurrentClassTimeBuilding(event.currentTarget.value)}/>
        </Flex>
        <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
            {t("CreateSubject.classroom")}
            <Textarea className={classes.degreeDropdown} autosize value={currentClassTimeClassroom}
                      onChange={(event) => setCurrentClassTimeClassroom(event.currentTarget.value)}/>
        </Flex>
        <Flex mih={50} gap="xl" justify="right" align="center" direction="row" wrap="wrap">
            <Button color="green" onClick={() => handleClassTimeCreation()}>{t("CreateSubject.createClassTime")}</Button>
        </Flex>
    </Modal>

    { /* Actual Page */ }
    <Navbar />
    <div className={classes.container50}>
        <form className={classes.form}>
            <h1 className={classes.title}>{t("CreateSubject.editSubjectTitle")}</h1>

            <br />
            {errorAlert &&
                <Alert variant="light" color="yellow" title={errorMessageTitle} icon={icon}>
                    <Flex direction="row" justify="space-between" wrap="wrap">
                        {errorMessage}
                        <ActionIcon size={24} variant="transparent" color="gray" onClick={() => setErrorAlert(false)}>
                            <IconX style={{ width: rem(24), height: rem(24) }} />
                        </ActionIcon>
                    </Flex>
                </Alert>}

            <Tabs value={activeTab} onChange={(value) => setActiveTab(value)}>
                <Tabs.List>
                    <Tabs.Tab value="general-info">
                        {t("CreateSubject.generalInfo")}
                    </Tabs.Tab>
                    <Tabs.Tab value="classes">{t("CreateSubject.classes")}</Tabs.Tab>
                </Tabs.List>

                { /* General Informaiton Tab */ }
                <Tabs.Panel value="general-info">
                    <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
                        <div>
                            {t("CreateSubject.id")}
                            <h6 className={classes.optional}>
                                {t("CreateSubject.idHelp")}
                            </h6>
                        </div>
                            <Textarea className={classes.departmentDropdown} autosize value={subjectId} description={t("CreateSubject.disabledSubjectIdField")}
                                      onChange={(event) => handleNewSubjectId(event.currentTarget.value)} disabled/>
                    </Flex>
                    <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
                        {t("CreateSubject.name")}
                        {emptySubjectName ? <Textarea className={classes.departmentDropdown} autosize value={subjectName}
                                                      onChange={(event) => setSubjectName(event.currentTarget.value)} error={t("CreateSubject.emptySubjectName")}/>:
                            <Textarea className={classes.departmentDropdown} autosize value={subjectName}
                                      onChange={(event) => setSubjectName(event.currentTarget.value)}/>}
                    </Flex>
                    <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
                        {t("CreateSubject.department")}
                        <Combobox store={comboboxDepartment} width={300} onOptionSubmit={(value) => {
                            setDepartment(value);
                            comboboxDepartment.closeDropdown();
                        }}>
                            <Combobox.Target>
                                <InputBase className={classes.departmentDropdown} component="button" type="button" pointer
                                           rightSection={<Combobox.Chevron />} rightSectionPointerEvents="none" onClick={() => comboboxDepartment.toggleDropdown()}>
                                    {department}
                                </InputBase>
                            </Combobox.Target>
                            <Combobox.Dropdown>{deaprtmentOptions}</Combobox.Dropdown>
                        </Combobox>
                    </Flex>
                    <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
                        {t("CreateSubject.credits")}
                        <NumberInput className={classes.departmentDropdown} value={credits} min={MINIMUM_CREDITS} max={MAXIMUM_CREDITS} onChange={(value) => handleCreditChange(value)}/>
                    </Flex>
                    <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
                        {t("CreateSubject.degree")}
                        <Flex  direction="column">
                            { selectedDegrees.length > 0 && degrees.length > 0 &&
                                selectedDegrees.map((degree) =>
                                    <Flex direction="row" align="center">
                                        <p>{searchForDegreeName(degree)}</p>
                                        <ActionIcon size={24} variant="transparent" onClick={() => handleRemoveSelectedDegree(degree)}>
                                            <IconX style={{ width: rem(24), height: rem(24) }} />
                                        </ActionIcon>
                                    </Flex>)
                            }
                            <Button onClick={() => setOpenedDegreeModal(true)}>
                                {t("CreateSubject.addDegree")}
                            </Button>
                        </Flex>
                    </Flex>
                    <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
                        <div>
                            {t("CreateSubject.prerequisites")}
                            <h6 className={classes.optional}>
                                ({t("CreateSubject.optional")})
                            </h6>
                        </div>
                        {<Button
                            onClick={() => setOpenedPrereqModal(true)}>
                            {t("CreateSubject.addPrereq")}
                        </Button>}
                    </Flex>
                    <Flex mih={50} miw={500} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
                        {t("CreateSubject.professor")}
                        <Flex direction="column" justify="flex-end" maw={400}>
                            <MultiSelect
                                placeholder={t("CreateSubject.professorLabel")}
                                data={professors.map((p) => p.name)}
                                onSearchChange={handleSearchProfessors}
                                searchable
                                onOptionSubmit={(professor) => handleProfessorAddition(professor)}
                                onRemove={(professor) => handleProfessorRemove(professor)}
                            />
                        </Flex>
                    </Flex>
                    <Flex mih={50} gap="xl" justify="right" align="center" direction="row" wrap="wrap">
                        { createdProfessors.length > 0 &&
                            createdProfessors.map((professor) =>
                                <Flex direction="row" align="center">
                                    <p>{professor}</p>
                                    <ActionIcon size={24} variant="transparent" onClick={() => handleRemoveCreatedProfessor(professor)}>
                                        <IconX style={{ width: rem(24), height: rem(24) }} />
                                    </ActionIcon>
                                </Flex>)
                        }
                        <Button onClick={() => setOpenedProfessorModal(true)}>
                            {t("CreateSubject.createProfessor")}
                        </Button>
                    </Flex>
                    <Flex mih={50} gap="xl" justify="center" align="center" direction="row" wrap="wrap">
                        <Button variant="default" onClick={() => setActiveTab("classes")}>
                            {t("CreateSubject.next")}
                        </Button>
                    </Flex>
                </Tabs.Panel>

                { /* Classes Tab */ }
                <Tabs.Panel value="classes">
                    <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
                        {t("CreateSubject.classes")}
                        <Button onClick={() => setOpenedClassModal(true)}>
                            {t("CreateSubject.addClasses")}
                        </Button>
                    </Flex>
                    <Table>
                        <Table.Thead>
                            <Table.Tr>
                                <Table.Th>{t("CreateSubject.class")}</Table.Th>
                                <Table.Th>{t("CreateSubject.professors")}</Table.Th>
                                <Table.Th></Table.Th>
                            </Table.Tr>
                        </Table.Thead>
                        <Table.Tbody>
                            {selectedClasses.length > 0 && selectedClasses.map((clas) => <Table.Tr>
                                <Table.Th>
                                    <Flex direction="row" justify="space-between">
                                        {clas.idClass}
                                        <ActionIcon size={18} variant="default" onClick={() => handleClassEditModal(clas)}>
                                            <IconPencil style={{ width: rem(24), height: rem(24) }} />
                                        </ActionIcon>
                                        <ActionIcon size={18} variant="default" onClick={() => handleRemoveClass(clas)}>
                                            <IconX style={{ width: rem(24), height: rem(24) }} />
                                        </ActionIcon>
                                    </Flex>
                                </Table.Th>
                                <Table.Th>{clas.professors.join(";")}</Table.Th>
                                <Table.Tr>
                                    <Table.Th>{t("CreateSubject.day")}</Table.Th>
                                    <Table.Th>{t("CreateSubject.timeStart")}</Table.Th>
                                    <Table.Th>{t("CreateSubject.timeEnd")}</Table.Th>
                                    <Table.Th>{t("CreateSubject.mode")}</Table.Th>
                                    <Table.Th>{t("CreateSubject.building")}</Table.Th>
                                    <Table.Th>{t("CreateSubject.classroom")}</Table.Th>
                                </Table.Tr>
                                { clas.locations.length > 0 && clas.locations.map((location) => <Table.Tr>
                                    <Table.Th>{classDays[location.day]}</Table.Th>
                                    <Table.Th>{location.startTime}</Table.Th>
                                    <Table.Th>{location.endTime}</Table.Th>
                                    <Table.Th>{location.mode}</Table.Th>
                                    <Table.Th>{location.building}</Table.Th>
                                    <Table.Th>{location.location}</Table.Th>
                                    <Table.Th>
                                        <ActionIcon size={18} variant="default" onClick={() => handleRemoveClassTime(location, clas)}>
                                            <IconX style={{ width: rem(24), height: rem(24) }} />
                                        </ActionIcon>
                                    </Table.Th>
                                </Table.Tr>)
                                }
                                <Table.Th><Button variant="default" onClick={() => handleOpenClassTimeModalCreation(clas)}>
                                    {t("CreateSubject.addClassTimes")}
                                </Button></Table.Th>
                            </Table.Tr>)}
                        </Table.Tbody>
                    </Table>
                    <Flex mih={50} gap="xl" justify="center" align="center" direction="row" wrap="wrap">
                        <Button variant="default"
                                onClick={() => setActiveTab("general-info")}>{t("CreateSubject.previous")}
                        </Button>
                        <Button color="green" onClick={() => handleSubjectEdit()}>
                            {t("CreateSubject.editSubjectTitle")}
                        </Button>
                    </Flex>
                </Tabs.Panel>
            </Tabs>
        </form>
    </div>
</div>
    );
}
