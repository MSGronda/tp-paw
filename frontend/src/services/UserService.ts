import {SelectedSubject} from "../models/SelectedSubject.ts";
import {axiosService} from "./index.tsx";
import {handleResponse} from "../handlers/responseHandler.tsx";
import {User} from "../models/User.ts";
import authService from "./AuthService.ts";

const path = "/users"

export class UserService {

    getUserId() {
        return this.getUserData()?.id;
    }

    getUserData(): User|null {
        let userData;
        if (localStorage.getItem("user") != null)
            userData = localStorage.getItem("user");
        else if (sessionStorage.getItem("user") != null)
            userData = sessionStorage.getItem("user");
        
        return userData ? JSON.parse(userData) : null;
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
    
    async updateCachedUser() {
        let user = await this.getUser();
        if(user.failure) throw new Error("Unable to get user data");
        user = user.data;
        
        if(localStorage.getItem("user"))
            localStorage.setItem("user", JSON.stringify(user));
        else
            sessionStorage.setItem("user", JSON.stringify(user));
    }
    
    async getUser() {
        const userId = this.getUserId();
        return await this.getUserById(userId!);
    }
    
    async setDegreeAndSubjects(degreeId: number, subjectIds: string[]) {
        const userId = this.getUserId();
        const res = await axiosService.authAxiosWrapper(axiosService.PATCH, `${path}/${userId}`, {}, {
            degreeId,
            subjectIds
        });
        
        if(!res || res.status !== 200) {
            throw new Error("Unable to set degree and subjects");
        }
        
        await this.updateCachedUser();
    }
    
    async changePassword(oldPassword: string, newPassword: string) {
        try {
            const body = {
                oldPassword,
                newPassword
            }
            const res = await axiosService.authAxiosWrapper(
              axiosService.PATCH,
              `${path}/${this.getUserId()}`,
              {},
              body
            );
            
            authService.logout();
            window.location.reload();
            
            return handleResponse(res);
        } catch(e: any) {
            return handleResponse(e.response);
        }
    }
    
    async changeDegree(degreeId: number) {
        try {
            // Delete current semester
            const delRes = await axiosService.authAxiosWrapper(
              axiosService.DELETE,
              `${path}/${this.getUserId()}/plan`,
              {}
            );
            
            if(!delRes || delRes.status !== 204) 
                throw new Error();
            
            // Change degree ID
            const body = { degreeId };
            const res = await axiosService.authAxiosWrapper(
              axiosService.PATCH,
              `${path}/${this.getUserId()}`,
              {},
              body
            );
            
            await this.updateCachedUser();
            window.location.reload();
            
            return handleResponse(res);
        } catch (e: any) {
            return handleResponse(e.response);
        }
    }
    
    async changeUsername(userName: string) {
        try {
            const body = { userName };
            const res = await axiosService.authAxiosWrapper(
              axiosService.PATCH,
              `${path}/${this.getUserId()}`,
              {},
              body
            );
            
            await this.updateCachedUser();
            window.location.reload();
            
            return handleResponse(res);
        } catch (e: any) {
            return handleResponse(e.response);
        }
    }
    
    async changePicture(file: File) {
        try {
            const res = await axiosService.authAxiosWrapper(
              axiosService.POST,
              `${path}/${this.getUserId()}/picture`,
              {
                  headers: {
                    'Content-Type': file.name.endsWith(".png") ? "image/png" : "image/jpeg"
                  }
              },
              await file.arrayBuffer()
            );
            
            await this.updateCachedUser();
            window.location.reload();
            
            return handleResponse(res);
        } catch (e: any) {
            return handleResponse(e.response);
        }
    }
}
