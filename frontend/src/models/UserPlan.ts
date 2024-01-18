
interface Entry {
    key: string,
    value: string
}
interface Entries {
    entry: Entry[]
}
export interface UserPlan {
    userId: number,
    classes: Entries,
    planSubjects: string
}