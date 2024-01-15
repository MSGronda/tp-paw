import {useContext} from "react";
import AuthContext from "../../context/AuthContext.tsx";
import Landing from "../Landing/landing.tsx";
import {useTranslation} from "react-i18next";
import classes from "./Subject.module.css";
import {Card, Tabs, Text, rem} from '@mantine/core';
import {IconPhoto} from "@tabler/icons-react";
import Subject from "../../models/Subject.ts";
import {Grid} from "@mantine/core/lib";



export function SubjectInfo() {
    const { t } = useTranslation();
    const iconStyle = { width: rem(12), height: rem(12) };
    const subject: Subject = {
        id: "12.09",
        name: "Química",
        credits: 3,
        difficulty: "EASY",
        prerequisites: [],
        timeDemand: "LOW",
        reviewCount: 0,
        department: "Ciencias Exactas y Naturales",
    }
    return (
        <div className={classes.container50}>
            <div className={classes.breadcrumbArea}>

            </div>
            <div className={classes.editDeleteButtons}>
                <Text> {subject.name} - {subject.id}</Text>
                <></>
            </div>
            <Card className={classes.mainBody}>
                <Tabs defaultValue="general">
                    <Tabs.List>
                        <Tabs.Tab value="general" leftSection={<IconPhoto style={iconStyle} />}> {t("Subject.general")}  </Tabs.Tab>
                        <Tabs.Tab value="times-panel" leftSection={<IconPhoto style={iconStyle} />}> {t("Subject.times")} </Tabs.Tab>
                        <Tabs.Tab value="professors-panel" leftSection={<IconPhoto style={iconStyle} />}> {t("Subject.classProf")} </Tabs.Tab>
                    </Tabs.List>


                </Tabs>
            </Card>
        </div>
    );
}

export function SubjectScreen() {
    const authContext = useContext(AuthContext);
    const isLoggedIn = authContext.isAuthenticated;
    return (
        isLoggedIn ? <SubjectInfo/> : <Landing/>
    );
}

function getSubjectPrereqs(subject: Subject){
    const prereqs: JSX.Element[] = [];
    subject.prerequisites.forEach((item) => {
            prereqs.push(
                <a href='<c:url value="/subject/${prereq.id}"/>'>{item}</a>
            );
    }
    )
    return prereqs;
}