import {useNavigate, useSearchParams} from "react-router-dom";
import {Default_Navbar} from "../../components/default-navbar/default_navbar.tsx";
import {useEffect, useState} from "react";
import AuthService from "../../services/AuthService.ts";
import {useTranslation} from "react-i18next";
import {Button} from "@mantine/core";

import classes from './confirmEmail.module.css';

export default function ConfirmEmail() {
    const [params] = useSearchParams();
    const token = params.get('token');

    const [error, setError] = useState(false);
    const [confirmed, setConfirmed] = useState(false);

    useEffect(() => {
        if (!token) {
            return;
        }

        AuthService.confirmEmail(token).then((res) => {
            if (!res) {
                setError(true);
                return;
            }

            setConfirmed(true);
        });
    }, []);

    if (!token || error) return <InvalidToken/>;
    if (confirmed) return <Confirmed/>;

    return <></>;
}

function InvalidToken() {
    const {t} = useTranslation();
    return <div className={classes.fullsize}>
        <Default_Navbar/>
        <div className={classes.center}>
            <h1>{t('ConfirmEmail.InvalidToken.title')}</h1>
            <p>{t('ConfirmEmail.InvalidToken.body')}</p>
        </div>
    </div>;
}

function Confirmed() {
    const {t} = useTranslation();
    const navigate = useNavigate();

    return <div className={classes.fullsize}>
        <Default_Navbar/>
        <div className={classes.center}>
            <h1>{t('ConfirmEmail.Confirmed.title')}</h1>
            <p>{t('ConfirmEmail.Confirmed.body')}</p>
            <div>
                <Button onClick={() => navigate('/login')}>
                    {t('ConfirmEmail.Confirmed.login')}
                </Button>
            </div>
        </div>
    </div>;
}
