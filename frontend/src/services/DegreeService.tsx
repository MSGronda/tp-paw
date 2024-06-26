import {axiosService} from "."
import { handleResponse } from "../handlers/responseHandler";


const path = "/degrees"

export class DegreeService {
    
    async getDegrees(){
        try{
            const res = await axiosService.authAxiosWrapper(axiosService.GET, `${path}`, {});
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