import {t} from "i18next";
import "../../common/table-style.css";
import {SelectedSubject} from "../../models/SelectedSubject.ts";

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
    for(let i= 0; i<subjectClasses.length; i++){
        for(let j=0; j<subjectClasses[i].selectedClass.locations.length; j++){
            if(colIdx + 1 == subjectClasses[i].selectedClass.locations[j].day){
                const startTimeIdx = getRowIndex(subjectClasses[i].selectedClass.locations[j].startTime);
                const endTimeIdx = getRowIndex(subjectClasses[i].selectedClass.locations[j].endTime);
                if(rowIdx == startTimeIdx){
                    return [i,j];
                }
                else if(startTimeIdx < rowIdx && rowIdx < endTimeIdx){
                    return [HIDDEN_CELL, HIDDEN_CELL];
                }
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
    const timeSlots = generateTimeSlots(rows);

    const colors = [
        '#fda4a5', '#fdba74' ,'#fce046', '#bff265',
        '#86eead', '#5febd4', '#7dd2fd', '#a4b5fc'
    ]
    let currentColor = 1;
    const colorMap = new Map<string, string>();

    const getColor = (subjectId: string) => {
        if(colorMap.has(subjectId)){
            return colorMap.get(subjectId);
        }
        currentColor = (currentColor + 1) % colors.length;
        colorMap.set(subjectId, colors[currentColor]);
        return colors[currentColor];
    };

    const renderTableCells = (rowIdx: number, colIdx: number) => {
        const [i,j] = findEventByIdx(rowIdx, colIdx, subjectClasses);

        if(i>=0){
            const cellColor = getColor(subjectClasses[i].subject.id);
            return (
                <td rowSpan={calcRowSpan(subjectClasses[i].selectedClass.locations[j].startTime, subjectClasses[i].selectedClass.locations[j].endTime)} style={{backgroundColor: cellColor}}>
                    <div style={{textAlign: "center", fontWeight: "bold", display: "flex", justifyContent: "center"}}>
                        <h4 style={{maxWidth: "6rem", padding: 0, margin: 0}}>
                            {subjectClasses[i].subject.name}
                        </h4>
                    </div>
                    <div style={{textAlign: "center"}}>{subjectClasses[i].selectedClass.locations[j].location}</div>
                    <div style={{textAlign: "center"}}>{subjectClasses[i].selectedClass.locations[j].mode}</div>
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