import {Badge, Card} from "@mantine/core";
import { Link } from "react-router-dom";
import classes from './past-subject-card.module.css';
import {t} from "i18next";

interface PastSubjectCardProps {
    name: string;
    id: string;
    credits: number;
}

function PastSubjectCard(props: PastSubjectCardProps): JSX.Element {
    const { name, id, credits} = props;

    return (
        <Link to={`/subject/${id}`}>
            <Card className={classes.card_area}>
                <h4 style={{height: "25%", marginTop: "0.7rem"}}>{name} - {id}</h4>
                <Card.Section>
                    <div style={{ borderBottom: '1px solid #ccc', margin: '16px 0' }}></div>
                </Card.Section>
                <div className={classes.badge_row}>
                    <Badge className={classes.badge_row_elem} color={'blue.7'}>{t("SubjectCard.credits", {n: credits})}</Badge>
                </div>
            </Card>
        </Link>
    );
}

export default PastSubjectCard;
