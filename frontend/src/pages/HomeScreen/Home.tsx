import {
    Button,
    Card,
    Divider,
    Flex,
    Grid,
    Group,
    rem, RingProgress,
    Tabs,
    Text
} from '@mantine/core';
import { BarChart } from '@mantine/charts';
import {Navbar } from "../../components/navbar/navbar";
import classes from './home.module.css';
import {IconCheck, IconMessageCircle, IconPencil, IconPhoto, IconSettings} from "@tabler/icons-react";
import {useTranslation} from "react-i18next";
import SubjectCard from "../../components/subject-card/subject-card.tsx";
import { useContext, useEffect, useState } from 'react';
import AuthContext from '../../context/AuthContext.tsx';
import Landing from '../Landing/landing.tsx';
import {Subject} from "../../models/Subject.ts";
import TimeTable from "../../components/time-table/time-table.tsx";
import {Link, useNavigate} from "react-router-dom";
import ClassInfoCard from '../../components/class-info-card/class-info-card.tsx';
import Class from '../../models/Class.ts';
import ClassTime from '../../models/ClassTime.ts';
import { subjectService, userService } from '../../services/index.tsx';
import { handleService } from '../../handlers/serviceHandler.tsx';


export default function Home() {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const iconStyle = { width: rem(12), height: rem(12) };

    const [pastSubjects, setPastSubjects] = useState([]);
    const [futureSubjects, setFutureSubjects] = useState([]);

    const searchPastSubjects = async (userId: number) => {
        const res = await subjectService.getDoneSubjects(userId);
        const data = handleService(res,navigate);
        if(res) {
            setPastSubjects(data);
        }
    }

    const searchFutureSubjects = async (userId: number) => {
        const res = await subjectService.getUnlockableSubjects(userId);
        const data = handleService(res,navigate);
        if(res) {
            setFutureSubjects(data);
        }
    }

    useEffect(() => {
        const userId = userService.getUserId();
        searchPastSubjects(userId);
        searchFutureSubjects(userId);
    },[]);

    const data = [
        { year: t("Home.firstYear"), progress: 100},
        { year: t("Home.secondYear"), progress: 82},
        { year: t("Home.thirdYear"), progress: 100},
        { year: t("Home.fourthYear"), progress: 50},
        { year: t("Home.fifthYear"), progress: 7},
    ];

    const userSemester: Subject[] = [
        {id: "72.40", name: "Ingeniería en Software II", department: "Ohio Department", credits:3, classes: [{idSubject: "72.40", idClass: "1", professors: ["Juan Martín Sotuyo Dodero"], locations: [{day: 1, startTime: "19:00", endTime: "22:00", location: "701F", building: "Sede Distrito Financiero", mode: "Presencial"} as ClassTime]} as Class], difficulty: "1", timeDemand: "0", reviewCount: 3, prerequisites: ["72.37"]} as Subject,
    ];
    return (
        <div className={classes.background}>
            <Navbar/>
            <div className={classes.containter}>
                <div className={classes.dashboardArea}>
                    <div className={classes.choosingArea}>
                        <Tabs defaultValue="current-semester" className={classes.tabs}>
                            <Tabs.List>
                                <Tabs.Tab value="current-semester" leftSection={<IconPhoto style={iconStyle} />}>
                                    {t("Home.currentSemester")}
                                </Tabs.Tab>
                                <Tabs.Tab value="overview" leftSection={<IconMessageCircle style={iconStyle} />}>
                                    {t("Home.overview")}
                                </Tabs.Tab>
                                <Tabs.Tab value="future-subjects" leftSection={<IconSettings style={iconStyle} />}>
                                    {t("Home.futureSubjects")}
                                </Tabs.Tab>
                                <Tabs.Tab value="past-subjects" leftSection={<IconSettings style={iconStyle} />}>
                                    {t("Home.pastSubjects")}
                                </Tabs.Tab>
                            </Tabs.List>

                            <Tabs.Panel value="current-semester">
                                <div className={classes.currentSemesterArea}>
                                    {
                                        userSemester.length !== 0?
                                        <div>
                                            <div className={classes.timeTableArea}>
                                            <TimeTable />
                                            </div>
                                            <div className={classes.currentSemesterClassArea}>
                                                <Card className={classes.currentSemesterCard}>
                                                    <Card.Section>
                                                        <h4>{t("Home.currentSemester")}</h4>
                                                    </Card.Section>
                                                    <Card.Section>
                                                        <div className={classes.currentSemesterSubjectInfoList}>
                                                            {userSemester.map((subject) => (
                                                                <Link to={{pathname:`subject/` + subject.id}}>
                                                                    <ClassInfoCard subject={subject}/>
                                                                </Link>
                                                            ))}
                                                        </div>
                                                    </Card.Section>
                                                </Card>
                                            </div>
                                        </div>
                                            :
                                        <div className={classes.emptyTabArea}>
                                            <h3 className={classes.emptyTabInfo}>
                                                {t("Home.emptySemester")}
                                                <Link to={{pathname:`/builder`}}>
                                                    {t("Home.emptySemesterLink")}
                                                </Link>
                                            </h3>
                                        </div>
                                    }
                                </div>
                                {userSemester.length !== 0 && 
                                    <div className={classes.semesterEditArea}>
                                        <Link to={{pathname: `/builder/finish`}}>
                                            <Button size='lg' color="green" rightSection={<IconCheck size={20} />} className={classes.semesterEditButton}>
                                                {t("Home.finishCurrentSemester")}
                                            </Button>
                                        </Link>
                                        <Link to={{pathname: `/builder`}}>
                                            <Button size='lg' variant='default'  rightSection={<IconPencil size={20} />} className={classes.semesterEditButton}>
                                                {t("Home.editCurrentSemester")}
                                            </Button>
                                        </Link>
                                    </div>
                                }
                            </Tabs.Panel>

                            <Tabs.Panel value="overview">
                                <div className={classes.overviewArea}>
                                    <Flex gap="md"
                                          justify="start"
                                          align="center"
                                          direction="column"
                                          miw={200}>
                                        <Card shadow="xs"
                                              padding="xl"
                                              color="#efefef"
                                              className={classes.cardWidth}
                                        >
                                            <Flex gap="md"
                                                  justify="center"
                                                  align="flex-start"
                                                  direction="column">
                                                <div className={classes.row}>
                                                    <Group>
                                                        <Text fw={700}>
                                                            {t("Home.completedCredits")}
                                                        </Text>
                                                        <Divider orientation="vertical"/>
                                                        <Text fw={700}>
                                                            {getCompletedCredits()}
                                                        </Text>
                                                    </Group>
                                                </div>
                                                <div className={classes.row}>
                                                    <Group>
                                                        <Text fw={700}>
                                                            {t("Home.totalCredits")}
                                                        </Text>
                                                        <Divider orientation="vertical"/>
                                                        <Text fw={700}>
                                                            {getTotalCredits()}
                                                        </Text>
                                                    </Group>
                                                </div>
                                            </Flex>
                                        </Card>
                                    </Flex>
                                    <div className={classes.statRow}>
                                        <Card className={classes.progressCard}>
                                            <div slot={"header"}>
                                                <h3>{t("Home.overallProgress")}</h3>
                                            </div>
                                            <div className={classes.columnCenter}>
                                                <RingProgress
                                                    size={270}
                                                    thickness={19}
                                                    roundCaps
                                                    sections={[
                                                        { value: 40, color: 'blue' },
                                                    ]}
                                                />
                                                <h4>33%{/*userProgressPercentage*/}</h4>
                                            </div>
                                        </Card>
                                        <Card className={classes.progressCard}>
                                            <div slot={"header"}>
                                                <h3>{t("Home.completedCreditsByYear")}</h3>
                                            </div>
                                            <div className={classes.chartContainer}>
                                                <BarChart
                                                    h={300}
                                                    data={data}
                                                    dataKey="year"
                                                    unit="%"
                                                    series={[
                                                        { name: 'progress', color: 'blue.6' },
                                                    ]}
                                                />
                                            </div>
                                        </Card>
                                    </div>
                                </div>
                            </Tabs.Panel>

                            <Tabs.Panel value="future-subjects" >
                                <Grid gutter="sm" columns={12} className={classes.futureSubjectsArea}>
                                    {getSubjectsCards(futureSubjects).map((item) => <Grid.Col span={3}>{item}</Grid.Col>)}
                                </Grid>
                            </Tabs.Panel>

                            <Tabs.Panel value="past-subjects">
                                <Grid gutter="sm">
                                    {getSubjectsCards(pastSubjects).map((item) => <Grid.Col span={3}>{item}</Grid.Col>)}
                                </Grid>
                            </Tabs.Panel>
                        </Tabs>
                    </div>
                </div>
            </div>
        </div>

    );
}

export function HomeScreen() {
    const authContext = useContext(AuthContext);
    const isLoggedIn = authContext.isAuthenticated;
    return (
        isLoggedIn ? <Home/> : <Landing/>
    );
}
const getSubjectsCards = subjects => {
    let content = [];
    for (let subject of subjects) {
        content.push(<SubjectCard id={subject.id} credits={subject.credits} difficulty={subject.difficulty} name={subject.name} numReviews={subject.numReviews} prerequisites={subject.prerequisites} timeDemand={subject.timeDemand} progress={subject.progress} />);
    }
    return content;
};
function getCompletedCredits() {
    return 133;
}

function getTotalCredits() {
    return 244;
}