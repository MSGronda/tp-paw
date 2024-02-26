import React, { useContext, useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import classes from "./Subject.module.css";
import AuthContext from "../../context/AuthContext";
import {
    Card,
    Tabs,
    Text,
    rem,
    Table,
    Badge,
    Button,
    Breadcrumbs,
    Anchor,
    Tooltip,
    Group,
    Combobox,
    useCombobox,
    Alert,
    Notification,
} from '@mantine/core';
import { IconArrowsSort, IconCheck, IconPhoto, IconX } from "@tabler/icons-react";
import { Subject } from "../../models/Subject.ts";
import { Navbar } from "../../components/navbar/navbar.tsx";
import { Link, useLocation, useNavigate, useParams } from "react-router-dom";
import ReviewCard from "../../components/review-card/review-card.tsx";
import { subjectService, reviewService, userService } from "../../services";
import { handleService } from "../../handlers/serviceHandler.tsx";
import { Review } from "../../models/Review.ts";
import PaginationComponent from "../../components/pagination/pagination.tsx";
import { User } from "../../models/User.ts";


export function SubjectPage() {
    const iconStyle = { width: rem(12), height: rem(12) };
    const iconSort = <IconArrowsSort size={14} />;

    const { t } = useTranslation();
    const location = useLocation();
    const subjectId = useParams();
    const navigate = useNavigate();
    const { userId } = useContext(AuthContext);

    const [subject, setSubject] = useState({} as Subject);
    const [loading, setLoading] = useState(true);
    const [reviews, setReviews] = useState([{} as Review]);
    const [didUserReview, setDidUserReview] = useState(true);
    const [users, setUsers] = useState([{} as User]);
    const [maxPage, setMaxPage] = useState(1);
    const [editShowAlert, setEditShowAlert] = useState(false);
    const [deleteShowAlert, setDeleteShowAlert] = useState(false);
    const [editReviewValue, setEditReviewValue] = useState<Boolean>();
    const [deletedReviewValue, setDeletedReviewValue] = useState<Boolean>();

    const orderParams = new URLSearchParams(location.search);
    const orderBy = orderParams.get('orderBy');
    const dir = orderParams.get('dir');
    const page: number = orderParams.get('page') === null ? 1 : parseInt(orderParams.get('page') as string, 10);

    const INITIAL_PAGE = 1;
    const INITIAL_ORDER: string = "difficulty";
    const INITAL_DIR: string = "asc";

    const {state} = location;


    const searchSubject = async (subjectId: string) => {
        const res = await subjectService.getSubjectById(subjectId);
        const data = handleService(res, navigate);
        if (res) {
            setSubject(data);
        }
    }

    const getReviewsFromSubject = async (subjectId: string, page: number, orderBy: string, dir: string) => {
        const res = await reviewService.getReviewsBySubject(subjectId, page, orderBy, dir);
        const data = handleService(res, navigate);
        if (res) {
            setReviews(data);
        }
        setLoading(false);
    }

    const getUsersFromReviews = async (subjectId: string, page: number) => {
        const res = await userService.getUsersThatReviewedSubject(subjectId, page);
        const data = handleService(res, navigate);
        if (res) {
            setUsers(data);
        }
    }

    const getReviewFromUser = async (subjectId: string, userId: number) => {
        const res = await reviewService.getReviewFromSubjectAndUser(subjectId, userId);
        const data = handleService(res, navigate);
        if (res) {
            data.length === 0 ? setDidUserReview(false) : setDidUserReview(true);
        }
    }

    const handlePageChange = (newPage: number) => {
        const queryParams = new URLSearchParams(window.location.search);
        queryParams.set('page', newPage.toString());
        window.location.search = queryParams.toString();
    }

    useEffect(() => {
        if (subjectId.id !== undefined) {
            searchSubject(subjectId.id);
        }
    }, []);

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
        if (state && state.reviewUpdated !== undefined ) {
            setEditShowAlert(true);
            setEditReviewValue(state.reviewUpdated);
        }
        if( localStorage.hasOwnProperty('reviewDeleted')){
            setDeleteShowAlert(true);
            setDeletedReviewValue(localStorage.getItem('reviewDeleted') === "true");
            localStorage.removeItem('reviewDeleted');
        }
    }, []);

    const findUserName = (userId: number) => {
        let userName = "";
        users.forEach((user) => {
            if (user.id === userId) {
                userName = user.username;
            }
        })
        return userName;
    }

    // Degree Lookup
    const degree: any = {
        id: 1,
        name: "Ingenieria Informatica",
    };
    const year: number = 1;
    const progress: string = "DONE";

    const combobox = useCombobox({
        onDropdownClose: () => combobox.resetSelectedOption(),
    });
    return (
        <>
            <Navbar />
            {loading ? <div /> :
                <div className={classes.container}>
                    <div className={classes.background}>
                        {editReviewValue ? (
                            editShowAlert && (
                                <Notification
                                    icon={<IconCheck />}
                                    color="teal"
                                    title={t("Review.editSuccess")}
                                    onClose={() => {setEditShowAlert(false); }}
                                />
                            )
                        ) : (
                            editShowAlert && (
                                <Notification
                                    icon={<IconX />}
                                    color="red"
                                    title={t("Review.editFailure")}
                                    onClose={() => {setEditShowAlert(false); state.reviewUpdated = undefined}}
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
                            {degree !== null ?
                                <Breadcrumbs separator="→">
                                    <Anchor href={"/degree/" + degree.id}>
                                        {degree.name}
                                    </Anchor>
                                    {year === 0 ?
                                        <Anchor href={"/degree/" + degree.id + "?tab=electives"}>{t("Subject.electives")}</Anchor> :
                                        <Anchor href={"/degree/" + degree.id + "?tab=" + year}>{t("Subject.year")} {year}</Anchor>
                                    }
                                </Breadcrumbs> :
                                <></>
                            }
                        </div>
                        <div className={classes.editDeleteButtons}>
                            <Text size="xl" fw={500}> {subject.name} - {subject?.id}</Text>
                            <></>
                        </div>
                        <Card className={classes.mainBody}>
                            <Tabs defaultValue="general">
                                <Tabs.List>
                                    <Tabs.Tab value="general" leftSection={<IconPhoto style={iconStyle} />}> {t("Subject.general")}  </Tabs.Tab>
                                    <Tabs.Tab value="times-panel" leftSection={<IconPhoto style={iconStyle} />}> {t("Subject.times")} </Tabs.Tab>
                                    <Tabs.Tab value="professors-panel" leftSection={<IconPhoto style={iconStyle} />}> {t("Subject.classProf")} </Tabs.Tab>
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
                                                    {subject.prerequisites && subject.prerequisites.length === 0 ? <>{t("Subject.emptyPrerequisites")}</> : <></>}
                                                    {getSubjectPrereqs(subject.prerequisites)}
                                                </Table.Td>
                                            </Table.Tr>
                                            <Table.Tr>
                                                <Table.Th>{t("Subject.professors")}</Table.Th>
                                                <Table.Td>
                                                    {subject.classes && subject.classes.length === 0 ? <>{t("Subject.emptyProfessors")}</> : <></>}
                                                    {getProfessors(subject)}
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
                                                    <Table.Tr>
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
                                                        <Table.Td>{classtime.classNumber}</Table.Td>
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
                                                <Table.Tr>
                                                    <Table.Td>{item.idClass}</Table.Td>
                                                    <Table.Td>
                                                        {item.professors.map((professor) => (
                                                            <Badge color="blue">{professor}</Badge>
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
                                            <Button variant="filled" size="lg" radius="xl">
                                                <Text c={"white"}>{t("Subject.progressDone")}</Text>
                                            </Button> :
                                            <Button variant="filled" size="lg" radius="xl" className={classes.pendingButton}>
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
                        reviews.length !== 0 &&
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
                            reviews.length === 0 &&
                            <Text fw={700} size={"xl"}>{t("Subject.noreviews")}</Text>
                        }
                    </div>
                    <div className={classes.reviewsColumn}>
                        {reviews &&
                            reviews.map((review) => (
                                <ReviewCard subjectId={subject?.id}
                                    subjectName={subject.name}
                                    difficulty={review.difficulty}
                                    timeDemand={review.timeDemand}
                                    text={review.text}
                                    key={review.id}
                                    UserId={review.userId}
                                    userName={findUserName(review.userId)}
                                    anonymous={review.anonymous}
                                    id={review.id}
                                    forSubject={true}
                                />
                            ))
                        }
                        {reviews &&
                            <PaginationComponent page={page} lastPage={maxPage} setPage={handlePageChange} />
                        }
                    </div>
                </div>
            }
        </>
    );
}

function getSubjectPrereqs(prereqs: string[]) {
    const prereqsComponents: JSX.Element[] = [];
    let i = 0;
    if (prereqs === null || prereqs === undefined) {
        return <></>;
    }
    prereqs.forEach((item) => {
        prereqsComponents.push(
            <a href={"/subject/" + item}>{item}</a>
        );
        if (i !== prereqs.length - 1) {
            prereqsComponents.push(
                <> , </>
            );
        }
        i++;
    }
    )
    return prereqsComponents;
}

function getProfessors(subject: Subject) {
    const professorsComponents: JSX.Element[] = [];
    if (subject.classes === null || subject.classes === undefined) {
        return <></>;
    }
    subject.classes.forEach((classItem) => {
        classItem.professors.forEach((professor: string) => {
            professorsComponents.push(
                <Badge color="blue">{professor}</Badge>
            );
            professorsComponents.push(<> </>);
        })
    })
    return professorsComponents;
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