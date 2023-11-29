import { axiosService } from "."
import { handleResponse } from "../handlers/responseHandler";


const path = "/subjects"

export class SubjectService {

    async getSubjectById(id: string){
        try{
            let config: Record<string, any> = {}; // Add type annotation for config object
            config.headers = {Authorization: axiosService.getBasicToken("mcasiraghi@itba.edu.ar", "0987654321")};
            const res = await axiosService.axiosWrapper(axiosService.GET, `${path}/${id}`, config);
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }
}