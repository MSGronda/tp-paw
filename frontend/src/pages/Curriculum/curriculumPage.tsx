import classes from "./curriculum.module.css";
import Title from "../../components/title/title.tsx";
import {useTranslation} from "react-i18next";
import {Navbar} from "../../components/navbar/navbar.tsx";
import {
  Button,
  Center,
  Chip,
  ChipGroup,
  Container,
  Divider,
  Group,
  Loader,
  RangeSlider,
  SegmentedControl,
  Stack,
  Tabs,
  Title as MantineTitle
} from "@mantine/core";
import {ReactElement, useEffect, useRef, useState} from "react";
import {degreeService, subjectService, userService} from "../../services";
import {Degree} from "../../models/Degree.ts";
import {createSearchParams, useNavigate, useParams} from "react-router-dom";
import {Subject} from "../../models/Subject.ts";
import SubjectCard from "../../components/subject-card/subject-card.tsx";
import {IconFilter} from "@tabler/icons-react";
import {useIsVisible} from "../../hooks/useIsVisible.tsx";

export default function CurriculumPage() {
  const {t} = useTranslation(undefined, {keyPrefix: "Curriculum"});
  const params = useParams();

  const [degree, setDegree] = useState<Degree | null>(null);
  const [filters, setFilters] = useState<Record<string,[string]>|null>(null);

  const degreeId = params.id ? parseInt(params.id) : userService.getUserData()!.degreeId!;

  useEffect(() => {
    degreeService.getDegreeById(degreeId).then((res) => {
      if (res.failure) {
        console.error("Failed to get degree: Status ", res.status);
        return;
      }
      setDegree(res.data);
    }).then(() => subjectService.getSubjectFiltersForDegree(degreeId))
      .then((filters => setFilters(filters)))
      .catch(err => console.error("Failed to get degree and filters: ", err));
    
  }, [degreeId]);

  const tabs: ReactElement[] = [];
  const tabPanels: ReactElement[] = [];
  if (degree) {
    for (let i = 1; i < degree.semesterSubjects.length; i++) {
      const year = Math.floor((i-1) / 2) + 1;
      const semester = ((i-1) % 2) + 1;
      tabs.push(
        <Tabs.Tab value={i.toString()} key={i}>
          {t("semester", {year: year, semester: semester})}
        </Tabs.Tab>
      );
      tabPanels.push(<Tabs.Panel value={i.toString()} key={i}><SemesterTabPanel key={i} degreeId={degreeId}
                                                                        semester={i}/></Tabs.Panel>)
    }
    tabs.push(
      <Tabs.Tab value="-1" key="-1">
        {t("electives")}
      </Tabs.Tab>
    );
    tabPanels.push(<Tabs.Panel value="-1" key="-1"><SemesterTabPanel key="-1" degreeId={degreeId} semester={-1}/></Tabs.Panel>);
  }

  return <>
    <div className={classes.fullsize}>
      <Title text={t("title")}/>
      <Navbar/>

      {!degree ? <Center m="3rem"><Loader/></Center> :
        <Container size="100%" w="70%">
          <MantineTitle mx="1rem" my="2rem">{degree.name}</MantineTitle>

          <Filters filters={filters}/>

          <Divider my="1rem" color="#cccccc"/>

          <Tabs defaultValue="1">
            <Tabs.List key="list">
              {tabs}
            </Tabs.List>
            {tabPanels}
          </Tabs>
        </Container>
      }
    </div>
  </>;
}

function SemesterTabPanel({degreeId, semester}: { degreeId: number, semester: number }) {
  const [cachedDegreeId, setCachedDegreeId] = useState<number | null>(null);
  const [subjects, setSubjects] = useState<Subject[] | null>(null);
  const ref = useRef<HTMLDivElement>(null);
  const visible = useIsVisible(ref);

  useEffect(() => {
    if (cachedDegreeId == degreeId || !visible) return;

    subjectService.getSemesterSubjects(degreeId, semester).then((res) => {
      setCachedDegreeId(degreeId);
      setSubjects(res);
    }).catch(err => console.error("Failed to get semester subjects: ", err));
  }, [degreeId, semester, visible, subjects, cachedDegreeId]);

  return <div ref={ref}>
    {!subjects ? <Center m="5rem"><Loader/></Center> :
        <div style={{width: "90%", minHeight: '90%' , display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(250px, 1fr))', padding: '1rem 1rem 1rem 1rem', gap: '1rem'}}>
          { subjects.map((subject) => (
              <SubjectCard {...subject} key={subject.id} progress="incomplete" numReviews={subject.reviewCount}/>
          ))
          }
        </div>
    }
  </div>
}

function Filters({filters}: { filters: Record<string,[string]> | null }) {
  const { t } = useTranslation(undefined, {keyPrefix: "Curriculum"});
  const navigate = useNavigate(); 
  
  const [visible, setVisible] = useState(false);
  const [departments, setDepartments] = useState<string[]>([]);
  const [creditRange, setCreditRange] = useState([0, 12]);
  const [difficultyRange, setDifficultyRange] = useState([0, 2]);
  const [timeDemandRange, setTimeDemandRange] = useState([0, 2]);
  const [orderBy, setOrderBy] = useState("name");
  const [orderByDir, setOrderByDir] = useState("asc");
  
  const maxCredits = filters?.credits ? Math.max(...filters.credits.map(credit => parseInt(credit))) : 1;
  const minCredits = 0;
  const creditMarks = [];
  for(let i = minCredits; i <= maxCredits; i += 3) {
    creditMarks.push({value: i, label: i.toString()});
  }
  
  const difficultyMarks = [
    {value: 0, label: t("easy")},
    {value: 1, label: t("medium")},
    {value: 2, label: t("hard")}
  ];
  
  const timeDemandMarks = [
    {value: 0, label: t("low")},
    {value: 1, label: t("medium")},
    {value: 2, label: t("high")}
  ];
  
  const orderByOptions = [
    {value: "name", label: t("name")},
    {value: "credits", label: t("credits")},
    {value: "difficulty", label: t("difficulty")},
    {value: "timeDemand", label: t("timeDemand")},
    {value: "id", label: t("code")}
  ];
  
  const orderByDirOptions = [
    {value: "asc", label: `↿ ${t("ascending")}`},
    {value: "desc", label: `⇃  ${t("descending")}`}
  ];
  
  function onApply() {
    console.log("Departments: ", departments);
    console.log("Credits: ", creditRange);
    console.log("Difficulty: ", difficultyRange);
    console.log("Time Demand: ", timeDemandRange);
    console.log("Order By: ", orderBy, " ", orderByDir);
    
    const params = createSearchParams({
      department: departments?.[0],
      ob: orderBy,
      dir: orderByDir
    });
    
    navigate(`/search?${params}`)
  }
  
  return <>
    <Button onClick={() => setVisible(!visible)} ml="1rem" mb="1rem" variant="outline" bg="white" color="grey">
      <IconFilter size={20} style={{marginRight: "0.5rem"}}/>
      {t("filter")}
    </Button>
    { visible && filters &&
      <Stack mx="3rem">
        { filters.department &&
            <Stack>
              <MantineTitle order={4}>{t("department")}</MantineTitle>
              <ChipGroup multiple onChange={setDepartments}>
                  <Group gap="0.5rem">
                    { filters.department.map(dept => 
                        <Chip key={dept} value={dept} variant="outline">{dept}</Chip>
                    )}
                  </Group>
              </ChipGroup>
            </Stack>
        }
        <Group w="100%" gap="4rem" align="start">
          { filters.credits && 
            <Stack>
              <MantineTitle order={4}>{t("credits")}</MantineTitle>
              <RangeSlider
                  defaultValue={[minCredits, maxCredits]}
                  step={3}
                  min={minCredits}
                  max={maxCredits}
                  minRange={0}
                  label={null}
                  marks={creditMarks}
                  onChange={setCreditRange}
                  
                  mb="2rem"
                  w="225px"
              />
            </Stack>
          }
          { filters.difficulty &&
            <Stack>
              <MantineTitle order={4}>{t("difficulty")}</MantineTitle>
              <RangeSlider
                  defaultValue={[0, 2]}
                  step={1}
                  min={0}
                  max={2}
                  minRange={0}
                  label={null}
                  marks={difficultyMarks}
                  onChange={setDifficultyRange}
                  
                  mb="2rem"
                  w="225px" 
              />
            </Stack>
          }
          <Stack>
            <MantineTitle order={4}>{t("timeDemand")}</MantineTitle>
            <RangeSlider
              defaultValue={[0, 2]}
              step={1}
              min={0}
              max={2}
              minRange={0}
              label={null}
              marks={timeDemandMarks}
              onChange={setTimeDemandRange}

              mb="2rem"
              w="225px"
            />
          </Stack>
        </Group>
        <Stack w="500px">
          <MantineTitle order={4}>{t('orderBy')}</MantineTitle>
          <SegmentedControl data={orderByOptions} defaultValue="name" onChange={setOrderBy} mb="-1rem" mt="-0.5rem"/>
          <SegmentedControl data={orderByDirOptions} defaultValue="asc" onChange={setOrderByDir} />   
        </Stack>
        <Button onClick={onApply} color="blue" w="130px">{t("applyFilters")}</Button>
      </Stack>
    }
  </>
}
