import {useState} from "react";
import classes from "../SemesterBuilder/semester_builder.module.css";
import {Navbar} from "../../components/navbar/navbar.tsx";
import {ActionIcon, Card, Divider, Select} from "@mantine/core";
import {Subject, getDifficultyValue, getTimeDemandValue} from "../../models/Subject.ts";
import BuilderSubjectCard from "../../components/builder-subject-card/builder_subject_card.tsx";
import BuilderSelectedCard from "../../components/builder-selected-card/builder_selected_card.tsx";
import {SelectedSubject, selectedSubjectToSubject, subjectToSelectedSubject} from "../../models/SelectedSubject.ts";
import DifficultyChip from "../../components/difficulty-chip/difficulty-chip.tsx";
import TimeDemandChip from "../../components/time-demand-chip/time-demand-chip.tsx";
import {IconCalendarEvent, IconList, IconX} from "@tabler/icons-react";
import WeeklySchedule from "../../components/schedule/weekly-schedule.tsx";
import BuilderSelectClassCard from "../../components/builder-select-class-card/builder_select_class_card.tsx";
import Class from "../../models/Class.ts";

const dummySubjects: Subject[] = [
    {
        id: "11.15",
        name: "Metodos Numericos Avanzados",
        department: "Informatica",
        credits: 6,
        classes: [
            {
                idSubject: "11.15",
                professors: ["Hola"],
                idClass: "A",
                locations: [
                    {
                        day: 1,
                        startTime: "8:00",
                        endTime: "10:00",
                        classNumber: "101F",
                        building: "SDF",
                        mode: "Presencial"
                    },
                    {
                        day: 2,
                        startTime: "8:00",
                        endTime: "10:00",
                        classNumber: "101F",
                        building: "SDF",
                        mode: "Presencial"
                    }
                ]
            }
        ],
        difficulty: "HARD",
        timeDemand: "LOW",
        reviewCount: 5,
        prerequisites: ["11.14"]
    },
    {
        id: "11.16",
        name: "Redes de Informacion",
        department: "Informatica",
        credits: 6,
        classes: [
            {
                idSubject: "11.16",
                professors: ["Hola"],
                idClass: "Z",
                locations: [
                    {
                        day: 4,
                        startTime: "8:00",
                        endTime: "10:00",
                        classNumber: "101F",
                        building: "SDF",
                        mode: "Presencial"
                    },
                    {
                        day: 6,
                        startTime: "8:00",
                        endTime: "10:00",
                        classNumber: "101F",
                        building: "SDF",
                        mode: "Presencial"
                    }
                ]
            }
        ],
        difficulty: "HARD",
        timeDemand: "HIGH",
        reviewCount: 3,
        prerequisites: ["11.15"]
    },
    {
        id: "11.17",
        name: "Formacion para Emprendedores",
        department: "Economia",
        credits: 3,
        classes: [ {
            idSubject: "11.17",
            professors: ["Hola"],
            idClass: "A",
            locations: [
                {
                    day: 2,
                    startTime: "10:00",
                    endTime: "12:00",
                    classNumber: "101F",
                    building: "SDF",
                    mode: "Presencial"
                }
            ]
        }],
        difficulty: "HARD",
        timeDemand: "LOW",
        reviewCount: 11,
        prerequisites: []
    }
]
const dummySelected: SelectedSubject[] = []

const COLS = 7
const ROWS = 29

function timeStringToNumber(time: string) {
    return (parseInt(time.split(":")[0]) - 8)*2 + (parseInt(time.split(":")[1]) === 30 ? 1 : 0);
}


export default function SemesterBuilder() {
    // Available
    const [available, setAvailable] = useState<Subject[]>(dummySubjects);

    // Select class
    const [selectClass, setSelectClass] = useState<Subject>();

    // Selected
    const [selectedSubjects, setSelectedSubjects] = useState<SelectedSubject[]>(dummySelected);
    const [scheduleArray, setScheduleArray] = useState<number[]>(new Array(ROWS * COLS).fill(0));

    // Overview
    const [totalCredits, setTotalCredits] = useState(0);
    const [timeDemand, setTimeDemand] = useState(0);
    const [difficulty, setDifficulty] = useState(0);
    const [unlocked, setUnlocked] = useState<Subject[]>([]);

    // Conditional rendering
    const [showSchedule, setShowSchedule] = useState(false);
    const [showClassSelect, setShowClassSelect] = useState(false);

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
    const updateCredits = (delta: number) => {
        setTotalCredits(totalCredits + delta);
    }
    const updateTimeDemand = (delta: number) => {
        setTimeDemand(timeDemand + delta);
    }
    const updateDifficulty = (delta: number) => {
        setDifficulty(difficulty + delta)
    }
    const selectClassCallback = (idClass: string) => {
        if(!selectClass){
            return;
        }

        const selectedClass = selectClass.classes.find((c) => c.idClass == idClass)
        if(!selectedClass){
            return;
        }

        // Agrego a selected
        const newSelected = [...selectedSubjects]
        newSelected.push(subjectToSelectedSubject(selectClass, idClass))
        setSelectedSubjects(newSelected)

        setShowClassSelect(false);

        // Elimino de available
        const newAvailable = [...available]
        setAvailable(newAvailable.filter((s) => s.id != selectClass.id))

        // Update los stats
        updateCredits(selectClass.credits);
        updateDifficulty(getDifficultyValue(selectClass.difficulty))
        updateTimeDemand(getTimeDemandValue(selectClass.timeDemand))

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
        newAvailable.push(selectedSubjectToSubject(selectedSubject))
        setAvailable(newAvailable)

        // Elimino de selectedSubjects
        const newSelected = [...selectedSubjects]
        setSelectedSubjects(newSelected.filter((s) => s.subject.id != id))

        // Update los stats
        updateCredits(-selectedSubject.subject.credits);
        updateDifficulty(-getDifficultyValue(selectedSubject.subject.difficulty))
        updateTimeDemand(-getTimeDemandValue(selectedSubject.subject.timeDemand))

        // Update schedule array
        updateScheduleArray(selectedSubject.selectedClass, 0);
    }
    const showScheduleAction = () => {
        setShowSchedule(!showSchedule);
    }

    // Schedule checkers
    const checkSubjectEnabled = (id: string): boolean => {
        const subject = available.find((s) => s.id == id)
        if(!subject){
            return false;
        }
        // Para cada comision, me fijo si es viable (no tiene conflictos en los horarios)
        for(const sc of subject.classes){
            let viable = true;          // Comision viable

            for(const time of sc.locations){
                // Si una de los horarios de una clase tiene conflicto, no es viable la comision entera
                if(isOverlapped(time.day, time.startTime, time.endTime, scheduleArray)){
                    viable = false;
                    break;
                }
            }

            if(viable){
                return true;    // Con que una comision sea viable, toda la materia es viable
            }
        }
        return false;
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

    return (
        <div className={classes.general_area}>
            <Navbar />
            <div className={classes.container_95}>

                {/* Available Subjects */}
                {
                    !showClassSelect ?
                    <Card className={classes.available_card} withBorder>
                        <Card.Section>
                            <div className={classes.available_header}>
                                <h4 className={classes.section_titles}>Available Subjects</h4>
                                <Select
                                    data={['Name', 'Credits', 'Difficulty', 'Time Demand']}
                                    defaultValue="Name"
                                    allowDeselect={false}
                                />
                            </div>
                            <Divider/>
                        </Card.Section>
                        <div className={classes.available_subjects_list}>
                            {available.map((subject) => (
                                <BuilderSubjectCard
                                    subject={subject}
                                    selectionCallback={selectSubject}
                                    enabled={checkSubjectEnabled(subject.id)}
                                />
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
                                    <h4 className={classes.section_titles}>Select a class for {selectClass?.name}</h4>
                                    <ActionIcon variant="default" >
                                        <IconX style={{ width: '70%', height: '70%' }} stroke={1.5} onClick={closeClassSelect}/>
                                    </ActionIcon>
                                </div>
                                <Divider/>
                            </Card.Section>
                            <div className={classes.selected_list}>
                                {selectClass?.classes.map((subjectClass) => (
                                    <BuilderSelectClassCard
                                        subjectClass={subjectClass}
                                        addClassCallback={selectClassCallback}
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
                                    <h4 className={classes.section_titles}>Your timetable</h4>
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
                                <h4 className={classes.section_titles}>Selected Subjects</h4>
                                <ActionIcon variant="default" onClick={showScheduleAction}>
                                    <IconCalendarEvent style={{ width: '70%', height: '70%' }} stroke={1.5} />
                                </ActionIcon>
                            </div>
                            <Divider/>
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
                    <Card className={classes.selected_card} withBorder>
                        <Card.Section>
                            <div className={classes.selected_header}>
                                <h4 className={classes.section_titles}>Your semester overview</h4>
                            </div>
                            <Divider/>
                        </Card.Section>
                        <div>
                            <div className={classes.info_card}>
                                <Card  withBorder>
                                    <div className={classes.info_row}>
                                        <span style={{paddingRight: '0.5rem'}}>Number of credits</span>
                                        <Divider orientation="vertical" />
                                        <span style={{paddingLeft: '0.5rem'}}>{totalCredits}</span>
                                    </div>
                                </Card>
                            </div>
                            <div className={classes.info_card}>
                                <Card  withBorder>
                                    <div className={classes.info_row}>
                                        <span style={{paddingRight: '0.5rem'}}>Time Demand</span>
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
                                        <span style={{paddingRight: '0.5rem'}}>Overall difficulty</span>
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
                                        <p className={classes.subsection_title}>By doing this semester you unlock</p>
                                        <Divider />
                                    </Card.Section>
                                    <div className={classes.unlockable_area}>
                                    </div>
                                </Card>
                            </div>
                        </div>
                    </Card>
                    :
                    <></>
                }
            </div>
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
