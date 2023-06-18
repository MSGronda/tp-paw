package ar.edu.itba.paw.models;

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

    protected ReviewStats() {}

    public Difficulty getDifficulty(){
        if(easyCount == 0 && mediumCount == 0 && hardCount == 0){
            return Difficulty.NO_DATA;
        }

        if(easyCount > mediumCount){
            if(easyCount > hardCount)
                return Difficulty.EASY;
        }
        else if(mediumCount > hardCount) {
            return Difficulty.MEDIUM;
        }
        return Difficulty.HARD;
    }
    public TimeDemanding getTimeDemanding(){
        if(notTimeDemandingCount == 0 && timeDemandingCount == 0 && averageTimeDemandingCount == 0){
            return TimeDemanding.NO_DATA;
        }

        if(notTimeDemandingCount > averageTimeDemandingCount) {
            if (notTimeDemandingCount > timeDemandingCount)
                return TimeDemanding.LOW;
        } else if(averageTimeDemandingCount > timeDemandingCount) {
            return TimeDemanding.MEDIUM;
        }
        return TimeDemanding.HIGH;
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
