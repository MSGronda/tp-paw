import {axiosService} from "."
import { handleResponse } from "../handlers/responseHandler";
import {Subject} from "../models/Subject.ts";
import Class from "../models/Class.ts";
import {UserPlan} from "../models/UserPlan.ts";
import {handlePagedService} from "../handlers/serviceHandler.tsx";

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

    async getAllUserPlanSubjects(plan: UserPlan[]){
        const resp: Map<number, Subject[]> = new Map();

        const results = await Promise.all(plan.map(async (p: UserPlan): Promise<[number, Subject[]]> => {
            const res = await this.getUserPlanSubjects(p.userId, p.dateFinished);

            if(res.status == 200){
                return [p.dateFinished, res.data];
            }
            return [p.dateFinished, []]
        }));

        results.forEach(([dateFinished, subjects]) => {
            if (resp.has(dateFinished)) {
                resp.get(dateFinished)?.push(...subjects);
            } else {
                resp.set(dateFinished, subjects);
            }
        });

        return resp;
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
        async function request(degree: number, page: number) {
            const res = await axiosService.authAxiosWrapper(
              axiosService.GET, 
              `${path}?degree=${degree}&page=${page}`
            );
            
            const handled = handleResponse(res);
            
            if(handled.failure) {
                throw new Error("Unable to get subjects");
            }
            
            return {
                subjects: handled.data as Subject[],
                page,
                maxPage: handled.maxPage!
            }
        }
        
        let subjects: Subject[] = [];
        const first = await request(degreeId, 1);
        subjects = subjects.concat(first.subjects);
        
        const promises = [];
        for(let i = 2; i <= first.maxPage; i++) {
            promises.push(request(degreeId, i));
        }
        
        const results = await Promise.all(promises);
        results.forEach((result) => {
            subjects = subjects.concat(result.subjects);
        });

        const bySemester: Record<number, Subject[]> = {};
        subjects.forEach((subject: Subject) => {
            if(!subject.semester) return;

            if (!bySemester[subject.semester]) {
                bySemester[subject.semester] = [];
            }

            bySemester[subject.semester].push(subject);
        });

        return bySemester;
    }
    
    async getSemesterSubjects(degreeId: number, semester: number, page: number = 1)
      : Promise<{subjects: Subject[], lastPage?: number}> {
        const config = {
            params: {
                degree: degreeId,
                semester,
                page
            }
        }
        const res = await axiosService.authAxiosWrapper(axiosService.GET, path, config);
        const handledRes = handleResponse(res);
        
        if (handledRes.failure) {
            throw new Error("Unable to get subjects")
        }

        return { subjects: handledRes.data, lastPage: handledRes.maxPage } ;
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

    async deleteSubject(subjectId: string) {
        try {
            const res = await axiosService.authAxiosWrapper(axiosService.DELETE, `${path}/${subjectId}`);
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }
}
