import {rem, Tabs} from '@mantine/core';
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
                        Messages tab content
                    </Tabs.Panel>

                    <Tabs.Panel value="future-subjects">
                        <div className={classes.futureSubjectsArea}>
                            <SubjectCard id={"72.40"} credits={6} difficulty={"easy"} name={"Ingenieria del Software II"} numReviews={7} prerequisites={["72.38"]} timeDemand={"low"} progress={"incomplete"} />
                            <SubjectCard id={"72.40"} credits={6} difficulty={"easy"} name={"Ingenieria del Software II"} numReviews={7} prerequisites={["72.38"]} timeDemand={"low"} progress={"incomplete"} />
                            <SubjectCard id={"72.40"} credits={6} difficulty={"easy"} name={"Ingenieria del Software II"} numReviews={7} prerequisites={["72.38"]} timeDemand={"low"} progress={"incomplete"} />
                            <SubjectCard id={"72.40"} credits={6} difficulty={"easy"} name={"Ingenieria del Software II"} numReviews={7} prerequisites={["72.38"]} timeDemand={"low"} progress={"incomplete"} />
                            <SubjectCard id={"72.40"} credits={6} difficulty={"easy"} name={"Ingenieria del Software II"} numReviews={7} prerequisites={["72.38"]} timeDemand={"low"} progress={"incomplete"} />
                        </div>

                    </Tabs.Panel>

                    <Tabs.Panel value="past-subjects">
                        Settings tab content
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