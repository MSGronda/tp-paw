import { Avatar, Text, Paper, Button, Divider } from '@mantine/core';
import { Navbar } from "../../components/navbar/navbar";
import { Footer } from "../../components/footer/footer";
import { useTranslation } from "react-i18next";
import classes from './profile.module.css';

export default function Profile() {
    const { t } = useTranslation();
    return (
        <div className={classes.fullsize}>
            <Navbar/>
            <div className={classes.body}>
                <div className={classes.header}>
                    <div className={classes.image_container}>
                        <Avatar
                            src="https://raw.githubusercontent.com/mantinedev/mantine/master/.demo/avatars/avatar-8.png"
                            size={120}
                            radius={120}
                            mx="auto"
                        />
                    </div>
                    <div className={classes.title}>
                        <div className={classes.moderator_tag}>  
                            <Text fz="lg" fw={500} mt="md">
                                {t("Profile.loggeduser")}
                            </Text>
                        </div>
                        <div className={classes.logout_button}>
                            <Button variant="filled" radius="md">{t("Profile.logout")}</Button>
                        </div>
                    </div>   
                </div>
                <Paper radius="md" withBorder p="lg" bg="var(--mantine-color-body)">          
                    <div className={classes.table_area}>
                        <div>
                            <Text>
                                {t("Profile.username")}
                            </Text>
                            <Text>
                                Jane Fingerlicker
                            </Text>
                        </div>
                        <Divider size="sm"/>
                        <div>
                            <Text>
                                {t("Profie.email")}
                            </Text>
                            <Text>
                                mrojaspelliccia@itba.edu.ar
                            </Text>
                        </div>
                        <Divider size="sm"/>
                        <Button>
                            {t("Profile.change_password")}
                        </Button>
                        <Button>
                            {t("Profile.change_degree")}
                        </Button>
                    </div>
                    

                </Paper>
            </div>
            <Footer/>
        </div>
    )
}