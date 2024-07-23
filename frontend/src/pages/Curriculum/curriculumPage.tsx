import classes from "./curriculum.module.css";
import Title from "../../components/title/title.tsx";
import {useTranslation} from "react-i18next";
import {Navbar} from "../../components/navbar/navbar.tsx";
import {Center, Container, Divider, Loader, Tabs, Title as MantineTitle} from "@mantine/core";
import {ReactElement, useEffect, useRef, useState} from "react";
import {degreeService, subjectService, userService} from "../../services";
import {Degree} from "../../models/Degree.ts";
import {useParams, useSearchParams} from "react-router-dom";
import {Subject} from "../../models/Subject.ts";
import SubjectCard from "../../components/subject-card/subject-card.tsx";
import {useIsVisible} from "../../hooks/useIsVisible.tsx";
import SubjectFilters from "../../components/subject-filters/subjectFilters.tsx";

export default function CurriculumPage() {
  const {t} = useTranslation(undefined, {keyPrefix: "Curriculum"});
  const params = useParams();
  const [queryParams] = useSearchParams();

  const [degree, setDegree] = useState<Degree|undefined>(undefined);

  const degreeId = params.id ? parseInt(params.id) : userService.getUserData()!.degreeId!;
  const semester = queryParams.has("semester") ? queryParams.get("semester")! : "1";

  useEffect(() => {
    degreeService.getDegreeById(degreeId).then((res) => {
      if (res.failure) {
        console.error("Failed to get degree: Status ", res.status);
        return;
      }
      setDegree(res.data);
    }).catch(err => console.error("Failed to get degree: ", err));

  }, [degreeId]);
  
  function setSemesterParam(semester: string) {
    const currentUrl = `${location.protocol}//${location.host}${location.pathname}`;
    
    if(semester == "1") 
      queryParams.delete("semester");
    else {
      queryParams.set("semester", semester.toString());
    }
    
    const newUrl = queryParams.size == 0 ? currentUrl : `${currentUrl}?${queryParams.toString()}`;
    
    
    window.history.pushState(null, '', newUrl);
  }

  const tabs: ReactElement[] = [];
  const tabPanels: ReactElement[] = [];
  if (degree) {
    for (let i = 1; i < degree.semesterSubjects.length; i++) {
      const year = Math.floor((i-1) / 2) + 1;
      const semester = ((i-1) % 2) + 1;
      tabs.push(
        <Tabs.Tab value={i.toString()} key={i} onClick={() => setSemesterParam(i.toString())}>
          {t("semester", {year: year, semester: semester})}
        </Tabs.Tab>
      );
      tabPanels.push(<Tabs.Panel value={i.toString()} key={i}><SemesterTabPanel key={i} degreeId={degreeId}
                                                                        semester={i}/></Tabs.Panel>)
    }
    tabs.push(
      <Tabs.Tab value="electives" key="electives" onClick={() => setSemesterParam("electives")}>
        {t("electives")}
      </Tabs.Tab>
    );
    tabPanels.push(<Tabs.Panel value="electives" key="electives"><SemesterTabPanel key="electives" degreeId={degreeId} semester={-1}/></Tabs.Panel>);
  }

  return <>
    <div className={classes.fullsize}>
      <Title text={t("title")}/>
      <Navbar/>

      {!degree ? <Center m="3rem"><Loader/></Center> :
        <Container size="100%" w="70%">
          <MantineTitle mx="1rem" mt="2rem">{degree.name}</MantineTitle>

          <SubjectFilters degree={degreeId} redirect/>

          <Divider my="1rem" color="#cccccc"/>

          <Tabs defaultValue={semester.toString()}>
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
