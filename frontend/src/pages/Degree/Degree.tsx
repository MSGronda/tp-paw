import { useTranslation } from "react-i18next";
import { useNavigate, useParams } from "react-router-dom";
import { subjectService } from "../../services";
import { Navbar } from "../../components/navbar/navbar";
import classes from './degree.module.css';
import { useEffect, useState } from "react";
import { handleResponse } from "../../handlers/responseHandler";
import { handleService } from "../../handlers/serviceHandler";


const Degree = () => {
    const {id} = useParams(); 
    const { t } = useTranslation();
    const navigate = useNavigate();
    const [subject, setSubject] = useState<any>(null); // Add this line to define the 'setSubject' state

    const fetchData = async () => {
        const res = await subjectService.getSubjectById('31.08');
        const data = handleService(res, navigate);
        setSubject(data);
    };

    useEffect(() => {
        fetchData();
    }, [id]);

    useEffect(() => {
        console.log(subject)
    }, [subject])
    

    return (
        <div className={classes.fullsize}>
            <Navbar/>
        </div>
    )
}

export default Degree;