import { Avatar, Text, Paper, Button, Divider } from '@mantine/core';
import { Navbar } from "../../components/navbar/navbar";
import { Footer } from "../../components/footer/footer";
import { useTranslation } from "react-i18next";
import classes from './profile.module.css';
import {User} from "../../models/User.ts";
import {useContext, useState} from "react";
import AuthContext from "../../context/AuthContext.tsx";
import {userService} from "../../services";

export default function Profile() {
    const { t } = useTranslation();
    const user = userService.getUserData()

    return (
        <div className={classes.fullsize}>
            <Navbar/>
            <div className={classes.body}>
                <div className={classes.header}>
                    <div className={classes.image_container}>
                        <Avatar
                            src={user.profileImage}
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
                        { user.role === "moderator" &&
                            <div>
                                <Text fz="lg" fw={500} mt="md">
                                    {t("Profile.moderator")}
                                </Text>
                            </div>
                        }
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
                                {user.username}
                            </Text>
                        </div>
                        <Divider size="sm"/>
                        <div>
                            <Text>
                                {t("Profile.email")}
                            </Text>
                            <Text>
                                {user.email}
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
                <br></br>
                <Divider></Divider>
                <br></br>
            </div>
            <Footer/>
        </div>
    )
}