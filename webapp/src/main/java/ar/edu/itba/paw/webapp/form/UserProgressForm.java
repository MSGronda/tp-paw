package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;
import java.util.List;

public class UserProgressForm {
    @NotNull
    private List<String> newPassedSubjects;

    @NotNull
    private List<String> newNotPassedSubjects;

    public List<String> getNewPassedSubjects() {
        return newPassedSubjects;
    }

    public void setNewPassedSubjects(List<String> newPassedSubjects) {
        this.newPassedSubjects = newPassedSubjects;
    }

    public List<String> getNewNotPassedSubjects() {
        return newNotPassedSubjects;
    }

    public void setNewNotPassedSubjects(List<String> newNotPassedSubjects) {
        this.newNotPassedSubjects = newNotPassedSubjects;
    }
}
