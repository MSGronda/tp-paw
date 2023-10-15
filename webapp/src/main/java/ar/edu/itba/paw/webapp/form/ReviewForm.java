package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.enums.Difficulty;
import ar.edu.itba.paw.models.enums.TimeDemanding;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ReviewForm {

    @Size(max = 2000)
    private String text;

    @NotNull
    private Integer difficulty;

    @NotNull
    private Integer timeDemanding;

    @NotNull
    private Boolean anonymous;

    @NotNull
    @Pattern(regexp = "[0-9]{2}\\.[0-9]{2}")
    private String subjectId;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public Difficulty getDifficultyEnum() {
        return Difficulty.parse(difficulty.longValue());
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getTimeDemanding() {
        return timeDemanding;
    }

    public TimeDemanding getTimeDemandingEnum() {
        return TimeDemanding.parse(timeDemanding.longValue());
    }
    public String getSubjectId() {
        return subjectId;
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

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
}
