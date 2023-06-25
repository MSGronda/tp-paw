package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "classloctime")
public class SubjectClassTime {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "classloctime_idloctime_seq")
    @SequenceGenerator(sequenceName = "classloctime_idloctime_seq", name = "classloctime_idloctime_seq", allocationSize = 1)
    @Column(name = "idloctime")
    private long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "idsub", referencedColumnName = "idsub"),
            @JoinColumn(name = "idclass", referencedColumnName = "idclass")
    })
    private SubjectClass subjectClass;

    @Column(nullable = false)
    private int day;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(name = "class", length = 100, nullable = false)
    private String classLoc;

    @Column(length = 100, nullable = false)
    private String building;

    @Column(length = 100, nullable = false)
    private String mode;

    protected SubjectClassTime() {}

    public SubjectClassTime(SubjectClass subjectClass, int day, LocalTime startTime, LocalTime endTime, String classLoc, String building, String mode) {
        this.subjectClass = subjectClass;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.classLoc = classLoc;
        this.building = building;
        this.mode = mode;
    }

    public long getId() {
        return id;
    }

    public SubjectClass getSubjectClass() {
        return subjectClass;
    }

    public String getBuilding() {
        return building;
    }

    public String getClassLoc() {
        return classLoc;
    }

    public int getDay() {
        return day;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getMode() {
        return mode;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setClassLoc(String classLoc) {
        this.classLoc = classLoc;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubjectClassTime)) return false;
        SubjectClassTime that = (SubjectClassTime) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
