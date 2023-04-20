package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ReviewForm {

    @Email
    @Size(min = 1)
    private String email;

    @Size(max = 2000)
    private String text;

    @NotNull
    private Integer easy;

    @NotNull
    private Integer timeDemanding;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getEasy() {
        return easy;
    }

    public void setEasy(Integer easy) {
        this.easy = easy;
    }

    public Integer getTimeDemanding() {
        return timeDemanding;
    }

    public void setTimeDemanding(Integer timeDemanding) {
        this.timeDemanding = timeDemanding;
    }

}
