import classes from './review.module.css';
import { Navbar } from "../../components/navbar/navbar";
import { useTranslation } from 'react-i18next';
import { handleService } from '../../handlers/serviceHandler';
import { useNavigate, useParams } from 'react-router-dom';
import { subjectService } from '../../services';
import { useEffect, useState } from 'react';
import { Button, SegmentedControl, TextInput, Textarea } from '@mantine/core';


export default function Review() {
    const { t } = useTranslation();

    const { subjectCode } = useParams()
    console.log(subjectCode)
    const navigate = useNavigate();

    const [review, setReview] = useState("")
    const [difficultyValue, setDifficultyValue] = useState("0")
    const [timeDemandValue, setTimeDemandValue] = useState("0")
    const [AnonymousValue, setAnonymousValue] = useState("0")
    // useEffect(() => {
    //     if(!id || /\d{2}\.\d{2}/.test(id)){
    //         console.log("esoty aca")
    //         navigate(-1);
    //     }
    // }, [id, navigate])

    const subjectName = "Materia" //TODO cambiar cuando se pase el nombre desde subject

    // const searchSubject = async (id: string) => {
    //     const res = await subjectService.getSubjectById(id);
    //     const data = handleService(res, navigate)
    //     if(res){
    //         setSubject(data);
    //         console.log(data);
    //     }
    // }

    // useEffect(() => {
    //     if(id)
    //         searchSubject(id);
    // }, [])

    const handleReviewSubmit = async (e: { preventDefault: () => void; }) => {
        e.preventDefault();
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
                                { value: "0", label: t("Review.public") },
                                { value: "1", label: t("Review.anonymous") },
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