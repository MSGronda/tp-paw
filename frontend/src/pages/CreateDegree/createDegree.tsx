import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import classes from './createDegree.module.css';
import {Navbar} from "../../components/navbar/navbar.tsx";
import {Button, Flex, Tabs, Textarea, Modal, MultiSelect, NumberInput, ActionIcon, Table} from "@mantine/core";
import {useEffect, useState} from "react";
import {useDisclosure} from "@mantine/hooks";
import {degreeService, subjectService} from "../../services";
import {handleService} from "../../handlers/serviceHandler.tsx";
import {IconTrash} from "@tabler/icons-react";

export function CreateDegree() {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const [activeTab, setActiveTab] = useState<string | null>("general-info")
    const [appendedElements, setAppendedElements] = useState<JSX.Element[]>([]);
    const [opened, { open, close }] = useDisclosure(false);
    const [searchId, setSearchId] = useState<string>("");
    const [subjects, setSubjects] = useState<any[]>([]);
    const [semesterNum, setSemesterNum] = useState<string | number>('');
    const [selectedSubjects, setSelectedSubjects] = useState<{ semesterNumber: number, subjects: string[] }[]>([]);
    const [subjectId, setSubjectId] = useState([]);
    const [credits, setCredits] = useState<string | number>('');
    const [degreeName, setDegreeName] = useState<string>('');

    const handleSearchValue = (value: string) => {
        setSearchId(value);
        if(value.length > 1 && value.length < 5) {
            getSubjects();
        }

    }
    const OpenModal = () => {
        setSemesterNum('');
        setSubjectId([]);
        open();
    }

    const getSubjects = async() => {
        const res = await subjectService.getSubjectsByName(searchId,1, null, null, null, null, null, null);
        const data = handleService(res, navigate)
        setSubjects(data.subjects);
    }

    const handleDeleteSemester = (semesterNumDelete: Number) => {
        setSelectedSubjects(prevState => prevState.filter(subject => subject.semesterNumber !== semesterNumDelete));
        setAppendedElements(prevState => prevState.filter((element) => Number(element.key) !== semesterNumDelete));
    }

    useEffect(() => {
        const newElements = selectedSubjects.map(subject => (
            <Table.Tr key={subject.semesterNumber}>
                <Table.Td>
                    <span className={classes.tableItem}>
                        {subject.semesterNumber}
                    </span>
                </Table.Td>
                <Table.Td>
                    <ul className={classes.classList}>
                        {subject.subjects.map((subjectId, index) => (
                            <li key={index}>{subjectId}</li>
                        ))}
                    </ul>
                </Table.Td>
                <Table.Td>
                    <span className={classes.tableItem}>
                        <ActionIcon variant="white" color="red" onClick={() => handleDeleteSemester(subject.semesterNumber)}>
                            <IconTrash />
                        </ActionIcon>
                    </span>
                </Table.Td>
            </Table.Tr>
        ));
        setAppendedElements(newElements);
    }, [selectedSubjects]);

    const handleAddSemester = () => {
        const newSelectedSubjects = {semesterNumber: Number(semesterNum), subjects: subjectId};
        if(newSelectedSubjects.subjects.length != 0) {
            setSelectedSubjects(prevState =>[...prevState, newSelectedSubjects]
                .sort((a, b) => a.semesterNumber - b.semesterNumber));
        }
        close();
    }

    const handleCreateSubmit = async (e: { preventDefault: () => void; }) => {
        e.preventDefault();

        if(degreeName === "" || credits === "") {
            return;
        }

        const res1 = await degreeService.createDegree(degreeName, Number(credits));
        console.log(res1)
        if(res1?.data) {
            await degreeService.addSemestersToDegree(res1.data.id, selectedSubjects);
            console.log(res1?.data);
            navigate("/degrees/"+res1.data.id);
        }
    }

    return (
        <div className={classes.background}>
            <Navbar />
            <div className={classes.container50}>
                <h1 className={classes.title}>{t("CreateDegree.title")}</h1>
                <form onSubmit={handleCreateSubmit} className={classes.form}>
                    <Tabs value={activeTab} onChange={(value) => setActiveTab(value)}>
                        <Tabs.List>
                            <Tabs.Tab value={"general-info"}>
                                {t("CreateDegree.generalInfo")}
                            </Tabs.Tab>
                            <Tabs.Tab value={"subjects"}>
                                {t("CreateDegree.subjects")}
                            </Tabs.Tab>
                        </Tabs.List>

                        <Tabs.Panel value="general-info">
                            <br />
                            <Flex
                            justify="space-between"
                            align="center"
                            direction="row"
                            wrap="wrap">
                                {t("CreateDegree.name")}
                                <Textarea
                                    value={degreeName}
                                    onChange={(event) => setDegreeName(event.currentTarget.value)}
                                    autosize
                                />
                            </Flex>
                            <Flex
                                justify="space-between"
                                align="center"
                                direction="row"
                                wrap="wrap"
                                >
                                {t("CreateDegree.credits")}
                                <NumberInput
                                    value={credits}
                                    onChange={setCredits}
                                    min={1}
                                    max={243}
                                    allowNegative={false}
                                    allowDecimal={false}
                                    hideControls
                                />
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
                                    onClick={() => setActiveTab("subjects")}
                                >
                                    {t("CreateSubject.next")}
                                </Button>
                            </Flex>
                        </Tabs.Panel>
                        <Tabs.Panel value="subjects">
                            <Flex
                                mih={50}
                                gap="xl"
                                justify="center"
                                align="center"
                                direction="row"
                                wrap="wrap"
                            >
                                <Modal
                                    opened={opened}
                                    onClose={close}
                                    title={t("CreateDegree.addSemester")}
                                >

                                    <Flex
                                        justify="space-between"
                                        align="center"
                                        direction="row"
                                        wrap="wrap"
                                    >
                                        {t("CreateDegree.semester")}

                                        <NumberInput
                                            value={semesterNum}
                                            onChange={setSemesterNum}
                                            min={1}
                                            max={12}
                                            allowNegative={false}
                                            allowDecimal={false}
                                            hideControls
                                        />

                                    </Flex>
                                    <br/>
                                    <Flex
                                        justify="space-between"
                                        align="center"
                                        direction="row"
                                        wrap="wrap"
                                    >
                                        {t("CreateDegree.subjectNumber")}
                                        <MultiSelect
                                            searchable
                                            searchValue={searchId}
                                            onSearchChange={handleSearchValue}
                                            data=
                                                {subjects != undefined?  subjects.map((subject: { id: string; name: string }) => (
                                                    { value: subject.id, label: subject.id +" - "+ subject.name }
                                                    ))  : []}
                                            value={subjectId}
                                            onChange={setSubjectId}
                                            nothingFoundMessage={t("CreateDegree.nothingFound")}
                                            hidePickedOptions
                                            limit={10}
                                            maxDropdownHeight={200}
                                        />
                                    </Flex>

                                    <Flex
                                        mih={50}
                                        gap="xl"
                                        justify="center"
                                        align="center"
                                        direction="row"
                                        wrap="wrap"
                                    >
                                        <Button onClick={handleAddSemester}>{t("CreateDegree.addSemester")}</Button>
                                    </Flex>
                                </Modal>
                            </Flex>
                            <div className={classes.modalButton}>
                                <Button  onClick={OpenModal}>{t("CreateDegree.addSemester")}</Button>
                            </div>
                            <br />
                            <Table>
                                <Table.Thead>
                                    <Table.Tr>
                                        <Table.Th>
                                            {t("CreateDegree.semester")}
                                        </Table.Th>
                                        <Table.Th>
                                            {t("CreateDegree.subjects")}
                                        </Table.Th>
                                        <Table.Th>

                                        </Table.Th>
                                    </Table.Tr>
                                </Table.Thead>
                                <Table.Tbody>
                                    {appendedElements}
                                </Table.Tbody>
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
                                <Button color="green"
                                    type='submit'
                                >
                                    {t("CreateDegree.createDegree")}
                                </Button>
                            </Flex>
                        </Tabs.Panel>
                    </Tabs>



                </form>

            </div>
        </div>
    )
}