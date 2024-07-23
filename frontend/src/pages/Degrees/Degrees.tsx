import classes from './degrees.module.css';
import { Navbar } from "../../components/navbar/navbar";
import { Footer } from "../../components/footer/footer";
import { useTranslation } from "react-i18next";
import {degreeService, userService} from "../../services";
import { useEffect, useState } from 'react';
import { handleService } from '../../handlers/serviceHandler';
import { useNavigate } from 'react-router-dom';
import {ActionIcon, Button, Card, Center, Loader, Tooltip} from '@mantine/core';
import {IconTrash} from "@tabler/icons-react";
import Title from '../../components/title/title';
import {Degree} from "../../models/Degree.ts";


export default function Degrees() {
    const { t } = useTranslation();
    const navigate = useNavigate();

    const [degrees, setDegrees] = useState<Degree[]>([]);
    const [loading, setLoading] = useState(true);
    const [openedTooltips, setOpenedTooltips] = useState<{ [key: number]: boolean}>({});

    const searchDegrees = async () => {
        const res = await degreeService.getDegrees();
        const data = handleService(res, navigate);
        if (res) {
            setDegrees(data);
        }
        setLoading(false);
    }

    const handleDeleteDegree = async (id: number) => {
        const res = await degreeService.deleteDegree(id);
        handleService(res, navigate);
        await userService.getAndCacheUser();    // Una solucion fea por si se borra la carrera en la cual estas
        await searchDegrees();
    }

    const toggleTooltip = (id: number) => {
        setOpenedTooltips(prevState => ({
            ...prevState,
            [id]: !prevState[id]
        }));
    }

    useEffect(() => {
        searchDegrees();
    }, [])

    return (
        <div className={classes.fullsize}>
            <Title text={t("Degrees.title")}/>
            <Navbar />
            <div className={classes.container_70}>
                {loading ? <Center flex={1}><Loader size="xl"/></Center> : (
                    <>
                        <h1>{t('Degrees.title')}</h1>
                        {degrees.map((degree) => (
                            <Card
                                key={degree.id}
                                withBorder
                                className={classes.card_basic}
                                onClick={() => navigate("/degree/" + degree.id)}
                            >
                                <div className={classes.card}>
                                    <h3>{degree.name}</h3>
                                    <Tooltip label={
                                        <div className={classes.clickable}>
                                            <Card>
                                                <div className={classes.column_center}>
                                                    <span>{t("Degrees.areYouSure")}</span>
                                                    <div style={{ paddingTop: '1rem'}} className={classes.row}>
                                                        <Button
                                                            style={{ marginRight: '1rem' }}
                                                            onClick={(e) => {
                                                                e.stopPropagation();
                                                                handleDeleteDegree(degree.id);
                                                            }}
                                                            variant='outline'
                                                            color='red'
                                                        >
                                                            {t("Degrees.delete")}
                                                        </Button>
                                                        <Button
                                                            onClick={(e) => {
                                                                e.stopPropagation();
                                                                toggleTooltip(degree.id)
                                                            }}
                                                            variant='outline'
                                                            color='black'
                                                        >
                                                            {t("Degrees.cancel")}
                                                        </Button>
                                                    </div>
                                                </div>
                                            </Card>
                                        </div>
                                    } opened={openedTooltips[degree.id] || false} position='top' color='white' className={classes.outline}>
                                        <ActionIcon
                                            variant="white"
                                            color="red"
                                            onClick={(e) => {
                                                e.stopPropagation();
                                                toggleTooltip(degree.id);
                                            }}
                                        >
                                            <IconTrash />
                                        </ActionIcon>
                                    </Tooltip>
                                </div>
                            </Card>
                        ))}
                    </>
                )}
            </div>
            {/*<Footer />*/}
        </div>
    );

}
