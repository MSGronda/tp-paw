import {axiosService} from "."
import { handleResponse } from "../handlers/responseHandler";


const path = "/professors";

export class ProfessorService {

    async getProfessors() {
        try {
            const res = await axiosService.authAxiosWrapper(axiosService.GET, `${path}`, {});
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }
}