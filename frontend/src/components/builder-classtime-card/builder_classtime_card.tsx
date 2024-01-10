import {Card, ActionIcon, Divider} from "@mantine/core";
import classes from "./builder_classtime_card.module.css"
import "../../common/table-style.css";
import {IconCheck} from "@tabler/icons-react";
import ClassTime from "../../models/ClassTime.ts";
import {t} from "i18next";
import getDayName from "../../common/timeTable.ts";

interface BuilderClassTimeCardProps {
    className: string;
    times: ClassTime[];
    selectionCallback: () => void;
}



export default function BuilderClassTimeCard(props: BuilderClassTimeCardProps): JSX.Element {
    const {className, times, selectionCallback} = props;

    return (
        <Card padding={0} className={classes.card_area} withBorder>
            <Card.Section >
                <div className={classes.selection_row}>
                    <h4 className={classes.card_title}>{className}</h4>
                    <ActionIcon variant="default" onClick={selectionCallback}>
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
                    {times.map((time) => (
                        <tr>
                            <td>{getDayName(time.day)}</td>
                            <td>{time.startTime} - {time.endTime}</td>
                            <td>{time.classNumber}</td>
                            <td>{time.building}</td>
                            <td>{time.mode}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </Card>
    );
}