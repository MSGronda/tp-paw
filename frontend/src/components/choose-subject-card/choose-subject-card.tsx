import {Card, ActionIcon} from "@mantine/core";
import classes from "../builder-subject-card/builder_subject_card.module.css";
import DifficultyChip from "../difficulty-chip/difficulty-chip.tsx";
import {IconCheck, IconPlus} from "@tabler/icons-react";
import {t} from "i18next";
import {Subject} from "../../models/Subject.ts";


interface ChooseSubjectCardProps {
    subject: Subject
    selectionCallback: (id: string) => void;
    removalCallback: (id: string) => void;
    selected: boolean
}

export default function ChooseSubjectCard(props: ChooseSubjectCardProps): JSX.Element {
    const {subject, selectionCallback, selected, removalCallback} = props;

    return (
        <Card className={classes.card_area} withBorder>
            <div className={classes.card_row}>
                <div className={classes.card_column}>
                    <h4 style={{color: '#000000'}} className={classes.card_title}>{subject.name}</h4>
                    <span style={{color: '#000000'}}>{t("SubjectCard.credits", {n: subject.credits})}</span>
                </div>
                <div>
                    {
                        selected? <ActionIcon variant="filled" color="lime" onClick={() => {removalCallback(subject.id)}}>
                                      <IconCheck style={{ width: '70%', height: '70%' }} stroke={1.5} />
                                  </ActionIcon> :
                                  <ActionIcon variant="default" onClick={() => {selectionCallback(subject.id)}}>
                                      <IconPlus style={{ width: '70%', height: '70%' }} stroke={1.5} />
                                  </ActionIcon>
                    }
                </div>
            </div>
        </Card>
    );
}