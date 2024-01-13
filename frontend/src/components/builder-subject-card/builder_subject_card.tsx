import {Card, ActionIcon} from "@mantine/core";
import classes from "../builder-subject-card/builder_subject_card.module.css";
import DifficultyChip from "../difficulty-chip/difficulty-chip.tsx";
import {IconCheck} from "@tabler/icons-react";
import {t} from "i18next";
import Subject from "../../models/Subject.ts";


interface BuilderSubjectCardProps {
    subject: Subject
    selectionCallback: (id: string) => void;
}

export default function BuilderSubjectCard(props: BuilderSubjectCardProps): JSX.Element {
    const {subject, selectionCallback} = props;

    return (
        <Card className={classes.card_area} withBorder>
            <div className={classes.card_row}>
                <div className={classes.card_column}>
                    <h4 className={classes.card_title}>{subject.name}</h4>
                    <span>{t("SubjectCard.credits", {n: subject.credits})}</span>
                    <div>
                        <DifficultyChip numReviews={subject.reviewCount} difficulty={subject.difficulty}/>
                    </div>
                </div>
                <div>
                    <ActionIcon variant="default" onClick={() => {selectionCallback(subject.id)}}>
                        <IconCheck style={{ width: '70%', height: '70%' }} stroke={1.5} />
                    </ActionIcon>
                </div>
            </div>
        </Card>
    );
}