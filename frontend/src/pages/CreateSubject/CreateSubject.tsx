import { useTranslation } from "react-i18next";
import { Navbar } from "../../components/navbar/navbar";
import classes from './createsubject.module.css'
import { Button, Combobox, ComboboxOption, ComboboxOptions, Flex, InputBase, Modal, NumberInput, Textarea, useCombobox } from "@mantine/core";
import { useState } from "react";


export function CreateSubject() {
    const { t } = useTranslation();

    const [department, setDepartment] = useState<string>('');
    const [degree, setDegree] = useState<string>('');
    const [semester, setSemester] = useState<string>('');
    const [prerequisite, setPrerequisite] = useState<string>('');
    const [professor, setProfessor] = useState<string>('');
    const [credits, setCredits] = useState<string | number>(3);
    const [openedDegreeModal, setOpenedDegreeModal] = useState(false);
    const [openedProfessorModal, setOpenedProfessorModal] = useState(false);

    const MINIMUM_CREDITS = 1;
    const MAXIMUM_CREDITS = 12;

    const departments = ['Ambiente y Movilidad', 'Ciencias Exactas y Naturales', 'Ciencias de la Vida', 'Economia y Negocios', 'Sistemas Complejos y Energía', 'Sistemas Digitales y Datos'];
    const degrees = ['Ingeniería Informática', 'Ingeniería Mecánica', 'Ingeniería Química', 'Ingeniería Naval', 'Ingeniería Civil', 'Ingeniería Electrónica', 'Ingeniería Industrial', 'Ingeniería en Petróleo']
    const semesters = [];
    for (let i = 1 ; i < 11 ; i++) {
        semesters.push(t("CreateSubject.semesterOption",{number: i}));
    }
    semesters.push(t("CreateSubject.elective"));
    const prerequisites = ['Química','Física I', 'Física II', 'Física III', 'Física IV'];
    const professors = ['Sotuyo Dodero, Juan Martín', 'Roig, Ana María', 'Valles, Santiago', 'Meola, Franco'];


    const deaprtmentOptions = departments.map( (departament) => (
        <Combobox.Option key={departament} value={departament}>
            {departament}
        </Combobox.Option>
    ));
    const degreesOptions = degrees.map( (degree) => (
        <ComboboxOption key={degree} value={degree}>
            {degree}
        </ComboboxOption>
    ));
    const semestersOptions = semesters.map( (semester) => (
        <ComboboxOption key={semester} value={semester}>
            {semester}
        </ComboboxOption>
    ));
    const prerequisiteOptions = prerequisites.map( (prerequisite) => (
        <ComboboxOption key={prerequisite} value={prerequisite}>
            {prerequisite}
        </ComboboxOption>
    ));
    const professorsOptions = professors.map( (professor) => (
        <ComboboxOption key={professor} value={professor}>
            {professor}
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

    return(
    <div className={classes.background}>
        <Modal opened={openedDegreeModal} onClose={() => setOpenedDegreeModal(false)} title={t("CreateSubject.addDegree")}>
                <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
                    {t("CreateSubject.degreeModalDegree")}
                    <Combobox store={comboboxDegree} onOptionSubmit={(value) => {
                        setDegree(value);
                        comboboxDegree.closeDropdown();
                        }}>
                        <Combobox.Target>
                            <InputBase className={classes.degreeDropdown} component="button" type="button" pointer rightSection={<Combobox.Chevron />} rightSectionPointerEvents="none" onClick={() => comboboxDegree.toggleDropdown()}>
                                {degree}
                            </InputBase>
                        </Combobox.Target>
                        <Combobox.Dropdown>
                            <ComboboxOptions>
                                {degreesOptions}
                            </ComboboxOptions>
                        </Combobox.Dropdown>
                    </Combobox>
                </Flex>
                <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
                    {t("CreateSubject.semester")}
                    <Combobox store={comboboxSemester} onOptionSubmit={(value) => {
                        setSemester(value);
                        comboboxSemester.closeDropdown();
                        }}>
                        <Combobox.Target>
                            <InputBase className={classes.degreeDropdown} component="button" type="button" pointer rightSection={<Combobox.Chevron />} rightSectionPointerEvents="none" onClick={() => comboboxSemester.toggleDropdown()}>
                                {semester}
                            </InputBase>
                        </Combobox.Target>
                        <Combobox.Dropdown>
                            <ComboboxOptions>
                                {semestersOptions}
                            </ComboboxOptions>
                        </Combobox.Dropdown>
                    </Combobox>
                </Flex>
                <Flex mih={50} gap="xl" justify="center" align="center" direction="row" wrap="wrap">
                    <Button color="green">{t("CreateSubject.add")}</Button>
                </Flex>
        </Modal>
        <Modal opened={openedProfessorModal} onClose={() => setOpenedProfessorModal(false)} title={t("CreateSubject.createProfessor")}>
            <Flex mih={50} gap="xl" justify="center" align="center" direction="row" wrap="wrap">
                {t("CreateSubject.professorName")}
                <Textarea className={classes.degreeDropdown} autosize />
            </Flex>
            <Flex mih={50} gap="xl" justify="center" align="center" direction="row" wrap="wrap">
                {t("CreateSubject.professorLastName")}
                <Textarea className={classes.degreeDropdown} autosize />
            </Flex>
            <Flex mih={50} gap="xl" justify="right" align="center" direction="row" wrap="wrap">
                <Button color="green">{t("CreateSubject.add")}</Button>
            </Flex>
        </Modal>
        <Navbar/>
        <div className={classes.container50}>
            <form className={classes.form}>
                <h1 className={classes.title}>{t("CreateSubject.title")}</h1>

                <br />
                <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
                    {t("CreateSubject.id")}
                    <Textarea className={classes.departmentDropdown} autosize error={t("CreateSubject.idError")} description={t("CreateSubject.idHelp")}/>
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
                            <InputBase className={classes.departmentDropdown} component="button" type="button" pointer rightSection={<Combobox.Chevron />} rightSectionPointerEvents="none" onClick={() => comboboxDepartment.toggleDropdown()}>
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
                    <Button variant="outline" className={classes.departmentDropdown} onClick={() => setOpenedDegreeModal(true)}>{t("CreateSubject.addDegree")}</Button>
                </Flex>
                <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
                    <div>
                    {t("CreateSubject.prerequisites")}<h6 className={classes.optional}>({t("CreateSubject.optional")})</h6>
                    </div>
                    <Combobox store={comboboxPrerequisiste} width={300} onOptionSubmit={(value) => {
                        setPrerequisite(value);
                        comboboxPrerequisiste.closeDropdown();
                    }}>
                        <Combobox.Target>
                            <InputBase className={classes.departmentDropdown} component="button" type="button" pointer rightSection={<Combobox.Chevron />} rightSectionPointerEvents="none" onClick={() => comboboxPrerequisiste.toggleDropdown()}>
                                {prerequisite}
                            </InputBase>
                        </Combobox.Target>
                        <Combobox.Dropdown>{prerequisiteOptions}</Combobox.Dropdown>
                    </Combobox>
                </Flex>
                <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
                    {t("CreateSubject.professor")}
                    <Combobox store={comboboxProfessor} width={300} onOptionSubmit={(value) => {
                        setProfessor(value);
                        comboboxProfessor.closeDropdown();
                    }}>
                        <Combobox.Target>
                            <InputBase className={classes.departmentDropdown} component="button" type="button" pointer rightSection={<Combobox.Chevron />} rightSectionPointerEvents="none" onClick={() => comboboxProfessor.toggleDropdown()}>
                                {professor}
                            </InputBase>
                        </Combobox.Target>
                        <Combobox.Dropdown>{professorsOptions}</Combobox.Dropdown>
                    </Combobox>
                </Flex>
                <Flex mih={50} gap="xl" justify="right" align="center" direction="row" wrap="wrap">
                    <Button variant="outline" onClick={() => setOpenedProfessorModal(true)}>{t("CreateSubject.createProfessor")}</Button>
                </Flex>
                <Flex mih={50} gap="xl" justify="center" align="center" direction="row" wrap="wrap">
                    <Button color="green">{t("CreateSubject.next")}</Button>
                </Flex>
            </form>
        </div>
    </div>
    );
}