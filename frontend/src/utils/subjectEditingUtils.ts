// Week day and time formats

const SUBJECT_ID_REGEX = "[0-9][0-9]\\.[0-9][0-9]";


export function extractNumberFromSemesterName(semesterName: string) {
    return Number(semesterName.match(RegExp('[0-9][0-9]?'))) != 0 ? Number(semesterName.match(RegExp('[0-9][0-9]?'))) : -1
}
export function extractHoursFromTimeStamp(timestamp: string) {
    return Number(timestamp.slice(0,2));
}

export function timeStringToMinutes(time: string): number {
    const [hours, minutes] = time.split(':').map(Number);
    return hours * 60 + minutes;
}

export function calculateHoursDifference(startTime: string, endTime: string): number {
    const minutes1 = timeStringToMinutes(startTime);
    const minutes2 = timeStringToMinutes(endTime);
    return Math.abs(minutes2 - minutes1) / 60;
}

export function validSubjectIdPattern(subjectId: string){
    if(subjectId.length !== 5){
        return false;
    }
    return subjectId.match(SUBJECT_ID_REGEX);
}