import { useContext } from 'react';
import Title from '../../components/title/title'
import classes from './error.module.css'
import { t } from "i18next";
import AuthContext from '../../context/AuthContext';
import { Default_Navbar } from '../../components/default-navbar/default_navbar';
import { Navbar } from '../../components/navbar/navbar';
import { Button } from '@mantine/core';
import { useLocation, useNavigate } from 'react-router-dom';


const errorMessages: { [key: string]: { name: string } } = {
    404: {
        name: "Error.NotFound.",
    },
    403: {
        name: "Error.Unauthorized.",
    },
    500: {
        name: "Error.ServerError.",
    }
};


export default function Error() {
    const { isAuthenticated } = useContext(AuthContext);
    const navigate = useNavigate();
    const location = useLocation();
    const searchParams = new URLSearchParams(location.search);
    let code = searchParams.get('code');
    if (code === null) {
        code = "404";
    }

    const errorName = errorMessages[code].name;


    const handleClick = () => {
        navigate('/');
    }

    return (
        <div className={classes.fullsize}>
            <Title text={t("Error.title")} />
            <div className={classes.fullWidth}>
                {!isAuthenticated ? <Default_Navbar /> : <Navbar />}
            </div>
            <div className={classes.center}>
                <h2 className={classes.titleSize}>{t(errorName + "header")}</h2>
                <span className={classes.subtitleSize}>{t(errorName + "subtitle")}</span>
                <div className={classes.buttonDiv}>
                    <Button className={classes.homeButton} onClick={() => handleClick()}>
                        {t(errorName + "returnHome")}
                    </Button>
                </div>

            </div>
        </div>
    )
}