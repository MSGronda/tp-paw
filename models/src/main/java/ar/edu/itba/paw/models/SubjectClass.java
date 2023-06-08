package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "class")
public class SubjectClass {
    @EmbeddedId
    private Key key;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId("idsub")
    @JoinColumn(name = "idsub")
    private Subject subject;

    @ManyToMany
    @JoinTable(
            name = "classprofessors",
            joinColumns = {
                    @JoinColumn(name = "idsub", referencedColumnName = "idsub"),
                    @JoinColumn(name = "idclass", referencedColumnName = "idclass")
            },
            inverseJoinColumns = @JoinColumn(name = "idprof")
    )
    private List<Professor> professors;

    @OneToMany(mappedBy = "subjectClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubjectClassTime> classTimes;

    protected SubjectClass() {}

    public SubjectClass(final String classId, final Subject subject) {
        this.key = new Key(classId, subject.getId());
        this.subject = subject;
        this.professors = new ArrayList<>();
        this.classTimes = new ArrayList<>();
    }

    public List<SubjectClassTime> getClassTimes() {
        return classTimes;
    }

    public List<Professor> getProfessors() {
        return professors;
    }

    public String getClassId() {
        return key.idclass;
    }

    public Subject getSubject() {
        return subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubjectClass)) return false;
        SubjectClass that = (SubjectClass) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Embeddable
    public static class Key implements Serializable {
        @Column(name = "idclass")
        private String idclass;

        @Column(name = "idsub")
        private String idsub;

        public Key(final String idClass, final String idSub) {
            this.idclass = idClass;
            this.idsub = idSub;
        }

        protected Key() {}

        public String getClassId() {
            return idclass;
        }

        public String getSubjectId() {
            return idsub;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return Objects.equals(idclass, key.idclass) && Objects.equals(idsub, key.idsub);
        }

        @Override
        public int hashCode() {
            return Objects.hash(idclass, idsub);
        }
    }
}
