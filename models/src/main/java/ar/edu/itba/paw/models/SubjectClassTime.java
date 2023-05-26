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

    public SubjectClassTime(final int day, final LocalTime startTime, final LocalTime endTime,
                            final String classLoc, final String building, final String mode) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.classLoc = classLoc;
        this.building = building;
        this.mode = mode;
    }

    SubjectClassTime() {}

    public long getId() {
        return id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubjectClassTime classTime = (SubjectClassTime) o;
        return Objects.equals(day, classTime.day) && Objects.equals(startTime, classTime.startTime) && Objects.equals(endTime, classTime.endTime) && Objects.equals(classLoc, classTime.classLoc) && Objects.equals(building, classTime.building) && Objects.equals(mode, classTime.mode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, startTime, endTime, classLoc, building, mode);
    }
}
