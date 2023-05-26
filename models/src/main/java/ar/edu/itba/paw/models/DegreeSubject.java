package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "subjectsdegrees")
class DegreeSubject {
    @EmbeddedId
    private Key key;

    @ManyToOne
    @MapsId("iddeg")
    @JoinColumn(name = "iddeg")
    private Degree degree;

    @ManyToOne
    @MapsId("idsub")
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
        private long iddeg;
        private String idsub;

        Key() {}

        public long getDegreeId() {
            return iddeg;
        }

        public String getSubjectId() {
            return idsub;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return iddeg == key.iddeg && idsub.equals(key.idsub);
        }

        @Override
        public int hashCode() {
            return Objects.hash(iddeg, idsub);
        }
    }
}
