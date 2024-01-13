import ClassTime from "./ClassTime.ts";

export default interface Class {
    idSubject: string,
    idClass: string,
    professors: string[],
    locations: ClassTime[]
}