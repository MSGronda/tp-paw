export interface User {
    id: number,
    username: string,
    email: string,
    image: string,
    roles: string[],
    degreeId: number | undefined,
    creditsDone: number,
    progressByYear: number[]
}
