import {axiosService} from "."
import { handleResponse } from "../handlers/responseHandler";


const path = "/degrees"

export class DegreeService {
    
    async getDegrees(subjectId?: string){
        try{
            const config: any = {}; // Add type annotation to config object
            config.params = { // Access 'params' property directly
                subjectId: subjectId
            };
            const res = await axiosService.authAxiosWrapper(axiosService.GET, `${path}`, config);
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }

    async getDegreeById(id: number){
        try{
            const res = await axiosService.authAxiosWrapper(axiosService.GET, `${path}/${id}`, {});
            return handleResponse(res);
        }catch (error: any) {
            return handleResponse(error.response);
        }
    }

    async createDegree(degreeName: string, credits: number) {
        const degreeData = {
            name: degreeName,
            totalCredits: credits
        }
        try {
            const res = await axiosService.authAxiosWrapper(axiosService.POST, `${path}`, {},degreeData);
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }

    async addSemestersToDegree(degreeId: number, semesters: {semesterNumber: number, subjects: string[]}[]) {
        const semesterData = {
            semesters: semesters
        }
        try {
            const res = await axiosService.authAxiosWrapper(axiosService.POST, `${path}/${degreeId}/semesters`, {},semesterData);
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }

    async deleteDegree(degreeId: number) {
        try {
            const res = await axiosService.authAxiosWrapper(axiosService.DELETE, `${path}/${degreeId}`, {});
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }

    async getSemesters(degreeId: number, subjectId?: string) {
        try {
            const config: any = {};
            config.params = {
                subjectId: subjectId
            };
            const res = await axiosService.authAxiosWrapper(axiosService.GET, `${path}/${degreeId}/semesters`, config);
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }

    async getSubjectYear(subjectId: string) {
        try {
            const res = await axiosService.authAxiosWrapper(axiosService.GET, `${path}/${subjectId}/year`, {});
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }

    async getDegreeForSubject(subjectId: string) {
        try {
            const res = await axiosService.authAxiosWrapper(axiosService.GET, `${path}/${subjectId}/degree`, {});
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }
}
