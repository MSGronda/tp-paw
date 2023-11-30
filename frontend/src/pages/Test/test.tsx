import classes from './test.module.css';
import "../../common/i18n/index"
import SubjectCard from "../../components/subject-card/subject-card.tsx";



export default function Test() {
    return (
        <div className={classes.fullsize}>
            <SubjectCard
                name="Fisica 99"
                id="11.11"
                credits={6}
                prerequisites={["Arqui", "Fisica 65"]}
                progress={"DONE"}
                difficulty={"EASY"}
                timeDemand={"MEDIUM"}
                numReviews={5}
            />
        </div>
    )
}