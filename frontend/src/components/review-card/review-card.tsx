import { ActionIcon, Badge, Button, Card, Divider } from "@mantine/core";
import classes from './review-card.module.css';
import { IconThumbDown, IconThumbUp, IconTrash } from "@tabler/icons-react";
import { useState } from "react";
import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";

interface ReviewCardProps {
  subjectId: string;
  subjectName: string | undefined;
  text: string;
  timeDemand: string;
  difficulty: string;
  userId: number;
  userName: string | undefined;
  anonymous: boolean;
}

function ReviewCard(props: ReviewCardProps): JSX.Element {
    const { t } = useTranslation();
    const { subjectId, subjectName, text, timeDemand, difficulty, userId, userName, anonymous } = props;

    const [showMore, setShowMore] = useState(false);
    const toggleShowMore = () => {
        setShowMore(!showMore);
    };

    const truncatedText = text.substring(0, 500);
    const remainingText = text.substring(500);

    return (
        <Card className={classes.card}>
        <div slot="header" className={classes.header}>
            {subjectName !== undefined? 
                <Link className={classes.username_redirect} to={"/subject/" + subjectId}>
                {subjectId} - {subjectName}
                </Link>
                :
                <>{
                !anonymous? 
                    <Link className={classes.username_redirect} to={"/user/" + userId}>
                    {userName}
                    </Link>
                    :
                    <>{t("Review.anonymous")}</>
                }</>
            }
            <ActionIcon variant="outline" color="red">
            <IconTrash />
            </ActionIcon>
        </div>
        <Divider className={classes.divider}/>
        <div>
            {text.length > 500 ? (
            showMore ? (
                <div>
                {text}
                <br />
                <div className={classes.showmore}>
                    <Button variant="outline" size="xs" onClick={toggleShowMore}>
                    {t("ReviewCard.showLess")}
                    </Button>
                </div>
                </div>
            ) : (
                <div>
                {truncatedText}
                {remainingText && <span>...</span>}
                <br />
                <div className={classes.showmore}>
                    <Button variant="outline" size="xs" onClick={toggleShowMore}>
                    {t("ReviewCard.showMore")}
                    </Button>
                </div>
                </div>
            )
            ) : (
            <div>{text}</div>
            )}
        </div>
        <br/>
        <div className={classes.badge_row}>
            { difficulty == 'EASY' ? <Badge className={classes.badge_row_elem} color={'green.7'}>{t("SubjectCard.easy")}</Badge> : null }
            { difficulty == 'MEDIUM' ? <Badge className={classes.badge_row_elem} color={'blue.7'}>{t("SubjectCard.normal")}</Badge> : null }
            { difficulty == 'HARD' ? <Badge className={classes.badge_row_elem} color={'red.7'}>{t("SubjectCard.hard")}</Badge> : null }

            { timeDemand == 'LOW' ? <Badge className={classes.badge_row_elem} color={'green.7'}>{t("SubjectCard.low")}</Badge> : null }
            { timeDemand == 'MEDIUM' ? <Badge className={classes.badge_row_elem} color={'blue.7'}>{t("SubjectCard.medium")}</Badge> : null }
            { timeDemand == 'HIGH' ? <Badge className={classes.badge_row_elem} color={'red.7'}>{t("SubjectCard.high")}</Badge> : null }
        </div>
        <Divider className={classes.divider}/>
        <div slot="footer" className={classes.like_buttons}>
            <ActionIcon variant="outline" className={classes.like_button}><IconThumbUp/></ActionIcon>
            <ActionIcon variant="outline" className={classes.like_button}><IconThumbDown/></ActionIcon>
        </div>
        </Card>
    );
}

export default ReviewCard;
