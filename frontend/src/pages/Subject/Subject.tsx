import {useContext} from "react";
import AuthContext from "../../context/AuthContext.tsx";
import Landing from "../Landing/landing.tsx";
import {useTranslation} from "react-i18next";
import classes from "./Subject.module.css";
import {Card, Tabs, Text, rem, Table, Badge} from '@mantine/core';
import {IconPhoto} from "@tabler/icons-react";
import {Subject} from "../../models/Subject.ts";
import {Navbar} from "../../components/navbar/navbar.tsx";
import Class from "../../models/Class.ts";
import ClassTime from "../../models/ClassTime.ts";



export function SubjectInfo() {
    const { t } = useTranslation();
    const iconStyle = { width: rem(12), height: rem(12) };
    const classTime: ClassTime = {
        day: 1,
        startTime: "12:00",
        endTime: "14:00",
        classNumber: "102R",
        building: "SDR",
        mode: "Presencial",
    };
    const classTime2: ClassTime = {
        day: 3,
        startTime: "10:00",
        endTime: "12:00",
        classNumber: "203F",
        building: "SDF",
        mode: "Presencial",
    };
    const classTime3: ClassTime = {
        day: 5,
        startTime: "8:00",
        endTime: "10:00",
        classNumber: "-",
        building: "-",
        mode: "Virtual",
    };
    const subjectClass: Class = {
        idSubject: "12.09",
        idClass: "S",
        professors: ["John Doe","Magnus Mefisto","Melaco Motoda"],
        locations: [classTime, classTime2, classTime3],
    };
    const subjectClass2: Class = {
        idSubject: "12.09",
        idClass: "S1",
        professors: ["Random Name","Magnus Mefisto","Ugly Uhhh Dude"],
        locations: [classTime, classTime2, classTime3],
    };
    const subject: Subject = {
        id: "12.09",
        name: "Química",
        credits: 3,
        difficulty: "",
        prerequisites: [],
        timeDemand: "HIGH",
        reviewCount: 0,
        department: "Ciencias Exactas y Naturales",
        classes: [subjectClass, subjectClass2],
    }
    const prereqs: Subject[] = [subject, subject, subject];
    const professors: string[] = ["John Doe","Magnus Mefisto","Melaco Motoda","John Johnson","Juan Johnson","asadasd, asdADA"];
    return (
        <>
            <Navbar/>
            <div className={classes.container}>

                <div className={classes.background}>
                    <div className={classes.breadcrumbArea}>

                    </div>
                    <div className={classes.editDeleteButtons}>
                        <Text size="xl" fw={500}> {subject.name} - {subject.id}</Text>
                        <></>
                    </div>
                    <Card className={classes.mainBody}>
                        <Tabs defaultValue="general">
                            <Tabs.List>
                                <Tabs.Tab value="general" leftSection={<IconPhoto style={iconStyle} />}> {t("Subject.general")}  </Tabs.Tab>
                                <Tabs.Tab value="times-panel" leftSection={<IconPhoto style={iconStyle} />}> {t("Subject.times")} </Tabs.Tab>
                                <Tabs.Tab value="professors-panel" leftSection={<IconPhoto style={iconStyle} />}> {t("Subject.classProf")} </Tabs.Tab>
                            </Tabs.List>

                            <Tabs.Panel value="general">
                                <Table>
                                    <Table.Tbody>
                                        <Table.Tr>
                                            <Table.Th>{t("Subject.department")}</Table.Th>
                                            <Table.Td>{subject.department}</Table.Td>
                                        </Table.Tr>
                                        <Table.Tr>
                                            <Table.Th>{t("Subject.credits")} </Table.Th>
                                            <Table.Td>{subject.credits}</Table.Td>
                                        </Table.Tr>
                                        <Table.Tr>
                                            <Table.Th>{t("Subject.prerequisites")}</Table.Th>
                                            <Table.Td>
                                                {prereqs.length === 0? <>{t("Subject.emptyPrerequisites")}</> : <></>}
                                                {getSubjectPrereqs(prereqs)}
                                            </Table.Td>
                                        </Table.Tr>
                                        <Table.Tr>
                                            <Table.Th>{t("Subject.professors")}</Table.Th>
                                            <Table.Td>
                                                {professors.length === 0? <>{t("Subject.emptyProfessors")}</> : <></>}
                                                {getProfessors(professors)}
                                            </Table.Td>
                                        </Table.Tr>
                                        <Table.Tr>
                                            <Table.Th>{t("Subject.difficulty")}</Table.Th>
                                            <Table.Td>
                                                { (() => {
                                                    {
                                                        switch (subject.difficulty) {
                                                            case "EASY":
                                                                return <Badge color="green"> {t("SubjectCard.easy")} </Badge>;
                                                            case "NORMAL":
                                                                return <Badge color="blue"> {t("SubjectCard.normal")} </Badge>;
                                                            case "HARD":
                                                                return <Badge color="red"> {t("SubjectCard.hard")} </Badge>;
                                                            default:
                                                                return <Badge color="gray"> {t("SubjectCard.no_reviews")} </Badge>;
                                                        }
                                                    }
                                                })()}
                                            </Table.Td>
                                        </Table.Tr>
                                        <Table.Tr>
                                            <Table.Th>{t("Subject.time")}</Table.Th>
                                            <Table.Td>
                                                { (() => {
                                                    {
                                                        switch (subject.timeDemand) {
                                                            case "LOW":
                                                                return <Badge color="green"> {t("SubjectCard.low")} </Badge>;
                                                            case "MEDIUM":
                                                                return <Badge color="blue"> {t("SubjectCard.medium")} </Badge>;
                                                            case "HIGH":
                                                                return <Badge color="red"> {t("SubjectCard.high")} </Badge>;
                                                            default:
                                                                return <Badge color="gray"> {t("SubjectCard.no_info")} </Badge>;
                                                        }
                                                    }
                                                })()}
                                            </Table.Td>
                                        </Table.Tr>
                                    </Table.Tbody>
                                </Table>
                            </Tabs.Panel>

                            <Tabs.Panel value="times-panel">
                                <Table>
                                    <Table.Thead>
                                        <Table.Tr>
                                            <Table.Th><Text fw={600}>{t("Subject.classCode")}</Text></Table.Th>
                                            <Table.Th><Text fw={600}>{t("Subject.classDay")}</Text></Table.Th>
                                            <Table.Th><Text fw={600}>{t("Subject.classTime")}</Text></Table.Th>
                                            <Table.Th><Text fw={600}>{t("Subject.classMode")}</Text></Table.Th>
                                            <Table.Th><Text fw={600}>{t("Subject.classNumber")}</Text></Table.Th>
                                        </Table.Tr>
                                    </Table.Thead>
                                    <Table.Thead>
                                        {subject.classes.map((item) => (
                                            item.locations.map((classtime, index) => (
                                                <Table.Tr>
                                                    <Table.Td>
                                                        { index === 0 ? item.idClass : ""}
                                                    </Table.Td>
                                                    { classtime.day >= 1 && classtime.day <= 7 ?
                                                        <Table.Td>{t(getDayOfTheWeek(classtime.day))}</Table.Td> :
                                                        <Table.Td> - </Table.Td>
                                                    }
                                                    <Table.Td>{classtime.startTime} - {classtime.endTime}</Table.Td>
                                                    <Table.Td>{classtime.mode}</Table.Td>
                                                    <Table.Td>{classtime.building}</Table.Td>
                                                    <Table.Td>{classtime.classNumber}</Table.Td>
                                                </Table.Tr>
                                            ))
                                        ))}
                                    </Table.Thead>
                                </Table>
                            </Tabs.Panel>

                            <Tabs.Panel value="professors-panel">
                                <Table>
                                    <Table.Thead>
                                        <Table.Tr>
                                            <Table.Th>{t("Subject.classCode")}</Table.Th>
                                            <Table.Th>{t("Subject.classProf")}</Table.Th>
                                        </Table.Tr>
                                    </Table.Thead>
                                    <Table.Tbody>
                                        {subject.classes.map((item) => (
                                            <Table.Tr>
                                                <Table.Td>{item.idClass}</Table.Td>
                                                <Table.Td>
                                                    {item.professors.map((professor) => (
                                                        <Badge color="blue">{professor}</Badge>
                                                    ))}
                                                </Table.Td>
                                            </Table.Tr>
                                        ))}
                                    </Table.Tbody>
                                </Table>
                            </Tabs.Panel>
                        </Tabs>
                    </Card>
                </div>

            </div>
        </>
    );
}

export function SubjectScreen() {
    const authContext = useContext(AuthContext);
    const isLoggedIn = authContext.isAuthenticated;
    return (
        isLoggedIn ? <SubjectInfo/> : <Landing/>
    );
}

function getSubjectPrereqs(prereqs: Subject[]){
    const prereqsComponents: JSX.Element[] = [];
    let i = 0;
    prereqs.forEach((item) => {
            prereqsComponents.push(
                <a href={"/subject/" + item.id}>{item.name}</a>
            );
            if (i !== prereqs.length - 1) {
                prereqsComponents.push(
                    <> , </>
                );
            }
            i++;
    }
    )
    return prereqsComponents;
}

function getProfessors(professors: string[]){
    const professorsComponents: JSX.Element[] = [];
    professors.forEach((professor) => {
            professorsComponents.push(
                <Badge color="blue">{professor}</Badge>
            );
            professorsComponents.push(<> </>);
        }
    )
    return professorsComponents;
}

function getDayOfTheWeek(day: number){
    return "TimeTable.day"+day;
}

