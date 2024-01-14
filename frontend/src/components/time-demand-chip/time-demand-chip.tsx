import {Badge} from "@mantine/core";
import classes from "../subject-card/subject-card.module.css";
import {t} from "i18next";

interface TimeDemandChipProps {
    numReviews: number;
    timeDemand: string;
}

export default function TimeDemandChip(props: TimeDemandChipProps): JSX.Element {
    const {numReviews, timeDemand} = props;

    return (

        <div>
            <div>
                {numReviews == 0 ? <Badge className={classes.badge_row_elem} color={'gray.7'}>{t("SubjectCard.no_reviews")}</Badge> : null }
                { numReviews > 0 && timeDemand == 'LOW' ? <Badge className={classes.badge_row_elem} color={'green.7'}>{t("SubjectCard.low")}</Badge> : null }
                { numReviews > 0 && timeDemand == 'MEDIUM' ? <Badge className={classes.badge_row_elem} color={'blue.7'}>{t("SubjectCard.medium")}</Badge> : null }
                { numReviews > 0 && timeDemand == 'HIGH' ? <Badge className={classes.badge_row_elem} color={'red.7'}>{t("SubjectCard.high")}</Badge> : null }
                { timeDemand == 'NO-INFO' ? <Badge className={classes.badge_row_elem} color={'gray.7'}>{t("SubjectCard.no_info")}</Badge> : null }
            </div>
        </div>
    );
}