import Subject from "./Subject.ts";
import Class from "./Class.ts";


export interface SelectedSubject {
    subject: Subject,
    selectedClass: Class
}

export function subjectToSelectedSubject(subject: Subject, className: string): SelectedSubject {
    const c = subject.classes.find((Class) => Class.idClass == className);
    return {
        subject: subject,
        selectedClass: c != undefined ? c :
        // Esto nunca deberia ocurrir
        {
            idSubject: "",
            idClass: "",
            professors: [],
            locations: []
        }
    }
}

export function selectedSubjectToSubject(selected: SelectedSubject): Subject {
    return selected.subject;
}