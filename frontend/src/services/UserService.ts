import {SelectedSubject} from "../models/SelectedSubject.ts";
import {axiosService} from "./index.tsx";
import {handleResponse} from "../handlers/responseHandler.tsx";

const path = "/users"

export class UserService {

    getUserId() {
        let userData;
        if (localStorage.getItem("user") != null)
            userData = localStorage.getItem("user");
        else if (sessionStorage.getItem("user") != null)
            userData = sessionStorage.getItem("user");
        if (!userData) {
            return;
        }
        return JSON.parse(userData).id;
    }

    async getUserPlan(userId: number) {
        try{
            const res = await axiosService.authAxiosWrapper(axiosService.GET, `${path}/${userId}/plan`, {});
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }
    async setUserSemester(usedId: number, subjects: SelectedSubject[]) {
        const subs: string[] = [];
        const classes: string[] = [];

        subjects.forEach((subject) => {
            subs.push(subject.subject.id);
            classes.push(subject.selectedClass.idClass);
        })
        const data = {
            idSub: subs,
            idClass: classes
        }
        try{
            const res = await axiosService.authAxiosWrapper(axiosService.PUT, `${path}/${usedId}/plan`, {}, data);
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }
}