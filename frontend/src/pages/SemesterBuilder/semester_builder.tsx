import {useState} from "react";
import classes from "../SemesterBuilder/semester_builder.module.css";
import {Navbar} from "../../components/navbar/navbar.tsx";
import {ActionIcon, Card, Divider, Select} from "@mantine/core";
import Subject from "../../models/Subject.ts";
import BuilderSubjectCard from "../../components/builder-subject-card/builder_subject_card.tsx";
import BuilderSelectedCard from "../../components/builder-selected-card/builder_selected_card.tsx";
import {SelectedSubject, subjectToSelectedSubject} from "../../models/SelectedSubject.ts";
import DifficultyChip from "../../components/difficulty-chip/difficulty-chip.tsx";
import TimeDemandChip from "../../components/time-demand-chip/time-demand-chip.tsx";
import {IconCalendarEvent, IconList, IconX} from "@tabler/icons-react";
import WeeklySchedule from "../../components/schedule/weekly-schedule.tsx";
import BuilderSelectClassCard from "../../components/builder-select-class-card/builder_select_class_card.tsx";

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
        difficulty: "EASY",
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
                idSubject: "11.15",
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
            idSubject: "11.15",
            professors: ["Hola"],
            idClass: "A",
            locations: [
                {
                    day: 2,
                    startTime: "9:00",
                    endTime: "12:00",
                    classNumber: "101F",
                    building: "SDF",
                    mode: "Presencial"
                },
                {
                    day: 3,
                    startTime: "15:00",
                    endTime: "19:00",
                    classNumber: "101F",
                    building: "SDF",
                    mode: "Presencial"
                }
            ]
        }],
        difficulty: "EASY",
        timeDemand: "LOW",
        reviewCount: 11,
        prerequisites: []
    }
]
const dummySelected: SelectedSubject[] = []

export default function SemesterBuilder() {
    // Available
    const [available, setAvailable] = useState<Subject[]>(dummySubjects);

    // Select class
    const [selectClass, setSelectClass] = useState<Subject>();

    // Selected
    const [selectedSubjects, setSelectedSubjects] = useState<SelectedSubject[]>(dummySelected);

    // Overview
    const [totalCredits, setTotalCredits] = useState(0);
    const [timeDemand, setTimeDemand] = useState("NO-INFO");
    const [difficulty, setDifficulty] = useState("NO-INFO");
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

    const selectClassCallback = (idClass: string) => {
        if(!selectClass){
            return;
        }

        const selectedClass = selectClass.classes.find((c) => c.idClass == idClass)
        if(selectedClass){
            // Agrego a selected
            const newSelected = [...selectedSubjects]

            newSelected.push(subjectToSelectedSubject(selectClass, idClass))

            setSelectedSubjects(newSelected)
            setShowClassSelect(false);

            // Elimino de available
            const newAvailable = [...available]
            setAvailable(newAvailable.filter((s) => s.id != selectClass.id))
        }
    }

    const removeSubject = (id: string, classId: string) => {
        // TODO
    }

    const showScheduleAction = () => {
        setShowSchedule(!showSchedule);
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
                                <WeeklySchedule rows={29} cols={7} subjectClasses={selectedSubjects}/>
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
                                            <TimeDemandChip numReviews={5} timeDemand={timeDemand}/>
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
                                            <DifficultyChip numReviews={5} difficulty={difficulty}/>
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