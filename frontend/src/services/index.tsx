import { SubjectService } from "./SubjectService";
import { AxiosService } from "./axios.service";

const subjectService = new SubjectService();
const axiosService = new AxiosService();


export {
    subjectService,
    axiosService
};