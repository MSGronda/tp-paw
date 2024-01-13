import classes from './test.module.css';
import "../../common/i18n/index";
import WeeklySchedule from "../../components/schedule/weekly-schedule.tsx";

export default function Test() {
    const classTimes = [
        {
            day: 1,
            startTime: "8:00",
            endTime: "10:00",
            classNumber: "101F",
            building: "SDF",
            mode: "Presencial"
        },
        {
            day: 5,
            startTime: "11:00",
            endTime: "22:00",
            classNumber: "101F",
            building: "SDF",
            mode: "Presencial"
        }
    ];
    const subjectNames = [
        "Cacona 1",
        "Cacona 1",
    ]

    return (
        <div className={classes.fullsize}>
                <div style={{width: 600, height: 900}}>
                    <WeeklySchedule
                        rows={29}
                        cols={7}
                        classTimes={classTimes}
                        subjectNames={subjectNames}
                    />
                </div>
        </div>
    )
}