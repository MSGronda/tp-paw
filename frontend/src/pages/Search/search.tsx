import classes from './search.module.css';
import { Navbar } from "../../components/navbar/navbar";
import { useTranslation } from 'react-i18next';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { subjectService } from '../../services';
import { handleService } from '../../handlers/serviceHandler';
import { useEffect, useState } from 'react';


export default function Search() {
    const location = useLocation();
    const searchParams = new URLSearchParams(location.search);
    const query = searchParams.get('q');
    
    const { t } = useTranslation();
    const navigate = useNavigate();
    const [subjects, setSubjects] = useState<any>(null);

    const searchSubjects = async (searchValue: string) => {
        const res = await subjectService.getSubjectsByName(searchValue);
        const data = handleService(res, navigate)
        setSubjects(data);
    }

    useEffect(() => {
        searchSubjects(query ?? '');
    }, [query]);

    useEffect(() => {
        console.log(subjects)
    }, [subjects])

    return (
        <div className={classes.fullsize}>
            <Navbar/>
        </div>
    )
}