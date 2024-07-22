import {axiosService} from "."
import { handleResponse } from "../handlers/responseHandler";
import Class from "../models/Class.ts";
import {Subject} from "../models/Subject.ts";
import {Professor} from "../models/Professor.ts";

const path = "/professors";

export class ProfessorService {

    async getProfessors(subjectId?: string, classId?: string, q?: string) {
        try {
            const config: any = {params: {}};
            if(subjectId != undefined){
                config.params.subjectId = subjectId;
            }
            if(classId != undefined){
                config.params.classId = classId;
            }
            if(q != undefined){
                config.params.q = q;
            }

            const res = await axiosService.authAxiosWrapper(axiosService.GET, `${path}`, config);
            return handleResponse(res);
        } catch (error: any) {
            return handleResponse(error.response);
        }
    }

    async getProfessorsForSubject(subject: Subject){
        const resp = new Map<string, Professor[]>();

        // Hacemos los requests en paralelo
        const results = await Promise.all(subject.classes.map(async (subjectClass: Class): Promise<[string, Professor[]]> => {
            const res = await this.getProfessors(subjectClass.idSubject, subjectClass.idClass);
            if(res.status == 200){
                return [subjectClass.idClass, res.data];
            }
            return [subjectClass.idClass, []]
        }));

        results.forEach(([idClass, professors]) => {
            if (resp.has(idClass)) {
                resp.get(idClass)?.push(...professors);
            } else {
                resp.set(idClass, professors);
            }
        });
        return resp;
    }
}