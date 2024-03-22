import { useTranslation } from "react-i18next";
import { degreeService, reviewService, subjectService, userService } from "../../services";
import classes from './user.module.css';
import { useContext, useEffect, useState } from "react";
import { Navbar } from "../../components/navbar/navbar";
import { Link, useNavigate, useParams } from "react-router-dom";
import AuthContext from "../../context/AuthContext";
import { Review } from "../../models/Review";
import { Subject } from "../../models/Subject";
import { Avatar, Button, Card, Combobox, Table, Text, useCombobox } from "@mantine/core";
import type { User } from "../../models/User";
import ReviewCard from "../../components/review-card/review-card";
import { IconArrowsSort } from "@tabler/icons-react";
import { Degree } from "../../models/Degree";


export default function User() {
    const iconSort = <IconArrowsSort size={14} />;
    const { t } = useTranslation();
    const navigate = useNavigate();

    const [loadingUser, setLoadingUser] = useState(true);
    const [loadingReviews, setLoadingReviews] = useState(true);
    const [user, setUser] = useState({} as User);
    const [planSubjects, setPlanSubjects] = useState([{} as Subject]);
    const [degree, setDegree] = useState({} as Degree);
    const [reviews, setReviews] = useState([{} as Review]);
    const [subjects, setSubjects] = useState([{} as Subject]);

    const { id } = useParams();
    const { userId, role } = useContext(AuthContext);

    const orderParams = new URLSearchParams(location.search);
    const orderBy = orderParams.get('orderBy');
    const dir = orderParams.get('dir');
    const page: number = orderParams.get('page') === null ? 1 : parseInt(orderParams.get('page') as string, 10);

    const INITIAL_PAGE = 1;
    const INITIAL_ORDER: string = "difficulty";
    const INITAL_DIR: string = "asc";

    const getUser = async () => {
        const res = await userService.getUserById(Number(id));
        if (res?.data) {
            setUser(res.data);
            getUserDegree(res.data.degreeId);
        }
        setLoadingUser(false);
    }

    const getUserPlan = async () => {
        const res = await subjectService.getUserPlanSubjects(Number(id));
        if (res?.data) {
            setPlanSubjects(res.data);
        }
    }

    const getUserDegree = async(degreeId: number | undefined) => {
        if( degreeId === undefined) return;
        const res = await degreeService.getDegreeById(degreeId);
        if(res?.data){
            setDegree(res.data)
        }
    }

    const getReviewsFromUser = async (userId: number, page: number, orderBy: string, dir: string) => {
        const res = await reviewService.getReviewsFromUser(userId, page, orderBy, dir);
        if (res?.data) {
            setReviews(res.data);
        }
        setLoadingReviews(false);
    }

    const getSubjectsFromReviews = async (userId: number, page: number) => {
        const res = await subjectService.getSubjectsFromReviews(userId, page);
        if (res?.data) {
            setSubjects(res.data);
        }
    }

    const handlePageChange = (newPage: number) => {
        const queryParams = new URLSearchParams(window.location.search);
        queryParams.set('page', newPage.toString());
        window.location.search = queryParams.toString();
    }

    useEffect(() => {
        if (id == userId) {
            navigate('/profile');
        } else {
            getUser();
            getUserPlan();
        }
    }, [id, userId]);

    useEffect(() => {
        if (id != null) {
            if (page == null || orderBy == null || dir == null) {
                getReviewsFromUser(Number(id), INITIAL_PAGE, INITIAL_ORDER, INITAL_DIR);
            } else {
                getReviewsFromUser(Number(id), page, orderBy, dir);
            }
            getSubjectsFromReviews(Number(id), page);
        }

    }, []);

    const isModerator = () => {
        user.roles.forEach((role) => {
            if (role === "EDITOR") {
                return true;
            }
        });
        return false;
    }

    const findSubjectName = (subjectId: string) => {
        let subjectName = "";
        subjects.forEach((subject) => {
            if (subject.id === subjectId) {
                subjectName = subject.name;
            }
        })
        return subjectName;
    }

    const combobox = useCombobox({
        onDropdownClose: () => combobox.resetSelectedOption(),
    });

    return (
        <div className={classes.fullsize}>
            <Navbar />
            {
                !loadingUser && <>
                    <div className={classes.container}>
                        <div className={classes.body}>
                            <Card>
                                <div className={classes.header}>
                                    <div className={classes.image_container}>
                                        <Avatar
                                            src={user.profileImage}
                                            size={100}
                                            radius={100}
                                            mx="auto"
                                        />
                                    </div>
                                    <div className={classes.title}>
                                        <div className={classes.moderator_tag}>
                                            <h1 className={classes.userName}>
                                                {user.username}
                                            </h1>
                                        </div>
                                        {isModerator() &&
                                            <div>
                                                <Text className={classes.editor_text} fz="lg" fw={500} mt="md">
                                                    {t("Profile.moderator")}
                                                </Text>
                                            </div>
                                        }
                                    </div>
                                    {role === "EDITOR" && !isModerator() &&
                                        <div className={classes.moderator_tag} >
                                            <Button variant="outline">
                                                {t("User.makeModerator")}
                                            </Button>
                                        </div>
                                    }
                                </div>
                                <br />
                                <Table className={classes.planTable}>
                                    <Table.Tbody>
                                        <Table.Tr>
                                            <Table.Td>{t("User.degree")}</Table.Td>
                                            <Table.Td>
                                                {degree.name ?
                                                    degree.name
                                                    :
                                                    t("User.noDegree")
                                                }
                                            </Table.Td>
                                        </Table.Tr>
                                        {
                                            planSubjects.length > 0 && planSubjects.map((subject, index) => (
                                                <Table.Tr key={subject.id + index}>
                                                    <Table.Td>
                                                        {index == 0 && t("User.currentSemester")}
                                                    </Table.Td>
                                                    <Table.Td>
                                                        <Link to={"/subjects/" + subject.id}>{subject.name}</Link>
                                                    </Table.Td>
                                                </Table.Tr>
                                            ))
                                        }{
                                            true &&
                                            <Table.Tr>
                                                <Table.Td>{t("User.completedCredits")}</Table.Td>
                                                {/*TODO agregar total credits de degree*/}
                                                <Table.Td>{user.creditsDone} {t("User.outOf")} {degree.totalCredits}</Table.Td>
                                            </Table.Tr>
                                        }

                                    </Table.Tbody>
                                </Table>
                            </Card>
                        </div>
                        <br />
                        <hr className={classes.hrSeparator} />
                        {
                            reviews.length !== 0 &&
                            <div className={classes.filter}>
                                <Text className={classes.userReviews}>{t("User.userReviews")}</Text>
                                {loadingReviews ? <></> : <CurrentFilterComponent orderBy={orderBy ? orderBy : ""} dir={dir ? dir : ""} />}
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
                                reviews.length === 0 ?
                                    <Text fw={700} size={"xl"}>{t("User.noreviews")}</Text>
                                    : <></>
                            }
                        </div>
                        <div className={classes.reviewsColumn}>
                            {
                                !loadingReviews && reviews.length > 0 && reviews.map((review) => (
                                    !review.anonymous &&
                                    <ReviewCard
                                        key={review.id}
                                        subjectId={review.subjectId}
                                        subjectName={findSubjectName(review.subjectId)}
                                        text={review.text}
                                        timeDemand={review.timeDemand}
                                        difficulty={review.difficulty}
                                        UserId={review.userId}
                                        userName={user.username}
                                        anonymous={review.anonymous}
                                        id={review.id}
                                        forSubject={false}
                                        upvotes={review.upVotes}
                                        downvotes={review.downVotes}
                                    />
                                ))
                            }

                        </div>
                    </div>


                </>
            }
        </div>
    )

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