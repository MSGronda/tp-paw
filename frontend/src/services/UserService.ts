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

    getUserData() {
        let userData;
        if (localStorage.getItem("user") != null)
            userData = localStorage.getItem("user");
        else if (sessionStorage.getItem("user") != null)
            userData = sessionStorage.getItem("user");
        if (!userData) {
            return;
        }
        return JSON.parse(userData);
    }

    async getUsersThatReviewedSubject(subjectId: string, page: number){
        try{
            let config: any = {};
            config.params = {
                subjectId: subjectId,
                page: page
            };
            const res = await axiosService.authAxiosWrapper(axiosService.GET,`${path}`,config);
            return handleResponse(res);
        } catch (error: any){
            return handleResponse(error.response);
        }
    }

    async getUserPlan(userId: number) {
        try{
            const res = await axiosService.authAxiosWrapper(axiosService.GET, `${path}/${userId}/plan`, {});
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }
    async setUserSemester(userId: number, subjects: SelectedSubject[]) {
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
            const res = await axiosService.authAxiosWrapper(axiosService.PUT, `${path}/${userId}/plan`, {}, data);
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }
    async completeSemester(userId: number) {
        const semesterData = {
            type: 3,
        }
        try{
            const res = await axiosService.authAxiosWrapper(axiosService.PATCH, `${path}/${userId}/plan`, {}, semesterData);
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }

    async setFinishedSubjects(userId: number, passed: string[], notPassed: string[]) {
        const progressData = {
            newPassedSubjects: passed,
            newNotPassedSubjects: notPassed
        }
        try{
            const res = await axiosService.authAxiosWrapper(axiosService.PATCH, `${path}/${userId}/progress`, {}, progressData);
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }

    async getUserProgress(userId: number) {
        try{
            const res = await axiosService.authAxiosWrapper(axiosService.GET, `${path}/${userId}/progress`, {});
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }

    async getUserById(userId: number) {
        try{
            const res = await axiosService.authAxiosWrapper(axiosService.GET, `${path}/${userId}`, {});
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }
}