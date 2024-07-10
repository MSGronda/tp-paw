import classes from './recover.module.css';
import Title from "../../components/title/title.tsx";
import {Alert, Button, PasswordInput, TextInput, Title as MantineTitle} from "@mantine/core";
import {Default_Navbar} from "../../components/default-navbar/default_navbar.tsx";
import {useTranslation} from "react-i18next";
import {useForm} from "@mantine/form";
import {validateEmail, validatePassword} from "../../utils/register_utils.ts";
import AuthService from "../../services/AuthService.ts";
import {IconAt, IconCheck, IconExclamationCircle, IconLock} from "@tabler/icons-react";
import {useState} from "react";
import {Link, useParams} from "react-router-dom";

export default function Recover() {
  const { t } = useTranslation(undefined, { keyPrefix: "Recover" });
  const params = useParams();
  
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState(false);
  const [success, setSuccess] = useState(false);
  
  const form = useForm({
    validateInputOnBlur: true,
    initialValues: {
      email: "",
    },
    validate: {
      email: (val) => validateEmail(val, () => {}) ? null : t("invalid_email"),
    }
  });
  
  async function handleFormSubmit({email}: typeof form.values) {
    setSubmitting(true);
    setError(false);
    try {
      const res = await AuthService.requestRecoverPassword(email);
      
      if(res) setSuccess(true);
      else setError(true);
      
    } catch (e) {
      console.error(e);
      setError(true);
      
    } finally {
      setSubmitting(false);
    }
  }
  
  if(params.token) return <RecoverWithToken token={params.token}></RecoverWithToken>;
  
  const emailIcon = <IconAt width="1.2rem" stroke="1.5" />
  
  return (
    <div className={classes.fullsize}>
      <Title text={t("title")}/>
      <Default_Navbar/>
      <div className={classes.center}>
        <MantineTitle order={2} hidden={success}>{t("title")}</MantineTitle>
        <Alert mt="lg" variant="light" color="red" title={t('error_alert_title')} icon={<IconExclamationCircle />} hidden={!error}>
          {t("error")}
        </Alert>
        <Alert mt="lg" variant="light" color="green" title={t('success_alert_title')} icon={<IconCheck/>} hidden={!success}>
          {t("success")}
        </Alert>
        <form onSubmit={form.onSubmit(handleFormSubmit)} hidden={success}>
          <TextInput 
            my="1rem"
            label={t("email")} 
            placeholder="name@example.com" 
            leftSection={emailIcon}
            disabled={success} 
            {...form.getInputProps("email")}
          />
          <Button type="submit" disabled={submitting || success}>
            {t("submit")}
          </Button>
        </form>
      </div>
    </div>
  );
}

function RecoverWithToken({ token }: { token: string }) {
  const { t } = useTranslation(undefined, { keyPrefix: "Recover.WithToken" });
  
  const [error, setError] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [success, setSuccess] = useState(false);
  
  const form = useForm({
    validateInputOnBlur: true,
    initialValues: {
      pass: "",
      confirmPass: ""
    },
    validate: {
      pass: (val) => validatePassword(val, () => {}) ? null : t("invalid_password"),
      confirmPass: (val, values) => val === values.pass ? null : t("passwords_dont_match")
    }
  });
  
  async function handleFormSubmit({ pass }: typeof form.values) {
    setSubmitting(true);
    setError(false);
    
    try {
      await AuthService.recoverPassword(token, pass);
      setSuccess(true);
      
    } catch (e) {
      setError(true);
      
    } finally {
      setSubmitting(false);
    }
  }
  
  const lockIcon = <IconLock width="1.2rem" stroke="1.5" />
  
  return (
    <div className={classes.fullsize}>
      <Title text={t("title")}/>
      <Default_Navbar/>
      <div className={classes.center}>
        <MantineTitle order={2} hidden={success}>{t("title")}</MantineTitle>
        <Alert mt="lg" variant="light" color="red" title={t('error_alert_title')} icon={<IconExclamationCircle />} hidden={!error}>
          {t("error")}
        </Alert>
        <Alert mt="lg" variant="light" color="green" title={t('success_alert_title')} icon={<IconCheck/>} hidden={!success}>
          {t("success")}
        </Alert>
        <Link to={"/"} hidden={!success}>
          <Button mt="lg">
            {t("back_home")}
          </Button>
        </Link>
        <form onSubmit={form.onSubmit(handleFormSubmit)} hidden={success}>
          <PasswordInput
            my="1rem" 
            label={t("password")}
            leftSection={lockIcon}
            disabled={success}
            {...form.getInputProps("pass")}
          />
          <PasswordInput
            my="1rem"
            label={t("password_confirm")}
            leftSection={lockIcon}
            disabled={success}
            {...form.getInputProps("confirmPass")}
          />
          <Button type="submit" disabled={submitting || success}>
            {t("submit")}
          </Button>
        </form>
      </div>
    </div>
  );
}
