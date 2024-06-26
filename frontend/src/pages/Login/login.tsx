import classes from "../Login/login.module.css";
import {Default_Navbar} from "../../components/default-navbar/default_navbar.tsx";
import {Button, Checkbox, PasswordInput, TextInput} from "@mantine/core";
import {
    validateEmail,
    validatePassword
} from "../../utils/register_utils.ts";
import {useContext, useEffect, useState} from "react";
import {t} from "i18next";
import {useNavigate} from "react-router-dom";
import AuthService from "../../services/AuthService.ts";
import AuthContext from "../../context/AuthContext.tsx";
import React from "react";
import Title from "../../components/title/title.tsx";

export default function Login(){

    useEffect(() => {
        document.title = t("Login.title")
    }, [])

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [rememberMe, setRememberMe] = useState(false)

    const [emailError, setEmailError] = useState('');
    const [passwordError, setPasswordError] = useState('');

    const [invalidCredentials, setInvalidCredentials] = useState(false)

    const navigate = useNavigate();

    const authContext = React.useContext(AuthContext);

    const handleFormSubmit = async (e: { preventDefault: () => void; }) => {
        e.preventDefault();

        validateEmail(email, setEmailError);
        validatePassword(password, setPasswordError);

        const token = await AuthService.login(email, password, rememberMe)
        if(!token){
            setInvalidCredentials(true)
        }
        else{
            authContext.loginHandler(token)
            navigate('/')
        }
    };

    const isSubmitDisabled = !email || !password || !!emailError || !!passwordError;


    return (
        <div className={classes.fullsize}>
            <Title text={t("Login.title")} />
            <Default_Navbar />
            <div className={classes.center}>
                <h2>{t("Login.title")}</h2>
                {invalidCredentials && <p className={classes.invalid_cred}>{t("Login.invalid_credentials")}</p>}
                <form onSubmit={handleFormSubmit}>
                    <TextInput
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        onBlur={() => validateEmail(email, setEmailError)}
                        label={t("Login.email")}
                        placeholder={t("Login.email_example")}
                        className={classes.padding}
                        error={emailError}
                    />
                    <PasswordInput
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        onBlur={() => validatePassword(password, setPasswordError)}
                        label={t("Login.password")}
                        className={classes.padding}
                        error={passwordError}
                    />
                    <Checkbox
                        checked={rememberMe}
                        onChange={() => setRememberMe(!rememberMe)}
                        label={t("Login.rememberMe")}
                    />
                    <div className={classes.login_button_div}>
                        <Button type='submit' color='green.7' disabled={isSubmitDisabled}>
                            {t("Login.login")}
                        </Button>
                    </div>
                </form>
                <div>
                    <p>
                        {t("Login.noAccount")}
                        <Button onClick={() => navigate('/register')} variant="transparent">
                            {t("Login.register")}
                        </Button>
                    </p>
                    <p>
                        {t("Login.forgotPassword")}
                        <Button onClick={() => navigate('/recovery')} variant="transparent">
                            {t("Login.recover")}
                        </Button>
                    </p>
                </div>
            </div>
        </div>
    )
}