package ar.edu.itba.paw.webapp.dto;


public class DepartmentDto {
    private String name;
    
    public static DepartmentDto fromString(String name) {
        final DepartmentDto res = new DepartmentDto();
        res.name = name;
        return res;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

