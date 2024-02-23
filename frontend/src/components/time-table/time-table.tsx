import classes from "./time-table.module.css";
import {useTranslation} from "react-i18next";


export default function TimeTable(){
    const { t } = useTranslation();
    return(
        <div className={classes.timeTable}>
            <table>
                <thead>
                    <tr>
                        <th></th>
                        <th>{t("Home.classDay1")}</th>
                        <th>{t("Home.classDay2")}</th>
                        <th>{t("Home.classDay3")}</th>
                        <th>{t("Home.classDay4")}</th>
                        <th>{t("Home.classDay5")}</th>
                        <th>{t("Home.classDay6")}</th>
                    </tr>
                </thead>
                <tbody id="weekly-schedule">

                </tbody>
            </table>
        </div>
    );
}