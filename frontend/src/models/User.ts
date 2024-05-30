export interface User {
    creditsDone: number,
    id: number,
    username: string,
    email: string,
    image: string,
    roles: string[],
    degreeId: number | undefined,
}
