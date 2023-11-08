import { Text, Card, Button } from '@mantine/core';
import { Default_Navbar } from "../../components/default-navbar/default_navbar"
import classes from './landing.module.css';
import { useTranslation } from 'react-i18next';
import "../../common/i18n/index"
import { useNavigate } from 'react-router-dom';


export default function Landing() {
    const { t } = useTranslation();
    const navigate = useNavigate();
    return (
        <div className={classes.fullsize}>
            <Default_Navbar/>
            <div className={classes.background}>
                <main className={classes.main}>
                    <div className={classes.landing_row}>
                        <Card className={classes.landing}>
                            <h2 className={classes.header}>
                                {t("Landing.title")}
                            </h2>
                            <div className={classes.get_started}>
                                <h3 className={classes.subtitle}>{t("Landing.get_started")}</h3>
                                <div className={classes.buttons}>
                                    <Button onClick={() => navigate('/login')}>
                                        {t("Landing.login")}
                                    </Button>
                                    <Button variant='outline'
                                        onClick={() => navigate('/register')}>
                                        {t("Landing.register")}
                                    </Button>
                                </div>
                            </div>
                        </Card>
                        <div className={classes.info}>
                            <h3>{t("Landing.message1")}</h3>
                            <h3>{t("Landing.message2")}</h3>
                            <h3>{t("Landing.message3")}</h3>
                        </div>
                    </div>
                </main>
            </div>
        </div>
    )
}