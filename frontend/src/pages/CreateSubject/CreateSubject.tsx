import { useTranslation } from "react-i18next";
import { Navbar } from "../../components/navbar/navbar";
import classes from "./createsubject.module.css";
import {
  Button,
  Combobox,
  ComboboxOption,
  ComboboxOptions,
  Flex,
  InputBase,
  Modal,
  NumberInput,
  Table,
  Tabs,
  Textarea,
  useCombobox,
} from "@mantine/core";
import { TimeInput } from "@mantine/dates";
import { useState } from "react";

export function CreateSubject() {
  const { t } = useTranslation();

  const [activeTab, setActiveTab] = useState<string | null>("general-info");
  const [department, setDepartment] = useState<string>("");
  const [degree, setDegree] = useState<string>("");
  const [semester, setSemester] = useState<string>("");
  const [prerequisite, setPrerequisite] = useState<string>("");
  const [professor, setProfessor] = useState<string>("");
  const [classProfessor, setClassProfessor] = useState<string>("");
  const [classDay, setClassDay] = useState<string>("");
  const [credits, setCredits] = useState<string | number>(3);
  const [openedDegreeModal, setOpenedDegreeModal] = useState(false);
  const [openedProfessorModal, setOpenedProfessorModal] = useState(false);
  const [openedClassModal, setOpenedClassModal] = useState(false);

  const MINIMUM_CREDITS = 1;
  const MAXIMUM_CREDITS = 12;

  const departments = [
    "Ambiente y Movilidad",
    "Ciencias Exactas y Naturales",
    "Ciencias de la Vida",
    "Economia y Negocios",
    "Sistemas Complejos y Energía",
    "Sistemas Digitales y Datos",
  ];
  const degrees = [
    "Ingeniería Informática",
    "Ingeniería Mecánica",
    "Ingeniería Química",
    "Ingeniería Naval",
    "Ingeniería Civil",
    "Ingeniería Electrónica",
    "Ingeniería Industrial",
    "Ingeniería en Petróleo",
  ];
  const semesters = [];
  for (let i = 1; i < 11; i++) {
    semesters.push(t("CreateSubject.semesterOption", { number: i }));
  }
  semesters.push(t("CreateSubject.elective"));
  const prerequisites = [
    "Química",
    "Física I",
    "Física II",
    "Física III",
    "Física IV",
  ];
  const professors = [
    "Sotuyo Dodero, Juan Martín",
    "Roig, Ana María",
    "Valles, Santiago",
    "Meola, Franco",
  ];
  const classProfessors = professors.slice(0, 2);
  const classDays = [
    t("CreateSubject.day1"),
    t("CreateSubject.day2"),
    t("CreateSubject.day3"),
    t("CreateSubject.day4"),
    t("CreateSubject.day5"),
    t("CreateSubject.day6"),
    t("CreateSubject.day7"),
  ];

  const deaprtmentOptions = departments.map((departament) => (
    <Combobox.Option key={departament} value={departament}>
      {departament}
    </Combobox.Option>
  ));
  const degreesOptions = degrees.map((degree) => (
    <ComboboxOption key={degree} value={degree}>
      {degree}
    </ComboboxOption>
  ));
  const semestersOptions = semesters.map((semester) => (
    <ComboboxOption key={semester} value={semester}>
      {semester}
    </ComboboxOption>
  ));
  const prerequisiteOptions = prerequisites.map((prerequisite) => (
    <ComboboxOption key={prerequisite} value={prerequisite}>
      {prerequisite}
    </ComboboxOption>
  ));
  const professorsOptions = professors.map((professor) => (
    <ComboboxOption key={professor} value={professor}>
      {professor}
    </ComboboxOption>
  ));
  const classProfessorsOptions = classProfessors.map((classProfessor) => (
    <ComboboxOption key={classProfessor} value={classProfessor}>
      {classProfessor}
    </ComboboxOption>
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
      <Modal
        opened={openedDegreeModal}
        onClose={() => setOpenedDegreeModal(false)}
        title={t("CreateSubject.addDegree")}
      >
        <Flex
          mih={50}
          gap="xl"
          justify="space-between"
          align="center"
          direction="row"
          wrap="wrap"
        >
          {t("CreateSubject.degreeModalDegree")}
          <Combobox
            store={comboboxDegree}
            onOptionSubmit={(value) => {
              setDegree(value);
              comboboxDegree.closeDropdown();
            }}
          >
            <Combobox.Target>
              <InputBase
                className={classes.degreeDropdown}
                component="button"
                type="button"
                pointer
                rightSection={<Combobox.Chevron />}
                rightSectionPointerEvents="none"
                onClick={() => comboboxDegree.toggleDropdown()}
              >
                {degree}
              </InputBase>
            </Combobox.Target>
            <Combobox.Dropdown>
              <ComboboxOptions>{degreesOptions}</ComboboxOptions>
            </Combobox.Dropdown>
          </Combobox>
        </Flex>
        <Flex
          mih={50}
          gap="xl"
          justify="space-between"
          align="center"
          direction="row"
          wrap="wrap"
        >
          {t("CreateSubject.semester")}
          <Combobox
            store={comboboxSemester}
            onOptionSubmit={(value) => {
              setSemester(value);
              comboboxSemester.closeDropdown();
            }}
          >
            <Combobox.Target>
              <InputBase
                className={classes.degreeDropdown}
                component="button"
                type="button"
                pointer
                rightSection={<Combobox.Chevron />}
                rightSectionPointerEvents="none"
                onClick={() => comboboxSemester.toggleDropdown()}
              >
                {semester}
              </InputBase>
            </Combobox.Target>
            <Combobox.Dropdown>
              <ComboboxOptions>{semestersOptions}</ComboboxOptions>
            </Combobox.Dropdown>
          </Combobox>
        </Flex>
        <Flex
          mih={50}
          gap="xl"
          justify="center"
          align="center"
          direction="row"
          wrap="wrap"
        >
          <Button color="green">{t("CreateSubject.add")}</Button>
        </Flex>
      </Modal>
      <Modal
        opened={openedProfessorModal}
        onClose={() => setOpenedProfessorModal(false)}
        title={t("CreateSubject.createProfessor")}
      >
        <Flex
          mih={50}
          gap="xl"
          justify="center"
          align="center"
          direction="row"
          wrap="wrap"
        >
          {t("CreateSubject.professorName")}
          <Textarea className={classes.degreeDropdown} autosize />
        </Flex>
        <Flex
          mih={50}
          gap="xl"
          justify="center"
          align="center"
          direction="row"
          wrap="wrap"
        >
          {t("CreateSubject.professorLastName")}
          <Textarea className={classes.degreeDropdown} autosize />
        </Flex>
        <Flex
          mih={50}
          gap="xl"
          justify="right"
          align="center"
          direction="row"
          wrap="wrap"
        >
          <Button color="green">{t("CreateSubject.add")}</Button>
        </Flex>
      </Modal>
      <Modal
        opened={openedClassModal}
        onClose={() => setOpenedClassModal(false)}
        title={t("CreateSubject.addClasses")}
      >
        <Flex
          mih={50}
          gap="xl"
          justify="space-between"
          align="center"
          direction="row"
          wrap="wrap"
        >
          {t("CreateSubject.class")}
          <Textarea className={classes.degreeDropdown} autosize></Textarea>
        </Flex>
        <Flex
          mih={50}
          gap="xl"
          justify="space-between"
          align="center"
          direction="row"
          wrap="wrap"
        >
          {t("CreateSubject.professors")}
          <Combobox
            store={comboboxClassProfessor}
            onOptionSubmit={(value) => {
              setClassProfessor(value);
              comboboxClassProfessor.closeDropdown();
            }}
          >
            <Combobox.Target>
              <InputBase
                className={classes.degreeDropdown}
                component="button"
                type="button"
                pointer
                rightSection={<Combobox.Chevron />}
                rightSectionPointerEvents="none"
                onClick={() => comboboxClassProfessor.toggleDropdown()}
              >
                {classProfessor}
              </InputBase>
            </Combobox.Target>
            <Combobox.Dropdown>
              <ComboboxOptions>{classProfessorsOptions}</ComboboxOptions>
            </Combobox.Dropdown>
          </Combobox>
        </Flex>
        <Flex
          mih={50}
          gap="xl"
          justify="space-between"
          align="center"
          direction="row"
          wrap="wrap"
        >
          {t("CreateSubject.day")}
          <Combobox
            store={comboboxClassDays}
            onOptionSubmit={(value) => {
              setClassDay(value);
              comboboxClassDays.closeDropdown();
            }}
          >
            <Combobox.Target>
              <InputBase
                className={classes.degreeDropdown}
                component="button"
                type="button"
                pointer
                rightSection={<Combobox.Chevron />}
                rightSectionPointerEvents="none"
                onClick={() => comboboxClassDays.toggleDropdown()}
              >
                {classDay}
              </InputBase>
            </Combobox.Target>
            <Combobox.Dropdown>
              <ComboboxOptions>{classDayOptions}</ComboboxOptions>
            </Combobox.Dropdown>
          </Combobox>
        </Flex>
        <Flex
          mih={50}
          gap="xl"
          justify="space-between"
          align="center"
          direction="row"
          wrap="wrap"
        >
          {t("CreateSubject.timeStart")}
          <TimeInput className={classes.degreeDropdown} />
        </Flex>
        <Flex
          mih={50}
          gap="xl"
          justify="space-between"
          align="center"
          direction="row"
          wrap="wrap"
        >
          {t("CreateSubject.timeEnd")}
          <TimeInput className={classes.degreeDropdown} />
        </Flex>
        <Flex
          mih={50}
          gap="xl"
          justify="space-between"
          align="center"
          direction="row"
          wrap="wrap"
        >
          {t("CreateSubject.mode")}
          <Textarea className={classes.degreeDropdown} autosize></Textarea>
        </Flex>
        <Flex
          mih={50}
          gap="xl"
          justify="space-between"
          align="center"
          direction="row"
          wrap="wrap"
        >
          {t("CreateSubject.building")}
          <Textarea className={classes.degreeDropdown} autosize></Textarea>
        </Flex>
        <Flex
          mih={50}
          gap="xl"
          justify="space-between"
          align="center"
          direction="row"
          wrap="wrap"
        >
          {t("CreateSubject.classroom")}
          <Textarea className={classes.degreeDropdown} autosize></Textarea>
        </Flex>
        <Flex
          mih={50}
          gap="xl"
          justify="right"
          align="center"
          direction="row"
          wrap="wrap"
        >
          <Button color="green">{t("CreateSubject.add")}</Button>
        </Flex>
      </Modal>
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

            <Tabs.Panel value="general-info">
              <Flex
                mih={50}
                gap="xl"
                justify="space-between"
                align="center"
                direction="row"
                wrap="wrap"
              >
                <div>
                  {t("CreateSubject.id")}
                  <h6 className={classes.optional}>
                    {t("CreateSubject.idHelp")}
                  </h6>
                </div>
                <Textarea
                  className={classes.departmentDropdown}
                  autosize
                  error={t("CreateSubject.idError")}
                />
              </Flex>
              <Flex
                mih={50}
                gap="xl"
                justify="space-between"
                align="center"
                direction="row"
                wrap="wrap"
              >
                {t("CreateSubject.name")}
                <Textarea className={classes.departmentDropdown} autosize />
              </Flex>
              <Flex
                mih={50}
                gap="xl"
                justify="space-between"
                align="center"
                direction="row"
                wrap="wrap"
              >
                {t("CreateSubject.department")}
                <Combobox
                  store={comboboxDepartment}
                  width={300}
                  onOptionSubmit={(value) => {
                    setDepartment(value);
                    comboboxDepartment.closeDropdown();
                  }}
                >
                  <Combobox.Target>
                    <InputBase
                      className={classes.departmentDropdown}
                      component="button"
                      type="button"
                      pointer
                      rightSection={<Combobox.Chevron />}
                      rightSectionPointerEvents="none"
                      onClick={() => comboboxDepartment.toggleDropdown()}
                    >
                      {department}
                    </InputBase>
                  </Combobox.Target>
                  <Combobox.Dropdown>{deaprtmentOptions}</Combobox.Dropdown>
                </Combobox>
              </Flex>
              <Flex
                mih={50}
                gap="xl"
                justify="space-between"
                align="center"
                direction="row"
                wrap="wrap"
              >
                {t("CreateSubject.credits")}
                <NumberInput
                  className={classes.departmentDropdown}
                  value={credits}
                  min={MINIMUM_CREDITS}
                  max={MAXIMUM_CREDITS}
                  onChange={setCredits}
                />
              </Flex>
              <Flex
                mih={50}
                gap="xl"
                justify="space-between"
                align="center"
                direction="row"
                wrap="wrap"
              >
                {t("CreateSubject.degree")}
                <Button
                  className={classes.departmentDropdown}
                  onClick={() => setOpenedDegreeModal(true)}
                >
                  {t("CreateSubject.addDegree")}
                </Button>
              </Flex>
              <Flex
                mih={50}
                gap="xl"
                justify="space-between"
                align="center"
                direction="row"
                wrap="wrap"
              >
                <div>
                  {t("CreateSubject.prerequisites")}
                  <h6 className={classes.optional}>
                    ({t("CreateSubject.optional")})
                  </h6>
                </div>
                <Combobox
                  store={comboboxPrerequisiste}
                  width={300}
                  onOptionSubmit={(value) => {
                    setPrerequisite(value);
                    comboboxPrerequisiste.closeDropdown();
                  }}
                >
                  <Combobox.Target>
                    <InputBase
                      className={classes.departmentDropdown}
                      component="button"
                      type="button"
                      pointer
                      rightSection={<Combobox.Chevron />}
                      rightSectionPointerEvents="none"
                      onClick={() => comboboxPrerequisiste.toggleDropdown()}
                    >
                      {prerequisite}
                    </InputBase>
                  </Combobox.Target>
                  <Combobox.Dropdown>{prerequisiteOptions}</Combobox.Dropdown>
                </Combobox>
              </Flex>
              <Flex
                mih={50}
                gap="xl"
                justify="space-between"
                align="center"
                direction="row"
                wrap="wrap"
              >
                {t("CreateSubject.professor")}
                <Combobox
                  store={comboboxProfessor}
                  width={300}
                  onOptionSubmit={(value) => {
                    setProfessor(value);
                    comboboxProfessor.closeDropdown();
                  }}
                >
                  <Combobox.Target>
                    <InputBase
                      className={classes.departmentDropdown}
                      component="button"
                      type="button"
                      pointer
                      rightSection={<Combobox.Chevron />}
                      rightSectionPointerEvents="none"
                      onClick={() => comboboxProfessor.toggleDropdown()}
                    >
                      {professor}
                    </InputBase>
                  </Combobox.Target>
                  <Combobox.Dropdown>{professorsOptions}</Combobox.Dropdown>
                </Combobox>
              </Flex>
              <Flex
                mih={50}
                gap="xl"
                justify="right"
                align="center"
                direction="row"
                wrap="wrap"
              >
                <Button onClick={() => setOpenedProfessorModal(true)}>
                  {t("CreateSubject.createProfessor")}
                </Button>
              </Flex>
              <Flex
                mih={50}
                gap="xl"
                justify="center"
                align="center"
                direction="row"
                wrap="wrap"
              >
                <Button
                  variant="default"
                  onClick={() => setActiveTab("classes")}
                >
                  {t("CreateSubject.next")}
                </Button>
              </Flex>
            </Tabs.Panel>
            <Tabs.Panel value="classes">
              <Flex
                mih={50}
                gap="xl"
                justify="space-between"
                align="center"
                direction="row"
                wrap="wrap"
              >
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
                <Table.Tbody></Table.Tbody>
              </Table>
              <Flex
                mih={50}
                gap="xl"
                justify="center"
                align="center"
                direction="row"
                wrap="wrap"
              >
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
