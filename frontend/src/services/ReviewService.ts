import {axiosService} from "./index.tsx";
import {handleResponse} from "../handlers/responseHandler.tsx";


const path = "/reviews"

export class ReviewService{

    async getReviewsBySubject(subjectId: string,page: number, orderBy: string,dir: string){
        try{
            let config: any = {};
            config.params = {
                subjectId: subjectId,
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

    async getReviewFromSubjectAndUser(subjectId: string, userId: number){
        try{
            let config: any = {};
            config.params = {
                subjectId: subjectId,
                userId: userId
            };
            const res = await axiosService.authAxiosWrapper(axiosService.GET,`${path}`,config);
            return handleResponse(res);
        } catch (error: any){
            return handleResponse(error.response);
        }
    }

    async publishReview(subjectId: string, review: string, difficulty: number, timeDemand: number, anonymous: boolean){
        try{
            const data = {
                subjectId: subjectId,
                text: review,
                difficulty: difficulty,
                timeDemanding: timeDemand,
                anonymous: anonymous
            }
            const res = await axiosService.authAxiosWrapper(axiosService.POST, `${path}`, {}, data);
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }

    async deleteReview(reviewId: number){
        try{
            const res = await axiosService.authAxiosWrapper(axiosService.DELETE, `${path}/${reviewId}`);
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }
}