import {useEffect, useState} from "react";
import classes from "../SemesterBuilder/semester_builder.module.css";
import {Navbar} from "../../components/navbar/navbar.tsx";
import {ActionIcon, Card, Divider, rem, Select, UnstyledButton} from "@mantine/core";
import {
    Subject,
    getDifficultyValue,
    getTimeDemandValue,
    sortByNameDesc,
    sortByCreditsDesc,
    sortByDifficultyDesc,
    sortByTimeDemandDesc,
    sortByNameAsc,
    sortByCreditsAsc,
    sortByTimeDemandAsc,
    sortByDifficultyAsc
} from "../../models/Subject.ts";
import BuilderSubjectCard from "../../components/builder-subject-card/builder_subject_card.tsx";
import BuilderSelectedCard from "../../components/builder-selected-card/builder_selected_card.tsx";
import {SelectedSubject, selectedSubjectToSubject, subjectToSelectedSubject} from "../../models/SelectedSubject.ts";
import DifficultyChip from "../../components/difficulty-chip/difficulty-chip.tsx";
import TimeDemandChip from "../../components/time-demand-chip/time-demand-chip.tsx";
import {
    IconArrowNarrowDown,
    IconArrowNarrowUp,
    IconCalendarEvent,
    IconCheck,
    IconList,
    IconX
} from "@tabler/icons-react";
import WeeklySchedule from "../../components/schedule/weekly-schedule.tsx";
import BuilderSelectClassCard from "../../components/builder-select-class-card/builder_select_class_card.tsx";
import Class from "../../models/Class.ts";
import {t} from "i18next";
import {subjectService, userService} from '../../services';
import {handlePagedService, handleService} from "../../handlers/serviceHandler.tsx";
import {useNavigate} from "react-router-dom";
import FloatingButton from "../../components/floating-button/floating-button.tsx";
import FloatingMessage from "../../components/floating-message/floating_message.tsx";
import Title from "../../components/title/title.tsx";
import {createEmptySubjectClass, createSelectedSubjects} from "../../utils/user_plan_utils.ts";
import {NavigateFunction} from "react-router/dist/lib/hooks";

const COLS = 7
const ROWS = 29


export default function SemesterBuilder() {
    // Navigation
    const navigate = useNavigate();

    // Available
    const [available, setAvailable] = useState<Subject[]>([]);
    const getAvailable = async () => {
        const userId = userService.getUserId();
        if(!userId){
            navigate('/login');
            return;
        }

        const subjects = await getAllSubjects(subjectService.getAvailableSubjects.bind(subjectService), userId, navigate);

        if(!subjects){return;}

        setAvailable(subjects);
    }

    // Select class - para cuando tenes que elegir comision de materia
    const [selectClass, setSelectClass] = useState<Subject>();

    // Selected
    const [selectedSubjects, setSelectedSubjects] = useState<SelectedSubject[]>([]);
    const getUserPlan = async () => {
        const userId = userService.getUserId();
        if(!userId){
            navigate('/login');
            return;
        }

        const respSubjects = await subjectService.getUserPlanSubjects(userId);
        const dataSubjects = handleService(respSubjects, navigate);

        const respPlan = await userService.getUserPlan(userId);
        const dataPlan = handleService(respPlan, navigate);

        const subjects = createSelectedSubjects(dataPlan, dataSubjects);

        // Tenemos que setear el arreglo con 1 si es que ya tenia materias anotadas
        replaceScheduleArray(subjects)

        setSelectedSubjects(subjects);
    }

    const [scheduleArray, setScheduleArray] = useState<number[]>(new Array(ROWS * COLS).fill(0));

    // Overview
    const [totalCredits, setTotalCredits] = useState(0);
    const [timeDemand, setTimeDemand] = useState(0);
    const [difficulty, setDifficulty] = useState(0);
    const updateOVerviewStats = () => {
        let credits = 0;
        let diff = 0;
        let td = 0;
        for(const ss of selectedSubjects){
            credits += ss.subject.credits;
            diff += getDifficultyValue(ss.subject.difficulty);
            td += getTimeDemandValue(ss.subject.timeDemand);
        }
        setTotalCredits(credits);
        setDifficulty(diff);
        setTimeDemand(td);
    }
    useEffect(() => {
        updateOVerviewStats()
    }, [selectedSubjects])

    // Unlocking of subjects
    const [doneSubjects, setDoneSubjects] = useState<Subject[]>([]);
    const getDone = async () => {
        const userId = userService.getUserId();
        if(!userId){
            navigate('/login');
            return;
        }
        
        const subjects = await getAllSubjects(subjectService.getDoneSubjects.bind(subjectService), userId, navigate);
        
        if(!subjects){return;}

        setDoneSubjects(subjects)
    }

    const [unlockables, setUnlockables] = useState<Subject[]>([]);
    const getUnlockable = async () => {
        const userId = userService.getUserId();
        if(!userId){
            navigate('/login');
            return;
        }
        
        const subjects = await getAllSubjects(subjectService.getUnlockableSubjects.bind(subjectService), userId, navigate);

        if(!subjects){return;}

        setUnlockables(subjects)
    }

    // API Calls
    useEffect( () => {
        getDone();
        getAvailable();
        getUnlockable();
        getUserPlan();
    }, []);

    // Conditional rendering
    const [showSchedule, setShowSchedule] = useState(false);
    const [showClassSelect, setShowClassSelect] = useState(false);

    const [ascendingSort, setAscendingSort] = useState(false);
    const [sortingType, setSortingType] = useState(t("Builder.sortName"));

    const selectSubject = (id: string) => {
        const selected = available.find((subject) => subject.id == id);

        if(selected){
            setSelectClass(selected);
            setShowClassSelect(true);
        }
    }
    const closeClassSelect = () => {
        setSelectClass(undefined);
        setShowClassSelect(false);
    }

    const selectClassCallback = (selectedClass: Class) => {
        if(!selectClass){
            return;
        }

        // Agrego a selected
        const newSelected = [...selectedSubjects]
        newSelected.push(subjectToSelectedSubject(selectClass, selectedClass))
        setSelectedSubjects(newSelected)

        setShowClassSelect(false);

        // Elimino de available
        const newAvailable = [...available];
        setAvailable(newAvailable.filter((s) => s.id != selectClass.id))

        // Update schedule array
        updateScheduleArray(selectedClass, 1);
    }
    const removeSubject = (id: string) => {
        const selectedSubject = selectedSubjects.find((ss) => ss.subject.id == id)

        if(!selectedSubject){
            return;
        }

        // Agrego a available
        const newAvailable = [...available]
        const newSubject = selectedSubjectToSubject(selectedSubject);
        if(!newAvailable.find((s) => s.id == newSubject.id)){
            newAvailable.push(newSubject);
        }
        setAvailable(newAvailable.sort(getSorter(sortingType)))

        // Elimino de selectedSubjects
        const newSelected = [...selectedSubjects]
        setSelectedSubjects(newSelected.filter((s) => s.subject.id != id))

        // Update schedule array
        updateScheduleArray(selectedSubject.selectedClass, 0);
    }
    const showScheduleAction = () => {
        setShowSchedule(!showSchedule);
    }

    // Schedule checkers
    const alreadySignedUp = (subject: Subject): boolean => {
        for(const ss of selectedSubjects){
            if(ss.subject.id == subject.id){
                return true;
            }
        }
        return false;
    }

    const subjectEnabled = (subject: Subject): boolean => {
        // Para cada comision, me fijo si es viable (no tiene conflictos en los horarios)
        if(subject.classes.length == 0){
            return true;
        }
        for(const sc of subject.classes){
            if(classEnabled(sc)){
                return true;    // Con que una comision sea viable, toda la materia es viable
            }
        }
        return false;
    }

    const replaceScheduleArray = (subjectClasses: SelectedSubject[]) => {
        const newScheduleArray = new Array(ROWS * COLS).fill(0);
        for(const subjectClass of subjectClasses){
            for(const time of subjectClass.selectedClass.locations){
                const startTimeValue = timeStringToNumber(time.startTime);
                const endTimeValue = timeStringToNumber(time.endTime);
                for(let i = startTimeValue; i < endTimeValue; i++){
                    newScheduleArray[time.day + i * COLS] = 1;
                }
            }
        }
        setScheduleArray(newScheduleArray);
    }
    const updateScheduleArray = (subjectClass: Class, value: number) => {

        const newScheduleArray = [...scheduleArray]     // Ay no :|

        for(const time of subjectClass.locations){
            const startTimeValue = timeStringToNumber(time.startTime);
            const endTimeValue = timeStringToNumber(time.endTime);
            for(let i = startTimeValue; i < endTimeValue; i++){
                newScheduleArray[time.day + i * COLS] = value;
            }
        }
        setScheduleArray(newScheduleArray);
    }
    const classEnabled = (subjectClass: Class): boolean => {
        let viable = true;          // Comision viable
        for(const time of subjectClass.locations){
            // Si una de los horarios de una clase tiene conflicto, no es viable la comision entera
            if(isOverlapped(time.day, time.startTime, time.endTime, scheduleArray)){
                viable = false;
                break;
            }
        }
        return viable;
    }

    // Unlockable checkers
    const subjectUnlocked = (subject: Subject): boolean => {
        for(const prereq of subject.prerequisites){

            // TODO: quizas usar un Set / Bloom filter

            if(doneSubjects && !doneSubjects.find((s) => s.id == prereq) && !selectedSubjects.find((s) => s.subject.id == prereq)){
                return false;
            }
        }
        return true;
    }

    // Sorting
    const sortDirectionButton = () => {
        setAscendingSort(!ascendingSort);
        sortAvailable(sortingType);
    }
    const getSorter = (value: string | null): (a: Subject, b: Subject) => number => {
        switch (value) {
            case "Name":
                if (ascendingSort)
                    return sortByNameDesc;
                else
                    return sortByNameAsc;

            case "Credits":
                if (ascendingSort)
                    return sortByCreditsDesc;
                else
                    return sortByCreditsAsc;

            case "Difficulty":
                if (ascendingSort)
                    return sortByDifficultyDesc;
                else
                    return sortByDifficultyAsc;

            case "Time Demand":
                if (ascendingSort)
                    return sortByTimeDemandDesc;
                else
                    return sortByTimeDemandAsc;

            default:
                if (ascendingSort)
                    return sortByNameDesc;
                else
                    return sortByNameAsc;
        }
    }
    const sortAvailable = (value: string | null) => {
        setSortingType(value ? value : "Name");

        const newAvailable = [...available].sort(getSorter(value))
        setAvailable(newAvailable);
    }

    // Save schedule
    const [savedSuccessfully, setSavedSuccessfully] = useState(false);
    const [savedUnsuccessfully, setSavedUnsuccessfully] = useState(false);
    const saveFailIcon = <IconX style={{ width: rem(20), height: rem(20) }} />;
    const saveSuccessIcon = <IconCheck style={{ width: rem(20), height: rem(20) }} />;

    const saveSchedule = async () => {
        const userId = userService.getUserId();
        if(!userId){
            navigate('/login');
            return;
        }

        const resp = await userService.setUserSemester(userId, selectedSubjects);
        if(resp && resp.status == 202){
            setSavedSuccessfully(true);
            setTimeout(() => {setSavedSuccessfully(false)}, 3000);
        }
        else{
            setSavedUnsuccessfully(true);
            setTimeout(() => {setSavedUnsuccessfully(false)}, 3000);
        }
    }


    return (
        <div className={classes.general_area}>
            <Title text={t('Builder.title')}/>
            <Navbar />
            <div className={classes.container_95}>

                {/* Available Subjects */}
                {
                    !showClassSelect ?
                    <Card className={classes.available_card} withBorder>
                        <Card.Section>
                            <div className={classes.available_header}>
                                <h4 className={classes.section_titles}>{t("Builder.available")}</h4>
                                <div className={classes.available_sorting}>
                                    <Select
                                        data={[t("Builder.sortName"), t("Builder.sortCredits"), t("Builder.sortDifficulty"), t("Builder.sortTimeDemand")]}
                                        defaultValue={t("Builder.sortName")}
                                        allowDeselect={false}
                                        onChange={(value) => sortAvailable(value)}
                                        style={{paddingRight: "0.5rem", width: "10rem"}}
                                    />
                                    <ActionIcon variant="default">
                                        {
                                            ascendingSort ?
                                                <IconArrowNarrowUp style={{ width: '70%', height: '70%' }} stroke={1.5} onClick={sortDirectionButton}/>
                                                :
                                                <IconArrowNarrowDown style={{ width: '70%', height: '70%' }} stroke={1.5} onClick={sortDirectionButton}/>
                                        }
                                    </ActionIcon>

                                </div>

                            </div>
                            <Divider/>
                        </Card.Section>
                        <div className={classes.available_subjects_list}>
                            {available.map((subject) => (
                                !alreadySignedUp(subject) ?
                                <BuilderSubjectCard
                                    subject={subject}
                                    selectionCallback={selectSubject}
                                    enabled={subjectEnabled(subject)}
                                />
                                    :
                                <></>
                            ))}
                        </div>
                    </Card>
                    :
                    <></>
                }

                {/* Select Class */}
                {
                    showClassSelect ?
                        <Card className={classes.available_card} withBorder>
                            <Card.Section>
                                <div className={classes.available_header}>
                                    <h4 className={classes.section_titles}>{t("Builder.selectClass")} {selectClass?.name}</h4>
                                    <ActionIcon variant="default" >
                                        <IconX style={{ width: '70%', height: '70%' }} stroke={1.5} onClick={closeClassSelect}/>
                                    </ActionIcon>
                                </div>
                                <Divider/>
                            </Card.Section>
                            <div className={classes.selected_list}>
                                {
                                    selectClass && (!selectClass?.classes || !selectClass?.classes.length || selectClass.classes.length == 0)
                                    ?
                                        <BuilderSelectClassCard
                                            subjectClass={createEmptySubjectClass(selectClass)}
                                            addClassCallback={selectClassCallback}
                                            enabled={classEnabled(createEmptySubjectClass(selectClass))}
                                        />
                                    :
                                        <></>
                                }
                                {selectClass?.classes.map((subjectClass) => (
                                    <BuilderSelectClassCard
                                        subjectClass={subjectClass}
                                        addClassCallback={selectClassCallback}
                                        enabled={classEnabled(subjectClass)}
                                    />
                                ))}
                            </div>
                        </Card>
                        :
                        <></>
                }

                {/* Schedule */}
                {
                    showSchedule ?
                        <Card className={classes.schedule_card} withBorder>
                            <Card.Section>
                                <div className={classes.selected_header}>
                                    <h4 className={classes.section_titles}>{t("Builder.timeTable")}</h4>
                                    <ActionIcon variant="default" onClick={showScheduleAction}>
                                        <IconList style={{ width: '70%', height: '70%' }} stroke={1.5} />
                                    </ActionIcon>
                                </div>
                                <Divider/>
                            </Card.Section>
                            <div className={classes.schedule_area}>
                                <WeeklySchedule rows={ROWS} cols={COLS} subjectClasses={selectedSubjects}/>
                            </div>
                        </Card>
                        :
                    <></>
                }

                {/* Selected Subjects */}
                {
                    !showSchedule ?
                    <Card className={classes.selected_card} withBorder>
                        <Card.Section>
                            <div className={classes.selected_header}>
                                <h4 className={classes.section_titles}>{t("Builder.selected")}</h4>
                                <ActionIcon variant="default" onClick={showScheduleAction}>
                                    <IconCalendarEvent style={{ width: '70%', height: '70%' }} stroke={1.5} />
                                </ActionIcon>
                            </div>
                            <Divider style={{marginTop: '1rem'}}/>
                        </Card.Section>
                        <div className={classes.selected_list}>
                            {selectedSubjects.map((selected) => (
                                <BuilderSelectedCard
                                    selected={selected}
                                    removeCallback={removeSubject}
                                />
                            ))}
                        </div>
                    </Card>
                        :
                    <></>
                }


                {/* Overview */}
                {
                    !showSchedule ?
                    <Card className={classes.overview} withBorder>
                        <Card.Section>
                            <div className={classes.selected_header}>
                                <h4 className={classes.section_titles}>{t("Builder.overview")}</h4>
                            </div>
                            <Divider style={{marginTop: '1rem'}}/>
                        </Card.Section>
                        <div>
                            <div className={classes.info_card}>
                                <Card  withBorder>
                                    <div className={classes.info_row}>
                                        <span style={{paddingRight: '0.5rem'}}>{t("Builder.overviewCredits")}</span>
                                        <Divider orientation="vertical" />
                                        <span style={{paddingLeft: '0.5rem'}}>{totalCredits}</span>
                                    </div>
                                </Card>
                            </div>
                            <div className={classes.info_card}>
                                <Card  withBorder>
                                    <div className={classes.info_row}>
                                        <span style={{paddingRight: '0.5rem'}}>{t("Builder.overviewTimeDemand")}</span>
                                        <Divider orientation="vertical" />
                                        <div style={{paddingLeft: '0.5rem'}}>
                                            <TimeDemandChip numReviews={5} timeDemand={calcTimeDemand(timeDemand, selectedSubjects.length, totalCredits)}/>
                                        </div>
                                    </div>
                                </Card>
                            </div>
                            <div className={classes.info_card}>
                                <Card  withBorder>
                                    <div className={classes.info_row}>
                                        <span style={{paddingRight: '0.5rem'}}>{t("Builder.overviewDifficulty")}</span>
                                        <Divider orientation="vertical" />
                                        <div style={{paddingLeft: '0.5rem'}}>
                                            <DifficultyChip numReviews={5} difficulty={calcDifficulty(difficulty, selectedSubjects.length, totalCredits)}/>
                                        </div>
                                    </div>
                                </Card>
                            </div>
                            <div className={classes.info_card}>
                                <Card withBorder>
                                    <Card.Section>
                                        <p className={classes.subsection_title}>{t("Builder.unlock")}</p>
                                        <Divider />
                                    </Card.Section>
                                    <div className={classes.unlockable_area}>
                                        {
                                            unlockables.map((subject) =>
                                                subjectUnlocked(subject) ?
                                                <UnstyledButton style={{padding: "1rem", color: "#4a90e2"}} component={"a"} href={`/subject/${subject.id}`}>{subject.name}</UnstyledButton>
                                                :
                                                <></>
                                            )
                                        }
                                    </div>
                                </Card>
                            </div>
                        </div>
                    </Card>
                    :
                    <></>
                }
            </div>

            {/* Floating buttons and messages */}

            <FloatingButton text={t('Builder.save')} onClick={saveSchedule} bottom={'6rem'} right={'2rem'} color={"blue.7"}/>
            <FloatingButton text={t('Builder.done')} onClick={()=> {navigate('/')}} bottom={'2rem'} right={'2rem'} color={"green.7"}/>
            {
                savedSuccessfully ?
                <FloatingMessage header={t('Builder.saveSuccessTitle')} text={t('Builder.saveSuccessBody')} bottom={"2rem"} color={"teal"} icon={saveSuccessIcon}/>
                :
                <></>
            }
            {
                savedUnsuccessfully ?
                <FloatingMessage header={t('Builder.saveFailTitle')} text={t('Builder.saveFailBody')} bottom={"2rem"} color={"red"} icon={saveFailIcon}/>
                :
                <></>
            }
        </div>
    )
}

// = = = = = Overview calculations = = = = =
function readjustWithCredits(average: number, credits: number){
    let ret = average;

    if(credits == 0)
        ret += 0;
    else if(credits > 0 && credits < 6)
        ret += 0.1;
    else if(credits >= 6 && credits <= 18)
        ret += 0.5;
    else if(credits > 18 && credits < 24)
        ret += 1;
    else if(credits >= 24 && credits < 27)
        ret += 2;
    else
        ret = 3;

    return Math.min(ret,3);
}
function calcDifficulty(difficultyValue: number, selectedLength: number, totalCredits: number){
    let finalValue = 0;

    if(selectedLength != 0){
        finalValue = (difficultyValue * (totalCredits/24) ) / selectedLength;
    }

    finalValue = readjustWithCredits(finalValue, totalCredits);

    if(finalValue > 0 && finalValue <= 1){
        return "EASY";
    }
    else if(finalValue > 1 && finalValue <= 2){
        return "NORMAL";
    }
    else if(finalValue > 2 && finalValue <= 3){
        return "HARD";
    }
    return "NO-INFO";
}
function calcTimeDemand(difficultyValue: number, selectedLength: number, totalCredits: number){
    let finalValue = 0;
    if(selectedLength != 0){
        finalValue = (difficultyValue * (totalCredits/24) ) / selectedLength;
    }

    finalValue = readjustWithCredits(finalValue, totalCredits);

    if(finalValue > 0 && finalValue <= 1){
        return "LOW";
    }
    else if(finalValue > 1 && finalValue <= 2){
        return "MEDIUM";
    }
    else if(finalValue > 2 && finalValue <= 3){
        return "HIGH";
    }
    return "NO-INFO";
}

//  = = = = = Schedule checkers = = = = =
function timeStringToNumber(time: string) {
    return (parseInt(time.split(":")[0]) - 8) * 2 + (parseInt(time.split(":")[1]) === 30 ? 1 : 0);
}
function isOverlapped(day: number, startTime: string, endTime: string, scheduleArray: number[]){
    const startTimeValue = timeStringToNumber(startTime);
    const endTimeValue = timeStringToNumber(endTime);
    for(let i = startTimeValue; i < endTimeValue; i++){
        if(scheduleArray[day + i * COLS] != 0){
            return true;
        }
    }
    return false;
}


//  = = = = = Misc. = = = = =


function removeInvalidSubjects(subjects: Subject[]) {
    return subjects.filter((subject) => subject.credits != 0);
}

async function getAllSubjects(serviceGet: (userId:number, page:number) => Promise<any>, userId: number, navigate: NavigateFunction) {

    const subjects: Subject[] = []

    let page = 1;
    let gotAllPages = false;
    while (!gotAllPages) {
        const resp = handlePagedService(await serviceGet(userId, page), navigate);
        if (!resp) {return;}
        const [data, nextPage] = resp;

        subjects.push(...removeInvalidSubjects(data != "" ? data : [])); // TODO: cambiar esto a algo mejor

        if (page == nextPage) {
            gotAllPages = true;
        } else {
            page += 1;
        }
    }

    return subjects;
}
