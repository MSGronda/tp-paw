package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.enums.Difficulty;
import ar.edu.itba.paw.services.enums.UserSemesterEditType;

import javax.validation.constraints.NotNull;
import java.util.List;

public class UserSemesterEditForm {
    @NotNull
    private Long type;

    private List<String> subjectIds;

    private List<String> classIds;

    private List<String> passedSubjectIds;

    public UserSemesterEditType getEditType() {
        return UserSemesterEditType.parse(type);
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public List<String> getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(List<String> subjectIds) {
        this.subjectIds = subjectIds;
    }

    public List<String> getClassIds() {
        return classIds;
    }

    public void setClassIds(List<String> classIds) {
        this.classIds = classIds;
    }

    public List<String> getPassedSubjectIds() {
        return passedSubjectIds;
    }

    public void setPassedSubjectIds(List<String> passedSubjectIds) {
        this.passedSubjectIds = passedSubjectIds;
    }
}
