package ar.edu.itba.paw.models.utils;

public class DegreeSearchParams {
    private final String subjectId;

    public DegreeSearchParams(final String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public boolean hasSubject() {
        return subjectId != null;
    }
}
