package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;

public class UserSemesterForm {

    @NotNull
    private String idSub;

    @NotNull
    private String idClass;

    public String getIdSub() {
        return idSub;
    }

    public String getIdClass() {
        return idClass;
    }

    public void setIdSub(String idSub) {
        this.idSub = idSub;
    }

    public void setIdClass(String idClass) {
        this.idClass = idClass;
    }
}
