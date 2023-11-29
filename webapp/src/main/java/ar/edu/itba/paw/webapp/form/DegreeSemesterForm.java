package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public class DegreeSemesterForm {

    @NotNull
    private Map<Integer, List<String>> semesters;

    public Map<Integer, List<String>> getSemesters() {
        return semesters;
    }

    public void setSemesters(Map<Integer, List<String>> semesters) {
        this.semesters = semesters;
    }
}
