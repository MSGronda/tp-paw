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

export function sortByNameDesc(a: Subject,b: Subject){
    return b.name.localeCompare(a.name);
}
export function sortByNameAsc(a: Subject,b: Subject){
    return a.name.localeCompare(b.name);
}

export function sortByCreditsDesc(a: Subject,b: Subject){
    return b.credits - a.credits;
}
export function sortByCreditsAsc(a: Subject,b: Subject){
    return  a.credits-b.credits;
}
export function sortByDifficultyDesc(a: Subject,b: Subject){
    return  getDifficultyValue(b.difficulty) - getDifficultyValue(a.difficulty);
}
export function sortByDifficultyAsc(a: Subject,b: Subject){
    return  getDifficultyValue(a.difficulty) - getDifficultyValue(b.difficulty);
}
export function sortByTimeDemandDesc(a: Subject,b: Subject){
    return  getTimeDemandValue(b.timeDemand) - getTimeDemandValue(a.timeDemand);
}
export function sortByTimeDemandAsc(a: Subject,b: Subject){
    return  getTimeDemandValue(a.timeDemand) - getTimeDemandValue(b.timeDemand);
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