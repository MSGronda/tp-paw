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

    @OneToMany(mappedBy = "degree", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DegreeSubject> subjects;

    @Column(name = "totalCredits", nullable = false, columnDefinition = "int default 244")
    private int totalCredits;

    public Degree(Builder builder) {
        this.name = builder.name;
        this.totalCredits = builder.totalCredits;
        this.subjects = builder.subjects;
    }

    protected Degree() {}

    public int getTotalCredits(){
        return totalCredits;
    }

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

    public List<DegreeSubject> getDegreeSubjects() {
        return subjects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Degree)) return false;
        Degree degree = (Degree) o;
        return getId() == degree.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builderFrom(Degree degree) {
        return new Builder(degree);
    }

    public static class Builder {
        private String name;
        private int totalCredits;
        private List<DegreeSubject> subjects;

        private Builder() {
            this.subjects = new ArrayList<>();
        }

        private Builder(Degree degree) {
            this.name = degree.name;
            this.totalCredits = degree.totalCredits;
            this.subjects = degree.subjects;
        }

        public Builder name(final String name) {
            this.name = name;
            return this;
        }
        public Builder totalCredits(final int totalCredits) {
            this.totalCredits = totalCredits;
            return this;
        }
        public Builder subjects(final List<DegreeSubject> subjects) {
            this.subjects = subjects;
            return this;
        }
        public String getName() {
            return name;
        }
        public int getTotalCredits() {
            return totalCredits;
        }
        public List<DegreeSubject> getSubjects() {
            return subjects;
        }
        public Degree build() {
            return new Degree(this);
        }
    }
}
