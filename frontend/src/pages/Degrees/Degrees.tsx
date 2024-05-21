import classes from './degrees.module.css';
import { Navbar } from "../../components/navbar/navbar";
import { useTranslation } from "react-i18next";
import { degreeService } from "../../services";
import { useEffect, useState } from 'react';
import { handleService } from '../../handlers/serviceHandler';
import { useNavigate } from 'react-router-dom';
import { Card } from '@mantine/core';



export default function Degrees() {
    const { t } = useTranslation();
    const navigate = useNavigate();

    const [degrees, setDegrees] = useState<any>([]);
    const [loading, setLoading] = useState(true);

    const searchDegrees = async () => {
        const res = await degreeService.getDegrees();
        const data = handleService(res, navigate);
        if (res) {
            console.log(data);
            setDegrees(data);
        }
        setLoading(false);
        console.log(degrees);
    }

    useEffect(() => {
        searchDegrees();
    }, [])

    return (
        <div className={classes.fullsize}>
            <Navbar />
            <div className={classes.container_70}>
                {!loading &&
                    <>
                        <h1>{t('Degrees.title')}</h1>
                        {degrees.map((degree: any) => (
                            <Card key={degree.id} className={classes.card_basic} onClick={() => navigate("/degree/" + degree.id)} >
                                <h3>{degree.name}</h3>
                            </Card>
                        ))}
                    </>
                }
            </div>
        </div>

    )
}