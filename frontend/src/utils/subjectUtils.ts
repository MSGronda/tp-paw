export function getSemesterYearFormat(semester: number) {
    const year = Math.floor((semester - 1) / 2) + 1;
    const semesterNumber = ((semester - 1) % 2) + 1;
    return {
      year,
      semester: semesterNumber
    };
}
