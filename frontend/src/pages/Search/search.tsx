import classes from './search.module.css';
import { Navbar } from "../../components/navbar/navbar";
import { useTranslation } from 'react-i18next';
import { useLocation, useNavigate } from 'react-router-dom';
import { subjectService } from '../../services';
import { handleService } from '../../handlers/serviceHandler';
import { JSXElementConstructor, ReactElement, ReactNode, ReactPortal, useEffect, useState } from 'react';
import SubjectCard from '../../components/subject-card/subject-card';
import PaginationComponent from '../../components/pagination/pagination';
import { Button, Divider } from '@mantine/core';
import { IconAdjustments } from '@tabler/icons-react';

export default function Search() {
    const location = useLocation();
    const searchParams = new URLSearchParams(location.search);
    const query = searchParams.get('q');
    const page = searchParams.get('page');
    const credits = searchParams.get('credits');
    const department = searchParams.get('department');
    const difficulty = searchParams.get('difficulty');
    const timeDemand = searchParams.get('time');
    const orderBy = searchParams.get('ob');
    const dir = searchParams.get('dir');

    const { t } = useTranslation();
    const navigate = useNavigate();
    const [subjects, setSubjects] = useState<any>([]);
    const [filters, setFilters] = useState([])
    const [loading, setLoading] = useState(true);
    const [maxPage, setMaxPage] = useState(1);
    const [isFilterSectionVisible, setIsFilterSectionVisible] = useState(false);

    const searchSubjects = async (searchValue: string, pageNumber: number, credits: number | null, department: string | null, difficulty: number | null, timeDemand: number | null, orderBy: string | null, dir: string | null ) => {
        const res = await subjectService.getSubjectsByName(searchValue, pageNumber, credits, department, difficulty, timeDemand, orderBy, dir);
        const data = handleService(res, navigate);
        if (res) {
            setSubjects(data.subjects);
            setMaxPage(res.maxPage || 1);
            setFilters(data.filters.entry)           
        }
        setLoading(false);        
    }

    useEffect(() => {
        // const first = filters[0]
        // const second = filters[1]
        // console.log(first);
        // console.log(second);
        // // filters[1].value.array.forEach((element: string) => {
        // //     console.log(element);
        // // })
        // console.log(second['key'])
      }, [filters]);

      const handleFilterClick = (type: string, selectedFilter: string) => {
        // Assuming your current URL looks like: /your/path
        const currentUrl = new URL(window.location.href);
      
        // Manually encode the filter value before updating the URL
        const encodedFilter = encodeURIComponent(selectedFilter);
      
        // Get the existing query parameters
        const queryParams = currentUrl.searchParams.toString();
      
        // Construct the new query string
        const newQueryString = queryParams
          ? `${queryParams}&${type}=${encodedFilter}`
          : `${type}=${encodedFilter}`;
      
        // Update the URL without triggering a page reload
        window.history.pushState({}, '', `${currentUrl.pathname}?${newQueryString}${currentUrl.hash}`);
        window.location.reload();
      };
      

    const handlePageChange = (newPage: number) => {
        // Update the URL with the new page number when pagination changes
        const queryParams = new URLSearchParams(window.location.search);
        queryParams.set('page', newPage.toString());
        navigate(`${window.location.pathname}?${queryParams}`);
    }

    const toggleFilterSection = () => {
        setIsFilterSectionVisible(prevState => !prevState);
    };

    useEffect(() => {
        searchSubjects(query ?? '', parseInt(page ?? '1'), credits != null ? parseInt(credits) : null, department, difficulty != null ? parseInt(difficulty) : null, timeDemand != null ? parseInt(timeDemand) : null, orderBy, dir);
    }, [query, page, credits, difficulty, timeDemand, department, orderBy, dir]);

    return (
        <div className={classes.fullsize}>
            <Navbar />
            <div className={classes.container_70}>
                <div>
                    {loading ? <div /> :
                        subjects.length > 0 ? (
                            <>
                                <div className={classes.filters_area}>
                                    <div className={classes.filter}>
                                        <Button onClick={toggleFilterSection} leftSection={<IconAdjustments/>} variant='default' size='small' className={classes.filter_button}>
                                            {t("Search.filter")}
                                        </Button>
                                    </div>
                                    <section style={{ display: isFilterSectionVisible ? 'flex' : 'none' }} className={classes.filter_section}>
                                        <Divider orientation='vertical' className={classes.vert_divider}/>
                                        <div className={classes.filter_option}>
                                            <h5>{t("Search.filterDepartment")}</h5>
                                            {((filters[1]['value'] as string[]) || []).map((element: string) => (
                                                <>
                                                <span onClick={() => handleFilterClick("department", element)} className={classes.filter_option_button}>{element}</span>
                                                <br/>
                                                </>
                                            ))}
                                        </div>
                                        <Divider orientation='vertical' className={classes.vert_divider}/>
                                        <div className={classes.filter_option}>
                                            <h5>{t("Search.filterCredits")}</h5>
                                            {((filters[0]['value'] as string[]) || []).map((element: string) => (
                                                <>
                                                <span onClick={() => handleFilterClick("credits", element)} className={classes.filter_option_button}  >{element}</span>
                                                <br/>
                                                </>
                                            ))}
                                        </div>
                                        <Divider orientation='vertical' className={classes.vert_divider}/>
                                        <div className={classes.filter_option}>
                                            <h5>{t("Search.filterDifficulty")}</h5>
                                            {((filters[2]['value'] as string[]) || []).map((element: string) => (
                                                <>
                                                <span onClick={() => handleFilterClick("difficulty", element)} className={classes.filter_option_button}  >{element}</span>
                                                <br/>
                                                </>
                                            ))}                                            
                                        </div>
                                        <Divider orientation='vertical' className={classes.vert_divider}/>
                                        <div className={classes.filter_option}>
                                            <h5>{t("Search.filterTimeDemand")}</h5>
                                            {((filters[3]['value'] as string[]) || []).map((element: string) => (
                                                <>
                                                <span onClick={() => handleFilterClick("time", element)} className={classes.filter_option_button}  >{element}</span>
                                                <br/>
                                                </>
                                            ))}
                                        </div>
                                        <Divider orientation='vertical' className={classes.vert_divider}/>
                                        <div className={classes.filter_option}>
                                            <h5>{t("Search.filterOrderBy")}</h5>
                                            
                                        </div>
                                    </section>
                                {/* {filters.map((filter: { key: string; value: any[] }) => {
                                        const { key, value } = filter;
                                        console.log(`Filter Key: ${key}`);
                                        
                                        // Iterate through the value array
                                        if (Array.isArray(value)) {
                                            value.forEach((item: any) => {
                                                console.log(`Item: ${item}`);
                                            });
                                        }
                                        return null;
                                    })} */}
                                </div>
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
                            </>
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
