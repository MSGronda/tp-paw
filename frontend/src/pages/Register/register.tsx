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

    // Function to validate email
    const validateEmail = (value: string) => {
        if (!value || !/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(value)) {
            setEmailError(t("Register.email_error"));
            return false;
        } else {
            setEmailError('');
            return true;
        }
    };

    // Function to validate username
    const validateUsername = (value: string) => {
        if (!value || value.length === 0) {
            setUsernameError(t("Register.username_error"));
            return false;
        } else {
            setUsernameError('');
            return true;
        }
    };

    // Function to validate password
    const validatePassword = (value: string) => {
        if (!value || value.length === 0) {
            setPasswordError(t("Register.password_error"));
            return false;
        } else {
            setPasswordError('');
            return true;
        }
    };

    // Function to validate confirm password
    const validateConfirmPassword = (value: string) => {
        if (!value || value !== password) {
            setConfirmPasswordError(t("Register.confirm_password_error"));
            return false;
        } else {
            setConfirmPasswordError('');
            return true;
        }
    };

    // Handle form submission
    const handleFormSubmit = (e: { preventDefault: () => void; }) => {
        e.preventDefault();
        
        // Call validation functions
        const isEmailValid = validateEmail(email);
        const isUsernameValid = validateUsername(username);
        const isPasswordValid = validatePassword(password);
        const isConfirmPasswordValid = validateConfirmPassword(confirmPassword);

        // Determine if the submit button should be disabled
        const isSubmitDisabled = !isEmailValid || !isUsernameValid || !isPasswordValid || !isConfirmPasswordValid;
    };

    const isSubmitDisabled = !email || !username || !password || !confirmPassword || !!emailError || !!usernameError || !!passwordError || !!confirmPasswordError;

    return (
        <div className={classes.fullsize}>
            <Default_Navbar />
            <div className={classes.center}>
                <h2>Register</h2>
                <form onSubmit={handleFormSubmit}>
                    <TextInput
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        onBlur={() => validateEmail(email)}
                        label={t("Register.email")}
                        placeholder={t("Register.email_example")}
                        className={classes.padding}
                        error={emailError}
                    />
                    <TextInput
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        onBlur={() => validateUsername(username)}
                        label={t("Register.username")}
                        placeholder={t("Register.username_example")}
                        className={classes.padding}
                        error={usernameError}
                    />
                    <PasswordInput
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        onBlur={() => validatePassword(password)}
                        label={t("Register.password")}
                        className={classes.padding}
                        error={passwordError}
                    />
                    <PasswordInput
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        onBlur={() => validateConfirmPassword(confirmPassword)}
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
