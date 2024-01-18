import { SubjectService } from "./SubjectService";
import { AxiosService } from "./axios.service";
import {UserService} from "./UserService.ts";

const subjectService = new SubjectService();
const axiosService = new AxiosService();
const userService = new UserService();


export {
    subjectService,
    axiosService,
    userService
};