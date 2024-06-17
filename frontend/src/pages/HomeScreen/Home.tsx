import {
    Button,
    Card,
    Divider,
    Flex,
    Grid,
    Group,
    Pagination,
    RingProgress,
    Tabs,
    Text
} from '@mantine/core';
import { BarChart } from '@mantine/charts';
import {Navbar } from "../../components/navbar/navbar";
import classes from './home.module.css';
import {
    IconCheck,
    IconPencil,
} from "@tabler/icons-react";
import {useTranslation} from "react-i18next";
import SubjectCard from "../../components/subject-card/subject-card.tsx";
import { useContext, useEffect, useState } from 'react';
import AuthContext from '../../context/AuthContext.tsx';
import Landing from '../Landing/landing.tsx';
import {Subject} from "../../models/Subject.ts";
import {Link, useNavigate} from "react-router-dom";
import ClassInfoCard from '../../components/class-info-card/class-info-card.tsx';
import { subjectService, userService } from '../../services/index.tsx';
import { handleService } from '../../handlers/serviceHandler.tsx';
import {SelectedSubject} from "../../models/SelectedSubject.ts";
import {createSelectedSubjects} from "../../utils/user_plan_utils.ts";
import PastSubjectCard from "../../components/past-subject-card/past-subject-card.tsx";
import WeeklySchedule from "../../components/schedule/weekly-schedule.tsx";

const COLS = 7
const ROWS = 29

export function HomeScreen() {
    const authContext = useContext(AuthContext);
    const isLoggedIn = authContext.isAuthenticated;
    return (
        isLoggedIn ? <Home/> : <Landing/>
    );
}

export default function Home() {
    const { t } = useTranslation();
    const navigate = useNavigate();

    const INITIAL_PAGE = 1;

    const [activeTab, setActiveTab] = useState<string | null>("current-semester");

    // = = = Current semester = = =
    const [userSemester, setUserSemester] = useState<SelectedSubject[]>([]);

    const getUserSemester = async () => {
        const userId = userService.getUserId();
        if(!userId)
            navigate('/login');

        const respSubjects = await subjectService.getUserPlanSubjects(userId);
        const dataSubjects = handleService(respSubjects, navigate);

        const respPlan = await userService.getUserPlan(userId);
        const dataPlan = handleService(respPlan, navigate);

        setUserSemester(createSelectedSubjects(dataPlan, dataSubjects.subjects));
    }

    // = = = Overview = = =
    const [completedCredits, setCompletedCredits] = useState(0);
    const [totalCreditsInDegree, setTotalCreditsInDegree] = useState(0);
    const [overallProgress, setOverallProgress] = useState(0.0);
    const [progressByYear, setProgressByYear] = useState([
        { year: t("Home.firstYear"), progress: 0},
        { year: t("Home.secondYear"), progress: 0},
        { year: t("Home.thirdYear"), progress: 0},
        { year: t("Home.fourthYear"), progress: 0},
        { year: t("Home.fifthYear"), progress: 0},
    ])

    // = = = Future subjects = = =
    const [futureSubjects, setFutureSubjects] = useState<Subject[]>([]);
    const [currentFutureSubjectsPage, setCurrentFutureSubjectsPage] = useState(INITIAL_PAGE);
    const [futureSubjectsMaxPage, setFutureSubjectsMaxPage] = useState(1);
    const searchFutureSubjects = async (userId: number, page: number) => {
        const res = await subjectService.getAvailableSubjects(userId,page);
        const data = handleService(res, navigate);
        if(res) {
            setFutureSubjects(data.subjects);
        }
    }

    // = = = Past subjects = = =
    const [pastSubjects, setPastSubjects] = useState<Subject[]>([]);
    const [currentPastSubjectsPage, setCurrentPastSubjectsPage] = useState(INITIAL_PAGE);
    const [pastSubjectsMaxPage, setPastSubjectsMaxPage] = useState(1);
    const searchPastSubjects = async (userId: number, page: number) => {
        const res = await subjectService.getDoneSubjects(userId,page);
        const data = handleService(res,navigate);
        if(res) {
            setPastSubjects(data.subjects);
        }
    }

    // = = = API calls = = =

    useEffect(() => {
        getUserSemester();           // TODO: uncomment
    }, []);

    useEffect(() => {
        const userId = userService.getUserId();
        searchFutureSubjects(userId, currentFutureSubjectsPage);
    },[currentFutureSubjectsPage]);

    useEffect(() => {
        const userId = userService.getUserId();
        searchPastSubjects(userId, currentPastSubjectsPage);
    },[currentPastSubjectsPage]);


    return (
        <div className={classes.background}>
            <Navbar/>
            <div className={classes.containter}>
                <div className={classes.dashboardArea}>
                    <div className={classes.choosingArea}>
                        <Tabs value={activeTab} className={classes.tabs} onChange={(value) => setActiveTab(value)}>
                            <Tabs.List>
                                <Tabs.Tab value="current-semester">
                                    {t("Home.currentSemester")}
                                </Tabs.Tab>
                                <Tabs.Tab value="overview">
                                    {t("Home.overview")}
                                </Tabs.Tab>
                                <Tabs.Tab value="future-subjects">
                                    {t("Home.futureSubjects")}
                                </Tabs.Tab>
                                <Tabs.Tab value="past-subjects">
                                    {t("Home.pastSubjects")}
                                </Tabs.Tab>
                            </Tabs.List>

                            <Tabs.Panel value="current-semester" w="100%">
                                <Flex h="100%" w="100%" justify="center" align="center">
                                    { userSemester.length !== 0?
                                        <div className={classes.currentSemesterArea}>

                                            <div className={classes.timeTableArea}>
                                                <WeeklySchedule rows={ROWS} cols={COLS} subjectClasses={userSemester}/>
                                            </div>

                                            <div className={classes.currentSemesterClassArea}>
                                                <Card  padding={0}>
                                                    <Card.Section w="100%">
                                                        <h4 style={{margin: "0.75rem"}} className={classes.section_titles}>{t("Home.thisSemester")}</h4>
                                                        <Divider/>
                                                    </Card.Section>
                                                    <Card.Section>
                                                        <div style={{display: "flex", flexDirection: "column", alignItems: "center", width: "100%", height: "100%"}}>
                                                            <div style={{maxHeight: "80vh" ,minHeight: "80vh", overflowY: "auto", flex: "1", width: "100%"}}>
                                                                {userSemester.map((subject) => (
                                                                    <div style={{padding: "0.5rem 0.5rem"}}>
                                                                        <Link  to={{pathname:`subject/` + subject.subject.id}}>
                                                                            <ClassInfoCard subject={subject.subject} subjectClass={subject.selectedClass}/>
                                                                        </Link>
                                                                    </div>

                                                                ))}
                                                            </div>
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
                                    {userSemester.length !== 0 &&
                                        <div className={classes.semesterEditArea}>
                                            <Link to={{pathname: `/builder`}}>
                                                <Button size='lg' variant='default'  rightSection={<IconPencil size={20} />} className={classes.semesterEditButton}>
                                                    {t("Home.editCurrentSemester")}
                                                </Button>
                                            </Link>
                                            <Link to={{pathname: `/builder/finish`}}>
                                                <Button size='lg' color="green" rightSection={<IconCheck size={20} />} className={classes.semesterEditButton}>
                                                    {t("Home.finishCurrentSemester")}
                                                </Button>
                                            </Link>
                                        </div>
                                    }
                                </Flex>
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
                                                            {completedCredits}
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
                                                            {totalCreditsInDegree}
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
                                                        { value: overallProgress, color: 'blue' },
                                                    ]}
                                                />
                                                <h4>{overallProgress} %</h4>
                                            </div>
                                        </Card>
                                        <Card className={classes.progressCard}>
                                            <div slot={"header"}>
                                                <h3>{t("Home.completedCreditsByYear")}</h3>
                                            </div>
                                            <div className={classes.chartContainer}>
                                                <BarChart
                                                    h={300}
                                                    withTooltip={false}
                                                    data={progressByYear}
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

                            <Tabs.Panel value="future-subjects"  h="90%">
                                <Flex gap="xl" align="center" justify="center" direction="column" mih={50} w="100%">
                                    <Flex w="85%" pt="1.5rem" mih="80%">
                                        <Grid w="100%" gutter="sm" columns={5}>
                                            {
                                                futureSubjects.map((subject) =>
                                                    <Grid.Col span={1} key={subject.id} >
                                                        <SubjectCard
                                                            id={subject.id}
                                                            credits={subject.credits}
                                                            difficulty={subject.difficulty}
                                                            name={subject.name}
                                                            numReviews={subject.reviewCount}
                                                            prerequisites={subject.prerequisites}
                                                            timeDemand={subject.timeDemand}
                                                            progress={""}
                                                        />
                                                    </Grid.Col>
                                                )
                                            }
                                        </Grid>
                                    </Flex>

                                    <Flex justify="center" align="center">
                                        <Pagination value={currentFutureSubjectsPage} total={futureSubjectsMaxPage} onChange={setCurrentFutureSubjectsPage} />
                                    </Flex>
                                </Flex>
                            </Tabs.Panel>

                            <Tabs.Panel value="past-subjects" h="90%">
                                <Flex gap="xl" align="center" justify="center" direction="column" h="100%" w="100%">
                                    <Flex w="85%" pt="1.5rem" mih="80%">
                                        <Grid w="100%" gutter="sm" columns={6} >
                                            {
                                                pastSubjects.map((subject) =>
                                                    <Grid.Col span={1} key={subject.id} >
                                                        <PastSubjectCard
                                                            id={subject.id}
                                                            credits={subject.credits}
                                                            name={subject.name}
                                                        />
                                                    </Grid.Col>
                                                )
                                            }
                                        </Grid>
                                    </Flex>
                                    <Flex justify="center" align="center">
                                        <Pagination value={currentPastSubjectsPage} total={pastSubjectsMaxPage} onChange={setCurrentPastSubjectsPage} />
                                    </Flex>
                                </Flex>
                            </Tabs.Panel>
                        </Tabs>
                    </div>
                </div>
            </div>
        </div>

    );

}

