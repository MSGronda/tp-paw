package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class DegreeForm {

    @Size(min=1, max=50)
    private String name;
    @Size(min=1,max=3)
    private String totalCredits;


    public String getName() {
        return name;
    }
    public void setName(final String name) {
        this.name = name;
    }
    public String getTotalCredits() {
        return totalCredits;
    }
    public void setTotalCredits(final String totalCredits) {
        this.totalCredits = totalCredits;
    }
}
