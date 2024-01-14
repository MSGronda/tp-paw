import {t} from "i18next";
import "../../common/table-style.css";
import SelectedSubject from "../../models/SelectedSubject.ts";

interface WeeklyScheduleProps {
    rows: number;
    cols: number;

    // Hay para cada classTime, hay un subject name
    subjectClasses: SelectedSubject[];
}

const HIDDEN_CELL = -1;
const EMPTY_CELL = -2;

function getRowIndex(time: string){
    if(time === '')
        return -1
    return (parseInt(time.split(":")[0]) - 8)*2 + (parseInt(time.split(":")[1]) === 30 ? 1 : 0);
}
function calcRowSpan(startTime: string, endTime: string): number {
    return getRowIndex(endTime) - getRowIndex(startTime);
}
function findEventByIdx(rowIdx: number, colIdx: number, subjectClasses: SelectedSubject[]): [number, number]{
    for(let i=0; i<subjectClasses.length; i++){
        for(let j=0; j<subjectClasses[i].times.length; j++){
            if(colIdx + 1 == subjectClasses[i].times[j].day){
                const startTimeIdx = getRowIndex(subjectClasses[i].times[j].startTime);
                const endTimeIdx = getRowIndex(subjectClasses[i].times[j].endTime);
                if(rowIdx == startTimeIdx){
                    return [i,j];
                }
                else if(startTimeIdx < rowIdx && rowIdx < endTimeIdx){
                    return [HIDDEN_CELL, HIDDEN_CELL];
                }
                return [EMPTY_CELL, EMPTY_CELL];
            }
        }

    }
    return [EMPTY_CELL, EMPTY_CELL];
}

function generateTimeSlots(rows: number): string[] {
    const resp: string[] = [];
    for(let i=0; i<rows; i++){
        const hours = (8 + Math.floor(i / 2)) + "";
        const minutes = i % 2 === 0 ? "00" : "30";
        resp.push(hours + ":" + minutes);
    }
    return resp;
}

export default function WeeklySchedule(props: WeeklyScheduleProps) {
    const {rows, cols, subjectClasses} = props;
    const colors = [
        '#fda4a5', '#fdba74' ,'#fce046', '#bff265',
        '#86eead', '#5febd4', '#7dd2fd', '#a4b5fc'
    ]
    const timeSlots = generateTimeSlots(rows);
    let currentColor = 1;

    const getNextColor = () => {
        currentColor = (currentColor + 1) % colors.length
        return colors[currentColor];
    };

    const renderTableCells = (rowIdx: number, colIdx: number) => {
        const [i,j] = findEventByIdx(rowIdx, colIdx, subjectClasses);

        if(i>=0){
            const cellColor = getNextColor();
            return (
                <td rowSpan={calcRowSpan(subjectClasses[i].times[j].startTime, subjectClasses[i].times[j].endTime)} style={{backgroundColor: cellColor}}>
                    <div style={{maxWidth: "6rem", textAlign: "center", alignSelf: "center", fontWeight: "bold"}}>
                        {subjectClasses[i].name}
                    </div>
                    <div style={{textAlign: "center"}}>{subjectClasses[i].times[j].classNumber}</div>
                    <div style={{textAlign: "center"}}>{subjectClasses[i].times[j].mode}</div>
                </td>
            );
        }
        else if(i==HIDDEN_CELL){
            return <td style={{display: "none"}}></td>;
        }
        return <td></td>;
    };
    const renderTableRow = (time: string, rowIdx: number) => {
        return (
            <tr>
                <th>{time}</th>
                {Array.from({ length: cols - 1 }, (_, colIdx) => renderTableCells(rowIdx, colIdx))}
            </tr>
        );
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
            <tbody>
                {timeSlots.map((time, index)=> renderTableRow(time, index))}
            </tbody>
        </table>
    )
}