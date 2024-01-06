package ar.edu.itba.paw.webapp.form;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

public class SubjectClassForm {
    @NotNull
    @Pattern(regexp = "[A-Za-z]+")
    private String code;

    @NotNull
    private List<String> professors;

    @NotNull
    @Valid
    private List<SubjectClassTimeForm> classTimes;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getProfessors() {
        return professors;
    }

    public void setProfessors(List<String> professors) {
        this.professors = professors;
    }

    public List<SubjectClassTimeForm> getClassTimes() {
        return classTimes;
    }

    public void setClassTimes(List<SubjectClassTimeForm> classTimes) {
        this.classTimes = classTimes;
    }
}
