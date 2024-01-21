import classes from './search.module.css';
import { Navbar } from "../../components/navbar/navbar";
import { useTranslation } from 'react-i18next';
import { useLocation, useNavigate } from 'react-router-dom';
import { subjectService } from '../../services';
import { handleService } from '../../handlers/serviceHandler';
import { JSXElementConstructor, ReactElement, ReactNode, ReactPortal, useEffect, useState } from 'react';
import SubjectCard from '../../components/subject-card/subject-card';
import PaginationComponent from '../../components/pagination/pagination';
import { ActionIcon, Button, CloseButton, Divider } from '@mantine/core';
import { IconAdjustments, IconArrowNarrowDown, IconArrowNarrowUp } from '@tabler/icons-react';

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

    const [showAscArrow, setShowAscArrow] = useState(false);

    const [nameFilter, setNameFilter] = useState(false);
    const [creditsFilter, setCreditsFilter] = useState(false);
    const [idFilter, setIdFilter] = useState(false);
    const [difficultyFilter, setDifficultyFilter] = useState(false);
    const [timeFilter, setTimeFilter] = useState(false);

    useEffect(() => {
        if (orderBy !== null) {
            if (orderBy === 'name') {
                setNameFilter(true);
                setCreditsFilter(false);
                setIdFilter(false);
                setDifficultyFilter(false);
                setTimeFilter(false);
            } else if (orderBy === 'credits') {
                setNameFilter(false);
                setCreditsFilter(true);
                setIdFilter(false);
                setDifficultyFilter(false);
                setTimeFilter(false);
            } else if (orderBy === 'id') {
                setNameFilter(false);
                setCreditsFilter(false);
                setIdFilter(true);
                setDifficultyFilter(false);
                setTimeFilter(false);
            } else if (orderBy === 'difficulty') {
                setNameFilter(false);
                setCreditsFilter(false);
                setIdFilter(false);
                setDifficultyFilter(true);
                setTimeFilter(false);
            } else {
                setNameFilter(false);
                setCreditsFilter(false);
                setIdFilter(false);
                setDifficultyFilter(false);
                setTimeFilter(true);
            }
        }
        if( dir !== null){
            if (dir === 'asc') {
                setShowAscArrow(true);
            } else {
                setShowAscArrow(false);
            }
        }

    }, [orderBy, dir])

    const { t } = useTranslation();
    const navigate = useNavigate();
    const [subjects, setSubjects] = useState<any>([]);
    const [filters, setFilters] = useState([])
    const [loading, setLoading] = useState(true);
    const [maxPage, setMaxPage] = useState(1);
    const [isFilterSectionVisible, setIsFilterSectionVisible] = useState(false);

    const searchSubjects = async (searchValue: string, pageNumber: number, credits: number | null, department: string | null, difficulty: number | null, timeDemand: number | null, orderBy: string | null, dir: string | null) => {
        const res = await subjectService.getSubjectsByName(searchValue, pageNumber, credits, department, difficulty, timeDemand, orderBy, dir);
        const data = handleService(res, navigate);
        if (res) {
            setSubjects(data.subjects);
            setMaxPage(res.maxPage || 1);
            setFilters(data.filters?.entry || [])
        }
        setLoading(false);
    }

    const handleFilterClick = (type: string, selectedFilter: string) => {
        // Assuming your current URL looks like: /your/path
        const currentUrl = new URL(window.location.href);

        // Manually encode the filter value before updating the URL
        const encodedFilter = encodeURIComponent(selectedFilter);

        // Get the existing query parameters
        const queryParams = new URLSearchParams(currentUrl.search);

        let newQueryString = "";

        if (type === 'dir') {
            queryParams.delete('dir');
            if (selectedFilter === 'asc') {
                setShowAscArrow(true);
            } else {
                setShowAscArrow(false);
            }
        }

        if (type === "ob") {
            queryParams.delete('ob');
            queryParams.delete('dir');
            newQueryString = queryParams
                ? `${queryParams}&${type}=${encodedFilter}&dir=asc`
                : `${type}=${encodedFilter}&dir=asc`;
        } else {
            if( (department !== null && selectedFilter === department) || (credits !== null && selectedFilter === credits) || (difficulty !== null && selectedFilter === difficulty) || (timeDemand !== null && selectedFilter === timeDemand)){
                return;
            }
            // Construct the new query string
            newQueryString = queryParams
                ? `${queryParams}&${type}=${encodedFilter}`
                : `${type}=${encodedFilter}`;
        }

        // Update the URL without triggering a page reload
        window.history.pushState({}, '', `${currentUrl.pathname}?${newQueryString}${currentUrl.hash}`);
        window.location.reload();
    };

    const closeFilter = (type: string) => {
        const currentUrl = new URL(window.location.href);

        // Get the existing query parameters
        const queryParams = new URLSearchParams(currentUrl.search);

        queryParams.delete(type);

        window.history.pushState({}, '', `${currentUrl.pathname}?${queryParams.toString()}${currentUrl.hash}`);
        window.location.reload();
    }


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
        if( credits !== null || difficulty !== null || timeDemand !== null || department !== null || orderBy !== null || dir !== null){
            setIsFilterSectionVisible(true);
        }
    }, [query, page, credits, difficulty, timeDemand, department, orderBy, dir]);

    return (
        <div className={classes.fullsize}>
            <Navbar />
            <div className={classes.container_70}>
                <div>
                    {loading ? <div /> :
                        subjects && subjects.length > 0 && filters.length > 0 ? (
                            <>
                                <div className={classes.filters_area}>
                                    <div className={classes.filter}>
                                        <Button onClick={toggleFilterSection} leftSection={<IconAdjustments />} variant='default' size='small' className={classes.filter_button}>
                                            {t("Search.filter")}
                                        </Button>
                                    </div>
                                    <section style={{ display: isFilterSectionVisible ? 'flex' : 'none' }} className={classes.filter_section}>
                                        <Divider orientation='vertical' className={classes.vert_divider} />
                                        <div className={classes.filter_option}>
                                            <h5>{t("Search.filterDepartment")}</h5>
                                            {((filters[1]['value'] as string[]) || []).map((element: string) => (
                                                <>
                                                    <div className={classes.orderBy_button}>
                                                    <span id={element} onClick={() => handleFilterClick("department", element)} className={classes.filter_option_button}>{element}</span>
                                                    {department !== null ? <CloseButton onClick={() => closeFilter("department")} variant="transparent" className={classes.closeButton}/> : <></>}
                                                    </div>
                                                    <br />
                                                </>
                                            ))}
                                        </div>
                                        <Divider orientation='vertical' className={classes.vert_divider} />
                                        <div className={classes.filter_option}>
                                            <h5>{t("Search.filterCredits")}</h5>
                                            {((filters[0]['value'] as string[]) || []).map((element: string) => (
                                                <>
                                                    <div className={classes.orderBy_button}>
                                                    <span id={element} onClick={() => handleFilterClick("credits", element)} className={classes.filter_option_button}  >{element}</span>
                                                    {credits !== null ? <CloseButton onClick={() => closeFilter("credits")} variant="transparent" className={classes.closeButton}/> : <></>}
                                                    </div>
                                                    <br />
                                                </>
                                            ))}
                                        </div>
                                        <Divider orientation='vertical' className={classes.vert_divider} />
                                        <div className={classes.filter_option}>
                                            <h5>{t("Search.filterDifficulty")}</h5>
                                            {((filters[2]['value'] as string[]) || []).map((element: string) => (
                                                <>
                                                    <div className={classes.orderBy_button}>
                                                    <span id={element} onClick={() => handleFilterClick("difficulty", element)} className={classes.filter_option_button}  >{element}</span>
                                                    {difficulty !== null ? <CloseButton onClick={() => closeFilter("difficulty")} variant="transparent" className={classes.closeButton}/> : <></>}
                                                    </div>
                                                    <br />
                                                </>
                                            ))}
                                        </div>
                                        <Divider orientation='vertical' className={classes.vert_divider} />
                                        <div className={classes.filter_option}>
                                            <h5>{t("Search.filterTimeDemand")}</h5>
                                            {((filters[3]['value'] as string[]) || []).map((element: string) => (
                                                <>
                                                    <div className={classes.orderBy_button}>
                                                    <span id={element} onClick={() => handleFilterClick("time", element)} className={classes.filter_option_button}  >{element}</span>
                                                    {timeDemand !== null ? <CloseButton onClick={() => closeFilter("time")} variant="transparent" className={classes.closeButton}/> : <></>}
                                                    </div>
                                                    <br />
                                                </>
                                            ))}
                                        </div>
                                        <Divider orientation='vertical' className={classes.vert_divider} />
                                        <div className={classes.filter_option}>
                                            <h5>{t("Search.filterOrderBy")}</h5>
                                            <div className={classes.orderBy_button}>
                                                <span onClick={() => handleFilterClick("ob", "name")} className={classes.filter_option_button}>{t("Search.filterAZ")}</span>
                                                <div>
                                                    {nameFilter && showAscArrow && <IconArrowNarrowUp className={classes.arrowButtonIcon} onClick={() => handleFilterClick("dir", "desc")} />}
                                                    {nameFilter && !showAscArrow && <IconArrowNarrowUp className={classes.arrowButtonIcon} style={{ display: 'none' }} />}
                                                    {nameFilter && !showAscArrow && <IconArrowNarrowDown className={classes.arrowButtonIcon} onClick={() => handleFilterClick("dir", "asc")} />}
                                                    {nameFilter && showAscArrow && <IconArrowNarrowDown className={classes.arrowButtonIcon} style={{ display: 'none' }} />}
                                                </div>
                                            </div>
                                            <br />
                                            <div className={classes.orderBy_button}>
                                                <span onClick={() => handleFilterClick("ob", "credits")} className={classes.filter_option_button}>{t("Search.filterCredits")}</span>
                                                <div>
                                                    {creditsFilter && showAscArrow && <IconArrowNarrowUp className={classes.arrowButtonIcon} onClick={() => handleFilterClick("dir", "desc")} />}
                                                    {creditsFilter && !showAscArrow && <IconArrowNarrowUp className={classes.arrowButtonIcon} style={{ display: 'none' }} />}
                                                    {creditsFilter && !showAscArrow && <IconArrowNarrowDown className={classes.arrowButtonIcon} onClick={() => handleFilterClick("dir", "asc")} />}
                                                    {creditsFilter && showAscArrow && <IconArrowNarrowDown className={classes.arrowButtonIcon} style={{ display: 'none' }} />}
                                                </div>
                                            </div>
                                            <br />
                                            <div className={classes.orderBy_button}>
                                                <span onClick={() => handleFilterClick("ob", "id")} className={classes.filter_option_button}>{t("Search.filterId")}</span>
                                                <div>
                                                    {idFilter && showAscArrow && <IconArrowNarrowUp className={classes.arrowButtonIcon} onClick={() => handleFilterClick("dir", "desc")} />}
                                                    {idFilter && !showAscArrow && <IconArrowNarrowUp className={classes.arrowButtonIcon} style={{ display: 'none' }} />}
                                                    {idFilter && !showAscArrow && <IconArrowNarrowDown className={classes.arrowButtonIcon} onClick={() => handleFilterClick("dir", "asc")} />}
                                                    {idFilter && showAscArrow && <IconArrowNarrowDown className={classes.arrowButtonIcon} style={{ display: 'none' }} />}
                                                </div>
                                            </div>
                                            <br />
                                            <div className={classes.orderBy_button}>
                                                <span onClick={() => handleFilterClick("ob", "difficulty")} className={classes.filter_option_button}>{t("Search.filterDifficulty")}</span>
                                                <div>
                                                    {difficultyFilter && showAscArrow && <IconArrowNarrowUp className={classes.arrowButtonIcon} onClick={() => handleFilterClick("dir", "desc")} />}
                                                    {difficultyFilter && !showAscArrow && <IconArrowNarrowUp className={classes.arrowButtonIcon} style={{ display: 'none' }} />}
                                                    {difficultyFilter && !showAscArrow && <IconArrowNarrowDown className={classes.arrowButtonIcon} onClick={() => handleFilterClick("dir", "asc")} />}
                                                    {difficultyFilter && showAscArrow && <IconArrowNarrowDown className={classes.arrowButtonIcon} style={{ display: 'none' }} />}
                                                </div>
                                            </div>
                                            <br />
                                            <div className={classes.orderBy_button}>
                                                <span onClick={() => handleFilterClick("ob", "time")} className={classes.filter_option_button}>{t("Search.filterTimeDemand")}</span>
                                                <div>
                                                    {timeFilter && showAscArrow && <IconArrowNarrowUp className={classes.arrowButtonIcon} onClick={() => handleFilterClick("dir", "desc")} />}
                                                    {timeFilter && !showAscArrow && <IconArrowNarrowUp className={classes.arrowButtonIcon} style={{ display: 'none' }} />}
                                                    {timeFilter && !showAscArrow && <IconArrowNarrowDown className={classes.arrowButtonIcon} onClick={() => handleFilterClick("dir", "asc")} />}
                                                    {timeFilter && showAscArrow && <IconArrowNarrowDown className={classes.arrowButtonIcon} style={{ display: 'none' }} />}
                                                </div>
                                            </div>

                                        </div>
                                    </section>
                                </div>
                                <br/>
                                <Divider className={classes.divider} />
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
