import {t} from "i18next";
import "../../common/table-style.css";
import {useState} from "react";
interface WeeklyScheduleProps {
    rows: number;
    cols: number;
}

export default function WeeklySchedule(props: WeeklyScheduleProps) {
    const {rows, cols} = props;

    const colors = [
        '#fda4a5', '#fdba74' ,'#fce046', '#bff265',
        '#86eead', '#5febd4', '#7dd2fd', '#a4b5fc'
    ]

    const [currentColor, setCurrentColor] = useState(0);
    const [schedule, setSchedule] = useState<string[]>(Array.from({ length: rows * cols }, () => ""));

    const updateScheduleByIdx = (index: number, newContent: string) => {
        setSchedule((prevElements) => {
            const newElements = [...prevElements];
            newElements[index] = newContent;
            return newElements;
        });
    };

    return (
        <table>
            <thead>
            <tr>
                <th></th>
                <th>{t("TimeTable.day1")}</th>
                <th>{t("TimeTable.day2")}</th>
                <th>{t("TimeTable.day3")}</th>
                <th>{t("TimeTable.day4")}</th>
                <th>{t("TimeTable.day5")}</th>
                <th>{t("TimeTable.day6")}</th>
            </tr>
            </thead>
            <tbody id="weekly-schedule">
                {}
            </tbody>
        </table>
    )
}