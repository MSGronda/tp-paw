import classes from './search.module.css';
import { Navbar } from "../../components/navbar/navbar";
import { useTranslation } from 'react-i18next';
import { useLocation, useNavigate } from 'react-router-dom';
import { subjectService } from '../../services';
import { handleService } from '../../handlers/serviceHandler';
import { useEffect, useState } from 'react';
import SubjectCard from '../../components/subject-card/subject-card';
import PaginationComponent from '../../components/pagination/pagination';

export default function Search() {
    const location = useLocation();
    const searchParams = new URLSearchParams(location.search);
    const query = searchParams.get('q');
    const page = searchParams.get('page');

    const { t } = useTranslation();
    const navigate = useNavigate();
    const [subjects, setSubjects] = useState<any>([]);
    const [loading, setLoading] = useState(true);
    const [maxPage, setMaxPage] = useState(1);

    const searchSubjects = async (searchValue: string, pageNumber: number) => {
        const res = await subjectService.getSubjectsByName(searchValue, pageNumber);
        const data = handleService(res, navigate);
        if (res) {
            setSubjects(data);
            setMaxPage(res.maxPage || 1);
        }
        setLoading(false);
    }

    const handlePageChange = (newPage: number) => {
        // Update the URL with the new page number when pagination changes
        const queryParams = new URLSearchParams(window.location.search);
        queryParams.set('page', newPage.toString());
        navigate(`${window.location.pathname}?${queryParams}`);
    }

    useEffect(() => {
        searchSubjects(query ?? '', parseInt(page ?? '1'));
    }, [query, page]);

    return (
        <div className={classes.fullsize}>
            <Navbar />
            <div className={classes.container_70}>
                <div>
                    {loading ? <div /> :
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
                                <div className={classes.center_pagination}>
                                    <PaginationComponent page={page} lastPage={maxPage} setPage={handlePageChange} />
                                </div>
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
