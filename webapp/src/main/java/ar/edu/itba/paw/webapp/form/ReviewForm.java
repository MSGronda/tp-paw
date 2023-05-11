package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ReviewForm {

    @Size(max = 2000)
    private String text;

    @NotNull
    private Integer easy;

    @NotNull
    private Integer timeDemanding;

    @NotNull
    private Boolean anonymous;

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

    public Boolean getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
    }

}
