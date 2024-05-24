import {useTranslation} from "react-i18next";
import {degreeService, reviewService, subjectService, userService} from "../../services";
import classes from './user.module.css';
import {useContext, useEffect, useState} from "react";
import {Navbar} from "../../components/navbar/navbar";
import {Link, useNavigate, useParams} from "react-router-dom";
import AuthContext from "../../context/AuthContext";
import {Review} from "../../models/Review";
import {Subject} from "../../models/Subject";
import {
  Avatar,
  Button,
  Card,
  Combobox,
  Divider,
  Group,
  Loader,
  Paper,
  Space,
  Table,
  Text,
  useCombobox
} from "@mantine/core";
import type {User} from "../../models/User";
import ReviewCard from "../../components/review-card/review-card";
import {IconArrowsSort, IconPencil} from "@tabler/icons-react";
import {Degree} from "../../models/Degree";
import PaginationComponent from "../../components/pagination/pagination";
import {isModerator} from "../../utils/userUtils.ts";


export default function User() {
  const iconSort = <IconArrowsSort size={14}/>;
  const {t} = useTranslation();
  const navigate = useNavigate();

  const [loadingUser, setLoadingUser] = useState(true);
  const [loadingReviews, setLoadingReviews] = useState(true);
  const [user, setUser] = useState({} as User);
  const [planSubjects, setPlanSubjects] = useState<Subject[]>([]);
  const [degree, setDegree] = useState({} as Degree);
  const [reviews, setReviews] = useState<Review[]>([]);
  const [subjects, setSubjects] = useState<Subject[]>([]);
  const [maxPage, setMaxPage] = useState(1);

  const params = useParams();
  const {userId} = useContext(AuthContext);
  const id = params.id || userId;
  const isProfile = !params.id;

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
      await getUserDegree(res.data.degreeId);
    }
    setLoadingUser(false);
  }

  const getUserPlan = async () => {
    const res = await subjectService.getUserPlanSubjects(Number(id));
    if (res?.data) {
      setPlanSubjects(res.data);
    }
  }

  const getUserDegree = async (degreeId: number | undefined) => {
    if (degreeId === undefined) return;
    const res = await degreeService.getDegreeById(degreeId);
    if (res?.data) {
      setDegree(res.data)
    }
  }

  const getReviewsFromUser = async (userId: number, page: number, orderBy: string, dir: string) => {
    const res = await reviewService.getReviewsFromUser(userId, page, orderBy, dir);
    if (res?.data) {
      setReviews(res.data);
      setMaxPage(res.maxPage || 1);
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
    if (!isProfile && id == userId) {
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
      <Navbar/>
      {
        !loadingUser && <>
              <div className={classes.container}>
                  <div className={classes.body}>
                    {isProfile ?
                      <ProfileSection user={user}/>
                      : <UserSection user={user} degree={degree} plan={planSubjects}/>
                    }
                  </div>
                  <br/>
                  <hr className={classes.hrSeparator}/>
                {
                  reviews.length !== 0 &&
                    <div className={classes.filter}>
                        <Text className={classes.userReviews}>{t("User.userReviews")}</Text>
                      {loadingReviews ? <></> :
                        <CurrentFilterComponent orderBy={orderBy ? orderBy : ""} dir={dir ? dir : ""}/>}
                        <Combobox
                            store={combobox}
                            width={200}
                            onOptionSubmit={(val) => {
                              setOrderParameters(val);
                              combobox.closeDropdown();
                            }}
                        >
                            <Combobox.Target>
                                <Button leftSection={iconSort} variant={"light"} className={classes.filterDropdown}
                                        size="md" onClick={() => combobox.toggleDropdown()}>{t("Subject.sort")}</Button>
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
                                        <div>
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
                        <Text fw={700} size={"xl"}>{t("User.noReviews")}</Text>
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
                    {reviews && maxPage > 1 &&
                        <PaginationComponent page={page} lastPage={maxPage} setPage={handlePageChange}/>
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

const CurrentFilterComponent: React.FC<CurrentFilterComponentProps> = ({orderBy, dir}) => {
  const {t} = useTranslation();

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


function UserSection({user, degree, plan}: { user?: User, degree?: Degree, plan?: Subject[] }) {
  const {t} = useTranslation();
  const {role} = useContext(AuthContext);

  if (!user || !degree || !plan) return <></>;

  return <>
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
            {isModerator(user) &&
                <h2 className={classes.editor_text}>
                  {t("User.moderator")}
                </h2>
            }
          </div>
        </div>
        {role === "EDITOR" && !isModerator(user) &&
            <div className={classes.moderator_tag}>
                <Button variant="outline"> {/* TODO Ver endpoint de make moderator y conectarlo*/}
                  {t("User.makeModerator")}
                </Button>
            </div>
        }
      </div>
      <br/>
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
            degree.name && plan.length > 0 && plan.map((subject, index) => (
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
          degree.name &&
            <Table.Tr>
                <Table.Td>{t("User.completedCredits")}</Table.Td>
              {/*TODO agregar total credits de degree*/}
                <Table.Td>{user.creditsDone} {t("User.outOf")} {degree.totalCredits}</Table.Td>
            </Table.Tr>
        }

        </Table.Tbody>
      </Table>
    </Card>
  </>;
}

function ProfileSection({user}: { user?: User }) {
  const {t} = useTranslation();

  if (!user) return <Loader/>;

  const EditButton = () => (
    <Button variant="outline" w="42px" h="32px" p="0">
      <IconPencil size="18px"/>
    </Button>
  );

  return <>
    <div className={classes.header}>
      <div className={classes.image_container}>
        <Avatar
          src={user.profileImage}
          size={120}
          radius={120}
          mx="auto"
        />
      </div>
      <div className={classes.title}>
        <div className={classes.moderator_tag}>
          <Text fz="32px" fw="bolder" mt="md">
            {t("Profile.loggeduser")}
          </Text>
        </div>
        {isModerator(user) && (
          <div>
            <Text fz="32px" fw="bolder" mt="md" c="red">
              {t("Profile.moderator")}
            </Text>
          </div>
        )}
        <Button variant="filled" radius="md" my="auto">
          {t("Profile.logout")}
        </Button>
      </div>
    </div>
    <div className={classes.spacing}> {/* Wrapper for spacing */}
      <Paper radius="md" mx="xl" my="md" px="2.5rem" py="lg" bg="var(--mantine-color-body)">
        <Table className={classes.dataTable}>
          <Table.Tbody>
            <Table.Tr>
              <Table.Td><Text fw="500">{t("Profile.username")}</Text> </Table.Td>
              <Table.Td><Text>{user.username}</Text></Table.Td>
              <Table.Td><EditButton/></Table.Td>
            </Table.Tr>
            <Table.Tr>
              <Table.Td><Text fw="500">{t("Profile.email")}</Text></Table.Td>
              <Table.Td><Text>{user.email}</Text></Table.Td>
              <Table.Td><EditButton/></Table.Td>
            </Table.Tr>
          </Table.Tbody>
        </Table>
        <Space h="md"/>
        <Group justify="center">
          <Button variant="outline">{t("Profile.change_password")}</Button>
          <Button variant="outline">{t("Profile.change_degree")}</Button>
        </Group>
      </Paper>
    </div>
  </>;
}
