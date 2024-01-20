import classes from "./finish_semester.module.css";
import {Button, Card, Checkbox, Divider} from "@mantine/core";
import {useState} from "react";
import {Subject} from "../../models/Subject.ts";
import {subjectService, userService} from "../../services";

const dummySubjects: Subject[] = [
    {
        id: "11.11",
        name: "Cacona I",
        department: "Departamento de Cacona",
        credits: 6,
        classes: [],
        difficulty: "EASY",
        timeDemand: "NORMAL",
        reviewCount: 5,
        prerequisites: []
    },
    {
        id: "11.12",
        name: "Cacona II",
        department: "Departamento de Cacona",
        credits: 6,
        classes: [],
        difficulty: "EASY",
        timeDemand: "NORMAL",
        reviewCount: 5,
        prerequisites: []
    },
]

export default function FinishSemester() {
    const [subjects, setSubjects] = useState<Subject[]>(dummySubjects);
    const [completedSubjects, setCompletedSubjects] = useState<string[]>([]);

    const addCompleted = (checked: boolean, id: string) => {
        if(!checked){
            const newCompleted = completedSubjects.filter((s) => s == id);
            setCompletedSubjects(newCompleted);
        }
        else{
            if(!completedSubjects.find((s) => s == id)){
                const newCompleted = [...completedSubjects];
                newCompleted.push(id);
                setCompletedSubjects(newCompleted)
            }
        }
    }
    const submit = async () => {
        const resp = await userService.completeSemester(completedSubjects);

    }

    return (
        <div className={classes.general_area}>

                <Card className={classes.card_area}>
                    <Card.Section>
                        <h3 className={classes.section_title}>Select the subjects you passed</h3>
                        <Divider />
                    </Card.Section>
                    <div className={classes.subject_list}>
                        {
                            subjects.map((subject) =>
                                <div className={classes.subject_row}>
                                    <span className={classes.subject_name}>{subject.name} - {subject.id}</span>
                                    <Checkbox onChange={
                                        (event) => {addCompleted(event.currentTarget.checked, subject.id)}
                                    }/>
                                </div>
                            )
                        }
                    </div>
                    <Card.Section>
                        <Divider />
                        <div className={classes.button_row}>
                            <Button size={"md"} variant={"default"}>
                                Cancel
                            </Button>
                            <Button onClick={submit} size={"md"} color={"green.7"}>
                                Submit
                            </Button>
                        </div>
                    </Card.Section>
                </Card>
        </div>
    )
}