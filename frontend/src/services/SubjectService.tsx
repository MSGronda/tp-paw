import {axiosService} from "."
import { handleResponse } from "../handlers/responseHandler";

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
            return handleResponse(res);
        } catch (error: any) {
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

    async getAvailableSubjects(userId: number) {
        const config: any = {};
        config.params = {
            available: userId
        }
        return this.getUserSubject(config);
    }

    async getDoneSubjects(userId: number) {
        const config: any = {};
        config.params = {
            done: userId
        }
        return this.getUserSubject(config);
    }

    async getUnlockableSubjects(userId: number) {
        const config: any = {};
        config.params = {
            unLockable: userId
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
}