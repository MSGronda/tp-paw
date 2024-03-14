import { SubjectService } from "./SubjectService";
import { AxiosService } from "./axios.service";
import {UserService} from "./UserService.ts";
import {ReviewService} from "./ReviewService.ts";
import { DegreeService } from "./DegreeService.tsx";

const subjectService = new SubjectService();
const axiosService = new AxiosService();
const userService = new UserService();
const reviewService = new ReviewService();
const degreeService = new DegreeService();


export {
    subjectService,
    axiosService,
    userService,
    reviewService,
    degreeService
};