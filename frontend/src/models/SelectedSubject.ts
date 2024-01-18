import {Subject} from "./Subject.ts";
import Class from "./Class.ts";


export interface SelectedSubject {
    subject: Subject,
    selectedClass: Class
}

export function subjectToSelectedSubject(subject: Subject, c: Class): SelectedSubject {
    return {
        subject: subject,
        selectedClass: c
    }
}

export function selectedSubjectToSubject(selected: SelectedSubject): Subject {
    return selected.subject;
}