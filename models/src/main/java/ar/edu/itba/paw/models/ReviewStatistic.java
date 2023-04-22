package ar.edu.itba.paw.models;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class ReviewStatistic {
    private final String idSub;
    private final int reviewCount;
    private final int easyCount;
    private final int mediumCount;
    private final int hardCount;
    private final int notTimeDemandingCount;
    private final int timeDemandingCount;

    private final int dificulty;
    private final int timeDemaning;

    private static final int NO_DATA=-1, EASY = 0, MEDIUM = 1, HARD = 2;
    private static final int NOT_TIME_DEMANDING = 0, TIME_DEMANDING = 1;

    public ReviewStatistic(String idSub, int reviewCount, int easyCount, int mediumCount, int hardCount,
                           int notTimeDemandingCount, int timeDemandingCount) {
        this.idSub = idSub;
        this.reviewCount = reviewCount;
        this.easyCount = easyCount;
        this.mediumCount = mediumCount;
        this.hardCount = hardCount;
        this.notTimeDemandingCount = notTimeDemandingCount;
        this.timeDemandingCount = timeDemandingCount;

        this.dificulty = getDifficulty();
        this.timeDemaning = getTimeDifficulty();
    }

    public ReviewStatistic(String idSub){
        this.idSub = idSub;
        this.reviewCount = 0;
        this.easyCount = 0;
        this.mediumCount = 0;
        this.hardCount = 0;
        this.notTimeDemandingCount = 0;
        this.timeDemandingCount = 0;

        this.dificulty = getDifficulty();
        this.timeDemaning = getTimeDifficulty();

    }

    public int getDifficulty(){
        // -1 no data,  0 is easy, 1 is medium, 2 is hard
        if(easyCount == 0 && mediumCount == 0 && hardCount == 0){
            return NO_DATA;
        }

        if(easyCount > mediumCount){
            if(easyCount > hardCount)
                return EASY;
        }
        else{
            if(mediumCount > hardCount)
                return MEDIUM;
        }
        return HARD;
    }
    public int getTimeDifficulty(){
        System.out.println("not: " + notTimeDemandingCount + " is: " + timeDemandingCount);
        // -1 no data, 0 not, 1 yes
        if(notTimeDemandingCount == 0 && timeDemandingCount == 0){
            return NO_DATA;
        }
        if(notTimeDemandingCount > timeDemandingCount)
            return NOT_TIME_DEMANDING;
        return TIME_DEMANDING;
    }

    public String getIdSub() {
        return idSub;
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

    public int getTimeDemandingCount() {
        return timeDemandingCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewStatistic that = (ReviewStatistic) o;
        return Objects.equals(idSub, that.idSub);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSub);
    }
}
