import {useContext} from "react";
import AuthContext from "../../context/AuthContext.tsx";
import Landing from "../Landing/landing.tsx";
import {useTranslation} from "react-i18next";
import classes from "./Subject.module.css";
import {Card, Tabs, Text, rem, Table, Badge} from '@mantine/core';
import {IconPhoto} from "@tabler/icons-react";
import {Subject} from "../../models/Subject.ts";
import {Navbar} from "../../components/navbar/navbar.tsx";



export function SubjectInfo() {
    const { t } = useTranslation();
    const iconStyle = { width: rem(12), height: rem(12) };
    const subject: Subject = {
        id: "12.09",
        name: "Química",
        credits: 3,
        difficulty: "",
        prerequisites: [],
        timeDemand: "HIGH",
        reviewCount: 0,
        department: "Ciencias Exactas y Naturales",
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

