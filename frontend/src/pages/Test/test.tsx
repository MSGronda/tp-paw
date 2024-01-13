import classes from './test.module.css';
import "../../common/i18n/index";
import WeeklySchedule from "../../components/schedule/weekly-schedule.tsx";

export default function Test() {

    return (
        <div className={classes.fullsize}>
                <div style={{width: 600, height: 900}}>
                    <WeeklySchedule/>
                </div>
        </div>
    )
}