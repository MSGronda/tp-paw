import Class from "./Class.ts";

export function getDifficultyValue(difficulty: string){
    switch (difficulty){
        case 'EASY':
            return 1;
        case 'NORMAL':
            return 2;
        case 'HARD':
            return 3;
        default:
            return 0;
    }
}

export function getTimeDemandValue(timeDemand: string){
    switch (timeDemand){
        case 'LOW':
            return 1;
        case 'MEDIUM':
            return 2;
        case 'HIGH':
            return 3;
        default:
            return 0;
    }
}


export interface Subject {
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