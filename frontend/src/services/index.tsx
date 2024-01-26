import { SubjectService } from "./SubjectService";
import { AxiosService } from "./axios.service";
import {UserService} from "./UserService.ts";
import {ReviewService} from "./ReviewService.ts";

const subjectService = new SubjectService();
const axiosService = new AxiosService();
const userService = new UserService();
const reviewService = new ReviewService();


export {
    subjectService,
    axiosService,
    userService,
    reviewService
};