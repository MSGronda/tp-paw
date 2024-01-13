import SubjectCard from "../../components/subject-card/subject-card.tsx";
import {subjectService} from "../../services";
import {useEffect, useState} from "react";
import classes from "../SemesterBuilder/semester_builder.module.css";
import {handleService} from "../../handlers/serviceHandler.tsx";
import {useNavigate} from "react-router-dom";
import {Navbar} from "../../components/navbar/navbar.tsx";
import {Card} from "@mantine/core";


export default function SemesterBuilder() {

    const [subjects, setSubjects] = useState<any>([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();
    const getSubjects = async (userId: number) => {
        const res = await subjectService.getSubjectsUserCan(userId);
        const data = handleService(res, navigate);
        if (res) {
            setSubjects(data);
        }
        setLoading(false);
    }

    useEffect(() => {
        getSubjects(1);
    });
    
    return (
        <div className={classes.background}>
            <Navbar />
            <Card className={classes.available_card}>
                <Card.Section>
                    Available
                </Card.Section>
                { loading ? <div/> :
                    subjects.length > 0 ? (
                        <div className={classes.results_area}>
                            <div className={classes.search_area}>
                                {subjects.map((subject: { id: string; credits: number; difficulty: string; name: string; reviewCount: number; prerequisites: string[]; timeDemand: string; progress: string; }) => (
                                    <SubjectCard
                                        key={subject.id}
                                        id={subject.id}
                                        credits={subject.credits}
                                        difficulty={subject.difficulty}
                                        name={subject.name}
                                        numReviews={subject.reviewCount}
                                        prerequisites={[]}
                                        timeDemand={subject.timeDemand}
                                        progress={"incomplete"}
                                    />
                                ))}
                            </div>
                        </div>

                    ) : (
                        <div></div>
                    )}
            </Card>

        </div>
    )
}