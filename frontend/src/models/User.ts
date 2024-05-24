export interface User {
    creditsDone: number,
    id: number,
    username: string,
    email: string,
    profileImage: string,
    roles: string[],
    degreeId: number | undefined,
}
