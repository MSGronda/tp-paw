import {axiosService} from "./index.tsx";

const PATH = "/departments";

export class DepartmentService {
  
  async getDepartments(): Promise<string[]> {
    const res = await axiosService.authAxiosWrapper(axiosService.GET, PATH);
    if(!res) throw new Error("Unable to get departments");
    if(res.status == 204) return [];
    
    const data: any[] = res.data;
    
    return data.map((department: any) => department.name);
  }
}
