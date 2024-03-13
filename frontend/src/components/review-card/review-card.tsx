import { ActionIcon, Badge, Button, Card, Divider, Group, Tooltip } from "@mantine/core";
import classes from './review-card.module.css';
import { IconEdit, IconPencil, IconThumbDown, IconThumbUp, IconTrash } from "@tabler/icons-react";
import {useContext, useEffect, useState} from "react";
import { useTranslation } from "react-i18next";
import { Link, useNavigate } from "react-router-dom";
import AuthContext from "../../context/AuthContext";
import { reviewService } from "../../services";
import {useDisclosure} from "@mantine/hooks";

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
    voteValue: VoteValue
}
enum VoteValue {
    UpVote = 1,
    DownVote = -1,

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
    const { t } = useTranslation();
    const { userId } = useContext(AuthContext)
    const { subjectId, subjectName, text, timeDemand, difficulty, UserId, userName, anonymous, id, forSubject, upvotes, downvotes } = props;
    const [openedTooltip, setOpenedTooltip] = useState(false);
    const [showMore, setShowMore] = useState(false);
    const [didUserUpVote, setDidUserUpVote] = useState(false);
    const [didUserDownVote, setDidUserDownVote] = useState(false);
    const [loading,  toggle ] = useState(false);


    const navigate = useNavigate();
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
        const res = await reviewService.voteReview(reviewId, GetVoteValue(vote))
        localStorage.setItem('reviewVoted', JSON.stringify(!res?.failure))
        toggle(true)
    }

    const unVoteAction = async (reviewId: number, userId: number | undefined) => {
        const res = await reviewService.unVoteReview(reviewId, userId)
        localStorage.setItem('reviewUnVoted', JSON.stringify(!res?.failure))
        toggle(true)
    }

    const getVoteFromUser = async (reviewId:number, userId:number) => {
        const res = await reviewService.getVotes(reviewId)
        const vote = res?.data.find((v: { userId: number; }) => v.userId === userId)
        if(vote !== undefined) {
            if(vote.vote === 1) {
                setDidUserUpVote(true)
                setDidUserDownVote(false)
            } else if(vote.vote === -1) {
                setDidUserUpVote(false)
                setDidUserDownVote(true)
            }
        }
    }

    useEffect(() => {
        if(userId !== undefined) {
            if(id !== undefined) {
                getVoteFromUser(id, userId)
            }
        }
    })

    return (
        <Card className={classes.card}>
            <div slot="header" className={classes.header}>
                {forSubject === false ?
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
                <span>{upvotes}</span>
                { !(didUserUpVote)  ?
                    <ActionIcon variant="outline" className={classes.like_button}
                            onClick={ () => voteAction(id,VoteValue.UpVote) }
                                loading={loading}>
                        <IconThumbUp />
                    </ActionIcon> :
                    <ActionIcon variant="filled" className={classes.like_button}
                            onClick={ () => unVoteAction(id, userId)}
                                loading={loading}>
                        <IconThumbUp />
                    </ActionIcon>
                }
                <span>{downvotes}</span>
                { !(didUserDownVote ) ?
                    <ActionIcon variant="outline" className={classes.like_button}
                                onClick={() => voteAction(id, VoteValue.DownVote )}
                                loading={loading}>
                        <IconThumbDown />
                    </ActionIcon> :
                    <ActionIcon variant="filled" className={classes.like_button}
                                onClick={() => unVoteAction(id, userId)}
                                loading={loading}>
                        <IconThumbDown />
                    </ActionIcon>
                }
            </div>
        </Card>
    );
}



export default ReviewCard;
