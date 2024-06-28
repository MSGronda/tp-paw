package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "userSemester")
public class UserSemester {
    @EmbeddedId
    private Key key;

    @ManyToOne(optional = false)
    @MapsId("iduser")
    @JoinColumn(name = "iduser")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumns({
            @JoinColumn(name = "idsub", referencedColumnName = "idsub", insertable = false, updatable = false), // TODO: POR FAVOR CHEQUEA ESTO
            @JoinColumn(name = "idclass", referencedColumnName = "idclass", insertable = false, updatable = false) // TODO: POR FAVOR CHEQUEA ESTO
    })
    private SubjectClass subjectClass;

    @Column(name = "datefinished")
    private Timestamp dateFinished;

    protected UserSemester() {}

    public UserSemester(final User user, final SubjectClass subjectClass, final Timestamp dateFinished) {
        this.user = user;
        this.subjectClass = subjectClass;
        this.dateFinished = dateFinished;
        this.key = new UserSemester.Key(user.getId(), subjectClass.getClassId(), subjectClass.getSubject().getId());
    }

    public UserSemester(final User user, final SubjectClass subjectClass) {
        this.user = user;
        this.subjectClass = subjectClass;
        this.dateFinished = null;
        this.key = new UserSemester.Key(user.getId(), subjectClass.getClassId(), subjectClass.getSubject().getId());
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

    public boolean isActive(){
        return dateFinished == null;
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
        if (!(o instanceof UserSemester)) return false;
        UserSemester that = (UserSemester) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Embeddable
    public static class Key implements Serializable {
        @Column(name = "iduser")
        private Long iduser;

        @Column(name = "idclass")
        private String idclass;

        @Column(name = "idsub")
        private String idsub;

        public Key(final Long idUser, final String idClass, final String idSub) {
            this.iduser = idUser;
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

        public Long getIduser() {
            return iduser;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserSemester.Key key = (UserSemester.Key) o;
            return Objects.equals(iduser, key.iduser) && Objects.equals(idclass, key.idclass) && Objects.equals(idsub, key.idsub);
        }

        @Override
        public int hashCode() {
            return Objects.hash( iduser, idclass, idsub);
        }
    }
}
