import {Card, ActionIcon} from "@mantine/core";
import classes from "../builder-subject-card/builder_subject_card.module.css";
import DifficultyChip from "../difficulty-chip/difficulty-chip.tsx";
import {IconCheck} from "@tabler/icons-react";


interface BuilderSubjectCardProps {
    name: string;
    credits: number;
    numReviews: number;
    difficulty: string;
    selectionCallback: () => void;
}

export default function BuilderSubjectCard(props: BuilderSubjectCardProps): JSX.Element {
    const {name, credits,numReviews, difficulty, selectionCallback} = props;

    return (
        <Card className={classes.card_area} withBorder>
            <div className={classes.card_row}>
                <div className={classes.card_column}>
                    <h4 className={classes.card_title}>{name}</h4>
                    <span>Credits: {credits}</span>
                    <div>
                        <DifficultyChip numReviews={numReviews} difficulty={difficulty}/>
                    </div>
                </div>
                <div>
                    <ActionIcon variant="default" onClick={selectionCallback}>
                        <IconCheck style={{ width: '70%', height: '70%' }} stroke={1.5} />
                    </ActionIcon>
                </div>
            </div>
        </Card>
    );
}