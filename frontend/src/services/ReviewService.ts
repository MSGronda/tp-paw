import {axiosService, reviewService} from "./index.tsx";
import {handleResponse} from "../handlers/responseHandler.tsx";
import {ReviewVote} from "../models/ReviewVote.ts";
import {Review} from "../models/Review.ts";


const path = "/reviews"

export class ReviewService {

    async getReviewsBySubject(subjectId: string, page: number, orderBy: string, dir: string) {
        try {
            let config: any = {};
            config.params = {
                subjectId: subjectId,
                page: page,
            };
            if(orderBy != ""){
                config.params.orderBy = orderBy;
            }
            if(dir != ""){
                config.params.dir = dir;
            }
            const res = await axiosService.authAxiosWrapper(axiosService.GET, `${path}`, config);
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }

    async getReviewFromSubjectAndUser(subjectId: string, userId: number) {
        try {
            let config: any = {};
            config.params = {
                subjectId: subjectId,
                userId: userId
            };
            const res = await axiosService.authAxiosWrapper(axiosService.GET, `${path}`, config);
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }

    async getReviewsFromUser(userId: number, page: number, orderBy: string, dir: string){
        try{
            let config: any = {};
            config.params = {
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

    async publishReview(subjectId: string, review: string, difficulty: number, timeDemand: number, anonymous: boolean) {
        try {
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

    async deleteReview(reviewId: number) {
        try {
            const res = await axiosService.authAxiosWrapper(axiosService.DELETE, `${path}/${reviewId}`);
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }

    async editReview(reviewId: number, review: string, difficulty: number, timeDemand: number, anonymous: boolean, subjectId: string) {
        try {
            const data = {
                text: review,
                difficulty: difficulty,
                timeDemanding: timeDemand,
                anonymous: anonymous,
                subjectId: subjectId
            }
            const res = await axiosService.authAxiosWrapper(axiosService.PUT, `${path}/${reviewId}`, {}, data);
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }

    async voteReview(reviewId: number, vote: number) {
        try {
            const data = {
                vote: vote,
                reviewId: reviewId
            }
            const res = await axiosService.authAxiosWrapper(axiosService.POST, `${path}/${reviewId}/votes`, {}, data);
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }

    async unVoteReview(reviewId: number, userId: number | undefined) {
        try {
            const res = await axiosService.authAxiosWrapper(axiosService.DELETE, `${path}/${reviewId}/votes/${userId}`);
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response)
        }
    }

    async getAllVotes(reviews: Review[], userId: number) {
        const votes = new Map<number, ReviewVote[]>();

        // Hacemos los pedidos en paralelo
        await Promise.all(reviews.map(async (review: Review) => {
            return await this.getReviewVotes(review.id, userId);
        })).then((values) =>
            values.forEach((v) => votes.set(v[0], v[1]))
        );

        return votes;
    }

    private async getReviewVotes (id: number, userId: number): Promise<[number, ReviewVote[]]> {
        let res;
        try {
            const config: any = {};
            config.params = {
                userId: userId
            }
            const initialRes = await axiosService.authAxiosWrapper(axiosService.GET, `${path}/${id}/votes`, config);
            res =  handleResponse(initialRes);
        } catch (error: any) {
            res =  handleResponse(error.response);
        }

        if(!res || res.data == ""){
            return [id, [] as ReviewVote[]];
        }
        return [id, res.data as ReviewVote[]];
    }
}