import {subjectService, userService} from "../../services";
import {handleService} from "../../handlers/serviceHandler.tsx";
import {useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";
import {UserPlan} from "../../models/UserPlan.ts";
import {Subject} from "../../models/Subject.ts";
import classes from "../UserSemesters/user_semesters.module.css";
import Title from "../../components/title/title.tsx";
import {t} from "i18next";
import {Navbar} from "../../components/navbar/navbar.tsx";
import {SemesterCard} from "../../components/semester-card/semester-card.tsx";

export default function UserSemesters() {
    // Navigation
    const navigate = useNavigate();

    // User semesters
    const [userSemesters, setUserSemesters] = useState<UserPlan[]>([]);
    const [semesterSubjects, setSemesterSubjects] = useState<Map<number, Subject[]>>(new Map());
    const getUserSemesters = async () => {
        const userId = userService.getUserId();

        if(!userId){
            navigate('/login');
            return;
        }

        const plan: UserPlan[] = handleService(await userService.getUserPlan(userId), navigate);
        plan.sort((u1, u2) => {return u2.dateFinished - u1.dateFinished})
        setUserSemesters(plan);

        const newSemesterSubjects: Map<number,Subject[]> = new Map();

        for(const semester of plan){
            const subjectData = handleService(await subjectService.getUserPlanSubjects(userId, semester.dateFinished), navigate);
            newSemesterSubjects.set(semester.dateFinished, subjectData.subjects);

        }
        setSemesterSubjects(newSemesterSubjects);
    }

    // API calls
    useEffect(() => {
        getUserSemesters()
    }, [])


    return (
    <div className={classes.general_area}>
        <Title text={t('UserSemesters.title')}/>
        <Navbar />
        <div className={classes.container_70}>
            <div>
                <h2 style={{margin: ".5rem", fontSize: "2rem"}}>{t("UserSemesters.bodyTitle")}</h2>
                <div style={{ borderBottom: '1px solid #ccc', margin: '10px 0' }}></div>
            </div>
            <div className={classes.semester_area}>
                {
                    userSemesters.map((semester, i) =>
                        <SemesterCard
                            key={i}
                            dateFinished={semester.dateFinished}
                            subjects={semesterSubjects.get(semester.dateFinished) ?? []}
                            index={i}
                            totalSemester={userSemesters.length}
                        />
                    )
                }
            </div>
        </div>

    </div>);
}
