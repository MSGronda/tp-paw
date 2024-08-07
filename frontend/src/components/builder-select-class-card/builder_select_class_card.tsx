import {Card, ActionIcon, Divider} from "@mantine/core";
import classes from "./builder_select_class_card.module.css"
import "../../common/table-style.css";
import {IconCheck} from "@tabler/icons-react";
import {t} from "i18next";
import getDayName from "../../utils/time_table.ts";
import Class from "../../models/Class.ts";


interface BuilderSelectClassCardProps {
    subjectClass: Class;
    addClassCallback: (c: Class) => void;
    enabled: boolean;
}

export default function BuilderSelectClassCard(props: BuilderSelectClassCardProps): JSX.Element {
    const {subjectClass, addClassCallback, enabled} = props;

    const getTextColor = () => {
        if(enabled)
            return '#000000';
        else
            return '#a8a8a8';
    }

    return (
        <Card padding={0} className={classes.card_area} withBorder>
            <Card.Section >
                <div className={classes.selection_row}>
                    <h4 style={{color: getTextColor()}} className={classes.card_title}>{subjectClass.idClass}</h4>
                    <ActionIcon variant="default" onClick={() => {addClassCallback(subjectClass)}} disabled={!enabled}>
                        <IconCheck style={{ width: '70%', height: '70%' }} stroke={1.5} />
                    </ActionIcon>
                </div>
                <Divider/>
            </Card.Section>
            <table>
                <thead>
                <tr>
                    <th>{t("TimeTable.day")}</th>
                    <th>{t("TimeTable.time")}</th>
                    <th>{t("TimeTable.class")}</th>
                    <th>{t("TimeTable.building")}</th>
                    <th>{t("TimeTable.mode")}</th>
                </tr>
                </thead>
                <tbody>
                    {subjectClass.locations.map((time) => (
                        <tr>
                            <td>{getDayName(time.day)}</td>
                            <td>{time.startTime} - {time.endTime}</td>
                            <td>{time.location}</td>
                            <td>{time.building}</td>
                            <td>{time.mode}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </Card>
    );
}