import classes from './test.module.css';
import "../../common/i18n/index";
import WeeklySchedule from "../../components/schedule/weekly-schedule.tsx";
import ClassTime from "../../models/ClassTime.ts";

export default function Test() {
    const subjectClasses:[string, ClassTime][] = [
        [
            "Cacona 1",
            {
                day: 1,
                startTime: "8:00",
                endTime: "10:00",
                classNumber: "101F",
                building: "SDF",
                mode: "Presencial"
            }
        ],
        [
            "Cacona 1",
            {
                day: 5,
                startTime: "11:00",
                endTime: "22:00",
                classNumber: "101F",
                building: "SDF",
                mode: "Presencial"
            }
        ]
    ]

    return (
        <div className={classes.fullsize}>
                <div style={{width: 600, height: 900}}>
                    <WeeklySchedule
                        rows={29}
                        cols={7}
                        subjectClasses={subjectClasses}
                    />
                </div>
        </div>
    )
}