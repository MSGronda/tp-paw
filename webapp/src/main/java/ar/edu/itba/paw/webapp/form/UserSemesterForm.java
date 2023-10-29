package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;
import java.util.List;

public class UserSemesterForm {

    @NotNull
    private List<String> idSub;

    @NotNull
    private List<String> idClass;

    public List<String> getIdSub() {
        return idSub;
    }

    public void setIdSub(List<String> idSub) {
        this.idSub = idSub;
    }

    public List<String> getIdClass() {
        return idClass;
    }

    public void setIdClass(List<String> idClass) {
        this.idClass = idClass;
    }
}
