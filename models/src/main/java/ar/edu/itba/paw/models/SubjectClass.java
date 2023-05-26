package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "class")
public class SubjectClass {
    @EmbeddedId
    private Key key;

    @ManyToOne
    @MapsId("idsub")
    @JoinColumn(name = "idsub")
    private Subject subject;

    @ManyToMany
    @JoinTable(
            name = "classprofessors",
            joinColumns = {@JoinColumn(name = "idsub"), @JoinColumn(name = "idclass")},
            inverseJoinColumns = @JoinColumn(name = "idprof")
    )
    private Set<Professor> professors;

    @OneToMany
    @JoinColumns({
            @JoinColumn(name = "idsub"),
            @JoinColumn(name = "idclass")
    })
    private Set<SubjectClassTime> classTimes;

    SubjectClass() {}

    public Set<SubjectClassTime> getClassTimes() {
        return classTimes;
    }

    public Set<Professor> getProfessors() {
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
        if (o == null || getClass() != o.getClass()) return false;
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

        Key() {}

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
