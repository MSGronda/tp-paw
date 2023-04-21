package ar.edu.itba.paw.models;

import java.sql.Time;
import java.util.*;

public class SubjectClass {
    private final String idSub;
    private final String idClass;
    private final Set<Professor> professors;
    private final Set<ClassTime> classTimes;

    public SubjectClass(String idSub, String idClass) {
        this.idSub = idSub;
        this.idClass = idClass;
        this.professors = new LinkedHashSet<>();
        this.classTimes = new LinkedHashSet<>();
    }
    public SubjectClass(String idSub, String idClass, Set<Professor> professors, Set<ClassTime> classTimes) {
        this.idSub = idSub;
        this.idClass = idClass;
        this.professors = professors;
        this.classTimes = classTimes;
    }

    public Set<ClassTime> getClassTimes() {
        return classTimes;
    }

    public Set<Professor> getProfessors() {
        return professors;
    }

    public String getIdClass() {
        return idClass;
    }

    public String getIdSub() {
        return idSub;
    }
    public void setprofIds(Collection<Professor> professorIds){
        this.professors.addAll(professorIds);
    }
    public void setClassTimes(Collection<ClassTime> classTimes){
        this.classTimes.addAll(classTimes);
    }

    public static class ClassTime{
        private final Integer day;
        private final Time startTime;
        private final Time endTime;
        private final String classLoc;
        private final String building;
        private final String mode;

        public ClassTime(Integer day, Time startTime, Time endTime, String classLoc, String building, String mode) {
            this.day = day;
            this.startTime = startTime;
            this.endTime = endTime;
            this.classLoc = classLoc;
            this.building = building;
            this.mode = mode;
        }

        public String getBuilding() {
            return building;
        }

        public String getClassLoc() {
            return classLoc;
        }

        public Integer getDay() {
            return day;
        }

        public Time getEndTime() {
            return endTime;
        }

        public String getMode() {
            return mode;
        }

        public Time getStartTime() {
            return startTime;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ClassTime classTime = (ClassTime) o;
            return Objects.equals(day, classTime.day) && Objects.equals(startTime, classTime.startTime) && Objects.equals(endTime, classTime.endTime) && Objects.equals(classLoc, classTime.classLoc) && Objects.equals(building, classTime.building) && Objects.equals(mode, classTime.mode);
        }

        @Override
        public int hashCode() {
            return Objects.hash(day, startTime, endTime, classLoc, building, mode);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubjectClass that = (SubjectClass) o;
        return Objects.equals(idSub, that.idSub) && Objects.equals(idClass, that.idClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSub, idClass);
    }
}
