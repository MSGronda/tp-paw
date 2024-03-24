package ar.edu.itba.paw.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "subjects")
public class Subject {
    @Id
    @Column(length = 100)
    private String id;

    @Column(name = "subname", length = 100, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String department;

    @Column(nullable = false)
    private Integer credits;

    @OneToOne(mappedBy = "subject")
    @PrimaryKeyJoinColumn(foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private ReviewStats reviewStats;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "subject")
    private List<DegreeSubject> degreeSubjects;

    @ManyToMany
    @JoinTable(
            name = "prereqsubjects",
            joinColumns = @JoinColumn(name = "idsub"),
            inverseJoinColumns = @JoinColumn(name = "idprereq")
    )
    private Set<Subject> prerequisites;

    @ManyToMany
    @JoinTable(
            name = "professorssubjects",
            joinColumns = @JoinColumn(name = "idsub"),
            inverseJoinColumns = @JoinColumn(name = "idprof")
    )
    private Set<Professor> professors;

    @ManyToMany
    @JoinTable(
            name = "subjectsdegrees",
            joinColumns = @JoinColumn(name = "idsub"),
            inverseJoinColumns = @JoinColumn(name = "iddeg")
    )
    private Set<Degree> degrees;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "subject")
    private Set<SubjectClass> classes;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "subject")
    private List<Review> reviews;

    private Subject(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.department = builder.department;
        this.credits = builder.credits;
        this.prerequisites = builder.prerequisites;
        this.professors = builder.professors;
        this.degrees = builder.degrees;
        this.classes = builder.classes;
    }

    protected Subject() {}

    public Map<String, SubjectClass> getClassesById() {
        final Map<String, SubjectClass> res = new LinkedHashMap<>();
        for(SubjectClass subjectClass : classes) {
            res.put(subjectClass.getClassId(), subjectClass);
        }
        return res;
    }

    public Set<SubjectClass> getClasses() {
        return classes;
    }

    public Optional<SubjectClass> getClassById(final String classId){
        for(final SubjectClass sc : classes){
            if(sc.getClassId().equals(classId)){
                return Optional.of(sc);
            }
        }
        return Optional.empty();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public Set<Subject> getPrerequisites() {
        return prerequisites;
    }

    public Set<Professor> getProfessors() {
        return professors;
    }

    public Set<Degree> getDegrees() {
        return degrees;
    }
    
    public Integer getSemester(final long degreeId) {
        for(final DegreeSubject ds : degreeSubjects){
            if(ds.getDegree().getId() == degreeId){
                return ds.getSemester();
            }
        }
        return null;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(final int credits) {
        this.credits = credits;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setPrerequisites(Set<Subject> prerequisites) {
        this.prerequisites = prerequisites;
    }

    public void setProfessors(Set<Professor> professors) {
        this.professors = professors;
    }

    public void setDegrees(Set<Degree> degrees) {
        this.degrees = degrees;
    }

    public ReviewStats getReviewStats() {
        if(reviewStats == null) {
            return new ReviewStats();
        }

        return reviewStats;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subject)) return false;
        Subject subject = (Subject) o;
        return Objects.equals(getId(), subject.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return name;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builderFrom(Subject subject) {
        return new Builder(subject);
    }

    public static class Builder {
        private String id;
        private String name;
        private String department;
        private Integer credits;
        private Set<Subject> prerequisites;
        private Set<Professor> professors;
        private Set<Degree> degrees;
        private Set<SubjectClass> classes;

        private Builder() {
            this.prerequisites = new LinkedHashSet<>();
            this.professors = new LinkedHashSet<>();
            this.degrees = new LinkedHashSet<>();
            this.classes = new LinkedHashSet<>();
        }

        private Builder(Subject subject) {
            this.id = subject.id;
            this.name = subject.name;
            this.department = subject.department;
            this.credits = subject.credits;
            this.prerequisites = subject.prerequisites;
            this.professors = subject.professors;
            this.degrees = subject.degrees;
            this.classes = subject.classes;
        }

        public Builder id(final String id) {
            this.id = id;
            return this;
        }

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public Builder department(final String department) {
            this.department = department;
            return this;
        }

        public Builder credits(final Integer credits) {
            this.credits = credits;
            return this;
        }

        public Builder prerequisites(final Set<Subject> prerequisites) {
            this.prerequisites = prerequisites;
            return this;
        }

        public Builder professors(final Set<Professor> professors) {
            this.professors = professors;
            return this;
        }

        public Builder degrees(final Set<Degree> degrees) {
            this.degrees = degrees;
            return this;
        }

        public Builder classes(final Set<SubjectClass> classes) {
            this.classes = classes;
            return this;
        }

        public Subject build() {
            return new Subject(this);
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDepartment() {
            return department;
        }

        public Integer getCredits() {
            return credits;
        }

        public Set<Subject> getPrerequisites() {
            return prerequisites;
        }

        public Set<Professor> getProfessors() {
            return professors;
        }

        public Set<Degree> getDegrees() {
            return degrees;
        }

        public Set<SubjectClass> getClasses() {
            return classes;
        }
    }
}
