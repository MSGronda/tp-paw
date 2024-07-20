import {ActionIcon, Badge, Button, Card, Divider, Tooltip} from "@mantine/core";
import classes from './review-card.module.css';
import {IconEdit, IconThumbDown, IconThumbUp, IconTrash} from "@tabler/icons-react";
import {useContext, useEffect, useState} from "react";
import {useTranslation} from "react-i18next";
import {Link} from "react-router-dom";
import AuthContext from "../../context/AuthContext";
import {reviewService} from "../../services";
import {ReviewVote, VoteValue} from "../../models/ReviewVote.ts";


interface ReviewCardProps {
    subjectId: string;
    subjectName: string | undefined;
    text: string;
    timeDemand: string;
    difficulty: string;
    UserId: number;
    userName: string | undefined;
    anonymous: boolean;
    id: number
    forSubject: boolean
    upvotes: number
    downvotes: number
    votes: ReviewVote[]
}

function GetVoteValue(voteValue: VoteValue): number {
    switch (voteValue) {
        case VoteValue.UpVote:
            return 1;
        case VoteValue.DownVote:
            return -1;
        default:
            return 0;
    }
}

function ReviewCard(props: ReviewCardProps): JSX.Element {
    const {subjectId, subjectName, text, timeDemand, difficulty, UserId, userName, anonymous, id, forSubject, upvotes, downvotes, votes } = props;

    const { t } = useTranslation();
    const {userId } = useContext(AuthContext);
    const [openedTooltip, setOpenedTooltip] = useState(false);
    const [showMore, setShowMore] = useState(false);
    const [didUserUpVote, setDidUserUpVote] = useState<boolean>(false);
    const [didUserDownVote, setDidUserDownVote] = useState<boolean>(false);
    const [upVotes, setUpVotes] = useState(upvotes);
    const [downVotes, setDownVotes] = useState(downvotes);

    const votedColor = "#f5a623";

    const toggleShowMore = () => {
        setShowMore(!showMore);
    };

    const truncatedText = text.substring(0, 500);
    const remainingText = text.substring(500);

    const deleteAction = async (reviewId: number) => {
        const res = await reviewService.deleteReview(reviewId)
        localStorage.setItem('reviewDeleted', JSON.stringify(!res?.failure))
        window.location.reload()
    }
    const voteAction = async (reviewId: number, vote: VoteValue) => {
        const res = await reviewService.voteReview(reviewId, GetVoteValue(vote));
        localStorage.setItem('reviewVoted', JSON.stringify(!res?.failure))
        if (vote === VoteValue.UpVote) {

            setUpVotes(upVotes + 1);

            if(didUserDownVote){
                setDownVotes(downVotes - 1);
            }

            setDidUserDownVote(false);
            setDidUserUpVote(true);
        } else  {
            setDownVotes(downVotes + 1);

            if(didUserUpVote){
                setUpVotes(upVotes - 1);
            }

            setDidUserDownVote(true);
            setDidUserUpVote(false);
        }
    }

    const unVoteAction = async (reviewId: number, userId: number | undefined) => {
        const res = await reviewService.unVoteReview(reviewId, userId)
        localStorage.setItem('reviewUnVoted', JSON.stringify(!res?.failure))
        if (didUserUpVote) {
            setDidUserUpVote(false)
            setUpVotes(upVotes - 1);
        } else {
            setDidUserDownVote(false);
            setDownVotes(downVotes - 1);
        }
    }

    const userVoted = (voteType: number) => {
        return votes.some((v) => v.userId == userId && v.vote == voteType);
    }

    useEffect(() => {
        const up = userVoted(VoteValue.UpVote);
        const down = userVoted(VoteValue.DownVote);

        setDidUserUpVote(up);
        setDidUserDownVote(down)

    }, []);

    return (
        <Card className={classes.card}>
            <div slot="header" className={classes.header}>
                {!forSubject ?
                    <Link className={classes.username_redirect} to={"/subject/" + subjectId}>
                        {subjectId} - {subjectName}
                    </Link>
                    :
                    <>{
                        !anonymous ?
                            <Link className={classes.username_redirect} to={"/user/" + UserId}>
                                {userName}
                            </Link>
                            :
                            <>{t("Review.anonymous")}</>
                    }</>
                }
                <div>
                    {
                        userId === UserId &&
                        <Link to={`/review/${subjectId}/edit/${id}`} state={{text: text, timeDemand: timeDemand, difficulty: difficulty, anonymous: anonymous, subjectName: subjectName, fromSubject: forSubject}}>
                            <ActionIcon variant="transparent" color="grey" style={{marginRight: '0.7rem'}}>
                                <IconEdit />
                            </ActionIcon>
                        </Link>
                    }
                    {
                        userId === UserId && //TODO: agregar rol de admin del authContext
                        <Tooltip label={
                            <div className={classes.clickable}>
                                <Card>
                                    <div className={classes.column_center}>
                                        <span>{t("ReviewCards.doyouwish")}</span>
                                        <div style={{ paddingTop: '1rem' }} className={classes.row}>
                                            <Button
                                                style={{ marginRight: '1rem' }}
                                                onClick={() => deleteAction(id)}
                                                variant="outline"
                                                color="black"
                                            >
                                                {t("ReviewCards.confirm")}
                                            </Button>
                                            <Button
                                                onClick={() => setOpenedTooltip(false)}
                                                variant="outline"
                                                color="black"
                                            >
                                                {t("ReviewCards.cancel")}
                                            </Button>
                                        </div>
                                    </div>
                                </Card>
                            </div>
                        } opened={openedTooltip} position="top" color="white" className={classes.outline} >
                            <ActionIcon variant="white" color="red" onClick={() => setOpenedTooltip((o) => !o)}>
                                <IconTrash />
                            </ActionIcon>
                        </Tooltip>
                    }

                </div>
            </div>
            <Divider className={classes.divider} />
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
            <br />
            <div className={classes.badge_row}>
                {difficulty == 'EASY' ? <Badge className={classes.badge_row_elem} color={'green.7'}>{t("SubjectCard.easy")}</Badge> : null}
                {difficulty == 'MEDIUM' ? <Badge className={classes.badge_row_elem} color={'blue.7'}>{t("SubjectCard.normal")}</Badge> : null}
                {difficulty == 'HARD' ? <Badge className={classes.badge_row_elem} color={'red.7'}>{t("SubjectCard.hard")}</Badge> : null}

                {timeDemand == 'LOW' ? <Badge className={classes.badge_row_elem} color={'green.7'}>{t("SubjectCard.low")}</Badge> : null}
                {timeDemand == 'MEDIUM' ? <Badge className={classes.badge_row_elem} color={'blue.7'}>{t("SubjectCard.medium")}</Badge> : null}
                {timeDemand == 'HIGH' ? <Badge className={classes.badge_row_elem} color={'red.7'}>{t("SubjectCard.high")}</Badge> : null}
            </div>
            <Divider className={classes.divider} />
            <div slot="footer" className={classes.like_buttons}>
                {!(didUserUpVote) ?
                    <ActionIcon variant="transparent" className={classes.like_button}
                                onClick={() => voteAction(id, VoteValue.UpVote)}
                    >
                        <IconThumbUp/>
                    </ActionIcon> :
                    <ActionIcon variant="transparent" className={classes.like_button}
                                onClick={() => unVoteAction(id, userId)}
                    >
                        <IconThumbUp color={votedColor}/>
                    </ActionIcon>
                }
                <span>{upVotes}</span>
                {!(didUserDownVote) ?
                    <ActionIcon variant="transparent" className={classes.like_button}
                                onClick={() => voteAction(id, VoteValue.DownVote)}
                    >
                        <IconThumbDown/>
                    </ActionIcon> :
                    <ActionIcon variant="transparent" className={classes.like_button}
                                onClick={() => unVoteAction(id, userId)}
                    >
                        <IconThumbDown color={votedColor}/>
                    </ActionIcon>
                }
                <span>{downVotes}</span>
            </div>
        </Card>
    );
}




export default ReviewCard;
