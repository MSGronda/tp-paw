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
}