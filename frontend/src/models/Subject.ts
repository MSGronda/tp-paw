import Class from "./Class.ts";


export default interface Subject {
    id: string,
    name: string,
    department: string,
    credits: number,
    classes: Class[],
    difficulty: string,
    timeDemand: string,
    reviewCount: number,
    prerequisites: string[]
}