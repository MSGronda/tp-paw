import { useTranslation } from "react-i18next";
import { Navbar } from "../../components/navbar/navbar";
import classes from './createsubject.module.css'
import { Combobox, Flex, InputBase, NumberInput, Textarea, useCombobox } from "@mantine/core";
import { useState } from "react";


export function CreateSubject() {
    const { t } = useTranslation();

    const [department, setDepartment] = useState<string>('');
    const [credits, setCredits] = useState<string | number>(3);

    const MINIMUM_CREDITS = 1;
    const MAXIMUM_CREDITS = 12;

    const departaments = ['Ambiente y Movilidad', 'Ciencias Exactas y Naturales', 'Ciencias de la Vida', 'Economia y Negocios', 'Sistemas Complejos y Energía', 'Sistemas Digitales y Datos'];
    
    const options = departaments.map((departament) => (
        <Combobox.Option key={departament} value={departament}>
            {departament}
        </Combobox.Option>
    ));
    
    const combobox = useCombobox({
        onDropdownClose: () => combobox.resetSelectedOption(),
    });
    return(
    <div className={classes.background}>
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
                    <Combobox store={combobox} width={300} onOptionSubmit={(value) => {
                        setDepartment(value);
                        combobox.closeDropdown();
                    }}>
                        <Combobox.Target>
                            <InputBase className={classes.departmentDropdown} component="button" type="button" pointer rightSection={<Combobox.Chevron />} rightSectionPointerEvents="none" onClick={() => combobox.toggleDropdown()}>
                                {department}
                            </InputBase>
                        </Combobox.Target>
                        <Combobox.Dropdown>{options}</Combobox.Dropdown>
                    </Combobox>
                </Flex>
                <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
                    {t("CreateSubject.credits")}
                    <NumberInput className={classes.departmentDropdown} value={credits} min={MINIMUM_CREDITS} max={MAXIMUM_CREDITS} onChange={setCredits}/>
                </Flex>
            </form>
        </div>
    </div>
    );
}