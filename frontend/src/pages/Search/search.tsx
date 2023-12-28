import classes from './search.module.css';
import { Navbar } from "../../components/navbar/navbar";
import { useTranslation } from 'react-i18next';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { subjectService } from '../../services';
import { handleService } from '../../handlers/serviceHandler';
import { Key, useEffect, useState } from 'react';
import { Grid } from '@mantine/core';
import SubjectCard from '../../components/subject-card/subject-card';


export default function Search() {
    const location = useLocation();
    const searchParams = new URLSearchParams(location.search);
    const query = searchParams.get('q');

    const { t } = useTranslation();
    const navigate = useNavigate();
    const [subjects, setSubjects] = useState<any>([]);

    const searchSubjects = async (searchValue: string) => {
        const res = await subjectService.getSubjectsByName(searchValue);
        const data = handleService(res, navigate)
        // console.log(data)
        setSubjects(data);
    }

    useEffect(() => {
        searchSubjects(query ?? '');
    }, [query]);

    useEffect(() => {
        console.log(subjects)
    }, [subjects])

    return (
        <div className={classes.fullsize}>
            <Navbar/>
            <div className={classes.container_70}>
            <div>
                {subjects.length > 0 ? (
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
                ) : (
                    <div className={classes.not_found_area}>
                    <h3>{t("Search.not_found", { query })}</h3>
                    </div>
                )}
                </div>
            </div>
            
        </div>
    )
}