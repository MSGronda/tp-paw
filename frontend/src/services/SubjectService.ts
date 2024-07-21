import {axiosService} from "."
import { handleResponse } from "../handlers/responseHandler";
import {Subject} from "../models/Subject.ts";
import {List} from "postcss/lib/list";
import Class from "../models/Class.ts";

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

    async search(
      query?: string,
      page?: number,
      minCredits?: number,
      maxCredits?: number,
      department?: string,
      minDifficulty?: number,
      maxDifficulty?: number,
      minTimeDemand?: number,
      maxTimeDemand?: number,
      orderBy?: string,
      dir?: string,
      degree?: number
    ){
        try{
            const config: any = {}; // Add type annotation to config object
            config.params = { // Access 'params' property directly
                q: query,
                page,
                minCredits,
                maxCredits,
                department,
                minDifficulty,
                maxDifficulty,
                minTimeDemand,
                maxTimeDemand,
                orderBy,
                dir,
                degree
            };
            const res = await axiosService.authAxiosWrapper(axiosService.GET, path, config);
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }

    async getSubjectsByIds(ids: string[], page: number) {
        try{
            let config: any = {};
            config.params = {
                ids: ids.join(","),
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

    async getFutureSubjects(userId: number, page: number = 1) {
        const config: any = {};
        config.params = {
            future: userId,
            page: page
        }
        return this.getUserSubject(config);
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

    async getUnlockableSubjects(userId: number, page: number = 1) {
        const config: any = {};
        config.params = {
            unLockable: userId,
            page: page
        }
        return this.getUserSubject(config);
    }

    async getUserPlanSubjects(userId: number, dateFinished?: number) {
        const config: any = {};
        config.params = {
            plan: userId,
            planFinishedDate: dateFinished == undefined ? null : dateFinished
        }
        return this.getUserSubject(config);
    }

    async getSubjects(page: number) {
        try{
            const config: any = {};
            config.params = {
                page: page
            }
            const res = await axiosService.authAxiosWrapper(axiosService.GET, `${path}`, config);
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }
    async getSubjectsGroupedBySemester(degreeId: number): Promise<Record<number,Subject[]>> {
        const res = await axiosService.authAxiosWrapper(axiosService.GET, `/subjects?degree=${degreeId}`);
        if (!res || res.status !== 200) {
            throw new Error("Unable to get subjects")
        }

        const bySemester: Record<number, Subject[]> = {};
        res.data.subjects.forEach((subject: Subject) => {
            if(!subject.semester) return;

            if (!bySemester[subject.semester]) {
                bySemester[subject.semester] = [];
            }

            bySemester[subject.semester].push(subject);
        });

        return bySemester;
    }
    
    async getSemesterSubjects(degreeId: number, semester: number): Promise<Subject[]> {
        const config = {
            params: {
                degree: degreeId,
                semester: semester
            }
        }
        const res = await axiosService.authAxiosWrapper(axiosService.GET, path, config);
        if (!res || res.status !== 200) {
            throw new Error("Unable to get subjects")
        }

        return res.data.subjects;
    }
    
    async getSubjectFiltersForDegree(degreeId: number): Promise<Record<string, [string]>> {
        const config = {
            params: {
                q: "",
                degree: degreeId,
                page: 1
            }
        };
        const res = await axiosService.authAxiosWrapper(axiosService.GET, path, config);
        
        if(!res || res.status !== 200) {
            throw new Error("Unable to get filters")
        }
        
        if(!res.data.filters) return {};
        
        const filters: Record<string, [string]> = {};
        res.data.filters.entry.forEach((entry: Record<string,any>) => {
            filters[entry.key.toLowerCase()] = entry.value;
        });
        
        return filters;
    }

    async createSubject(id: string, name: string, department: string, credits: number, degreeIds: number[], semesters: number[],
                        requirementsIds: string[], professors: string[], subjectClasses: Class[]) {
        try {
            const data = {
                id: id,
                name: name,
                department: department,
                credits: credits,
                degreeIds: degreeIds,
                semesters: semesters,
                requirementIds: requirementsIds,
                professors: professors,
                subjectClasses: subjectClasses
            }
            const res = await axiosService.authAxiosWrapper(axiosService.POST, `${path}`, {}, data);
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }

    async editSubject(id: string, name: string, department: string, credits: number, degreeIds: number[], semesters: number[],
                      requirementsIds: string[], professors: string[], subjectClasses: Class[]) {
        try {
            const data = {
                id: id,
                name: name,
                department: department,
                credits: credits,
                degreeIds: degreeIds,
                semesters: semesters,
                requirementIds: requirementsIds,
                professors: professors,
                subjectClasses: subjectClasses
            }
            const res = await axiosService.authAxiosWrapper(axiosService.PUT, `${path}/${id}`, {}, data);
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }
}
