package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.enums.SubjectProgress;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @SequenceGenerator(sequenceName = "users_id_seq", name = "users_id_seq", allocationSize = 1)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "pass", length = 100)
    private String password;

    @Column(length = 100)
    private String username;

    @Column(length = 100, name = "confirmtoken")
    private String confirmToken;

    @Column(name = "image_id")
    private long imageId;

    @ElementCollection
    @CollectionTable(
            name = "usersubjectprogress",
            joinColumns = @JoinColumn(name = "iduser", referencedColumnName = "id")
    )
    @MapKeyColumn(name = "idsub")
    @Column(name = "subjectstate")
    private Map<String, SubjectProgress> subjectProgress;

    @Column
    private boolean confirmed;

    @Column(length = 32)
    private Locale locale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "degreeid")
    private Degree degree;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "userroles",
            joinColumns = @JoinColumn(name = "userid"),
            inverseJoinColumns = @JoinColumn(name = "roleid")
    )
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewVote> votes;

    @Formula("(SELECT COALESCE(SUM(s.credits), 0) " +
            "FROM subjects s JOIN usersubjectprogress up ON s.id = up.idsub " +
            "WHERE up.iduser = id AND up.subjectstate = 1)")
    private int creditsDone;

    private User(final Builder builder) {
        this.id = builder.id;
        this.email = builder.email;
        this.password = builder.password;
        this.username = builder.username;
        this.imageId = builder.imageId;
        this.confirmToken = builder.confirmToken;
        this.locale = builder.locale;
        this.confirmed = builder.confirmed;
        this.degree = builder.degree;
        this.roles = new HashSet<>();
        this.reviews = new ArrayList<>();
        this.votes = new ArrayList<>();
        this.subjectProgress = new HashMap<>();
    }

    protected User() {}

    public int getCreditsDone(){
        return creditsDone;
    }

    public boolean isEditor() {
        return roles.stream().anyMatch(role -> role.getName().equals("EDITOR"));
    }

    public long getImageId(){
        return imageId;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public Optional<String> getConfirmToken() {
        return Optional.ofNullable(confirmToken);
    }
    public boolean isConfirmed() {
        return confirmed;
    }

    public Map<String, SubjectProgress> getSubjectProgress() {
        return subjectProgress;
    }

    public Optional<Locale> getLocale(){
        return Optional.ofNullable(locale);
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public List<ReviewVote> getVotes() {
        return votes;
    }

    public Map<Review, ReviewVote> getVotesByReview() {
        final Map<Review, ReviewVote> res = new HashMap<>();

        for(ReviewVote vote : votes) {
            res.put(vote.getReview(), vote);
        }

        return res;
    }

    public Degree getDegree() {
        return degree;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setConfirmToken(String confirmToken) {
        this.confirmToken = confirmToken;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setSubjectProgress(Map<String, SubjectProgress> subjectProgress) {
        this.subjectProgress = subjectProgress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return confirmed == user.confirmed && Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(username, user.username) && Objects.equals(imageId, user.imageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builderFrom(final User user) {
        return new Builder(user);
    }

    public static class Builder {
        private Long id;
        private String email;
        private String password, username;
        private long imageId;
        private String confirmToken;
        private boolean confirmed;
        private Locale locale;

        private Degree degree;


        private Builder() {

        }

        private Builder(final User user) {
            this.id = user.id;
            this.email = user.email;
            this.password = user.password;
            this.username = user.username;
            this.imageId = user.imageId;
            this.confirmToken = user.confirmToken;
            this.confirmed = user.confirmed;
            this.locale = user.locale;
            this.degree = user.degree;
        }

        public Builder id(final long id) {
            this.id = id;
            return this;
        }

        public Builder email(final String email) {
            this.email = email;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder imageId(long imageId) {
            this.imageId = imageId;
            return this;
        }

        public Builder confirmToken(String token) {
            this.confirmToken = token;
            return this;
        }

        public Builder confirmed(boolean confirmed) {
            this.confirmed = confirmed;
            return this;
        }

        public Builder locale(Locale locale) {
            this.locale = locale;
            return this;
        }

        public Builder degree(Degree degree){
            this.degree = degree;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
