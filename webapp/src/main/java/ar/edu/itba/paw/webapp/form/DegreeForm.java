package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

public class DegreeForm {
    @NotNull
    @Size(min=1, max=50)
    private String name;
    @NotNull
    private Integer totalCredits;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
    public Integer getTotalCredits() {
        return totalCredits;
    }
    public void setTotalCredits(final Integer totalCredits) {
        this.totalCredits = totalCredits;
    }
}
