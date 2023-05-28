package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "subjectsdegrees")
class DegreeSubject {
    @EmbeddedId
    private Key key;

    @ManyToOne(optional = false)
    @MapsId("degreeId")
    @JoinColumn(name = "iddeg")
    private Degree degree;

    @ManyToOne
    @MapsId("subjectId")
    @JoinColumn(name = "idsub")
    private Subject subject;

    @Column
    private int semester;

    public Degree getDegree() {
        return degree;
    }

    public Subject getSubject() {
        return subject;
    }

    public int getSemester() {
        return semester;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DegreeSubject that = (DegreeSubject) o;
        return semester == that.semester && Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, semester);
    }

    @Embeddable
    public static class Key implements Serializable {
        @Column(name = "iddeg")
        private long degreeId;

        @Column(name = "idsub")
        private String subjectId;

        Key() {}

        public long getDegreeId() {
            return degreeId;
        }

        public String getSubjectId() {
            return subjectId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return degreeId == key.degreeId && subjectId.equals(key.subjectId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(degreeId, subjectId);
        }
    }
}
