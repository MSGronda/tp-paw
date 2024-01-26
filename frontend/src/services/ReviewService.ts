import {axiosService} from "./index.tsx";
import {handleResponse} from "../handlers/responseHandler.tsx";


const path = "/reviews"

export class ReviewService{

    async getReviewsBySubject(subjectId: string,userId: bigint,page: number, orderBy: string,dir: string){
        try{
            let config: any = {};
            config.params = {
                subjectId: subjectId,
                userId: userId,
                page: page,
                orderBy: orderBy,
                dir: dir
            };
            const res = await axiosService.authAxiosWrapper(axiosService.GET,`${path}`,config);
            return handleResponse(res);
        } catch (error: any){
            return handleResponse(error.response);
        }
    }
}