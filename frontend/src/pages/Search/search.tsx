import classes from './search.module.css';
import {Navbar} from "../../components/navbar/navbar";
import {useTranslation} from 'react-i18next';
import {useNavigate, useSearchParams} from 'react-router-dom';
import {subjectService} from '../../services';
import {handleService} from '../../handlers/serviceHandler';
import {useEffect, useState} from 'react';
import SubjectCard from '../../components/subject-card/subject-card';
import PaginationComponent from '../../components/pagination/pagination';
import Title from '../../components/title/title';
import {Subject} from "../../models/Subject.ts";
import {Loader, Title as MantineTitle} from "@mantine/core";
import SubjectFilters from "../../components/subject-filters/subjectFilters.tsx";
import {parseSearchParams} from "../../utils/searchUtils.ts";

export default function Search() {
    const { t } = useTranslation(undefined, { keyPrefix: "Search" });
    const navigate = useNavigate();
    const [params, setParams] = useSearchParams();
    
    const [subjects, setSubjects] = useState<Subject[]>([]);
    const [loading, setLoading] = useState(true);
    const [maxPage, setMaxPage] = useState(1);
    
    const parsedParams = parseSearchParams(params);

    function changePage(newPage: number) {
        setParams(params => { 
            params.set("page", newPage.toString());
            return params;
        });
    }

    useEffect(() => {
        async function searchSubjects() {
            setLoading(true);
            
            try{
                const res = await subjectService.search(
                  parsedParams.q,
                  parsedParams.page,
                  parsedParams.minCredits,
                  parsedParams.maxCredits,
                  parsedParams.department,
                  parsedParams.minDifficulty,
                  parsedParams.maxDifficulty,
                  parsedParams.minTimeDemand,
                  parsedParams.maxTimeDemand,
                  parsedParams.orderBy,
                  parsedParams.dir,
                  parsedParams.degree
                );
                
                const data = handleService(res, navigate);
                if (res) {
                    setSubjects(data);
                    setMaxPage(res.maxPage || 1);
                }
                
            } catch(err) {
                console.error(err);
                
            } finally {
                setLoading(false);
            }
        }
        searchSubjects().catch(console.error);
    }, [params, navigate]);

    return (
        <div className={classes.fullsize}>
          <Title text={`${t("title")} ${parsedParams.q ?? ""}`}/>
          <Navbar />
          <div className={classes.container_70}>
            {loading ? <Loader/> :
              <>
                  <MantineTitle mx="1rem" mt="2rem">{t("search_for", {query: parsedParams.q})}</MantineTitle>
                <SubjectFilters />
                {subjects && subjects.length > 0 ? (
                  <>
                    <div className={classes.results_area}>
                      <div className={classes.search_area}>
                        {subjects.map(subject => (
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
                        <PaginationComponent page={parsedParams.page} lastPage={maxPage} setPage={changePage} />
                      </div>
                    </div>
                  </>
                ) : (
                <div className={classes.not_found_area}>
                  <h3>{t("not_found", { query: parsedParams.q })}</h3>
                </div>
                )}
              </>
            }
          </div>
        </div>
    )
}

