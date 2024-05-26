import {useTranslation} from "react-i18next";
import {Card, Divider} from "@mantine/core";
import classes from "./class-info-card.module.css";
import {Subject} from "../../models/Subject.ts";

interface ClassInfoCardProps {
    subject: Subject;
}

export default function ClassInfoCard(props: ClassInfoCardProps): JSX.Element {
    const { t } = useTranslation();
    const subject = props.subject;
    return(
        <Card className={classes.classCard} withBorder>
            <Card.Section>
                <h5 style={{margin: "0.3rem"}}>{subject.name} - {subject.id}</h5>
                <Divider/>
            </Card.Section>
            <Card.Section>
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
                        {subject.classes[0].locations.map((classTime) => (
                            <tr>
                                {classTime.day >= 1 && classTime.day <= 7? <td>{t("Home.classDay" + classTime.day)}</td> : <td>-</td>}
                                <td>{classTime.startTime} - {classTime.endTime}</td>
                                <td>{classTime.location}</td>
                                <td>{classTime.building}</td>
                                <td>{classTime.mode}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </Card.Section>
        </Card>
    );
}