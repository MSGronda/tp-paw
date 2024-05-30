// Function to validate email
import {t} from "i18next";

export const validateEmail = (value: string, setErrorFunction: Function) => {
    if (!value || !/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(value)) {
        setErrorFunction(t("Register.email_error"));
        return false;
    } else {
        setErrorFunction('');
        return true;
    }
};

// Function to validate username
export const validateUsername = (value: string, setErrorFunction: Function) => {
    const regex = /^\p{L}(\p{L}|\s|_)*$/u;
    if(!value || value.length == 0) {
        setErrorFunction(t("Register.username_empty_error"));
        return false;
    }
    else if (!value || value.length == 0 || !regex.test(value)) {
        setErrorFunction(t("Register.username_error"));
        return false;
    } else if(value.length > 20) {
        setErrorFunction(t("Register.username_length_error"));
        return false;
    } else {
        setErrorFunction('');
        return true;
    }
};

// Function to validate password
export const validatePassword = (value: string, setErrorFunction: Function) => {
    if (!value || value.length == 0 || value.length > 25 || value.length < 8) {
        setErrorFunction(t("Register.password_error"));
        return false;
    } else {
        setErrorFunction('');
        return true;
    }
};

// Function to validate confirm password
export const validateConfirmPassword = (value: string, password: string, setErrorFunction: Function) => {
    if (!value || value !== password) {
        setErrorFunction(t("Register.confirm_password_error"));
        return false;
    } else {
        setErrorFunction('');
        return true;
    }
};
