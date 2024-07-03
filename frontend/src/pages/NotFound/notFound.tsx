import { useContext } from 'react';
import Title from '../../components/title/title'
import classes from './notFound.module.css'
import { t } from "i18next";
import AuthContext from '../../context/AuthContext';
import { Default_Navbar } from '../../components/default-navbar/default_navbar';
import { Navbar } from '../../components/navbar/navbar';
import { Button } from '@mantine/core';
import { useNavigate } from 'react-router-dom';


export default function NotFound() {
    const { isAuthenticated } = useContext(AuthContext);
    const navigate = useNavigate();

    const handleClick = () => {
        navigate('/');
    }

    return (
        <div className={classes.fullsize}>
            <Title text={t("NotFound.title")} />
            <div className={classes.fullWidth}>
                {!isAuthenticated ? <Default_Navbar /> : <Navbar />}
            </div>
            <div className={classes.center}>
                <h2 className={classes.titleSize}>{t("NotFound.header")}</h2>
                <span className={classes.subtitleSize}>{t("NotFound.subtitle")}</span>
                <div className={classes.buttonDiv}>
                    <Button className={classes.homeButton} onClick={() => handleClick()}>
                        {t("NotFound.returnHome")}
                    </Button>
                </div>

            </div>
        </div>
    )
}