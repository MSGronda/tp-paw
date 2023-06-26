package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.converter.DifficultyConverter;
import ar.edu.itba.paw.models.converter.TimeDemandingConverter;
import ar.edu.itba.paw.models.enums.Difficulty;
import ar.edu.itba.paw.models.enums.TimeDemanding;
import jdk.nashorn.internal.ir.annotations.Immutable;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Immutable
@Table(name = "subjectreviewstatistics")
public class ReviewStats {
    @Id
    @Column(name = "idsub", length = 100)
    private String subjectId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "idsub")
    private Subject subject;

    @Column(name = "reviewcount")
    private int reviewCount;

    @Column(name = "easycount")
    private int easyCount;

    @Column(name = "mediumcount")
    private int mediumCount;

    @Column(name = "hardcount")
    private int hardCount;

    @Column(name = "nottimedemandingcount")
    private int notTimeDemandingCount;

    @Column(name = "averagetimedemandingcount")
    private int averageTimeDemandingCount;

    @Column(name = "timedemandingcount")
    private int timeDemandingCount;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "difficulty")
    private Difficulty difficulty;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "timedemanding")
    private TimeDemanding timeDemanding;

    protected ReviewStats() {}

    public Difficulty getDifficulty(){
        if(difficulty == null)
            return Difficulty.NO_DATA;

        return difficulty;
    }
    public TimeDemanding getTimeDemanding(){
        if(timeDemanding == null)
            return TimeDemanding.NO_DATA;

        return timeDemanding;
    }

    public Subject getSubject() {
        return subject;
    }

    public int getEasyCount() {
        return easyCount;
    }

    public int getHardCount() {
        return hardCount;
    }

    public int getMediumCount() {
        return mediumCount;
    }

    public int getNotTimeDemandingCount() {
        return notTimeDemandingCount;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public int getAverageTimeDemandingCount(){
        return averageTimeDemandingCount;
    }

    public int getTimeDemandingCount() {
        return timeDemandingCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReviewStats)) return false;
        ReviewStats that = (ReviewStats) o;
        return Objects.equals(subjectId, that.subjectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjectId);
    }
}
