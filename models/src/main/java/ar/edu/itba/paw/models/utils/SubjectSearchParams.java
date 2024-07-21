package ar.edu.itba.paw.models.utils;

import ar.edu.itba.paw.models.User;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SubjectSearchParams {
    private Long degree;
    private Long semester;
    private Long available;
    private Long unLockable;
    private Long done;
    private Long future;
    private Long plan;
    private Long planFinishedDate;
    private String query;
    private Integer minCredits;
    private Integer maxCredits;
    private String department;
    private Integer minDifficulty;
    private Integer maxDifficulty;
    private Integer minTimeDemand;
    private Integer maxTimeDemand;

    private List<String> ids;

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
            Integer minCredits,
            Integer maxCredits,
            String department,
            Integer minDifficulty,
            Integer maxDifficulty,
            Integer minTimeDemand,
            Integer maxTimeDemand,
            String ids
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
        this.minCredits = minCredits;
        this.maxCredits = maxCredits;
        this.department = department;
        this.minDifficulty = minDifficulty;
        this.maxDifficulty = maxDifficulty;
        this.minTimeDemand = minTimeDemand;
        this.maxTimeDemand = maxTimeDemand;
        if(ids != null){
            this.ids = Arrays.stream(ids.split(",")).collect(Collectors.toList());
        }
    }

    public List<String> getIds() {
        return ids;
    }
    public void setIds(List<String> ids) {
        this.ids = ids;
    }
    public boolean hasIds(){
        return ids != null;
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

    public boolean hasMinCredits(){
        return minCredits != null;
    }
    public void setMinCredits(Integer minCredits) {
        this.minCredits = minCredits;
    }

    public boolean hasMaxCredits() {
        return maxCredits != null;
    }

    public void setMaxCredits(Integer maxCredits) {
        this.maxCredits = maxCredits;
    }

    public boolean hasDepartment(){
        return department != null;
    }
    public void setDepartment(String department) {
        this.department = department;
    }

    public boolean hasMinDifficulty(){
        return minDifficulty != null;
    }
    public void setMinDifficulty(Integer minDifficulty) {
        this.minDifficulty = minDifficulty;
    }

    public boolean hasMaxDifficulty(){
        return maxDifficulty != null;
    }
    public void setMaxDifficulty(Integer maxDifficulty) {
        this.maxDifficulty = maxDifficulty;
    }

    public boolean hasMinTimeDemand(){
        return minTimeDemand != null;
    }
    public void setMinTimeDemand(Integer minTimeDemand) {
        this.minTimeDemand = minTimeDemand;
    }

    public boolean hasMaxTimeDemand(){
        return maxTimeDemand != null;
    }
    public void setMaxTimeDemand(Integer maxTimeDemand) {
        this.maxTimeDemand = maxTimeDemand;
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

    public Integer getMinCredits() {
        return minCredits;
    }

    public Integer getMaxCredits() {
        return maxCredits;
    }

    public String getDepartment() {
        return department;
    }

    public Integer getMinDifficulty() {
        return minDifficulty;
    }

    public Integer getMaxDifficulty() {
        return maxDifficulty;
    }

    public Integer getMinTimeDemand() {
        return minTimeDemand;
    }

    public Integer getMaxTimeDemand() {
        return maxTimeDemand;
    }
}
