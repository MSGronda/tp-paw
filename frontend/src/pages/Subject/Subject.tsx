import React, { useContext, useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import classes from "./Subject.module.css";
import AuthContext from "../../context/AuthContext";
import {
    Card,
    Tabs,
    Text,
    Table,
    Badge,
    Button,
    Breadcrumbs,
    Tooltip,
    Group,
    Combobox,
    useCombobox,
    Notification, Center, Loader,
} from '@mantine/core';
import {IconArrowsSort, IconCheck, IconPencil, IconX} from "@tabler/icons-react";
import {SimpleSubject, Subject} from "../../models/Subject.ts";
import { Navbar } from "../../components/navbar/navbar.tsx";
import { Link, useLocation, useNavigate, useParams } from "react-router-dom";
import ReviewCard from "../../components/review-card/review-card.tsx";
import {subjectService, reviewService, userService, degreeService, professorService} from "../../services";
import { handleService } from "../../handlers/serviceHandler.tsx";
import { Review } from "../../models/Review.ts";
import PaginationComponent from "../../components/pagination/pagination.tsx";
import { User } from "../../models/User.ts";
import { Degree } from "../../models/Degree.ts";
import {ReviewVote} from "../../models/ReviewVote.ts";
import {Professor} from "../../models/Professor.ts";
import {getSemesterYearFormat} from "../../utils/subjectUtils.ts";

interface Semester {
    semester?: number,
    subjects: string,
}


export function SubjectPage() {
    const iconSort = <IconArrowsSort size={14} />;

    const { t } = useTranslation();
    const location = useLocation();
    const subjectId = useParams();
    const navigate = useNavigate();
    const { userId } = useContext(AuthContext);
    const { role } = useContext(AuthContext);

    const [subject, setSubject] = useState({} as Subject);
    const [subjectSemester, setSubjectSemester] = useState(undefined as Semester|undefined);
    const [user, setUser] = useState<User>();
    const [degree, setDegree] = useState({} as Degree);
    const [prerequisites, setPrerequisites] = useState([{} as SimpleSubject]);
    const [loading, setLoading] = useState(true);
    const [reviews, setReviews] = useState<Review[]|undefined>(undefined);
    const [reviewVotes, setReviewVotes] = useState<Map<number, ReviewVote[]>>(new Map());
    const [didUserReview, setDidUserReview] = useState(true);
    const [users, setUsers] = useState<User[]>([]);
    const [maxPage, setMaxPage] = useState(1);
    const [editShowAlert, setEditShowAlert] = useState(false);
    const [deleteShowAlert, setDeleteShowAlert] = useState(false);
    const [editReviewValue, setEditReviewValue] = useState<boolean>();
    const [deletedReviewValue, setDeletedReviewValue] = useState<boolean>();
    const [progress, setProgress] = useState("PENDING");

    const [professors, setProfessors] = useState<Map<string, Professor[]>>(new Map());

    const INITIAL_PAGE = 1;
    const INITIAL_ORDER: string = "difficulty";
    const INITAL_DIR: string = "asc";

    const { state } = location;
    const orderParams = new URLSearchParams(location.search);
    const orderBy = orderParams.get('orderBy');
    const dir = orderParams.get('dir');
    const page: number = orderParams.get('page') === null ? INITIAL_PAGE : parseInt(orderParams.get('page') as string, 10);
    
    const semesterFormat = subjectSemester?.semester ? getSemesterYearFormat(subjectSemester.semester) : undefined;


    const searchSubject = async (subjectId: string) => {
        const res = await subjectService.getSubjectById(subjectId);
        const data: Subject = handleService(res, navigate);
        if (res) {
            setSubject(data);

            await getProfessors(data);
        }
    }
    const getProfessors = async (subject: Subject) => {
        setProfessors(await professorService.getProfessorsForSubject(subject));
    }

    const getReviewsFromSubject = async (subjectId: string, page: number, orderBy: string, dir: string) => {
        const res = await reviewService.getReviewsBySubject(subjectId, page, orderBy, dir);
        const data = handleService(res, navigate);

        setReviews(data);
        
        if( data && data != "" && userId){

            const votes = await reviewService.getAllVotes(res.data, userId);
            setReviewVotes(votes);

        }
        setLoading(false);
    }


    const getUsersFromReviews = async (subjectId: string, page: number) => {
        const res = await userService.getUsersThatReviewedSubject(subjectId, page);
        const data = handleService(res, navigate);
        if (res && res.data != "") {
            setUsers(data);
        }
    }

    const getReviewFromUser = async (subjectId: string, userId: number) => {
        const res = await reviewService.getReviewFromSubjectAndUser(subjectId, userId);
        if (res) {
            res.status === 204 ? setDidUserReview(false) : setDidUserReview(true);
        }
    }

    const handlePageChange = (newPage: number) => {
        const queryParams = new URLSearchParams(window.location.search);
        queryParams.set('page', newPage.toString());
        window.location.search = queryParams.toString();
    }

    const getUserProgress = async (userId: number) => {
        const res = await userService.getUserProgress(userId);
        const data = handleService(res, navigate);
        if (res && subjectId.id !== undefined && res.data) {
            for (let i = 0; i < data.subjectProgress.entry.length; i += 1) {
                if (data.subjectProgress.entry[i].key === subjectId.id) {
                    setProgress(data.subjectProgress.entry[i].value);
                }
            }
        }
    }

    const getUser = async () => {
        if(userId) {
            const res = await userService.getUserById(userId);
            const data = handleService(res, navigate);
            if(res){
                setUser(data);
            }
        }
    }

    const getDegree = async (degreeId: number)=> {
        const res = await degreeService.getDegreeById(degreeId);
        const data = handleService(res, navigate);
        if(res){
            setDegree(data);
        }
    }

    const getSubjectSemester = async (degreeId: number, subjectId: string) => {
        const res = await degreeService.getSemesters(degreeId, subjectId);
        const data = handleService(res, navigate);
        if(res && data && data.length > 0){
            setSubjectSemester(data[0]);
        }
    }

    const setSubjectProgress = (userId: number, subjectId: string, newProgressState: string) => {
        newProgressState === "DONE" ? userService.setFinishedSubjects(userId, new Array(subjectId), []) : userService.setFinishedSubjects(userId, [], new Array(subjectId));
        setProgress(newProgressState);
    }

    const getPrerequisites = async (subjectIds: string[]) => {
        const subjects = [];
        for (const subjectId of subjectIds) {
            const res = await subjectService.getSubjectById(subjectId.toString());
            const data = handleService(res, navigate);
            if (res) {
                subjects.push({id: data.id, name: data.name});
            }
        }
        setPrerequisites(subjects);
    }

    useEffect(() => {
        if (subjectId.id !== undefined) {
            searchSubject(subjectId.id);
        }
    }, [subjectId.id]);

    useEffect(() => {
        if (subject.name !== undefined) {
            document.title = subject.name;
        }
    }, [subject]);

    useEffect(() => {
        if (subjectId.id !== undefined) {
            if (userId !== undefined) {
                getReviewFromUser(subjectId.id, userId);
                getUsersFromReviews(subjectId.id, page);
            }

            if (orderBy === null && dir === null && page === null) {
                getReviewsFromSubject(subjectId.id, INITIAL_PAGE, INITIAL_ORDER, INITAL_DIR);
            } else {
                getReviewsFromSubject(subjectId.id, page, orderBy ? orderBy : "", dir ? dir : "");
            }
        }
        setMaxPage(1 + subject.reviewCount / 10);
        if (state && state.reviewUpdated !== undefined) {
            setEditShowAlert(true);
            setEditReviewValue(state.reviewUpdated);
        }
        if (localStorage.hasOwnProperty('reviewDeleted')) {
            setDeleteShowAlert(true);
            setDeletedReviewValue(localStorage.getItem('reviewDeleted') === "true");
            localStorage.removeItem('reviewDeleted');
        }
    }, [subjectId.id]);

    // UserProgress Lookup
    useEffect(() => {
        if (userId !== undefined) {
            getUserProgress(userId);
        }
    }, [userId]);

    // User Lookup
    useEffect(() => {
        if(subjectId.id !== undefined) {
            getUser();
        }
    }, [subjectId.id]);

    // Degree Lookup
    useEffect(() => {
        if(user && user.degreeId){
            getDegree(user.degreeId);
        }
    }, [user]);

    // Semester Lookup
    useEffect(() => {
        if(subjectId.id !== undefined && degree && degree.id){
            getSubjectSemester(degree.id, subjectId.id);
        }
    }, [subjectId.id, degree]);

    // Prerequisites Names Lookup
    useEffect(() => {
        if(subject.prerequisites !== undefined) {
            getPrerequisites(subject.prerequisites);
        }
    }, [subject.prerequisites]);

    const findUserName = (userId: number) => {
        let userName = "";
        users.forEach((user) => {
            if (user.id === userId) {
                userName = user.username;
            }
        })
        return userName;
    }

    const combobox = useCombobox({
        onDropdownClose: () => combobox.resetSelectedOption(),
    });
    return (
        <>
            <Navbar />
            {loading ? <Center mt={120}><Loader size="xl"/></Center> :
                <div className={classes.container}>
                    <div className={classes.background}>
                        {editReviewValue ? (
                            editShowAlert && (
                                <Notification
                                    icon={<IconCheck />}
                                    color="teal"
                                    title={t("Review.editSuccess")}
                                    onClose={() => { setEditShowAlert(false); }}
                                />
                            )
                        ) : (
                            editShowAlert && (
                                <Notification
                                    icon={<IconX />}
                                    color="red"
                                    title={t("Review.editFailure")}
                                    onClose={() => { setEditShowAlert(false); state.reviewUpdated = undefined }}
                                />
                            )
                        )}
                        {
                            deletedReviewValue ? (
                                deleteShowAlert && (
                                    <Notification
                                        icon={<IconCheck />}
                                        color="teal"
                                        title={t("Review.deleteSuccess")}
                                        onClose={() => setDeleteShowAlert(false)}
                                    />
                                )
                            ) : (
                                deleteShowAlert && (
                                    <Notification
                                        icon={<IconX />}
                                        color="red"
                                        title={t("Review.deleteFailure")}
                                        onClose={() => setDeleteShowAlert(false)}
                                    />
                                )
                            )
                        }

                        <div className={classes.breadcrumbArea}>
                            {degree !== null && subjectSemester != undefined ?
                                <Breadcrumbs separator="→">
                                    <Link to={"/degree/" + degree.id}>
                                        {degree.name}
                                    </Link>
                                    {subjectSemester.semester && (subjectSemester.semester === -1 ?
                                        <Link to={"/degree/" + degree.id + "?semester=electives"}>{t("Subject.electives")}</Link> :
                                        <Link to={"/degree/" + degree.id + "?semester=" + subjectSemester.semester}>{t("Curriculum.semester", semesterFormat)}</Link>
                                    )}
                                </Breadcrumbs> :
                                <></>
                            }
                        </div>
                        <div className={classes.editDeleteButtons}>
                            <Text size="xl" fw={500}> {subject.name} - {subject?.id}</Text>
                            { role === 'EDITOR' && <Button radius="lg" rightSection={<IconPencil size={14}/>} onClick={() => {
                                navigate("/edit-subject/" + subject.id);
                            }}>
                                {t("Subject.editSubject")}
                            </Button>}
                        </div>
                        <Card className={classes.mainBody}>
                            <Tabs defaultValue="general">
                                <Tabs.List>
                                    <Tabs.Tab key={"general"} value="general"> {t("Subject.general")}  </Tabs.Tab>
                                    <Tabs.Tab key={"times-panel"} value="times-panel"> {t("Subject.times")} </Tabs.Tab>
                                    <Tabs.Tab key={"professors-panel"} value="professors-panel"> {t("Subject.classProf")} </Tabs.Tab>
                                </Tabs.List>

                                <Tabs.Panel value="general">
                                    <Table>
                                        <Table.Tbody>
                                            <Table.Tr>
                                                <Table.Th>{t("Subject.department")}</Table.Th>
                                                <Table.Td>{subject.department}</Table.Td>
                                            </Table.Tr>
                                            <Table.Tr>
                                                <Table.Th>{t("Subject.credits")} </Table.Th>
                                                <Table.Td>{subject.credits}</Table.Td>
                                            </Table.Tr>
                                            <Table.Tr>
                                                <Table.Th>{t("Subject.prerequisites")}</Table.Th>
                                                <Table.Td>
                                                    {prerequisites && prerequisites.length === 0 ? <>{t("Subject.emptyPrerequisites")}</> : <></>}
                                                    {   prerequisites && subject.prerequisites && prerequisites.length === subject.prerequisites.length ?
                                                        prerequisites.map((simpleSubject, index) => (
                                                            <Link key={index} to={{pathname:`/subject/${simpleSubject.id}`}}>
                                                                {simpleSubject.name}{ index < prerequisites.length - 1 ? ', ' : null}
                                                            </Link>
                                                        )) : <></>
                                                    }
                                                </Table.Td>
                                            </Table.Tr>
                                            <Table.Tr>
                                                <Table.Th>{t("Subject.professors")}</Table.Th>
                                                <Table.Td>
                                                    {subject.classes && subject.classes.length === 0 ? <>{t("Subject.emptyProfessors")}</> : <></>}
                                                    {
                                                        getAllProfessors(professors).map((professor) => <Badge color="blue" key={professor}>{professor}</Badge>)
                                                    }
                                                </Table.Td>
                                            </Table.Tr>
                                            <Table.Tr>
                                                <Table.Th>{t("Subject.difficulty")}</Table.Th>
                                                <Table.Td>
                                                    {(() => {
                                                        {
                                                            switch (subject.difficulty) {
                                                                case "EASY":
                                                                    return <Badge color="green"> {t("SubjectCard.easy")} </Badge>;
                                                                case "MEDIUM":
                                                                    return <Badge color="blue"> {t("SubjectCard.normal")} </Badge>;
                                                                case "HARD":
                                                                    return <Badge color="red"> {t("SubjectCard.hard")} </Badge>;
                                                                default:
                                                                    return <Badge color="gray"> {t("SubjectCard.no_reviews")} </Badge>;
                                                            }
                                                        }
                                                    })()}
                                                </Table.Td>
                                            </Table.Tr>
                                            <Table.Tr>
                                                <Table.Th>{t("Subject.time")}</Table.Th>
                                                <Table.Td>
                                                    {(() => {
                                                        {
                                                            switch (subject.timeDemand) {
                                                                case "LOW":
                                                                    return <Badge color="green"> {t("SubjectCard.low")} </Badge>;
                                                                case "MEDIUM":
                                                                    return <Badge color="blue"> {t("SubjectCard.medium")} </Badge>;
                                                                case "HIGH":
                                                                    return <Badge color="red"> {t("SubjectCard.high")} </Badge>;
                                                                default:
                                                                    return <Badge color="gray"> {t("SubjectCard.no_info")} </Badge>;
                                                            }
                                                        }
                                                    })()}
                                                </Table.Td>
                                            </Table.Tr>
                                        </Table.Tbody>
                                    </Table>
                                </Tabs.Panel>

                                <Tabs.Panel value="times-panel">
                                    <Table>
                                        <Table.Thead>
                                            <Table.Tr>
                                                <Table.Th><Text fw={600}>{t("Subject.classCode")}</Text></Table.Th>
                                                <Table.Th><Text fw={600}>{t("Subject.classDay")}</Text></Table.Th>
                                                <Table.Th><Text fw={600}>{t("Subject.classTime")}</Text></Table.Th>
                                                <Table.Th><Text fw={600}>{t("Subject.classMode")}</Text></Table.Th>
                                                <Table.Th><Text fw={600}>{t("Subject.classNumber")}</Text></Table.Th>
                                            </Table.Tr>
                                        </Table.Thead>
                                        <Table.Thead>
                                            {subject.classes?.map((item) => (
                                                item.locations.map((classtime, index) => (
                                                    <Table.Tr key={index}>
                                                        <Table.Td>
                                                            {index === 0 ? item.idClass : ""}
                                                        </Table.Td>
                                                        {classtime.day >= 1 && classtime.day <= 7 ?
                                                            <Table.Td>{t(getDayOfTheWeek(classtime.day))}</Table.Td> :
                                                            <Table.Td> - </Table.Td>
                                                        }
                                                        <Table.Td>{classtime.startTime} - {classtime.endTime}</Table.Td>
                                                        <Table.Td>{classtime.mode}</Table.Td>
                                                        <Table.Td>{classtime.building}</Table.Td>
                                                        <Table.Td>{classtime.location}</Table.Td>
                                                    </Table.Tr>
                                                ))
                                            ))}
                                        </Table.Thead>
                                    </Table>
                                </Tabs.Panel>

                                <Tabs.Panel value="professors-panel">
                                    <Table>
                                        <Table.Thead>
                                            <Table.Tr>
                                                <Table.Th>{t("Subject.classCode")}</Table.Th>
                                                <Table.Th>{t("Subject.classProf")}</Table.Th>
                                            </Table.Tr>
                                        </Table.Thead>
                                        <Table.Tbody>
                                            {subject.classes?.map((item) => (
                                                <Table.Tr key={item.idClass}>
                                                    <Table.Td>{item.idClass}</Table.Td>
                                                    <Table.Td>
                                                        {professors.get(item.idClass)?.map((p) => (
                                                            <Badge color="blue" key={p.name}>{p.name}</Badge>
                                                        ))}
                                                    </Table.Td>
                                                </Table.Tr>
                                            ))}
                                        </Table.Tbody>
                                    </Table>
                                </Tabs.Panel>
                            </Tabs>
                        </Card>
                    </div>
                    <br />
                    <div className={classes.reviewProgessArea}>
                        <div className={classes.reviewAndProgress}>
                            <Group>
                                {
                                    didUserReview ? <Button className={classes.button} variant="filled" size="lg" radius="xl" disabled>{t("Subject.review")}</Button> :
                                        <Button variant="filled" size="lg" radius="xl">
                                            <Link to={{ pathname: `/review/` + subject?.id }} state={{ name: subject.name }}><Text c="white">{t("Subject.review")}</Text></Link>
                                        </Button>
                                }
                                <form>
                                    <input type="hidden" name="idSub" id="idSub" value="{subject?.id}" />
                                    <input type="hidden" name="progress" id="progress" value="{progress.value}" />
                                    <Tooltip label={t("Subject.progressTooltip")}>
                                        {progress === "DONE" ?
                                            <Button variant="filled" size="lg" radius="xl" onClick={() => {
                                                if (userId !== undefined && subjectId.id !== undefined) {
                                                    setSubjectProgress(userId, subjectId.id, "PENDING")
                                                }
                                            }}>
                                                <Text c={"white"}>{t("Subject.progressDone")}</Text>
                                            </Button> :
                                            <Button variant="filled" size="lg" radius="xl" className={classes.pendingButton} onClick={() => {
                                                if (userId !== undefined && subjectId.id !== undefined) {
                                                    setSubjectProgress(userId, subjectId.id, "DONE")
                                                }
                                            }}>
                                                <Text c={"white"}>{t("Subject.progressPending")}</Text>
                                            </Button>
                                        }
                                    </Tooltip>
                                </form>
                            </Group>
                        </div>
                    </div>
                    {didUserReview &&
                        <div className={classes.textCenter}>
                            <Text>{t("Subject.alreadyReviewed")}</Text>
                        </div>
                    }
                    <br />
                    <hr className={classes.hrSeparator} />
                    {
                        reviews && reviews.length !== 0 &&
                        <div className={classes.filter}>
                            {loading ? <></> : <CurrentFilterComponent orderBy={orderBy ? orderBy : ""} dir={dir ? dir : ""} />}
                            <Combobox
                                store={combobox}
                                width={200}
                                onOptionSubmit={(val) => {
                                    setOrderParameters(val);
                                    combobox.closeDropdown();
                                }}
                            >
                                <Combobox.Target>
                                    <Button leftSection={iconSort} variant={"light"} className={classes.filterDropdown} size="md" onClick={() => combobox.toggleDropdown()}>{t("Subject.sort")}</Button>
                                </Combobox.Target>

                                <Combobox.Dropdown>
                                    <Combobox.Options>
                                        {
                                            <>
                                                <Combobox.Option value={"ascending-difficulty"} key={"ascending-difficulty"}>
                                                    <div>
                                                        {t("Subject.ascDiff")}
                                                    </div>
                                                </Combobox.Option>
                                                <Combobox.Option value={"descending-difficulty"} key={"descending-difficulty"}>
                                                    <div >
                                                        {t("Subject.descDiff")}
                                                    </div>
                                                </Combobox.Option>
                                                <Combobox.Option value={"ascending-time"} key={"ascending-time"}>
                                                    <div>
                                                        {t("Subject.ascTime")}
                                                    </div>
                                                </Combobox.Option>
                                                <Combobox.Option value={"descending-time"} key={"descending-time"}>
                                                    <div>
                                                        {t("Subject.descTime")}
                                                    </div>
                                                </Combobox.Option>
                                            </>
                                        }
                                    </Combobox.Options>
                                </Combobox.Dropdown>
                            </Combobox>
                        </div>
                    }
                    <div className={classes.noReviewsTitle}>
                        {
                            reviews && reviews.length === 0 &&
                            <Text fw={700} size={"xl"}>{t("Subject.noreviews")}</Text>
                        }
                    </div>
                    <div className={classes.reviewsColumn}>
                        {reviews &&
                            reviews.map((review) => (
                                <ReviewCard
                                    key={review.id}

                                    subjectId={subject?.id}
                                    subjectName={subject.name}
                                    difficulty={review.difficulty}
                                    timeDemand={review.timeDemand}
                                    text={review.text}
                                    UserId={review.userId}
                                    userName={findUserName(review.userId)}
                                    anonymous={review.anonymous}
                                    id={review.id}
                                    forSubject={true}
                                    upvotes={review.upVotes}
                                    downvotes={review.downVotes}
                                    votes={reviewVotes.get(review.id) ?? []}
                                />
                            ))
                        }
                        {reviews && maxPage > 1 &&
                            <PaginationComponent page={page} lastPage={maxPage} setPage={handlePageChange} />
                        }
                    </div>
                </div>
            }
        </>
    );
}

function getAllProfessors(professors: Map<string, Professor[]>): string[] {
    const allProfs: Set<string> = new Set();
    const resp: string[] = []

    professors.forEach((v) => v.forEach((p) => allProfs.add(p.name)));
    allProfs.forEach((p) => resp.push(p));

    return resp;
}


function getDayOfTheWeek(day: number) {
    return "TimeTable.day" + day;
}

interface CurrentFilterComponentProps {
    orderBy: string;
    dir: string;
}
const CurrentFilterComponent: React.FC<CurrentFilterComponentProps> = ({ orderBy, dir }) => {
    const { t } = useTranslation();

    if (orderBy === "" && dir === "") {
        return <Text>{t("Subject.currentFilter") + ": " + t("Subject.orderDifficulty") + " " + t("Subject.directionAsc")}</Text>;
    }
    if (orderBy === "difficulty" && dir === "desc") {
        return <Text>{t("Subject.currentFilter") + ": " + t("Subject.orderDifficulty") + " " + t("Subject.directionDesc")}</Text>;
    } else if (orderBy === "timeDemand" && dir === "asc") {
        return <Text>{t("Subject.currentFilter") + ": " + t("Subject.orderTimeDemand") + " " + t("Subject.directionAsc")}</Text>;
    } else if (orderBy === "timeDemand" && dir === "desc") {
        return <Text>{t("Subject.currentFilter") + ": " + t("Subject.orderTimeDemand") + " " + t("Subject.directionDesc")}</Text>;
    } else {
        return <Text>{t("Subject.currentFilter") + ": " + t("Subject.orderDifficulty") + " " + t("Subject.directionAsc")}</Text>;
    }
};

const setOrderParameters = (value: string) => {
    const orderParams = new URLSearchParams();
    if (value === "ascending-difficulty") {
        orderParams.set('orderBy', 'difficulty');
        orderParams.set('dir', 'asc');
    } else if (value === "descending-difficulty") {
        orderParams.set('orderBy', 'difficulty');
        orderParams.set('dir', 'desc');
    } else if (value === "ascending-time") {
        orderParams.set('orderBy', 'timedemanding');
        orderParams.set('dir', 'asc');
    } else if (value === "descending-time") {
        orderParams.set('orderBy', 'timedemanding');
        orderParams.set('dir', 'desc');
    }
    window.location.search = orderParams.toString();
}
