import classes from "./curriculum.module.css";
import Title from "../../components/title/title.tsx";
import {useTranslation} from "react-i18next";
import {Navbar} from "../../components/navbar/navbar.tsx";
import {Button, Center, Container, Divider, Loader, SimpleGrid, Tabs, Title as MantineTitle} from "@mantine/core";
import {ReactElement, useEffect, useRef, useState} from "react";
import {degreeService, subjectService, userService} from "../../services";
import {Degree} from "../../models/Degree.ts";
import {Link, useParams} from "react-router-dom";
import {Subject} from "../../models/Subject.ts";
import SubjectCard from "../../components/subject-card/subject-card.tsx";
import {IconFilter} from "@tabler/icons-react";
import {useIsVisible} from "../../hooks/useIsVisible.tsx";

export default function CurriculumPage() {
  const {t} = useTranslation(undefined, {keyPrefix: "Curriculum"});
  const params = useParams();

  const [degree, setDegree] = useState<Degree | null>(null);

  const degreeId = params.id ? parseInt(params.id) : userService.getUserData()!.degreeId!;

  useEffect(() => {
    degreeService.getDegreeById(degreeId).then((res) => {
      if (res.failure) {
        console.error("Failed to get degree: Status ", res.status);
        return;
      }
      setDegree(res.data);
    });
  }, [degreeId]);

  const tabs: ReactElement[] = [];
  const tabPanels: ReactElement[] = [];
  if (degree) {
    for (let i = 1; i < degree.semesterSubjects.length; i++) {
      tabs.push(
        <Tabs.Tab value={i.toString()} key={i}>
          {t("semester", {number: i})}
        </Tabs.Tab>
      );
      tabPanels.push(<Tabs.Panel value={i.toString()}><SemesterTabPanel key={i} degreeId={degreeId}
                                                                        semester={i}/></Tabs.Panel>)
    }
    tabs.push(
      <Tabs.Tab value="-1" key="-1">
        {t("electives")}
      </Tabs.Tab>
    );
    tabPanels.push(<Tabs.Panel value="-1"><SemesterTabPanel key="-1" degreeId={degreeId} semester={-1}/></Tabs.Panel>);
  }

  return <>
    <div className={classes.fullsize}>
      <Title text={t("title")}/>
      <Navbar/>

      {!degree ? <Center m="3rem"><Loader/></Center> :

        <Container size="100%" w="70%">
          <MantineTitle mx="1rem" my="2rem">{degree.name}</MantineTitle>

          <Link to={'/search'}>
            <Button ml="1rem" variant="outline" bg="white" color="grey">
              <IconFilter size={20} style={{marginRight: "0.5rem"}}/>
              {t("filter")}
            </Button>
          </Link>

          <Divider my="1rem" color="#cccccc"/>

          <Tabs defaultValue="1">
            <Tabs.List>
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
      <SimpleGrid cols={4} p="1rem" mb="1rem">

        { subjects.map((subject) => (
          <SubjectCard {...subject} progress="incomplete" numReviews={subject.reviewCount}/>
        )) 
        }
      </SimpleGrid>
    }
  </div>
}
