package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "userSemester")
public class UserSemesterSubject {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usersemester_id_seq")
    @SequenceGenerator(sequenceName = "usersemester_id_seq", name = "usersemester_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "iduser")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumns({
            @JoinColumn(name = "idsub", referencedColumnName = "idsub"),
            @JoinColumn(name = "idclass", referencedColumnName = "idclass")
    })
    private SubjectClass subjectClass;

    @Column(name = "datefinished")
    private Timestamp dateFinished;

    protected UserSemesterSubject() {}

    public UserSemesterSubject(final Long id, final User user, final SubjectClass subjectClass, final Timestamp dateFinished) {
        this.id = id;
        this.user = user;
        this.subjectClass = subjectClass;
        this.dateFinished = dateFinished;
    }

    public UserSemesterSubject(final User user, final SubjectClass subjectClass) {
        this.user = user;
        this.subjectClass = subjectClass;
        this.dateFinished = null;
    }

    public boolean isActive(){
        return dateFinished == null;

    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public SubjectClass getSubjectClass() {
        return subjectClass;
    }

    public Timestamp getDateFinished() {
        return dateFinished;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setDateFinished(Timestamp dateFinished) {
        this.dateFinished = dateFinished;
    }

    public void setSubjectClass(SubjectClass subjectClass) {
        this.subjectClass = subjectClass;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserSemesterSubject)) return false;
        UserSemesterSubject that = (UserSemesterSubject) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
