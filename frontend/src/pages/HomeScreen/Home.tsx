import {
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
import {IconMessageCircle, IconPhoto, IconSettings} from "@tabler/icons-react";
import {useTranslation} from "react-i18next";
import SubjectCard from "../../components/subject-card/subject-card.tsx";
import { useContext } from 'react';
import AuthContext from '../../context/AuthContext.tsx';
import Landing from '../Landing/landing.tsx';


export default function Home() {
    const { t } = useTranslation();
    const iconStyle = { width: rem(12), height: rem(12) };

    const data = [
        { year: t("Home.firstYear"), progress: 100},
        { year: t("Home.secondYear"), progress: 82},
        { year: t("Home.thirdYear"), progress: 100},
        { year: t("Home.fourthYear"), progress: 50},
        { year: t("Home.fifthYear"), progress: 7},
    ];

    let subjectsArray: { id: string, credits: number, difficulty: string, name: string, numReviews: number, prerequisites: [string], timeDemand: string, progress: string}[] = [
        { "id": "72.40", credits:6, difficulty:"easy","name": "Ingenieria en Software II",numReviews:732,prerequisites:["72.38"],timeDemand:"low",progress:"incomplete" },
        { "id": "72.41", credits:6, difficulty:"easy","name": "Gestion de Proyectos Informaticos",numReviews:732,prerequisites:["72.38"],timeDemand:"low",progress:"incomplete" },
        { "id": "72.42", credits:6, difficulty:"easy","name": "Progrmacion de Objetos Distribuidos",numReviews:732,prerequisites:["72.38"],timeDemand:"low",progress:"incomplete" },
        { "id": "72.43", credits:6, difficulty:"easy","name": "Base de Datos 2",numReviews:732,prerequisites:["72.38"],timeDemand:"low",progress:"incomplete" },
        { "id": "72.44", credits:6, difficulty:"easy","name": "Proyecto Aplicacion Web",numReviews:732,prerequisites:["72.38"],timeDemand:"low",progress:"incomplete" },
        { "id": "72.45", credits:6, difficulty:"easy","name": "Protocolos de Comunicacion",numReviews:732,prerequisites:["72.38"],timeDemand:"low",progress:"incomplete" },
        { "id": "72.46", credits:6, difficulty:"easy","name": "Metodos Numericos Avanzados",numReviews:732,prerequisites:["72.38"],timeDemand:"low",progress:"incomplete" },
    ];
    return (
        <>
            <Navbar/>
            <div className={classes.fullsize}>
                <div className={classes.center}>
                    <div className={classes.dashboardArea}>
                        <Tabs defaultValue="current-semester" className={classes.dashboard}>
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
                                Gallery tab content
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
                                    {getSubjectsCards(subjectsArray).map((item) => <Grid.Col span={3}>{item}</Grid.Col>)}
                                </Grid>
                            </Tabs.Panel>

                            <Tabs.Panel value="past-subjects">
                                <Grid gutter="sm">
                                    {getSubjectsCards(subjectsArray).map((item) => <Grid.Col span={3}>{item}</Grid.Col>)}
                                </Grid>
                            </Tabs.Panel>
                        </Tabs>
                    </div>
                </div>
            </div>
        </>

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