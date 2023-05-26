package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "degrees")
public class Degree {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "degrees_id_seq")
    @SequenceGenerator(sequenceName = "degrees_id_seq", name = "degrees_id_seq", allocationSize = 1)
    private long id;

    @Column(name = "degname")
    private String name;

    @OneToMany(mappedBy = "degree")
    private List<DegreeSubject> subjects;

    public Degree(String name) {
        this.name = name;
    }

    Degree() {}

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Subject> getSubjects() {
        return subjects
                .stream()
                .map(DegreeSubject::getSubject)
                .collect(Collectors.toList());
    }

    public List<Subject> getElectives() {
        return subjects.stream()
                .filter(s -> s.getSemester() == -1)
                .map(DegreeSubject::getSubject)
                .collect(Collectors.toList());
    }

    public List<DegreeSemester> getSemesters() {
        final Map<Integer, DegreeSemester> map = new HashMap<>();
        for (DegreeSubject ds : subjects) {
            if (ds.getSemester() == -1) continue;

            final DegreeSemester s = map.getOrDefault(ds.getSemester(), new DegreeSemester(ds.getSemester(), new ArrayList<>()));
            s.getSubjects().add(ds.getSubject());
            map.putIfAbsent(ds.getSemester(), s);
        }

        return new ArrayList<>(map.values());
    }

    public List<DegreeYear> getYears() {
        final List<DegreeSemester> semesters = getSemesters();
        final Map<Integer, DegreeYear> yearMap = new HashMap<>();

        for(DegreeSemester semester : semesters) {
            final int yearNum = (semester.getNumber() + 1) / 2;
            final DegreeYear year = yearMap.getOrDefault(yearNum, new DegreeYear(this, yearNum, new ArrayList<>()));
            year.getSubjects().addAll(semester.getSubjects());
            yearMap.putIfAbsent(yearNum, year);
        }

        return new ArrayList<>(yearMap.values());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Degree degree = (Degree) o;
        return id == degree.id && Objects.equals(name, degree.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
