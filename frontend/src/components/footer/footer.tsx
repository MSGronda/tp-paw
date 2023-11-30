import { Divider, Space } from "@mantine/core";
import { useTranslation } from "react-i18next";
import classes from './footer.module.css';




export function Footer() {
    const { t } = useTranslation();
    return (
        <footer className={classes.footer}>
            <div className={classes.footer_area}>
                <div className={classes.copyright_area}>
                    <div className={classes.content_row}>
                        <h4 className={classes.text_type}>UNI</h4>
                        <Space w="md"/>
                        <p className={classes.text_type}>{t('Footer.mission')}</p>
                    </div>
                    <Divider size="xs"/>
                    <h6 className={classes.text_type}>{t('Footer.rights')}</h6>
                </div>
            </div>
        </footer>
    )
}