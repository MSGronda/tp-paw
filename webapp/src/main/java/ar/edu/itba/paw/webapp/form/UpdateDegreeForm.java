package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;

public class UpdateDegreeForm {
    @NotNull
    private long degreeId;

    private String subjectIds;

    public long getDegreeId() {
        return degreeId;
    }

    public void setDegreeId(long degreeId) {
        this.degreeId = degreeId;
    }

    public String getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(String subjectIds) {
        this.subjectIds = subjectIds;
    }

}
