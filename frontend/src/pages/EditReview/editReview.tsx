import { useContext, useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import { reviewService } from "../../services";
import AuthContext from "../../context/AuthContext";
import { Button, SegmentedControl, Textarea } from "@mantine/core";
import { Navbar } from "../../components/navbar/navbar";
import classes from './editReview.module.css';
import { getDifficultyValue, getTimeDemandValue } from "../../models/Subject";
import { Helmet } from "react-helmet";
import Title from "../../components/title/title";


export default function EditReview() {
    const { t } = useTranslation();
    const navigate = useNavigate();

    const { reviewId, subjectId } = useParams()
    const { state } = useLocation()
    const subjectName = state.subjectName
    const fromSubject = state.fromSubject

    const [review, setReview] = useState(state.text)
    const [difficultyValue, setDifficultyValue] = useState(getDifficultyValue(state.difficulty).toString())
    const [timeDemandValue, setTimeDemandValue] = useState(getTimeDemandValue(state.timeDemand).toString() )
    const [AnonymousValue, setAnonymousValue] = useState(state.anonymous.toString())

    const [reviewError, setReviewError] = useState(false)

    useEffect(() => {
        if (review === "") {
            setReviewError(true)
        }else{
            setReviewError(false)
        }
    }, [review])

    const handleEditSubmit = async (e: { preventDefault: () => void; }) => {
        e.preventDefault()

        if( reviewId === undefined || subjectId === undefined){
            return;
        }

        const res = await reviewService.editReview(parseInt(reviewId), review, parseInt(difficultyValue), parseInt(timeDemandValue), AnonymousValue === "true", subjectId)

        if( fromSubject ){
            navigate(`/subject/${subjectId}`, { state: { reviewUpdated: !res?.failure}})
        }else{
            navigate(`/profile`)
        }

    }

    return (
        <div className={classes.fullsize}>
            <Title text={t("Review.editSubmit")}/>
            <Navbar />
            <div className={classes.container_50}>
                <div className={classes.flex}>
                    <h1 className={classes.title}>{t("Review.title", { subjectName })}</h1>
                    
                    <form onSubmit={handleEditSubmit} className={classes.form}>
                        <Textarea
                            value={review}
                            onChange={(e) => setReview(e.currentTarget.value)}
                            label={t("Review.review")}
                            className={classes.review_input}
                            minRows={5}
                            maxRows={8}
                            autosize
                            maxLength={2000}
                        />
                        <br />

                        <span>{t("Review.option")}</span>
                        <br />
                        <SegmentedControl
                            radius="sm"
                            size="sm"
                            color='blue'
                            value={difficultyValue}
                            onChange={setDifficultyValue}
                            data={[
                                { value: "0", label: t("Review.easy") },
                                { value: "1", label: t("Review.medium") },
                                { value: "2", label: t("Review.hard") }
                            ]}
                        />
                        <br />
                        <span  className={classes.help}>{t("Review.difficultyHelp")}</span>
                        <br />

                        <br />
                        <SegmentedControl
                            radius="sm"
                            size="sm"
                            color='blue'
                            value={timeDemandValue}
                            onChange={setTimeDemandValue}
                            data={[
                                { value: "0", label: t("Review.lowTimeDemand") },
                                { value: "1", label: t("Review.mediumTimeDemand") },
                                { value: "2", label: t("Review.highTimeDemand") }
                            ]}
                        />
                        <br />
                        <span  className={classes.help}>{t("Review.timeDemandHelp")}</span>

                        <br />
                        <br />
                        <SegmentedControl
                            radius="sm"
                            size="sm"
                            color='blue'
                            value={AnonymousValue}
                            onChange={setAnonymousValue}
                            data={[
                                { value: "false", label: t("Review.public") },
                                { value: "true", label: t("Review.anonymous") },
                            ]}
                        />

                        <br />
                        <br />
                        <Button type='submit' color='green.7' disabled={reviewError}>
                            {t("Review.editSubmit")}
                        </Button>
                    </form>
                </div>
            </div>
        </div>
    )
}