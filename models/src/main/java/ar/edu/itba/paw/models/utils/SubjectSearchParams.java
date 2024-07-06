package ar.edu.itba.paw.models.utils;

import ar.edu.itba.paw.models.User;

public class SubjectSearchParams {
    private Long degree;
    private Long semester;
    private Long available;
    private Long unLockable;
    private Long done;
    private Long future;
    private Long plan;
    private Long planFinishedDate;
    private Long userReviews;
    private String query;
    private Integer credits;
    private String department;
    private Integer difficulty;
    private Integer timeDemand;

    public SubjectSearchParams(
            final Long degree,
            Long semester,
            Long available,
            Long unLockable,
            Long done,
            Long future,
            Long plan,
            Long planFinishedDate,
            String query,
            Integer credits,
            String department,
            Integer difficulty,
            Integer timeDemand,
            Long userReviews
    ) {
        this.degree = degree;
        this.semester = semester;
        this.available = available;
        this.unLockable = unLockable;
        this.done = done;
        this.future = future;
        this.plan = plan;
        this.planFinishedDate = planFinishedDate;
        this.query = query;
        this.credits = credits;
        this.department = department;
        this.difficulty = difficulty;
        this.timeDemand = timeDemand;
        this.userReviews = userReviews;
    }

    public boolean hasDegree(){
        return degree != null;
    }
    public void setDegree(Long degree) {
        this.degree = degree;
    }

    public boolean hasSemester(){
        return semester != null;
    }
    public void setSemester(Long semester) {
        this.semester = semester;
    }
    public boolean hasAvailable(){
        return available != null;
    }
    public void setAvailable(Long available) {
        this.available = available;
    }

    public boolean hasUnLockable(){
        return unLockable != null;
    }
    public void setUnLockable(Long unLockable) {
        this.unLockable = unLockable;
    }

    public boolean hasDone(){
        return done != null;
    }
    public void setDone(Long done) {
        this.done = done;
    }

    public boolean hasFuture(){
        return future != null;
    }
    public void setFuture(Long future) {
        this.future = future;
    }

    public boolean hasPlan(){
        return plan != null;
    }
    public void setPlan(Long plan) {
        this.plan = plan;
    }

    public boolean hasPlanFinishedDate(){
        return planFinishedDate != null;
    }
    public void setPlanFinishedDate(Long planFinishedDate) {
        this.planFinishedDate = planFinishedDate;
    }

    public boolean hasQuery(){
        return query != null;
    }
    public void setQuery(String query) {
        this.query = query;
    }

    public boolean hasCredits(){
        return credits != null;
    }
    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public boolean hasDepartment(){
        return department != null;
    }
    public void setDepartment(String department) {
        this.department = department;
    }

    public boolean hasDifficulty(){
        return difficulty != null;
    }
    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public boolean hasTimeDemand(){
        return timeDemand != null;
    }
    public void setTimeDemand(Integer timeDemand) {
        this.timeDemand = timeDemand;
    }

    public boolean hasUserReviews(){
        return userReviews != null;
    }
    public void setUserReviews(Long userReviews) {
        this.userReviews = userReviews;
    }

    public Long getDegree() {
        return degree;
    }

    public Long getSemester() {
        return semester;
    }

    public Long getAvailable() {
        return available;
    }

    public Long getUnLockable() {
        return unLockable;
    }

    public Long getDone() {
        return done;
    }

    public Long getFuture() {
        return future;
    }

    public Long getPlan() {
        return plan;
    }

    public Long getPlanFinishedDate() {
        return planFinishedDate;
    }

    public String getQuery() {
        return query;
    }

    public Integer getCredits() {
        return credits;
    }

    public String getDepartment() {
        return department;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public Integer getTimeDemand() {
        return timeDemand;
    }

    public Long getUserReviews() {
        return userReviews;
    }
}