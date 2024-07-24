import {Subject} from "../../models/Subject.ts";
import classes from "../semester-card/semester-card.module.css";
import {Badge, Card} from "@mantine/core";
import {t} from "i18next";
import {Link} from "react-router-dom";

interface SemesterCardProps{
    dateFinished: number | undefined,
    subjects: Subject[]
    index: number
    totalSemester: number
}

export function SemesterCard(props: SemesterCardProps): JSX.Element {
    const {dateFinished, subjects, index, totalSemester} = props;
    const d = new Date(0);
    if(dateFinished){
        d.setUTCSeconds(dateFinished / 1000);
    }

    const credits = subjects.reduce((accumulator, subject) => accumulator + subject.credits, 0);

    return (
    <div className={classes.card_area}>
        <Card className={classes.card}>
            <h4 style={{height: "15%", margin: 0}}>
                {dateFinished == undefined ?
                    t("UserSemesters.cardTitleCurrent") :
                    t("UserSemesters.cardTitle", {n: totalSemester - index})
                }
            </h4>
            <div>
                {
                    dateFinished == undefined ?
                        <Badge className={classes.badge_row_elem} color={'green.7'}>{t("UserSemesters.active")}</Badge>
                        :
                        <Badge className={classes.badge_row_elem} color={'orange.7'}>{t("UserSemesters.cardTitle", {n: d.toDateString()})}</Badge>
                }
                <Badge className={classes.badge_row_elem} color={'blue.7'}>{t("UserSemesters.credits", {n: credits})}</Badge>
            </div>

            <Card.Section>
                <div style={{ borderBottom: '1px solid #ccc', margin: '16px 0' }}></div>
            </Card.Section>

            <ul className={classes.subject_list}>
                {
                    subjects.map((subject) =>
                        <Link to={"/subject/" + subject.id}>
                            <li key={subject.id}>
                                <span style={{fontWeight: 500}}>{subject.name}</span> - {subject.id}
                            </li>
                        </Link>
                    )
                }
            </ul>

        </Card>
    </div>);
}