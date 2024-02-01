import {Badge, Card, Tooltip} from "@mantine/core";
import { Link } from "react-router-dom";
import {IconCheck} from "@tabler/icons-react";
import classes from './subject-card.module.css';
import {t} from "i18next";

interface SubjectCardProps {
    name: string;
    id: string;
    credits: number;
    prerequisites: string[];
    progress: string;
    difficulty: string;
    timeDemand: string;
    numReviews: number;
}

function SubjectCard(props: SubjectCardProps): JSX.Element {
    const { name, id, credits, prerequisites, progress, difficulty, timeDemand, numReviews } = props;

    return (
        <Link to={{pathname:`/subject/${id}`}}>
            <Card className={classes.card_area}>
                <h3>{name} - {id}</h3>
                <div className={classes.badge_row}>
                    <Badge className={classes.badge_row_elem} color={'blue.7'}>{t("SubjectCard.credits", {n: credits})}</Badge>
                    <Tooltip multiline label={
                        prerequisites.map((prerequisite, index) => (<div key={index}>{prerequisite}</div>))
                    }>
                        <Badge className={classes.badge_row_elem} color={'orange.7'}>{t("SubjectCard.prerequisites", {n: prerequisites.length})}</Badge>
                    </Tooltip>
                    { progress === 'DONE' ?
                        <Tooltip label={t("SubjectCard.done_tooltip")}>
                            <IconCheck className={classes.badge_row_elem} stroke={1.0} />
                        </Tooltip>
                        : null
                    }
                </div>
                <Card.Section>
                    <div style={{ borderBottom: '1px solid #ccc', margin: '16px 0' }}></div>
                </Card.Section>
                <div className={classes.badge_row}>
                    { numReviews > 0 && difficulty == 'EASY' ? <Badge className={classes.badge_row_elem} color={'green.7'}>{t("SubjectCard.easy")}</Badge> : null }
                    { numReviews > 0 && difficulty == 'NORMAL' ? <Badge className={classes.badge_row_elem} color={'blue.7'}>{t("SubjectCard.normal")}</Badge> : null }
                    { numReviews > 0 && difficulty == 'HARD' ? <Badge className={classes.badge_row_elem} color={'red.7'}>{t("SubjectCard.hard")}</Badge> : null }

                    { numReviews > 0 && timeDemand == 'LOW' ? <Badge className={classes.badge_row_elem} color={'green.7'}>{t("SubjectCard.low")}</Badge> : null }
                    { numReviews > 0 && timeDemand == 'MEDIUM' ? <Badge className={classes.badge_row_elem} color={'blue.7'}>{t("SubjectCard.medium")}</Badge> : null }
                    { numReviews > 0 && timeDemand == 'HIGH' ? <Badge className={classes.badge_row_elem} color={'red.7'}>{t("SubjectCard.high")}</Badge> : null }

                    { numReviews > 0 ? <span>{t("SubjectCard.reviews", {n: numReviews})}</span> : null }
                    { numReviews == 0 ? <Badge color={'gray.7'}>{t("SubjectCard.done_tooltip")}</Badge> : null }
                </div>
            </Card>
        </Link>
    );
}

export default SubjectCard;
