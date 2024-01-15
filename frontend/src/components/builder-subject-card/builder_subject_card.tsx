import {Card, ActionIcon} from "@mantine/core";
import classes from "../builder-subject-card/builder_subject_card.module.css";
import DifficultyChip from "../difficulty-chip/difficulty-chip.tsx";
import {IconCheck} from "@tabler/icons-react";
import {t} from "i18next";
import {Subject} from "../../models/Subject.ts";


interface BuilderSubjectCardProps {
    subject: Subject
    selectionCallback: (id: string) => void;
    enabled: boolean
}

export default function BuilderSubjectCard(props: BuilderSubjectCardProps): JSX.Element {
    const {subject, selectionCallback, enabled} = props;

    const getTextColor = () => {
        if(enabled)
            return '#000000';
        else
            return '#a8a8a8';
    }

    return (
        <Card className={classes.card_area} withBorder>
            <div className={classes.card_row}>
                <div className={classes.card_column}>
                    <h4 style={{color: getTextColor()}} className={classes.card_title}>{subject.name}</h4>
                    <span style={{color: getTextColor()}}>{t("SubjectCard.credits", {n: subject.credits})}</span>
                    <div>
                        <DifficultyChip numReviews={subject.reviewCount} difficulty={subject.difficulty}/>
                    </div>
                </div>
                <div>
                    <ActionIcon variant="default" onClick={() => {selectionCallback(subject.id)}} disabled={!enabled}>
                        <IconCheck style={{ width: '70%', height: '70%' }} stroke={1.5} />
                    </ActionIcon>
                </div>
            </div>
        </Card>
    );
}