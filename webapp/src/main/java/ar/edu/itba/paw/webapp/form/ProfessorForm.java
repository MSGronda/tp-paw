package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Size;

public class ProfessorForm {
    @Size(min=1, max=100)
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
