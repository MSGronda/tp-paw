import classes from './register.module.css';
import { Default_Navbar } from "../../components/default-navbar/default_navbar";
import { PasswordInput, TextInput, Button } from '@mantine/core';
import { useTranslation } from 'react-i18next';
import {useEffect, useState} from 'react';
import {
    validateConfirmPassword,
    validateEmail,
    validatePassword,
    validateUsername
} from "../../utils/register_utils.ts";
import AuthService, {RegisterForm} from "../../services/AuthService.ts";
import RegisterComplete from "./registerComplete.tsx";
import Title from '../../components/title/title.tsx';

export default function Register() {
    const { t } = useTranslation();

    useEffect(() => {
        document.title = t("Register.title")
    }, [])

    // State variables to keep track of input values
    const [email, setEmail] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');

    // State variables to keep track of input validation
    const [emailError, setEmailError] = useState('');
    const [usernameError, setUsernameError] = useState('');
    const [passwordError, setPasswordError] = useState('');
    const [confirmPasswordError, setConfirmPasswordError] = useState('');
    
    const [registerComplete, setRegisterComplete] = useState(false);

    // Handle form submission
    function handleFormSubmit(e: { preventDefault: () => void; })  {
        e.preventDefault();

                
        const form: RegisterForm = {
            email,
            name: username,
            password,
            passwordConfirmation: confirmPassword
        }
        AuthService.register(form).then((res) => {
            if(!res) return; //error
            
            setRegisterComplete(true);
        })
    }

    const isSubmitDisabled = !email || !username || !password || !confirmPassword || !!emailError || !!usernameError || !!passwordError || !!confirmPasswordError;

    if(registerComplete) return <RegisterComplete />;
    
    return (
        <div className={classes.fullsize}>
            <Title text={t("Register.title")} />
            <Default_Navbar />
            <div className={classes.center}>
                <h2>{t("Register.title")}</h2>
                <form onSubmit={handleFormSubmit}>
                    <TextInput
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        onBlur={() => validateEmail(email, setEmailError)}
                        label={t("Register.email")}
                        placeholder={t("Register.email_example")}
                        className={classes.padding}
                        error={emailError}
                    />
                    <TextInput
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        onBlur={() => validateUsername(username, setUsernameError)}
                        label={t("Register.username")}
                        placeholder={t("Register.username_example")}
                        className={classes.padding}
                        error={usernameError}
                    />
                    <PasswordInput
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        onBlur={() => validatePassword(password, setPasswordError)}
                        label={t("Register.password")}
                        className={classes.padding}
                        error={passwordError}
                    />
                    <PasswordInput
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        onBlur={() => validateConfirmPassword(confirmPassword, password, setConfirmPasswordError)}
                        label={t("Register.confirm_password")}
                        className={classes.padding}
                        error={confirmPasswordError}
                    />
                    <div className={classes.register_button_div}>
                        <Button type='submit' color='green.7' disabled={isSubmitDisabled}>
                            {t("Register.register")}
                        </Button>
                    </div>
                </form>
            </div>
        </div>
    );
}
