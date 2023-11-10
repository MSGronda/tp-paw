import classes from './register.module.css';
import { Default_Navbar } from "../../components/default-navbar/default_navbar";
import { PasswordInput, TextInput, Button } from '@mantine/core';
import { useTranslation } from 'react-i18next';
import { useState } from 'react';

export default function Register() {
    const { t } = useTranslation();

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

    // Handle form submission
    const handleFormSubmit = (e: { preventDefault: () => void; }) => {
        e.preventDefault();

        // Validate form inputs here
        if (!email || !/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(email)) {
            setEmailError(t("Register.email_error"));
        } else {
            setEmailError('');
        }

        if (!username || username.length === 0) {
            setUsernameError(t("Register.username_error"));
        } else {
            setUsernameError('');
        }

        if (!password || password.length === 0) {
            setPasswordError(t("Register.password_error"));
        } else {
            setPasswordError('');
        }

        if (!confirmPassword || confirmPassword !== password) {
            setConfirmPasswordError(t("Register.confirm_password_error"));
        } else {
            setConfirmPasswordError('');
        }
    };

    // Determine if the submit button should be disabled
    const isSubmitDisabled = !!emailError || !!usernameError || !!passwordError || !!confirmPasswordError;

    return (
        <div className={classes.fullsize}>
            <Default_Navbar />
            <div className={classes.center}>
                <h2>Register</h2>
                <form onSubmit={handleFormSubmit}>
                    <TextInput
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        onBlur={() => setEmailError(email.length > 0 ? '' : t("Register.email_error"))}
                        label={t("Register.email")}
                        placeholder={t("Register.email_example")}
                        className={classes.padding}
                        error={emailError}
                    />
                    <TextInput
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        onBlur={() => setUsernameError(username.length > 0 ? '' : t("Register.username_error"))}
                        label={t("Register.username")}
                        placeholder={t("Register.username_example")}
                        className={classes.padding}
                        error={usernameError}
                    />
                    <PasswordInput
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        onBlur={() => setPasswordError(password.length > 0 ? '' : t("Register.password_error"))}
                        label={t("Register.password")}
                        className={classes.padding}
                        error={passwordError}
                    />
                    <PasswordInput
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        onBlur={() => setConfirmPasswordError(confirmPassword !== password ? t("Register.confirm_password_error") : '')}
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
