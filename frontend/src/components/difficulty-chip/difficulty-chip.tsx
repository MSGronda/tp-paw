import {Badge} from "@mantine/core";
import classes from "../subject-card/subject-card.module.css";
import {t} from "i18next";

interface DifficultyChipProps {
    numReviews: number;
    difficulty: string;
}

export default function DifficultyChip(props: DifficultyChipProps): JSX.Element {
    const {numReviews, difficulty} = props;

    return (

            <div>
                <div>
                    {numReviews == 0 ? <Badge className={classes.badge_row_elem} color={'gray.7'}>{t("SubjectCard.no_reviews")}</Badge> : null }
                    { numReviews > 0 && difficulty == 'EASY' ? <Badge className={classes.badge_row_elem} color={'green.7'}>{t("SubjectCard.easy")}</Badge> : null }
                    { numReviews > 0 && difficulty == 'NORMAL' ? <Badge className={classes.badge_row_elem} color={'blue.7'}>{t("SubjectCard.normal")}</Badge> : null }
                    { numReviews > 0 && difficulty == 'HARD' ? <Badge className={classes.badge_row_elem} color={'red.7'}>{t("SubjectCard.hard")}</Badge> : null }
                    { difficulty == 'NO-INFO' ? <Badge className={classes.badge_row_elem} color={'gray.7'}>{t("SubjectCard.no_info")}</Badge> : null }
                </div>
            </div>
    );
}