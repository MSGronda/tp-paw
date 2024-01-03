package ar.edu.itba.paw.webapp.form;
import ar.edu.itba.paw.services.enums.OperationType;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class EditDegreeSemesterForm {
    @NotNull
    @Valid
    private List<EditSemesterSubjectForm> operations;

    public List<EditSemesterSubjectForm> getOperations() {
        return operations;
    }

    public void setOperations(List<EditSemesterSubjectForm> operations) {
        this.operations = operations;
    }

    public static class EditSemesterSubjectForm {
        @NotNull
        private Integer op;

        @NotNull
        private String subjectId;

        @NotNull
        private Integer semester;

        public OperationType getOperationType(){
            return OperationType.parseType(op);
        }

        public Integer getOp() {
            return op;
        }

        public void setOp(Integer op) {
            this.op = op;
        }

        public String getSubjectId() {
            return subjectId;
        }

        public void setSubjectId(String subjectId) {
            this.subjectId = subjectId;
        }

        public Integer getSemester() {
            return semester;
        }

        public void setSemester(Integer semester) {
            this.semester = semester;
        }
    }
    public List<AbstractMap.SimpleEntry<OperationType, AbstractMap.SimpleEntry<Integer, String>>> getOperationsForEdit(){
        final List<AbstractMap.SimpleEntry<OperationType, AbstractMap.SimpleEntry<Integer, String>>> ops = new ArrayList<>();
        operations.forEach(form -> ops.add(new AbstractMap.SimpleEntry<>(form.getOperationType(), new AbstractMap.SimpleEntry<>(form.getSemester(), form.getSubjectId()))));
        return ops;
    }
}
