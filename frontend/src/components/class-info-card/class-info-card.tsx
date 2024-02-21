import {useTranslation} from "react-i18next";
import {Card} from "@mantine/core";
import classes from "./class-info-card.module.css";
import {Subject} from "../../models/Subject.ts";


export default function ClassInfoCard(subject:Subject) {
    const { t } = useTranslation();
    return(
        <Card className={classes.classCard}>
            <div className={classes.chooser}>
                <h5 className={classes.classCardName}>{subject.name} - {subject.id}</h5>
            </div>
            <div>
                <table>
                    <thead>
                    <tr>
                        <th>{t("Home.classDay")}</th>
                        <th>{t("Home.builderTime")}</th>
                        <th>{t("Home.builderClass")}</th>
                        <th>{t("Home.builderBuilding")}</th>
                        <th>{t("Home.builderMode")}</th>
                    </tr>
                    </thead>
                    <tbody>
                        {subject.classes.map((classTime) => (
                            <tr>
                                
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </Card>
    );
}