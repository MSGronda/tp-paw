import {Card, ActionIcon, Divider} from "@mantine/core";
import classes from "./builder_selected_card.module.css"
import "../../common/table-style.css";
import {IconX} from "@tabler/icons-react";
import {t} from "i18next";
import getDayName from "../../common/timeTable.ts";
import {SelectedSubject} from "../../models/SelectedSubject.ts";

interface BuilderClassTimeCardProps {
    selected: SelectedSubject;
    removeCallback: (id: string) => void;
}

export default function BuilderSelectedCard(props: BuilderClassTimeCardProps): JSX.Element {
    const {selected, removeCallback} = props;

    return (
        <Card padding={0} className={classes.card_area} withBorder>
            <Card.Section >
                <div className={classes.selection_row}>
                    <h4 className={classes.card_title}>{selected.subject.name} - {selected.selectedClass.idClass}</h4>
                    <ActionIcon variant="default" onClick={() => {removeCallback(selected.subject.id)}}>
                        <IconX style={{ width: '70%', height: '70%' }} stroke={1.5} />
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
                    {selected.selectedClass.locations.map((time) => (
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