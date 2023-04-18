package ar.edu.itba.paw.models;

import java.sql.Time;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SubjectClass {
    private final String idSub;
    private final String idClass;
    private final Set<Long> professorIds;
    private final Set<ClassTime> classTimes;

    public SubjectClass(String idSub, String idClass) {
        this.idSub = idSub;
        this.idClass = idClass;
        this.professorIds = new HashSet<>();
        this.classTimes = new HashSet<>();
    }
    public SubjectClass(String idSub, String idClass, Set<Long> professorIds, Set<ClassTime> classTimes) {
        this.idSub = idSub;
        this.idClass = idClass;
        this.professorIds = professorIds;
        this.classTimes = classTimes;
    }

    public Set<ClassTime> getClassTimes() {
        return classTimes;
    }

    public Set<Long> getProfessorIds() {
        return professorIds;
    }

    public String getIdClass() {
        return idClass;
    }

    public String getIdSub() {
        return idSub;
    }
    public void setprofIds(Collection<Long> professorIds){
        this.professorIds.addAll(professorIds);
    }
    public void setClassTimes(Collection<ClassTime> classTimes){
        this.classTimes.addAll(classTimes);
    }

    public static class ClassTime{
        private final String day;
        private final Time startTime;
        private final Time endTime;
        private final String classLoc;
        private final String building;
        private final String mode;

        public ClassTime(String day, Time startTime, Time endTime, String classLoc, String building, String mode) {
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

        public String getDay() {
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
