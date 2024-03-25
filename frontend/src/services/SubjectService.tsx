import {axiosService} from "."
import { handleResponse } from "../handlers/responseHandler";
import {Subject} from "../models/Subject.ts";

const path = "/subjects"

export class SubjectService {

    async getSubjectById(id: string){
        try{
            const res = await axiosService.authAxiosWrapper(axiosService.GET, `${path}/${id}`, {});
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }

    async getSubjectsByName(name: string, page: number, credits: number | null, department: string | null, difficulty: number | null, timeDemand: number | null, orderBy: string | null, dir: string | null){
        try{
            let config: any = {}; // Add type annotation to config object
            config.params = { // Access 'params' property directly
                q: name,
                page: page,
                credits: credits,
                department: department,
                difficulty: difficulty,
                timeDemand: timeDemand,
                orderBy: orderBy,
                dir: dir
            };
            const res = await axiosService.authAxiosWrapper(axiosService.GET, `${path}`, config);
            console.log(res)
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }

    async getSubjectsFromReviews(userId: number, page: number){
        try{
            let config: any = {};
            config.params = {
                reviewed: userId,
                page: page
            };
            const res = await axiosService.authAxiosWrapper(axiosService.GET, `${path}`, config);
            return handleResponse(res);
        } catch (error: any){
            return handleResponse(error.response);
        }
    }

    async getUserSubject(config: any) {
        try{
            const res = await axiosService.authAxiosWrapper(axiosService.GET, `${path}`, config);
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }

    async getAvailableSubjects(userId: number, page: number = 1) {
        const config: any = {};
        config.params = {
            available: userId,
            page: page
        }
        return this.getUserSubject(config);
    }

    async getDoneSubjects(userId: number, page: number = 1) {
        const config: any = {};
        config.params = {
            done: userId,
            page: page,
        }
        return this.getUserSubject(config);
    }

    async getUnlockableSubjects(userId: number) {
        const config: any = {};
        config.params = {
            unLockable: userId,
        }
        return this.getUserSubject(config);
    }

    async getUserPlanSubjects(userId: number) {
        const config: any = {};
        config.params = {
            plan: userId
        }
        return this.getUserSubject(config);
    }

    async getSubjects() {
        try{
            const res = await axiosService.authAxiosWrapper(axiosService.GET, `${path}`, {});
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }
    async getSubjectsBySemester(degreeId: number): Promise<Record<number,Subject[]>> {
        const res = await axiosService.authAxiosWrapper(axiosService.GET, `/subjects?degree=${degreeId}`);
        if (!res || res.status !== 200) {
            throw new Error("Unable to get subjects")
        }

        const bySemester: Record<number, Subject[]> = {};
        res.data.forEach((subject: Subject) => {
            if(!subject.semester) return;

            if (!bySemester[subject.semester]) {
                bySemester[subject.semester] = [];
            }

            bySemester[subject.semester].push(subject);
        });

        return bySemester;
    }
}