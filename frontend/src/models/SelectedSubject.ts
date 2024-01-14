import ClassTime from "./ClassTime.ts";
import Subject from "./Subject.ts";


export interface SelectedSubject {
    id: string,
    name: string,
    department: string,
    credits: number,
    className: string,
    times: ClassTime[]
}

export function subjectToSelectedSubject(subject: Subject, className: string): SelectedSubject {
    const c = subject.classes.find((Class) => Class.idClass == className);

    return {
        id: subject.id,
        name: subject.name,
        department: subject.department,
        credits: subject.credits,
        className: className,
        times: c != undefined ? c.locations : []
    }
}
