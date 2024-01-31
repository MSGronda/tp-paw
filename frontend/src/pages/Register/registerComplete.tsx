import classes from './register.module.css';
import {useTranslation} from "react-i18next";
import {Default_Navbar} from "../../components/default-navbar/default_navbar.tsx";

export default function RegisterComplete() {
    const { t } = useTranslation(); 
    
    return <div className={classes.fullsize}>
        <Default_Navbar/>
        <div className={classes.center}>
            <h1>{t('RegisterComplete.title')}</h1>
            <p>{t('RegisterComplete.body')}</p>
        </div>
    </div>;
}
