package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class SubjectProgressForm {
    @NotNull
    @Min(0)
    @Max(1)
    private int progress;

    @NotNull
    private String idSub;

    public String getIdSub() {
        return idSub;
    }

    public int getProgress() {
        return progress;
    }

    public void setIdSub(String idSub) {
        this.idSub = idSub;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
