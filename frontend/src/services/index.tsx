import { SubjectService } from "./SubjectService.ts";
import { AxiosService } from "./axios.service.ts";
import {UserService} from "./UserService.ts";
import {ReviewService} from "./ReviewService.ts";
import { DegreeService } from "./DegreeService.ts";
import {ProfessorService} from "./ProfessorService.ts";
import {DepartmentService} from "./DepartmentService.ts";

const subjectService = new SubjectService();
const axiosService = new AxiosService();
const userService = new UserService();
const reviewService = new ReviewService();
const degreeService = new DegreeService();
const professorService = new ProfessorService();
const departmentService = new DepartmentService();


export {
    subjectService,
    axiosService,
    userService,
    reviewService,
    degreeService,
    professorService,
    departmentService
};
