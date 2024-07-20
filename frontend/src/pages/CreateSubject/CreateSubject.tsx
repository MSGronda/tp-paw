import { useTranslation } from "react-i18next";
import { Navbar } from "../../components/navbar/navbar";
import classes from "./createsubject.module.css";
import {
  ActionIcon,
  Button,
  Combobox,
  ComboboxOption,
  ComboboxOptions,
  Flex, Grid,
  InputBase,
  Modal, MultiSelect,
  NumberInput, Pagination, rem,
  Table,
  Tabs,
  Textarea, TextInput, Tooltip,
  useCombobox,
} from "@mantine/core";
import {useEffect, useState} from "react";
import Title from "../../components/title/title";
import {degreeService, subjectService, professorService} from "../../services";
import {handleService} from "../../handlers/serviceHandler.tsx";
import {Subject} from "../../models/Subject.ts";
import {useNavigate} from "react-router-dom";
import {Degree} from "../../models/Degree.ts";
import {IconPencil, IconX} from "@tabler/icons-react";
import ChooseSubjectCard from "../../components/choose-subject-card/choose-subject-card.tsx";
import {Professor} from "../../models/Professor.ts";
import Class from "../../models/Class.ts";

export function CreateSubject() {
  const { t } = useTranslation();
  const navigate = useNavigate();

  // UI components states
  const [activeTab, setActiveTab] = useState<string | null>("general-info");
  const [openedDegreeModal, setOpenedDegreeModal] = useState(false);
  const [openedPrereqModal, setOpenedPrereqModal] = useState(false);
  const [openedProfessorModal, setOpenedProfessorModal] = useState(false);
  const [openedClassModal, setOpenedClassModal] = useState(false);
  const [currentPrereqPage, setCurrentPrereqPage] = useState<number>(1);
  const [maxPage, setMaxPage] = useState(2);

  // Form related states
  const [department, setDepartment] = useState<string>("");
  const [subjectId, setSubjectId] = useState<string>("");
  const [classProfessor, setClassProfessor] = useState<string>("");
  const [classDay, setClassDay] = useState<string>("");
  const [credits, setCredits] = useState<string | number>(3);
  const [selectedDegrees, setSelectedDegrees] = useState<number[]>([]);
  const [selectedSemesters, setSelectedSemesters] = useState<number[]>([]);
  const [selectedPrereqs, setSelectedPrereqs] = useState<number[]>([]);
  const [selectedProfessors, setSelectedProfessors] = useState<string[]>([]);
  const [createdProfessors, setCreatedProfessors] = useState<string[]>([]);
  const [selectedClasses, setSelectedClasses] = useState<Class[]>([]);

  // Fetched Values
  const [degrees, setDegrees] = useState<Degree[]>([]);
  const [subjects, setSubjects] = useState<Subject[]>([]);
  const [professors, setProfessors] = useState<Professor[]>([]);

  // Current selected values
  const [currentDegree, setCurrentDegree] = useState<number>(1);
  const [currentSemester, setCurrentSemester] = useState<string>("");
  const [currentSemesterOptions, setCurrentSemesterOptions] = useState<JSX.Element[]>([]);
  const [currentProfessorCreation, setCurrentProfessorCreation] = useState<string>("");
  const [currentClassName, setCurrentClassName] = useState<string>("");
  const [currentClassProfessors, setCurrentClassProfessors] = useState<string[]>([]);

  const MINIMUM_CREDITS = 1;
  const MAXIMUM_CREDITS = 12;

  const searchSubjects = async (page: number) => {
    const res = await subjectService.getSubjects(page);
    const data = handleService(res, navigate);
    if (res) {
      setSubjects(data.subjects);
      setMaxPage(res.maxPage || 1);
    }
  }

  const searchDegrees = async () => {
    const res = await degreeService.getDegrees();
    const data = handleService(res, navigate);
    if (res) {
      setDegrees(data);
    }
  }

  const searchProfessors = async() => {
    const res = await professorService.getProfessors();
    const data = handleService(res, navigate);
    if(res) {
      setProfessors(data);
    }
  }

  useEffect(() => {
    if(selectedDegrees.length > 0) {
      searchSubjects(currentPrereqPage);
    }

  }, [selectedDegrees, currentPrereqPage]);

  useEffect(() => {
    searchDegrees();
  }, []);

  useEffect(() => {
    searchProfessors();
  }, []);

  const carreerSemester = new Map<string, string[]>();
  useEffect(() => {
    for (let i = 0; i < degrees.length; i++) {
      carreerSemester.set(degrees[i].id.toString(), []);
      if(degrees[i].semesterSubjects) {
        for (let j = 1; j < degrees[i].semesterSubjects.length; j++) {
          carreerSemester.get(degrees[i].id.toString())?.push(t("CreateSubject.semesterOption", { number: j }));
        }
      }
      carreerSemester.get(degrees[i].id.toString())?.push(t("CreateSubject.elective"));
    }
  }, [degrees]);

  const departments = [
    "Ambiente y Movilidad",
    "Ciencias Exactas y Naturales",
    "Ciencias de la Vida",
    "Economia y Negocios",
    "Sistemas Complejos y Energía",
    "Sistemas Digitales y Datos",
  ];
  const classDays = [
    t("CreateSubject.day1"),
    t("CreateSubject.day2"),
    t("CreateSubject.day3"),
    t("CreateSubject.day4"),
    t("CreateSubject.day5"),
    t("CreateSubject.day6"),
    t("CreateSubject.day7"),
  ];

  useEffect(() => {
    if(carreerSemester != undefined && currentDegree != undefined && carreerSemester.get(currentDegree.toString()) != undefined) {
      setCurrentSemesterOptions(carreerSemester.get(currentDegree.toString()).map((semester) => (
          <ComboboxOption key={semester} value={semester}>
            {semester}
          </ComboboxOption>
      )));
    }
  }, [currentDegree, carreerSemester]);

  // Degrees/Semester Utils
  function searchForDegreeId(degreeName: string) {
    for(let i = 0; i < degrees.length; i++) {
      if(degrees[i].name === degreeName) {
        return degrees[i].id;
      }
    }
    return 0;
  }

  function searchForDegreeName(degreeId: number) {
    for(let i = 0; i < degrees.length; i++) {
      if(degrees[i].id === degreeId) {
        return degrees[i].name;
      }
    }
    return "";
  }

  function extractNumberFromSemesterName(semesterName: string) {
    return Number(semesterName.match(RegExp('[0-9][0-9]?'))) != 0 ? Number(semesterName.match(RegExp('[0-9][0-9]?'))) : -1
  }

  // Handlers
  function handleSelectedDegreeSemesters() {
    let index;
    if((index = selectedDegrees.indexOf(currentDegree)) !== -1) {
      selectedDegrees[index] = currentDegree;
      selectedSemesters[index] = extractNumberFromSemesterName(currentSemester);
    } else {
      setSelectedDegrees((selectedDegrees) => [...selectedDegrees, currentDegree]);
      setSelectedSemesters((selectedSemesters) => [...selectedSemesters, extractNumberFromSemesterName(currentSemester)]);
    }
    setOpenedDegreeModal(false);
  }

  function handleRemoveSelectedDegree(degreeId: number) {
    const index = selectedDegrees.indexOf(degreeId);
    setSelectedDegrees([...selectedDegrees.slice(0,index), ...selectedDegrees.slice(index + 1)]);
    setSelectedSemesters([...selectedSemesters.slice(0,index), ...selectedSemesters.slice(index + 1)]);
  }

  function handlePrereqSelection(subjectId: string) {
    if(!selectedPrereqs.includes(Number(subjectId))) {
      setSelectedPrereqs([...selectedPrereqs, Number(subjectId)]);
    }
  }

  function handlePrereqRemoval(subjectId: string) {
    if(selectedPrereqs.includes(Number(subjectId))) {
      const index = selectedPrereqs.indexOf(Number(subjectId));
      setSelectedPrereqs([...selectedPrereqs.slice(0,index), ...selectedPrereqs.slice(index + 1)]);
    }
  }

  function isPrereqSelected(subjectId: string) {
    return selectedPrereqs.includes(Number(subjectId));
  }

  function handleProfessorCreation() {
    if(!selectedProfessors.includes(currentProfessorCreation)){
      setSelectedProfessors([...selectedProfessors, currentProfessorCreation]);
    }
    setProfessors([...professors, {name: currentProfessorCreation, subjects: []}]);
    setCreatedProfessors([...createdProfessors, currentProfessorCreation]);
    setCurrentProfessorCreation("");
    setOpenedProfessorModal(false);
  }

  function handleRemoveCreatedProfessor(professor: string) {
    let index;
    if( ( index = createdProfessors.indexOf(professor)) != -1) {
      setCreatedProfessors([...createdProfessors.slice(0,index), ...createdProfessors.slice(index + 1)]);
    }
    let selectedIndex;
    if( (selectedIndex = selectedProfessors.indexOf(professor)) != -1) {
      setSelectedProfessors([...selectedProfessors.slice(0,selectedIndex), ...selectedProfessors.slice(index + 1)]);
    }
  }

  function handleProfessorRemove(professor: string) {
    let index;
    if( ( index = selectedProfessors.indexOf(professor)) != -1) {
      setSelectedProfessors([...selectedProfessors.slice(0,index), ...selectedProfessors.slice(index + 1)]);
    }
  }

  function handleProfessorAddition(professor: string) {
    setSelectedProfessors([...selectedProfessors, professor]);
  }

  function handleClassCreation() {
    const newClass = {idClass: currentClassName, idSubject: subjectId, locations: [], professors: currentClassProfessors};
    let idClassRepeated = false;
    for(const clas of selectedClasses) {
      if(clas.idClass === currentClassName){
        //TODO Error message that cannot repeat idClass name
        idClassRepeated = true;
      }
    }

    if(!idClassRepeated) {
      setSelectedClasses([...selectedClasses, newClass]);
      setCurrentClassName("");
      setCurrentClassProfessors([]);
      setOpenedClassModal(false);
    }
  }

  function handleRemoveClass(clas: Class) {
    let index;
    if( (index = selectedClasses.indexOf(clas) ) != -1) {
      setSelectedClasses([...selectedClasses.slice(0,index), ...selectedClasses.slice(index + 1)]);
    }
  }

  function handleClassEdit(clas: Class) {
    setCurrentClassName(clas.idClass);
    setCurrentClassProfessors(clas.professors);
    setOpenedClassModal(true);
  }

  // Combobox Options
  const deaprtmentOptions = departments.map((departament) => (
    <Combobox.Option key={departament} value={departament}>
      {departament}
    </Combobox.Option>
  ));
  const degreesOptions = degrees.map((degree) => (
    <ComboboxOption key={degree.id} value={degree.name}>
      {degree.name}
    </ComboboxOption>
  ));
  const professorsOptions = professors.slice(0,100).map((professor) => (
      `${professor.name}`
  ));
  const classDayOptions = classDays.map((classDay) => (
    <ComboboxOption key={classDay} value={classDay}>
      {classDay}
    </ComboboxOption>
  ));

  const comboboxDepartment = useCombobox({
    onDropdownClose: () => comboboxDepartment.resetSelectedOption(),
  });
  const comboboxDegree = useCombobox({
    onDropdownClose: () => comboboxDegree.resetSelectedOption(),
  });
  const comboboxSemester = useCombobox({
    onDropdownClose: () => comboboxSemester.resetSelectedOption(),
  });
  const comboboxPrerequisiste = useCombobox({
    onDropdownClose: () => comboboxPrerequisiste.resetSelectedOption(),
  });
  const comboboxProfessor = useCombobox({
    onDropdownClose: () => comboboxProfessor.resetSelectedOption(),
  });
  const comboboxClassProfessor = useCombobox({
    onDropdownClose: () => comboboxClassProfessor.resetSelectedOption(),
  });
  const comboboxClassDays = useCombobox({
    onDropdownClose: () => comboboxClassDays.resetSelectedOption(),
  });
  return (
    <div className={classes.background}>
      <Title text={t("CreateSubject.pageTitle")}/>

      { /* Degree Modal */}
      <Modal opened={openedDegreeModal} onClose={() => setOpenedDegreeModal(false)} title={t("CreateSubject.addDegree")}>
        <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
          {t("CreateSubject.degreeModalDegree")}
          <Combobox store={comboboxDegree} onOptionSubmit={(value) => {
              setCurrentDegree(searchForDegreeId(value));
              comboboxDegree.closeDropdown();
            }}>
            <Combobox.Target>
              <InputBase className={classes.degreeDropdown} component="button" type="button" pointer rightSection={<Combobox.Chevron />}
                rightSectionPointerEvents="none" onClick={() => comboboxDegree.toggleDropdown()}>
                {searchForDegreeName(currentDegree)}
              </InputBase>
            </Combobox.Target>
            <Combobox.Dropdown>
              <ComboboxOptions>{degreesOptions}</ComboboxOptions>
            </Combobox.Dropdown>
          </Combobox>
        </Flex>
        <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
          {t("CreateSubject.semester")}
          <Combobox store={comboboxSemester}
            onOptionSubmit={(value) => {
              setCurrentSemester(value);
              comboboxSemester.closeDropdown();
            }}>
            <Combobox.Target>
              <InputBase className={classes.degreeDropdown} component="button" type="button" pointer rightSection={<Combobox.Chevron />}
                rightSectionPointerEvents="none" onClick={() => comboboxSemester.toggleDropdown()}>
                { currentSemester }
              </InputBase>
            </Combobox.Target>
            <Combobox.Dropdown>
              <ComboboxOptions>{currentSemesterOptions}</ComboboxOptions>
            </Combobox.Dropdown>
          </Combobox>
        </Flex>
        <Flex mih={50} gap="xl" justify="center" align="center" direction="row" wrap="wrap">
          <Button color="green" onClick={() => handleSelectedDegreeSemesters()}>{t("CreateSubject.add")}</Button>
        </Flex>
      </Modal>

      {/* Prereqs Modal */}
      <Modal opened={openedPrereqModal} onClose={() => setOpenedPrereqModal(false)} title={t("CreateSubject.addPrereq")} centered size="80%">
        <Flex justify="center" direction="column" align="center" gap="md">
          <Grid >
            {subjects.map((subject) => (
                <Grid.Col span={2}>
                  <ChooseSubjectCard subject={subject} selectionCallback={handlePrereqSelection} selected={isPrereqSelected(subject.id)} removalCallback={handlePrereqRemoval} />
                </Grid.Col>
            ))}
          </Grid>
          <Flex>
            <Pagination total={maxPage} value={currentPrereqPage} onChange={setCurrentPrereqPage}/>
          </Flex>
        </Flex>
      </Modal>
      {/* Professor Modal */}
      <Modal opened={openedProfessorModal} onClose={() => setOpenedProfessorModal(false)} title={t("CreateSubject.createProfessor")}>
        <Flex mih={50} gap="xl" justify="center" align="center" direction="row" wrap="wrap">
          {t("CreateSubject.professorName")}
          <TextInput className={classes.degreeDropdown} value={currentProfessorCreation} onChange={(event) => setCurrentProfessorCreation(event.target.value)} />
        </Flex>
        <Flex mih={50} gap="xl" justify="right" align="center" direction="row" wrap="wrap">
          <Button color="green" onClick={() => handleProfessorCreation()}>{t("CreateSubject.add")}</Button>
        </Flex>
      </Modal>

      {/* Class Modal */}
      <Modal opened={openedClassModal} onClose={() => setOpenedClassModal(false)} title={t("CreateSubject.addClasses")}>
        <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
          {t("CreateSubject.class")}
          <Textarea className={classes.degreeDropdown} autosize value={currentClassName}
                    onChange={(event) => setCurrentClassName(event.currentTarget.value)}/>
        </Flex>
        <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
          {t("CreateSubject.professors")}
          <MultiSelect placeholder={t("CreateSubject.professorLabel")} data={selectedProfessors} value={currentClassProfessors} onChange={setCurrentClassProfessors}/>
        </Flex>
        {/*<Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
          {t("CreateSubject.day")}
          <Combobox store={comboboxClassDays} onOptionSubmit={(value) => {
            setClassDay(value);
            comboboxClassDays.closeDropdown();
          }}>
            <Combobox.Target>
              <InputBase className={classes.degreeDropdown} component="button" type="button" pointer
                         rightSection={<Combobox.Chevron/>} rightSectionPointerEvents="none"
                         onClick={() => comboboxClassDays.toggleDropdown()}>
                {classDay}
              </InputBase>
            </Combobox.Target>
            <Combobox.Dropdown>
              <ComboboxOptions>{classDayOptions}</ComboboxOptions>
            </Combobox.Dropdown>
          </Combobox>
        </Flex>
          <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
        {t("CreateSubject.timeStart")}
        <TimeInput className={classes.degreeDropdown}/>
      </Flex>
      <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
        {t("CreateSubject.timeEnd")}
        <TimeInput className={classes.degreeDropdown}/>
      </Flex>
      <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
        {t("CreateSubject.mode")}
        <Textarea className={classes.degreeDropdown} autosize></Textarea>
      </Flex>
      <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
        {t("CreateSubject.building")}
        <Textarea className={classes.degreeDropdown} autosize></Textarea>
      </Flex>
      <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
        {t("CreateSubject.classroom")}
        <Textarea className={classes.degreeDropdown} autosize></Textarea>
      </Flex>
      */}
        <Flex mih={50} gap="xl" justify="right" align="center" direction="row" wrap="wrap">
          <Button color="green" onClick={() => handleClassCreation()}>{t("CreateSubject.add")}</Button>
        </Flex>
      </Modal>

      { /* Actual Page */ }
      <Navbar />
      <div className={classes.container50}>
        <form className={classes.form}>
          <h1 className={classes.title}>{t("CreateSubject.title")}</h1>

          <br />
          <Tabs value={activeTab} onChange={(value) => setActiveTab(value)}>
            <Tabs.List>
              <Tabs.Tab value="general-info">
                {t("CreateSubject.generalInfo")}
              </Tabs.Tab>
              <Tabs.Tab value="classes">{t("CreateSubject.classes")}</Tabs.Tab>
            </Tabs.List>

            { /* General Informaiton Tab */ }
            <Tabs.Panel value="general-info">
              <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
                <div>
                  {t("CreateSubject.id")}
                  <h6 className={classes.optional}>
                    {t("CreateSubject.idHelp")}
                  </h6>
                </div>
                <Textarea className={classes.departmentDropdown} autosize error={t("CreateSubject.idError")}
                />
              </Flex>
              <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
                {t("CreateSubject.name")}
                <Textarea className={classes.departmentDropdown} autosize />
              </Flex>
              <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
                {t("CreateSubject.department")}
                <Combobox store={comboboxDepartment} width={300} onOptionSubmit={(value) => {
                    setDepartment(value);
                    comboboxDepartment.closeDropdown();
                  }}>
                  <Combobox.Target>
                    <InputBase className={classes.departmentDropdown} component="button" type="button" pointer
                               rightSection={<Combobox.Chevron />} rightSectionPointerEvents="none" onClick={() => comboboxDepartment.toggleDropdown()}>
                      {department}
                    </InputBase>
                  </Combobox.Target>
                  <Combobox.Dropdown>{deaprtmentOptions}</Combobox.Dropdown>
                </Combobox>
              </Flex>
              <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
                {t("CreateSubject.credits")}
                <NumberInput className={classes.departmentDropdown} value={credits} min={MINIMUM_CREDITS} max={MAXIMUM_CREDITS} onChange={setCredits}/>
              </Flex>
              <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
                {t("CreateSubject.degree")}
                <Flex  direction="column">
                  { selectedDegrees.length > 0 &&
                    selectedDegrees.map((degree) =>
                        <Flex direction="row" align="center">
                          <p>{searchForDegreeName(degree)}</p>
                          <ActionIcon size={24} variant="default" onClick={() => handleRemoveSelectedDegree(degree)}>
                            <IconX style={{ width: rem(24), height: rem(24) }} />
                          </ActionIcon>
                        </Flex>)
                  }
                  <Button onClick={() => setOpenedDegreeModal(true)}>
                    {t("CreateSubject.addDegree")}
                  </Button>
                </Flex>
              </Flex>
              <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
                <div>
                  {t("CreateSubject.prerequisites")}
                  <h6 className={classes.optional}>
                    ({t("CreateSubject.optional")})
                  </h6>
                </div>
                {selectedDegrees.length > 0 ?
                    <Button
                      onClick={() => setOpenedPrereqModal(true)}>
                        {t("CreateSubject.addPrereq")}
                    </Button> :
                    <Tooltip label={t("CreateSubject.prereqAdvice")}>
                      <Button disabled>
                        {t("CreateSubject.addPrereq")}
                      </Button>
                    </Tooltip>}
              </Flex>
              <Flex mih={50} miw={500} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
                {t("CreateSubject.professor")}
                <MultiSelect placeholder={t("CreateSubject.professorLabel")} data={professorsOptions} searchable  onOptionSubmit={(professor) => handleProfessorAddition(professor)} onRemove={(professor) => handleProfessorRemove(professor)}/>
              </Flex>
              <Flex mih={50} gap="xl" justify="right" align="center" direction="row" wrap="wrap">
                { createdProfessors.length > 0 &&
                    createdProfessors.map((professor) =>
                        <Flex direction="row" align="center">
                          <p>{professor}</p>
                          <ActionIcon size={24} variant="default" onClick={() => handleRemoveCreatedProfessor(professor)}>
                            <IconX style={{ width: rem(24), height: rem(24) }} />
                          </ActionIcon>
                        </Flex>)
                }
                <Button onClick={() => setOpenedProfessorModal(true)}>
                  {t("CreateSubject.createProfessor")}
                </Button>
              </Flex>
              <Flex mih={50} gap="xl" justify="center" align="center" direction="row" wrap="wrap">
                <Button variant="default" onClick={() => setActiveTab("classes")}>
                  {t("CreateSubject.next")}
                </Button>
              </Flex>
            </Tabs.Panel>

            { /* Classes Tab */ }
            <Tabs.Panel value="classes">
              <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
                {t("CreateSubject.classes")}
                <Button onClick={() => setOpenedClassModal(true)}>
                  {t("CreateSubject.addClasses")}
                </Button>
              </Flex>
              <Table>
                <Table.Thead>
                  <Table.Tr>
                    <Table.Th>{t("CreateSubject.class")}</Table.Th>
                    <Table.Th>{t("CreateSubject.professors")}</Table.Th>
                    <Table.Th>{t("CreateSubject.day")}</Table.Th>
                    <Table.Th>{t("CreateSubject.timeStart")}</Table.Th>
                    <Table.Th>{t("CreateSubject.timeEnd")}</Table.Th>
                    <Table.Th>{t("CreateSubject.mode")}</Table.Th>
                    <Table.Th>{t("CreateSubject.building")}</Table.Th>
                    <Table.Th>{t("CreateSubject.classroom")}</Table.Th>
                  </Table.Tr>
                </Table.Thead>
                <Table.Tbody>
                  {selectedClasses.length > 0 && selectedClasses.map((clas) => <Table.Tr>
                    <Table.Th>
                      <Flex direction="row" justify="space-between">
                        {clas.idClass}
                        <ActionIcon size={18} variant="default" onClick={() => handleClassEdit(clas)}>
                          <IconPencil style={{ width: rem(24), height: rem(24) }} />
                        </ActionIcon>
                        <ActionIcon size={18} variant="default" onClick={() => handleRemoveClass(clas)}>
                          <IconX style={{ width: rem(24), height: rem(24) }} />
                        </ActionIcon>
                      </Flex>
                    </Table.Th>
                    <Table.Th>{clas.professors.join(";")}</Table.Th>
                    <Table.Th></Table.Th>
                  </Table.Tr>)}
                </Table.Tbody>
              </Table>
              <Flex mih={50} gap="xl" justify="center" align="center" direction="row" wrap="wrap">
                <Button variant="default"
                  onClick={() => setActiveTab("general-info")}>{t("CreateSubject.previous")}</Button>
                <Button color="green">{t("CreateSubject.createSubject")}</Button>
              </Flex>
            </Tabs.Panel>
          </Tabs>
        </form>
      </div>
    </div>
  );
}
