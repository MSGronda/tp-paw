package ar.edu.itba.paw.webapp.dto;

import java.util.List;
import java.util.Map;

public class SubjectsFiltersDto {
    private List<SubjectDto> subjects;
    private Map<String, List<String>> filters;

    public SubjectsFiltersDto() {
    }
    public SubjectsFiltersDto(List<SubjectDto> subjects, Map<String, List<String>> filters) {
        this.subjects = subjects;
        this.filters = filters;
    }

    public List<SubjectDto> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectDto> subjects) {
        this.subjects = subjects;
    }

    public Map<String, List<String>> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, List<String>> filters) {
        this.filters = filters;
    }
}
