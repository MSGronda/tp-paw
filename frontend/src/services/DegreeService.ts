import {axiosService} from "./index.tsx";
import Degree from "../models/Degree.ts";
import {Subject} from "../models/Subject.ts";

export async function getDegrees(): Promise<Degree[]> {
  const res = await axiosService.authAxiosWrapper(axiosService.GET, "/degrees");
  if (!res || res.status !== 200) {
    throw new Error("Unable to get degrees")
  }

  return res.data;
}

export async function getSubjectsBySemester(degreeId: number): Promise<Record<number,Subject[]>> {
  const res = await axiosService.authAxiosWrapper(axiosService.GET, `/subjects?degree=${degreeId}`);
  if (!res || res.status !== 200) {
    throw new Error("Unable to get subjects")
  }
  
  const bySemester: Record<number, Subject[]> = {};
  res.data.forEach((subject: Subject) => {
    if(!subject.semester) return;
    
    if (!bySemester[subject.semester]) {
      bySemester[subject.semester] = [];
    }
    
    bySemester[subject.semester].push(subject);
  });

  return bySemester;
}
