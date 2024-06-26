import {UserPlan} from "../models/UserPlan.ts";
import {Subject} from "../models/Subject.ts";
import {SelectedSubject} from "../models/SelectedSubject.ts";
import Class from "../models/Class.ts";

export function createSelectedSubjects(userPlan: UserPlan, subjects: Subject[]): SelectedSubject[] {
    const selected: SelectedSubject[] = [];
    subjects.forEach((subject) => {

        const subjectClassPair = userPlan.classes.entry.find((t) => t.key == subject.id)

        const idClass = subjectClassPair ? subjectClassPair.value : "";

        const c = subject.classes.find((c) => c.idClass == idClass);
        selected.push({
            subject: subject,
            selectedClass:  c ? c : createEmptySubjectClass(subject) // No deberia ocurrir este caso pero bueno
        })
    })
    return selected;
}

export function createEmptySubjectClass(subject: Subject) : Class {
    return {
        idSubject: subject.id,
        idClass: "",
        professors: [],
        locations: []
    }
}