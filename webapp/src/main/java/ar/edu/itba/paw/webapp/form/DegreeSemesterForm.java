package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DegreeSemesterForm {

    @NotNull
    private List<SemesterSubForm> semesters;

    public List<SemesterSubForm> getSemesters() {
        return semesters;
    }

    public void setSemesters(List<SemesterSubForm> semesters) {
        this.semesters = semesters;
    }

    public Map<Integer, List<String>> getSemesterMap(){
        final Map<Integer, List<String>> semesterMap = new HashMap<>();
        for(final SemesterSubForm semesterSubForm : this.semesters) {
            semesterMap.put(semesterSubForm.semesterNumber, semesterSubForm.subjects);
        }

        return semesterMap;
    }


    // Nuestra idea original era usar un Map<Integer, List<String>> pero al parecer
    // no esta tan simple al poder usarse keys dinamicas o algo asi.
    // No nos gusta tener que implementar una subclase pero es la unica opcion que encontramos
    // y no vale la pena seguir probando (estuve como 3 hs probando distintas cosas)

    public static class SemesterSubForm {
        private Integer semesterNumber;
        private List<String> subjects;

        public int getSemesterNumber() {
            return semesterNumber;
        }

        public void setSemesterNumber(int semesterNumber) {
            this.semesterNumber = semesterNumber;
        }

        public List<String> getSubjects() {
            return subjects;
        }

        public void setSubjects(List<String> subjects) {
            this.subjects = subjects;
        }
    }
}


