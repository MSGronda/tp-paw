import {useLocation, useNavigate} from "react-router-dom";
import classes from './multi-review.module.css';
import {useEffect, useState} from "react";
import {Subject} from "../../models/Subject.ts";
import {reviewService, subjectService} from "../../services";
import {handleService} from "../../handlers/serviceHandler.tsx";
import {Progress} from "@mantine/core";
import {Button, SegmentedControl, Textarea} from "@mantine/core";
import {useTranslation} from "react-i18next";


export default function MultiReview() {
    const navigate = useNavigate();
    const { t } = useTranslation();
    const queryParams = new URLSearchParams(useLocation().search);
    
    // Review Info
    const [review, setReview] = useState("")
    const [difficultyValue, setDifficultyValue] = useState("-1")
    const [timeDemandValue, setTimeDemandValue] = useState("-1")
    const [AnonymousValue, setAnonymousValue] = useState("");

    // Subject Info
    const [subjects, setSubjects] = useState<Subject[]>([]);
    const [currentPos, setCurrentPos] = useState(0);

    // Review error
    const [reviewError, setReviewError] = useState(true)
    const [difficultyError, setDifficultyError] = useState(true)
    const [timeDemandError, setTimeDemandError] = useState(true)
    const [AnonymousError, setAnonymousError] = useState(true)

    // Subject Behavior
    const getSubject = async (id: string) => {
        const resp = await subjectService.getSubjectById(id);
        const data = handleService(resp, navigate);
        if(data == "") {
            navigate("/")
        }
        return data;
    }
    const getSubjects = async () => {
        const ids = parseQueryParams(queryParams.get('r'));

        const apiCalls = ids.map((id) => getSubject(id));
        const resp = await Promise.all(apiCalls);    // Hacemos los llamados en paralelo

        setSubjects(sortById(ids, resp));
    }

    // Review Behavior
    useEffect(() => {
        if (review === "") {
            setReviewError(true)
        }
        else {
            setReviewError(false)
        }

        if (difficultyValue === "-1") {
            setDifficultyError(true)
        } else {
            setDifficultyError(false)
        }

        if (timeDemandValue === "-1") {
            setTimeDemandError(true)
        } else {
            setTimeDemandError(false)
        }

        if (AnonymousValue === "") {
            setAnonymousError(true)
        }else {
            setAnonymousError(false)
        }
    }, [review, difficultyValue, timeDemandValue, AnonymousValue])
    const isSubmitDisabled = reviewError || difficultyError || timeDemandError || AnonymousError;

    const nextReview = () => {
        if(currentPos < subjects.length) {
            setCurrentPos(currentPos + 1);
        }
        else {
            navigate("/");
        }
    }
    const handleReviewSubmit = async (e: { preventDefault: () => void; }) => {
        e.preventDefault();

        if(subjects.length == 0){
            return;
        }
        await reviewService.publishReview(subjects[currentPos].id, review, parseInt(difficultyValue) , parseInt(timeDemandValue), AnonymousValue === "true");
        nextReview();
    }

    // API Call
    useEffect( () => {
        getSubjects()
    }, [])

    return <div className={classes.fullsize}>
        <div className={classes.progress_area}>
            <Progress
                radius="md"
                value={(currentPos/subjects.length) * 100}
                style={{ backgroundColor: 'grey' }}
            />
            <div className={classes.progress_text_area}>
                <span>{t("MultiReview.progressText", {c: currentPos + 1, l: subjects.length})}</span>
            </div>
        </div>
        {
            subjects.length > 0 ?
            <div className={classes.container_50}>
                <div className={classes.flex}>
                    <h1 className={classes.title}> {t("Review.title", {subjectName: subjects[currentPos].name})} </h1>

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
                        <Button variant="default" onClick={nextReview}>
                            {t("MultiReview.skip")}
                        </Button>
                        <Button type='submit' color='green.7' disabled={isSubmitDisabled}>
                            {t("Review.submit")}
                        </Button>
                    </form>
                </div>
            </div>
            :
            <></>
        }
    </div>
}

function parseQueryParams(query: string | null): string[] {
    if(!query){
        return [];
    }
    const resp: string[] = []
    query.split(" ").forEach((id) => {resp.push(id)});
    return resp;
}
function sortById(ids: string[], subjects: Subject[]) {
    const resp: Subject[] = [];
    for(const id of ids) {
        const sub = subjects.find((s) => s.id == id);
        if(sub){
            resp.push(sub);
        }
    }
    return resp;
}