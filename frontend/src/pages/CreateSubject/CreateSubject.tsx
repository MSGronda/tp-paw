import { useTranslation } from "react-i18next";
import { Navbar } from "../../components/navbar/navbar";
import classes from './createsubject.module.css'
import { Flex, Table, Textarea } from "@mantine/core";


export function CreateSubject() {
    const { t } = useTranslation();

    return(
    <div className={classes.background}>
        <Navbar/>
        <div className={classes.container50}>
            <form className={classes.form}>
                <h1 className={classes.title}>{t("CreateSubject.title")}</h1>

                <br />
                <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
                    {t("CreateSubject.id")}
                    <Textarea autosize description={t("CreateSubject.idHelp")}/>
                </Flex>
                <Flex mih={50} gap="xl" justify="space-between" align="center" direction="row" wrap="wrap">
                    {t("CreateSubject.name")}
                    <Textarea autosize />
                </Flex>
            </form>
        </div>
    </div>
    );
}