import {useState} from "react";
import classes from "../SemesterBuilder/semester_builder.module.css";
import {Navbar} from "../../components/navbar/navbar.tsx";
import {Card, Divider, Select} from "@mantine/core";
import Subject from "../../models/Subject.ts";
import BuilderSubjectCard from "../../components/builder-subject-card/builder_subject_card.tsx";
import BuilderSelectedCard from "../../components/builder-selected-card/builder_selected_card.tsx";
import SelectedSubject from "../../models/SelectedSubject.ts";

const dummySubjects: Subject[] = [
    {
        id: "11.15",
        name: "Metodos Numericos Avanzados",
        department: "Informatica",
        credits: 6,
        classes: [],
        difficulty: "EASY",
        timeDemand: "LOW",
        reviewCount: 5,
        prerequisites: ["11.14"]
    },
    {
        id: "11.15",
        name: "Redes de Informacion",
        department: "Informatica",
        credits: 6,
        classes: [],
        difficulty: "HARD",
        timeDemand: "HIGH",
        reviewCount: 3,
        prerequisites: ["11.15"]
    },
    {
        id: "11.15",
        name: "Formacion para Emprendedores",
        department: "Economia",
        credits: 3,
        classes: [],
        difficulty: "EASY",
        timeDemand: "LOW",
        reviewCount: 11,
        prerequisites: []
    },
    {
        id: "11.15",
        name: "Formacion para Emprendedores",
        department: "Economia",
        credits: 3,
        classes: [],
        difficulty: "EASY",
        timeDemand: "LOW",
        reviewCount: 11,
        prerequisites: []
    },    {
        id: "11.15",
        name: "Formacion para Emprendedores",
        department: "Economia",
        credits: 3,
        classes: [],
        difficulty: "EASY",
        timeDemand: "LOW",
        reviewCount: 11,
        prerequisites: []
    },    {
        id: "11.15",
        name: "Formacion para Emprendedores",
        department: "Economia",
        credits: 3,
        classes: [],
        difficulty: "EASY",
        timeDemand: "LOW",
        reviewCount: 11,
        prerequisites: []
    },    {
        id: "11.15",
        name: "Formacion para Emprendedores",
        department: "Economia",
        credits: 3,
        classes: [],
        difficulty: "EASY",
        timeDemand: "LOW",
        reviewCount: 11,
        prerequisites: []
    },    {
        id: "11.15",
        name: "Formacion para Emprendedores",
        department: "Economia",
        credits: 3,
        classes: [],
        difficulty: "EASY",
        timeDemand: "LOW",
        reviewCount: 11,
        prerequisites: []
    },
    {
        id: "11.15",
        name: "Formacion para Emprendedores",
        department: "Economia",
        credits: 3,
        classes: [],
        difficulty: "EASY",
        timeDemand: "LOW",
        reviewCount: 11,
        prerequisites: []
    },    {
        id: "11.15",
        name: "Formacion para Emprendedores",
        department: "Economia",
        credits: 3,
        classes: [],
        difficulty: "EASY",
        timeDemand: "LOW",
        reviewCount: 11,
        prerequisites: []
    },    {
        id: "11.15",
        name: "Formacion para Emprendedores",
        department: "Economia",
        credits: 3,
        classes: [],
        difficulty: "EASY",
        timeDemand: "LOW",
        reviewCount: 11,
        prerequisites: []
    },    {
        id: "11.15",
        name: "Formacion para Emprendedores",
        department: "Economia",
        credits: 3,
        classes: [],
        difficulty: "EASY",
        timeDemand: "LOW",
        reviewCount: 11,
        prerequisites: []
    },    {
        id: "11.15",
        name: "Formacion para Emprendedores",
        department: "Economia",
        credits: 3,
        classes: [],
        difficulty: "EASY",
        timeDemand: "LOW",
        reviewCount: 11,
        prerequisites: []
    },    {
        id: "11.15",
        name: "Formacion para Emprendedores",
        department: "Economia",
        credits: 3,
        classes: [],
        difficulty: "EASY",
        timeDemand: "LOW",
        reviewCount: 11,
        prerequisites: []
    },
]
const dummySelected: SelectedSubject[] = [
    {
        id: "11.15",
        name: "Metodos Numericos Avanzados",
        department: "AAAAA",
        credits: 6,
        className: "A",
        times: [
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
    },
    {
        id: "11.15",
        name: "Redes de Informacion",
        department: "AAAAA",
        credits: 6,
        className: "Z",
        times: [
            {
                day: 3,
                startTime: "10:00",
                endTime: "11:00",
                classNumber: "101F",
                building: "SDF",
                mode: "Presencial"
            },
            {
                day: 4,
                startTime: "8:00",
                endTime: "10:00",
                classNumber: "101F",
                building: "SDF",
                mode: "Presencial"
            }
        ]
    }

]

export default function SemesterBuilder() {

    const [subjects, setSubjects] = useState<Subject[]>(dummySubjects);
    const [selectedSubjects, setSelectedSubjects] = useState<SelectedSubject[]>(dummySelected);

    const selectSubject = (id: string) => {
        // TODO
    }
    const removeSubject = (id: string, classId: string) => {
        // TODO
    }

    return (
        <div className={classes.general_area}>
            <Navbar />
            <div className={classes.container_95}>

                {/* Available Subjects */}

                <Card className={classes.available_card} withBorder>
                    <Card.Section>
                        <div className={classes.available_header}>
                            <h4 className={classes.section_titles}>Available Subjects</h4>
                            <Select
                                data={['Name', 'Credits', 'Difficulty', 'Time Demand']}
                                defaultValue="Name"
                                style={{paddingRight: "1rem"}}
                                allowDeselect={false}
                            />
                        </div>
                        <Divider/>
                    </Card.Section>
                    <div className={classes.available_subjects_list}>
                        {subjects.map((subject) => (
                            <BuilderSubjectCard
                                subject={subject}
                                selectionCallback={selectSubject}
                            />
                        ))}
                    </div>
                </Card>

                {/* Selected Subjects */}

                <Card className={classes.selected_card} withBorder>
                    <Card.Section>
                        <div className={classes.selected_header}>
                            <h4 className={classes.section_titles}>Selected Subjects</h4>
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

                {/* Overview */}

                <Card className={classes.selected_card} withBorder>
                    <Card.Section>
                        <div className={classes.selected_header}>
                            <h4 className={classes.section_titles}>Your semester overview</h4>
                        </div>
                        <Divider/>
                    </Card.Section>
                    <div>

                    </div>
                </Card>
            </div>
        </div>
    )
}