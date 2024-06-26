import { useTranslation } from "react-i18next";
import { degreeService, reviewService, subjectService, userService } from "../../services";
import classes from './user.module.css';
import { useContext, useEffect, useRef, useState } from "react";
import { Navbar } from "../../components/navbar/navbar";
import { Link, useNavigate, useParams } from "react-router-dom";
import AuthContext from "../../context/AuthContext";
import { Review } from "../../models/Review";
import { Subject } from "../../models/Subject";
import {
  Alert,
  Avatar,
  Button,
  Card, Center,
  Combobox, Group,
  Input, InputBase,
  Loader, Modal, Paper, PasswordInput, rem,
  Table,
  Text, TextInput, useCombobox
} from "@mantine/core";
import type { User } from "../../models/User";
import ReviewCard from "../../components/review-card/review-card";
import {
  IconArrowsSort,
  IconCloudUpload,
  IconDownload,
  IconExclamationCircle,
  IconLock,
  IconPencil,
  IconX
} from "@tabler/icons-react";
import { Degree } from "../../models/Degree";
import PaginationComponent from "../../components/pagination/pagination";
import { isModerator } from "../../utils/userUtils.ts";
import { useDisclosure } from "@mantine/hooks";
import { useForm } from "@mantine/form";
import { validateConfirmPassword, validatePassword, validateUsername } from "../../utils/register_utils.ts";
import authService from "../../services/AuthService.ts";
import { Dropzone, MIME_TYPES } from "@mantine/dropzone";
import Title from "../../components/title/title.tsx";


export default function User() {
  const iconSort = <IconArrowsSort size={14} />;
  const { t } = useTranslation();
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
  const { userId } = useContext(AuthContext);
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
      setPlanSubjects(res.data.subjects);
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
      setSubjects(res.data.subjects);
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
      getUser().catch((e) => console.error(e));
      getUserPlan().catch((e) => console.error(e));
    }
  }, [id, userId]);

  useEffect(() => {
    if (id != null) {
      if (page == null || orderBy == null || dir == null) {
        getReviewsFromUser(Number(id), INITIAL_PAGE, INITIAL_ORDER, INITAL_DIR).catch((e) => console.error(e));
      } else {
        getReviewsFromUser(Number(id), page, orderBy, dir).catch((e) => console.error(e));
      }
      getSubjectsFromReviews(Number(id), page).catch((e) => console.error(e));
    }

  }, [dir, id, orderBy, page]);

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

  useEffect(() => {
    if (!isProfile) {
      if (user.username !== undefined) {
        document.title = user.username
      }
    } else {
      document.title = t("Profile.loggeduser")
    }
  }, [user.username])

  return (
    <div className={classes.fullsize}>
      <Navbar />
      {
        !loadingUser && <>
          <div className={classes.container}>
            <div className={classes.body}>
              {isProfile ?
                <ProfileSection user={user} />
                : <UserSection user={user} degree={degree} plan={planSubjects} />
              }
            </div>
            <br />
            <hr className={classes.hrSeparator} />
            {
              reviews.length !== 0 &&
              <div className={classes.filter}>
                <Text className={classes.userReviews}>{t("User.userReviews")}</Text>
                {loadingReviews ? <></> :
                  <CurrentFilterComponent orderBy={orderBy ? orderBy : ""} dir={dir ? dir : ""} />}
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
                <PaginationComponent page={page} lastPage={maxPage} setPage={handlePageChange} />
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


function UserSection({ user, degree, plan }: { user?: User, degree?: Degree, plan?: Subject[] }) {
  const { t } = useTranslation();
  const { role } = useContext(AuthContext);

  if (!user || !degree || !plan) return <></>;

  return <>
    <div className={classes.header}>
      <div className={classes.image_container}>
        <Avatar
          src={user.image}
          size={100}
          radius="100%"

          // eslint-disable-next-line @typescript-eslint/ban-ts-comment
          // @ts-ignore
          imageProps={{ "object-fit": "cover" }}

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
    <br />
    <Card py="md" px="lg" w="30rem">
      <Table className={classes.dataTable}>
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

function ProfileSection({ user }: { user?: User }) {
  const { t } = useTranslation();

  const [openedPassModal, { open: openPassModal, close: closePassModal }] = useDisclosure(false);
  const [openedDegreeModal, { open: openDegreeModal, close: closeDegreeModal }] = useDisclosure(false);
  const [openedUsernameModal, { open: openUsernameModal, close: closeUsernameModal }] = useDisclosure(false);
  const [openedPictureModal, { open: openPictureModal, close: closePictureModal }] = useDisclosure(false);

  const [degree, setDegree] = useState<Degree | null>(null);

  useEffect(() => {
    if (!user || !user.degreeId) return;

    degreeService.getDegreeById(user.degreeId).then(res => {
      if (res.failure) {
        console.error("Unable to get degree: ", res.status);
        return;
      }
      setDegree(res.data as Degree);
    });
  }, [user]);

  if (!user) return <Loader />;

  const EditButton = (props: { onClick?: () => void, className?: string, filled?: boolean }) => (
    <Button variant={props.filled ? "filled" : "outline"} w="42px" h="32px" p="0" {...props}>
      <IconPencil size="18px" />
    </Button>
  );

  return <>
    <div className={classes.header}>
      <div className={classes.image_container}>
        <Avatar
          src={user.image}
          radius="100%"

          // eslint-disable-next-line @typescript-eslint/ban-ts-comment
          // @ts-ignore
          imageProps={{ "object-fit": "cover" }}

          size={120}
          mx="auto"
        />
        <EditButton className={classes.imageEditButton} filled onClick={openPictureModal} />
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
        <Button variant="filled" radius="md" my="auto" onClick={() => { authService.logout(); window.location.reload(); }}>
          {t("Profile.logout")}
        </Button>
      </div>
    </div>
    <div className={classes.spacing}> {/* Wrapper for spacing */}
      <Paper radius="md" mx="xl" my="md" px="2.5rem" py="lg" bg="var(--mantine-color-body)">
        <Table className={classes.dataTable}>
          <Table.Tbody>
            <Table.Tr>
              <Table.Td><Text fw="500">{t("Profile.email")}</Text></Table.Td>
              <Table.Td><Text>{user.email}</Text></Table.Td>
              <Table.Td></Table.Td>
            </Table.Tr>
            <Table.Tr>
              <Table.Td><Text fw="500">{t("Profile.username")}</Text> </Table.Td>
              <Table.Td><Text>{user.username}</Text></Table.Td>
              <Table.Td><EditButton onClick={openUsernameModal} /></Table.Td>
            </Table.Tr>
            <Table.Tr>
              <Table.Td><Text fw="500">{t("Profile.password")}</Text></Table.Td>
              <Table.Td><Text>**********</Text></Table.Td>
              <Table.Td><EditButton onClick={openPassModal} /></Table.Td>
            </Table.Tr>
            <Table.Tr>
              <Table.Td><Text fw="500">{t("Profile.degree")}</Text></Table.Td>
              <Table.Td><Text>{degree?.name}</Text></Table.Td>
              <Table.Td><EditButton onClick={openDegreeModal} /></Table.Td>
            </Table.Tr>
          </Table.Tbody>
        </Table>
      </Paper>
    </div>
    <ChangePassModal opened={openedPassModal} onClose={closePassModal} />
    <ChangeDegreeModal opened={openedDegreeModal} onClose={closeDegreeModal} />
    <ChangeUserModal opened={openedUsernameModal} onClose={closeUsernameModal} />
    <ChangePictureModal opened={openedPictureModal} onClose={closePictureModal} />
  </>;
}

function ChangePassModal({ opened, onClose }: { opened: boolean, onClose: () => void }) {
  const { t } = useTranslation();
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  function valPass(pass: string, oldPass: string): string | null {
    let error: string | null = null;
    validatePassword(pass, (err: string) => error = err);

    error = error || pass === oldPass ? t('Profile.samePassError') : null;

    return error == "" ? null : error;
  }

  function valConfirmPass(pass: string, confirm: string): string | null {
    let error: string | null = null;
    validateConfirmPassword(pass, confirm, (err: string) => error = err);
    return error == "" ? null : error;
  }

  const form = useForm({
    validateInputOnBlur: true,
    initialValues: {
      oldPass: "",
      newPass: "",
      confirmNewPass: ""
    },
    validate: {
      oldPass: (val) => val.length > 0 ? null : " ",
      newPass: (val, values) => valPass(val, values.oldPass),
      confirmNewPass: (val, values) => valConfirmPass(values.newPass, val)
    }
  });

  async function onFormSubmit(values: typeof form.values) {
    setSubmitting(true);
    setError(null);

    const res = await userService.changePassword(values.oldPass, values.newPass);
    if (res.failure) {
      let err = t('Profile.changePassError');
      if (res.status === 409) {
        err = t('Profile.wrongOldPass');
        form.setFieldError('oldPass', err);
      } else {
        setError(err);
      }

      console.error("Error: ", res.status);
    }

    setSubmitting(false);
  }

  const icon = <IconLock width="1.2rem" stroke={1.5} />;

  return <>
    <Modal opened={opened} onClose={onClose} title={t("Profile.change_password")} centered>
      <div className={classes.modal}>
        <Alert mb="lg" variant="light" color="red" title="Error" icon={<IconExclamationCircle />} hidden={!error}>
          {error}
        </Alert>
        <form onSubmit={form.onSubmit(onFormSubmit, (e) => console.error(e))}>
          <PasswordInput
            withAsterisk
            label={t("Profile.oldPass")}
            leftSection={icon}
            {...form.getInputProps('oldPass')}
          />
          <PasswordInput
            withAsterisk
            label={t("Profile.newPass")}
            leftSection={icon}
            {...form.getInputProps('newPass')}
          />
          <PasswordInput
            withAsterisk
            label={t("Profile.confirmNewPass")}
            leftSection={icon}
            {...form.getInputProps('confirmNewPass')}
          />
          <Center mt="md">
            <Button type="submit" variant="filled" color="green" disabled={submitting}>
              {t("Profile.submit")}
            </Button>
          </Center>
        </form>
      </div>
    </Modal>
  </>;
}

function ChangeDegreeModal({ opened, onClose }: { opened: boolean, onClose: () => void }) {
  const { t } = useTranslation();

  const [degrees, setDegrees] = useState<Degree[]>([]);
  const [selected, setSelected] = useState<Degree | null>(null);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<boolean>(false);

  const user = userService.getUserData();
  const curDegId = user?.degreeId;

  const combobox = useCombobox({
    onDropdownClose: () => combobox.resetSelectedOption(),
    onDropdownOpen: (eventSource) => {
      if (eventSource === 'keyboard') {
        combobox.selectActiveOption();
      } else {
        combobox.updateSelectedOptionIndex('active');
      }
    },
  });

  useEffect(() => {
    degreeService.getDegrees().then(res => {
      if (res.failure) {
        console.error("Unable to get degrees");
        return;
      }

      const degrees = res.data as Degree[];
      const currentDegree = degrees.find(d => d.id === user?.degreeId);

      setDegrees(degrees);

      if (currentDegree) setSelected(currentDegree);
    });
  }, [user?.degreeId]);

  async function submit() {
    setSubmitting(true);
    setError(false);

    const res = await userService.changeDegree(selected!.id);

    if (res.failure) {
      setError(true);
      console.error("Error: ", res.status);
    }

    setSubmitting(false);
  }

  return <>
    <Modal opened={opened} onClose={onClose} title={t("Profile.change_degree")} centered>
      <div className={classes.modal}>
        {degrees.length == 0 ? <Center><Loader /></Center> : <>
          <Alert mb="lg" variant="light" color="red" title="Error" icon={<IconExclamationCircle />} hidden={!error}>
            {t('Profile.changeDegreeError')}
          </Alert>
          <Combobox
            store={combobox}
            resetSelectionOnOptionHover
            // withinPortal={false}
            onOptionSubmit={(val) => {
              const selected = degrees.find(d => d.id.toString() === val) ?? null;
              setSelected(selected);
              combobox.updateSelectedOptionIndex('active');
              combobox.closeDropdown();
            }}
          >
            <Combobox.Target targetType="button">
              <InputBase
                component="button"
                type="button"
                pointer
                rightSection={<Combobox.Chevron />}
                rightSectionPointerEvents="none"
                onClick={() => combobox.toggleDropdown()}
              >
                {selected?.name ??
                  <Input.Placeholder>{t('Profile.chooseDegree')}</Input.Placeholder>
                }
              </InputBase>
            </Combobox.Target>

            <Combobox.Dropdown>
              <Combobox.Options>
                {degrees.map(degree => (
                  <Combobox.Option value={degree.id.toString()} key={degree.id} active={selected?.id === degree.id}>
                    {degree.name}
                  </Combobox.Option>
                ))}
              </Combobox.Options>
            </Combobox.Dropdown>
          </Combobox>
          <Text c="red" fz="sm" p="sm"><b>{t('Profile.warning')}</b>{t('Profile.changeDegreeWarning')}</Text>
          <Center>
            <Button color="green" onClick={() => submit()} disabled={submitting || selected?.id === curDegId}>
              {t("Profile.submit")}
            </Button>
          </Center>
        </>
        }
      </div>
    </Modal>
  </>;
}

function ChangeUserModal({ opened, onClose }: { opened: boolean, onClose: () => void }) {
  const { t } = useTranslation();
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<boolean>(false);

  const user = userService.getUserData();

  function valUsername(username: string): string | null {
    let error: string | null = null;
    validateUsername(username, (err: string) => error = err);
    return error == "" ? null : error;
  }

  async function onSubmit(values: typeof form.values) {
    setSubmitting(true);
    setError(false);

    const res = await userService.changeUsername(values.username);
    if (res.failure) {
      setError(true);
      console.error("Error: ", res.status);
    }

    setSubmitting(false);
  }

  const form = useForm({
    validateInputOnBlur: true,
    initialValues: {
      username: ""
    },
    validate: {
      username: (val) => valUsername(val)
    }
  });

  return <>
    <Modal opened={opened} onClose={onClose} title={t("Profile.change_username")} centered>
      <div className={classes.modal}>
        {error &&
          <Alert mb="lg" variant="light" color="red" title="Error" icon={<IconExclamationCircle />} hidden={!error}>
            {t('Profile.changeUsernameError')}
          </Alert>
        }
        <form onSubmit={form.onSubmit(onSubmit)}>
          <TextInput
            withAsterisk
            label={t("Profile.newUsername")}
            placeholder={user?.username}
            {...form.getInputProps('username')}
          />
          <Center mt="md">
            <Button disabled={submitting} type="submit" color="green">{t("Profile.submit")}</Button>
          </Center>
        </form>
      </div>
    </Modal>
  </>;
}

function ChangePictureModal({ opened, onClose }: { opened: boolean, onClose: () => void }) {
  const { t } = useTranslation();

  const [file, setFile] = useState<File | null>(null);
  const [submitting, setSubmitting] = useState(false);

  const openRef = useRef<() => void>(null);

  function onDrop(files: File[]) {
    if (files.length !== 1) {
      console.error("Invalid file count: ", files.length);
      return;
    }

    const file = files[0];
    setFile(file);
  }

  function submit() {
    if (!file) return;

    setSubmitting(true);

    userService.changePicture(file).then(res => {
      if (res.failure) {
        console.error("Error: ", res.status);
        return;
      }
    }).finally(() => setSubmitting(false));
  }

  return <>
    <Modal opened={opened} onClose={onClose} title={t('Profile.change_picture')} size="lg" centered>
      <div className={classes.dropzoneWrapper}>
        <Dropzone
          onDrop={onDrop}
          openRef={openRef}
          className={classes.pictureDropzone}
          radius="md"
          accept={[MIME_TYPES.jpeg, MIME_TYPES.png]}
          maxFiles={1}
          maxSize={1024 * 1024 * 5}
          disabled={submitting}
          m="xl"
        >
          <div style={{ pointerEvents: 'none' }}>
            <Group justify="center">
              <Dropzone.Accept>
                <IconDownload
                  style={{ width: rem(50), height: rem(50) }}
                  color="blue"
                  stroke={1.5}
                />
              </Dropzone.Accept>
              <Dropzone.Reject>
                <IconX
                  style={{ width: rem(50), height: rem(50) }}
                  color="red"
                  stroke={1.5}
                />
              </Dropzone.Reject>
              <Dropzone.Idle>
                {file ?
                  <Avatar size="100px" src={URL.createObjectURL(file)} />
                  : <IconCloudUpload style={{ width: rem(50), height: rem(50) }} stroke={1.5} />
                }
              </Dropzone.Idle>
            </Group>

            <Text ta="center" fw={700} fz="lg" mt="xl">
              <Dropzone.Accept>{t('Profile.pictureDrop')}</Dropzone.Accept>
              <Dropzone.Reject>{t('Profile.pictureReject')}</Dropzone.Reject>
              <Dropzone.Idle>{t('Profile.pictureIdle')}</Dropzone.Idle>
            </Text>
            <Text ta="center" fz="sm" mt="xs" c="dimmed">
              {t('Profile.pictureHint')}
            </Text>
          </div>
        </Dropzone>

        <Button disabled={submitting} className={classes.pictureUpload} size="md" radius="xl" onClick={() => openRef?.current?.()}>
          {t('Profile.pictureButton')}
        </Button>
      </div>

      <Center mt="50px" mb="10px">
        <Button disabled={submitting} size="lg" color="green" onClick={submit}>
          {t('Profile.submit')}
        </Button>
      </Center>
    </Modal>
  </>;
}
