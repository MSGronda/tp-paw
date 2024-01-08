import {
    Card,
    Container,
    createTheme,
    Divider,
    Flex,
    Grid,
    Group,
    MantineProvider,
    rem,
    Tabs,
    Text
} from '@mantine/core';
import {Navbar } from "../../components/navbar/navbar";
import classes from './home.module.css';
import {IconMessageCircle, IconPhoto, IconSettings} from "@tabler/icons-react";
import {useTranslation} from "react-i18next";
import SubjectCard from "../../components/subject-card/subject-card.tsx";
import { useContext } from 'react';
import AuthContext from '../../context/AuthContext.tsx';
import Landing from '../Landing/landing.tsx';


export default function Home() {
    const iconStyle = { width: rem(12), height: rem(12) };
    const { t } = useTranslation();
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
        <div className={classes.fullsize}>
        <Navbar/>
            <div className={classes.center}>
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
                        <div className={classes.fullsizeTab}>
                            <Flex gap="md"
                                  justify="start"
                                  align="center"
                                  direction="column"
                                  miw={200}>
                                <Card shadow="xs"
                                      padding="xl"
                                      color="#efefef"
                                      className={classes.cardWidth}
                                      miw={}>
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
                                <Flex mih={50}
                                      bg="#efefef"
                                      gap="lg"
                                      justify="flex-start"
                                      align="center"
                                      direction="row"
                                      wrap="wrap">
                                    <Card color="#efefef"
                                          shadow="xs"
                                          padding="xl">
                                        <div>
                                            <Text>
                                                LALALALALAL
                                            </Text>
                                        </div>
                                    </Card>
                                    <Card color="#efefef"
                                          shadow="xs"
                                          padding="xl">
                                        <div>
                                            <Text>
                                                LALALALALAL
                                            </Text>
                                        </div>
                                    </Card>
                                </Flex>
                            </Flex>
                        </div>
                        <Grid gutter={{ base: 5, xs: 'md', md: 'xl', xl: 50 }}
                              columns={12}
                              justify="center">
                            <Grid.Col span={12}>
                                <Container size="responsive" w={rem(1500)}>
                                    <Card shadow="xs"
                                          padding="xl"
                                          color="#efefef">
                                        <Flex gap="md"
                                              justify="start"
                                              align="center"
                                              direction="column"
                                              wrap="wrap">
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
                                </Container>
                            </Grid.Col>
                            <Grid.Col span="content">
                                <Container size="responsive">
                                    <Flex mih={50}
                                          bg="#efefef"
                                          gap="lg"
                                          justify="flex-start"
                                          align="center"
                                          direction="row"
                                          wrap="wrap">
                                        <Card color="#efefef"
                                              shadow="xs"
                                              padding="xl">
                                            <div>
                                                <Text>
                                                    LALALALALAL
                                                </Text>
                                            </div>
                                        </Card>
                                        <Card color="#efefef"
                                              shadow="xs"
                                              padding="xl">
                                        <div>
                                                <Text>
                                                    LALALALALAL
                                                </Text>
                                            </div>
                                        </Card>
                                    </Flex>
                                </Container>
                            </Grid.Col>
                        </Grid>
                    </Tabs.Panel>

                    <Tabs.Panel value="future-subjects">
                        <Grid gutter="sm" columns={12}>
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