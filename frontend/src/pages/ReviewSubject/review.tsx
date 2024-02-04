import classes from './review.module.css';
import { Navbar } from "../../components/navbar/navbar";
import { useTranslation } from 'react-i18next';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { Button, SegmentedControl, Textarea } from '@mantine/core';
import { reviewService } from "../../services";


export default function Review() {
    const { t } = useTranslation();

    const { id } = useParams()
    const { state } = useLocation()
    const subjectName = state.name //TODO cambiar cuando se pase el nombre desde subject


    const navigate = useNavigate();

    const [review, setReview] = useState("")
    const [difficultyValue, setDifficultyValue] = useState("-1")
    const [timeDemandValue, setTimeDemandValue] = useState("-1")
    const [AnonymousValue, setAnonymousValue] = useState("-1")

    const handleReviewSubmit = async (e: { preventDefault: () => void; }) => {
        e.preventDefault();

        //validateText(review, setReviewError);

        if( id === undefined) {
            return;
        }
        const res = await reviewService.publishReview(id, review, parseInt(difficultyValue) , parseInt(timeDemandValue), AnonymousValue === "true");
        console.log(res)

        navigate('/subject/' + id)
    }


    return (
        <div className={classes.fullsize}>
            <Navbar />
            <div className={classes.container_50}>
                <div className={classes.flex}>
                    <h1 className={classes.title}>{t("Review.title", { subjectName })}</h1>
                    
                    <form onSubmit={handleReviewSubmit} className={classes.form}>
                        <Textarea
                            value={review}
                            onChange={(e) => setReview(e.currentTarget.value)}
                            label={t("Review.review")}
                            className={classes.review_input}
                            minRows={5}
                            maxRows={8}
                            autosize
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
                        <Button type='submit' color='green.7'>
                            {t("Review.submit")}
                        </Button>





                    </form>
                </div>
            </div>
        </div>
    )
}