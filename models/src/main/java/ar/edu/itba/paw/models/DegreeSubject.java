package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "subjectsdegrees")
public class DegreeSubject {
    @EmbeddedId
    private Key key;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId("degreeId")
    @JoinColumn(name = "iddeg")
    private Degree degree;

    @ManyToOne(optional = false)
    @MapsId("subjectId")
    @JoinColumn(name = "idsub")
    private Subject subject;

    @Column(nullable = false)
    private int semester;

    protected DegreeSubject() {}

    public DegreeSubject(final Degree degree, final Subject subject, final int semester) {
        this.degree = degree;
        this.subject = subject;
        this.semester = semester;
        this.key = new Key(degree.getId(), subject.getId());
    }

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
        if (!(o instanceof DegreeSubject)) return false;
        DegreeSubject that = (DegreeSubject) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Embeddable
    public static class Key implements Serializable {
        @Column(name = "iddeg")
        private long degreeId;

        @Column(name = "idsub")
        private String subjectId;

        protected Key() {}

        protected Key(final long degreeId, final String subjectId) {
            this.degreeId = degreeId;
            this.subjectId = subjectId;
        }

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
            return getDegreeId() == key.getDegreeId() && Objects.equals(getSubjectId(), key.getSubjectId());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getDegreeId(), getSubjectId());
        }
    }
}
