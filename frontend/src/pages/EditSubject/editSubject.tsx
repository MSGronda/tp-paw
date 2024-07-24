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
    ComboboxOptions, Divider,
    Flex,
    Grid,
    InputBase,
    Modal, MultiSelect, NumberInput, Pagination, rem, ScrollArea, Table, Tabs, Textarea, TextInput,
    useCombobox
} from "@mantine/core";
import ClassTime from "../../models/ClassTime.ts";
import classes from "../CreateSubject/createsubject.module.css";
import Title from "../../components/title/title.tsx";
import ChooseSubjectCard from "../../components/choose-subject-card/choose-subject-card.tsx";
import {TimeInput} from "@mantine/dates";
import {Navbar} from "../../components/navbar/navbar.tsx";
import {
    calculateHoursDifference, extractHoursFromTimeStamp,
    extractNumberFromSemesterName
} from "../../utils/subjectEditingUtils.ts";



export function EditSubject() {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const params = useParams();

    const MINIMUM_CREDITS = 0;
    const MAXIMUM_CREDITS = 12;
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
    classDays.forEach((clas, index) => weekDaysMap.set(clas, index+1));

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
    const [emptySubjectName, setEmptySubjectName] = useState(false);
    const [errorAlert, setErrorAlert] = useState(false);
    const [errorMessageTitle, setErrorMessageTitle] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [usedProfessorName, setUsedProfessorName] = useState(false);
    const [repeatedClass, setRepetedClass] = useState<boolean>(false);
    const [invalidDegreeOrSemester, setInvalidDegreeOrSemester] = useState<boolean>(false);

    // Form related states
    const [department, setDepartment] = useState<string>("");
    const [subjectName, setSubjectName] = useState<string>("");
    const [subjectId, setSubjectId] = useState<string>("");
    const [credits, setCredits] = useState<number>(MINIMUM_CREDITS);
    const [selectedDegrees, setSelectedDegrees] = useState<number[]>([]);
    const [selectedSemesters, setSelectedSemesters] = useState<number[]>([]);
    const [selectedPrereqs, setSelectedPrereqs] = useState<number[]>([]);
    const [selectedProfessors, setSelectedProfessors] = useState<string[]>([]);
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

    const [currentClassIndex, setCurrentClassIndex] = useState<number>();
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
        }
    }
    const searchDepartments = async () => {
        const res = await departmentService.getDepartments();
        if(res){
            setDepartments(res);
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
        if (res && data.length > 0) {
            setSelectedDegrees(data.map((degree: Degree) => degree.id));
        }
    }

    const searchSemesters = async (degreeIds: number[], subjectId: string) => {
        const selSemesters = [];
        for (const degreeId of degreeIds) {
            const res = await degreeService.getSemesters(degreeId, subjectId);
            const data = handleService(res, navigate);
            if(res && data && data.length > 0) {
                selSemesters.push(data[0].semester);
            }
        }
        setSelectedSemesters([...selectedSemesters, ...selSemesters]);
        setFetchedSelectedDegrees(true);
    }

    const handleSearchProfessors = async (value: string) => {
        if(value.length > 1) {
            const res = await professorService.getProfessors(undefined, undefined, value);
            if(res.status == 200 && res.data && res.data != ""){
                setProfessors(res.data);
            }
        }
    }

    const getProfessorsForSubject = async (subject: Subject) => {
        const res = await professorService.getProfessorsForSubject(subject);
        if(res){
            const selProfs: Professor[] = [];
            for(let i = 0; i < subject.classes.length; i++){
                if(res.has(subject.classes[i].idClass) && res.get(subject.classes[i].idClass) !== undefined){
                    const classProfs = res.get(subject.classes[i].idClass);

                    if(!classProfs){
                        continue;
                    }

                    subject.classes[i].professors = classProfs.map(prof => prof.name);
                    for(const prof of classProfs) {
                        if(!selProfs.some((p) => p.name == prof.name)){
                            selProfs.push(prof);
                        }
                    }
                }
            }
            setSelectedClasses(subject.classes);
            setSelectedProfessors(selProfs.map(prof => prof.name));
        }
    }

    const editSubject = async(subjectId: string, subjectName: string, department: string, credits: number, selectedDegrees: number[],
                              selectedSemesters: number[], prereqs: string[], professors: string[], selectedClasses: any[]) => {
        const res = await subjectService.editSubject(subjectId, subjectName, department, credits, selectedDegrees, selectedSemesters, prereqs, professors, selectedClasses);
        //const data = handleService(res, navigate);
        if(res){
            if(res.status === 200){// 200 updated
                navigate('/subject/' + subjectId)
            } else {
                handleErrorAlert(t("CreateSubject.oops"));
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
        if(selectedDegrees.length > 0){
            searchSubjects(currentPrereqPage);
        }
    }, [selectedDegrees, currentPrereqPage]);

    useEffect(() => {
        searchDegrees();
    }, []);

    useEffect(() => {
        searchDepartments()
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
            setSelectedPrereqs(subject.prerequisites.map((prereq) => Number(prereq)));
            getProfessorsForSubject(subject);
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

    // Utils
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

    // Week day and time formats
    function extractNumberFromWeekDay(weekDay: string) {
        return weekDaysMap.get(weekDay) || 1;
    }

    // Handlers
    function handleSelectedDegreeSemesters() {
        if(currentDegree == undefined || currentSemester == ""){
            setInvalidDegreeOrSemester(true);
            return;
        }

        let index;
        if((index = selectedDegrees.indexOf(currentDegree)) !== -1) {
            selectedDegrees[index] = currentDegree;
            selectedSemesters[index] = extractNumberFromSemesterName(currentSemester);
        } else {
            setSelectedDegrees((selectedDegrees) => [...selectedDegrees, currentDegree]);
            setSelectedSemesters((selectedSemesters) => [...selectedSemesters, extractNumberFromSemesterName(currentSemester)]);
        }
        setInvalidDegreeOrSemester(false);
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

        if(selectedClasses.some((c) => c.idClass == currentClassName)){
            setRepetedClass(true);
            return;
        }

        availableCreditsPerClass.set(newClass.idClass, credits);
        setAvailableCreditsPerClass(new Map<string,number>(availableCreditsPerClass));
        setSelectedClasses([...selectedClasses, newClass]);
        setCurrentClassName("");
        setCurrentClassProfessors([]);
        setMissingClassFields(false);
        setOpenedClassModal(false);
        setRepetedClass(false);
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
        const index = selectedClasses.indexOf(clas);

        setCurrentClassEditName(clas.idClass);
        setCurrentClassEditProfessors(clas.professors ?? []);
        setCurrentClassIndex(index);

        setOpenedClassEditModal(true);
    }

    function handleClassEdit() {

        if(currentClassIndex == undefined){
            return;
        }

        if(currentClassEditProfessors.length === 0 || currentClassEditName === ""){
            setMissingClassFields(true);
            return;
        }

        if(selectedClasses.some((c, i) => c.idClass == currentClassEditName && i != currentClassIndex)){
            setRepetedClass(true);
            return;
        }

        setRepetedClass(false)
        setRepetedClass(false)

        const editedClass = selectedClasses[currentClassIndex]
        editedClass.idClass = currentClassEditName;
        editedClass.professors = currentClassEditProfessors;

        availableCreditsPerClass.set(editedClass.idClass, credits);
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
        // Check values before submitting
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
    const classDayOptions = classDays.map((classDay, index) => (
        <ComboboxOption key={index + 1} value={classDay}>
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
            { invalidDegreeOrSemester && <Alert variant="light" color="yellow" title={t("CreateSubject.missingFields")} icon={<IconInfoCircle/>}>
                {t("CreateSubject.invalidDegreeOrSemester")}
            </Alert>}

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
            <div style={{
                width: "100%",
                display: 'grid',
                gridTemplateColumns: 'repeat(auto-fill, minmax(190px, 1fr))',
                padding: '1rem 1rem 1rem 1rem',
                gap: '1rem'
            }}>
                {subjects.map((subject) => (
                    <ChooseSubjectCard subject={subject} selectionCallback={handlePrereqSelection}
                                       selected={isPrereqSelected(subject.id)}
                                       removalCallback={handlePrereqRemoval}/>
                ))}
            </div>
            <Flex>
                <Pagination total={maxPage} value={currentPrereqPage} onChange={setCurrentPrereqPage}/>
            </Flex>
        </Flex>
    </Modal>
            {/* Professor Modal */}
            <Modal opened={openedProfessorModal} onClose={() => setOpenedProfessorModal(false)}
                   title={t("CreateSubject.createProfessor")} size="35%">
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
        { repeatedClass && <Alert variant="light" color="yellow" title={t("CreateSubject.missingFields")} icon={<IconInfoCircle/>}>
            {t("CreateSubject.repeatedClassId")}
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
        { repeatedClass && <Alert variant="light" color="yellow" title={t("CreateSubject.missingFields")} icon={<IconInfoCircle/>}>
            {t("CreateSubject.repeatedClassId")}
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
                                      disabled/>
                    </Flex>
                    <Divider my="md"/>
                    <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
                        {t("CreateSubject.name")}
                        {emptySubjectName ? <Textarea className={classes.departmentDropdown} autosize value={subjectName}
                                                      onChange={(event) => setSubjectName(event.currentTarget.value)} error={t("CreateSubject.emptySubjectName")}/>:
                            <Textarea className={classes.departmentDropdown} autosize value={subjectName}
                                      onChange={(event) => setSubjectName(event.currentTarget.value)}/>}
                    </Flex>
                    <Divider my="md"/>
                    <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
                        {t("CreateSubject.department")}
                        <Combobox style={{width: "17rem"}} store={comboboxDepartment} width={300} onOptionSubmit={(value) => {
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
                    <Divider my="md"/>
                    <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
                        {t("CreateSubject.credits")}
                        <NumberInput className={classes.departmentDropdown} value={credits} min={MINIMUM_CREDITS} max={MAXIMUM_CREDITS} onChange={(value) => handleCreditChange(value)}/>
                    </Flex>
                    <Divider my="md"/>
                    <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
                        {t("CreateSubject.degree")}
                        <Flex direction="column">
                            <ScrollArea h="10rem">
                                { selectedDegrees.length > 0 && degrees.length > 0 &&
                                    selectedDegrees.map((degree) =>
                                        <Flex direction="row" align="center">
                                            <p>{searchForDegreeName(degree)}</p>
                                            <ActionIcon size={24} variant="transparent" onClick={() => handleRemoveSelectedDegree(degree)}>
                                                <IconX style={{ width: rem(24), height: rem(24) }} />
                                            </ActionIcon>
                                        </Flex>)
                                }

                            </ScrollArea>
                            <Button onClick={() => setOpenedDegreeModal(true)}>
                                {t("CreateSubject.addDegree")}
                            </Button>
                        </Flex>
                    </Flex>
                    <Divider my="md"/>
                    <Flex pt={rem(5)} mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
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
                    <Divider my="md"/>
                    <Flex pt={rem(5)} mih={50} miw={500} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
                        {t("CreateSubject.professor")}
                        <Flex direction="column" justify="flex-end" maw={600}>
                            <MultiSelect
                                placeholder={t("CreateSubject.professorLabel")}
                                data={professors.map((p) => p.name)}
                                onSearchChange={handleSearchProfessors}
                                searchable
                                onOptionSubmit={(professor) => handleProfessorAddition(professor)}
                                onRemove={(professor) => handleProfessorRemove(professor)}
                                value={selectedProfessors}
                                withScrollArea={true}
                            />
                        </Flex>
                    </Flex>
                    <Divider my="md"/>
                    <Flex mih={50} gap="xl" justify="right" align="center" direction="row" wrap="wrap">
                        <Button onClick={() => setOpenedProfessorModal(true)}>
                            {t("CreateSubject.createProfessor")}
                        </Button>
                    </Flex>
                    <Divider my="md"/>
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
                                    <Flex direction="row" justify="space-between" align="center">
                                        <span style={{paddingRight: "0.5rem"}}>{clas.idClass}</span>

                                        <ActionIcon size={18} variant="default" onClick={() => handleClassEditModal(clas)}>
                                            <IconPencil style={{ width: rem(24), height: rem(24) }} />
                                        </ActionIcon>
                                        <ActionIcon size={18} variant="default" onClick={() => handleRemoveClass(clas)}>
                                            <IconX style={{ width: rem(24), height: rem(24) }} />
                                        </ActionIcon>
                                    </Flex>
                                </Table.Th>
                                <Table.Th>{clas.professors?.join(";")}</Table.Th>
                                <Table.Tr>
                                    <Table.Th>{t("CreateSubject.day")}</Table.Th>
                                    <Table.Th>{t("CreateSubject.timeStart")}</Table.Th>
                                    <Table.Th>{t("CreateSubject.timeEnd")}</Table.Th>
                                    <Table.Th>{t("CreateSubject.mode")}</Table.Th>
                                    <Table.Th>{t("CreateSubject.building")}</Table.Th>
                                    <Table.Th>{t("CreateSubject.classroom")}</Table.Th>
                                </Table.Tr>
                                { clas.locations.length > 0 && clas.locations.map((location) => <Table.Tr>
                                    <Table.Th>{classDays[location.day-1]}</Table.Th>
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
