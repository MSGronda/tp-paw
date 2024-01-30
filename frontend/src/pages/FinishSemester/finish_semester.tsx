import classes from "./finish_semester.module.css";
import {Button, Card, Checkbox, Divider} from "@mantine/core";
import {useEffect, useState} from "react";
import {Subject} from "../../models/Subject.ts";
import {subjectService, userService} from "../../services";
import {useNavigate} from "react-router-dom";
import FloatingMessage from "../../components/floating-message/floating_message.tsx";
import {t} from "i18next";
import {IconX} from "@tabler/icons-react";
import {rem} from "@mantine/core";
import {handleService} from "../../handlers/serviceHandler.tsx";
import Title from "../../components/title/title.tsx";

export default function FinishSemester() {
    // Navigation
    const navigate = useNavigate();

    const [subjects, setSubjects] = useState<Subject[]>([]);
    const getSubjects = async () => {
        const userId = userService.getUserId();
        if(!userId)
            navigate('/login')

        const resp = await subjectService.getUserPlanSubjects(userId)
        const data = handleService(resp, navigate);

        if(data == ""){
            navigate('/home');
        }
        setSubjects(data);
    }
    useEffect(() => {
        getSubjects()
    }, []);

    const [completedSubjects, setCompletedSubjects] = useState<string[]>([])
    const [savedUnsuccessfully, setSavedUnsuccessfully] = useState(false);

    const saveFailIcon = <IconX style={{ width: rem(20), height: rem(20) }} />;

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
        const userId = userService.getUserId();
        if(!userId)
            navigate('/login')

        const respSemester = await userService.completeSemester(userId);
        const respProgress = await userService.setFinishedSubjects(userId, completedSubjects, [])
        if(respSemester && respSemester.status == 202 && respProgress && respProgress.status == 202){
            navigate('/home');
        }

        else {
            setSavedUnsuccessfully(true);
            setTimeout(() => {setSavedUnsuccessfully(false)}, 3000);
        }
    }

    return (
        <div className={classes.general_area}>
            <Title text={t("BuilderFinish.title")}/>
                <Card className={classes.card_area}>
                    <Card.Section>
                        <h3 className={classes.section_title}>{t("BuilderFinish.select")}</h3>
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
                                {t("BuilderFinish.cancel")}
                            </Button>
                            <Button onClick={submit} size={"md"} color={"green.7"}>
                                {t("BuilderFinish.submit")}
                            </Button>
                        </div>
                    </Card.Section>
                </Card>

            {/* Floating messages */}

            {
                savedUnsuccessfully ?
                    <FloatingMessage header={t('BuilderFinish.saveFailTitle')} text={t('BuilderFinish.saveFailBody')} bottom={"2rem"} color={"red"} icon={saveFailIcon}/>
                    :
                    <></>
            }
        </div>
    )
}