import ClassTime from "./ClassTime.ts";


export default interface SelectedSubject {
    id: string,
    name: string,
    department: string,
    credits: number,
    className: string,
    times: ClassTime[]
}