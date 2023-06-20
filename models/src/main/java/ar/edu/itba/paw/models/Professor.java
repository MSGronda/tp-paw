package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "professors")
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "professors_id_seq")
    @SequenceGenerator(sequenceName = "professors_id_seq", name = "professors_id_seq", allocationSize = 1)
    private long id;

    @Column(name = "profname", length = 100, nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "professorssubjects",
            joinColumns = @JoinColumn(name = "idprof"),
            inverseJoinColumns = @JoinColumn(name = "idsub")
    )
    private List<Subject> subjects;

    public Professor(final String name){
        this.name = name;
        this.subjects = new ArrayList<>();
    }

    protected Professor() {}

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Professor)) return false;
        Professor professor = (Professor) o;
        return getId() == professor.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
