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
    private String verificationToken;

    @Column(name = "image_id")
    private long imageId;

    @ElementCollection
    @CollectionTable(
            name = "usersubjectprogress",
            joinColumns = @JoinColumn(name = "iduser", referencedColumnName = "id")
    )
    @MapKeyColumn(name = "idsub")
    @Column(name = "subjectstate")
    private Map<String, SubjectProgress> allSubjectProgress;

    @Column(name = "confirmed")
    private boolean verified;

    @Column(name = "locale", length = 32)
    private String locale;

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<ReviewVote> votes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<UserSemesterSubject> userSemester;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private RecoveryToken recoveryToken;

    @Formula("(SELECT COALESCE(SUM(s.credits), 0) " +
            "FROM subjects s JOIN subjectsdegrees sd ON s.id = sd.idsub JOIN usersubjectprogress up ON s.id = up.idsub " +
            "WHERE up.subjectstate = 1 AND up.iduser = id AND sd.iddeg = degreeid)")
    private int creditsDone;

    private User(final Builder builder) {
        this.id = builder.id;
        this.email = builder.email;
        this.password = builder.password;
        this.username = builder.username;
        this.imageId = builder.imageId;
        this.verificationToken = builder.confirmToken;
        this.locale = builder.locale == null ? null : builder.locale.toLanguageTag();
        this.verified = builder.confirmed;
        this.degree = builder.degree;
        this.roles = new HashSet<>();
        this.reviews = new ArrayList<>();
        this.votes = new ArrayList<>();
        this.allSubjectProgress = new HashMap<>();
        this.userSemester = new ArrayList<>();
    }

    protected User() {}

    public List<UserSemesterSubject> getUserSemester() {
        return userSemester;
    }

    public boolean hasSemester(){
        return userSemester.stream().anyMatch(UserSemesterSubject::isActive);
    }

    public int getCreditsDone(){
        return creditsDone;
    }

    public int getCreditsDoneForDegree(){
        int creditsDone = 0;
        for(DegreeYear dy : degree.getYears()){
            for(Subject s : dy.getSubjects()){
                if(allSubjectProgress.containsKey(s.getId())){
                    creditsDone += s.getCredits();
                }
            }
        }
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

    public Optional<String> getVerificationToken() {
        return Optional.ofNullable(verificationToken);
    }

    public boolean isVerified() {
        return verified;
    }

    public Map<String, SubjectProgress> getAllSubjectProgress() {
        return allSubjectProgress;
    }

    public SubjectProgress getSubjectProgress(final Subject subject) {
        final SubjectProgress progress = allSubjectProgress.get(subject.getId());
        return progress == null ? SubjectProgress.PENDING : progress;
    }

    public String getLocale(){
        return locale == null ? Locale.getDefault().toLanguageTag() : locale;
    }

    public Set<Role> getRoles() {
        return roles;
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

    public RecoveryToken getRecoveryToken() {
        return recoveryToken;
    }

    public double getTotalProgressPercentage() {
        return Math.floor( ((1.0 * getCreditsDone()) / degree.getTotalCredits()) * 100);
    }

    public double getTotalProgressPercentageForDegree() {
        int creditsDone = getCreditsDoneForDegree();
        return Math.floor( ((1.0 * creditsDone) / degree.getTotalCredits()) * 100);
    }

    public Map<Integer, Double> getTotalProgressPercentagePerYear() {
        final Map<Integer, Double> progress = new LinkedHashMap<>();

        for(DegreeYear year: degree.getYears()){
            int credits = 0;
            int totalCredits = 0;
            for(Subject sub : year.getSubjects()){
                if(allSubjectProgress.containsKey(sub.getId())){
                    credits += sub.getCredits();
                }
                totalCredits += sub.getCredits();
            }
            if (totalCredits == 0) totalCredits = 1;
            progress.put(year.getNumber(), 100.0 * credits / totalCredits);
        }

        return progress;
    }

    public double getElectiveProgressPercentage() {
        int credits = 0;
        int totalCredits = 0;
        for(Subject sub : degree.getElectives()){
            if(allSubjectProgress.containsKey(sub.getId())){
                credits += sub.getCredits();
            }
            totalCredits += sub.getCredits();
        }
        if(totalCredits == 0) totalCredits = 1;

        return 100.0 * credits / totalCredits;
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

    public void setVerificationToken(String confirmToken) {
        this.verificationToken = confirmToken;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public void setVerified(boolean confirmed) {
        this.verified = confirmed;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setAllSubjectProgress(Map<String, SubjectProgress> subjectProgress) {
        this.allSubjectProgress = subjectProgress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
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
        private String password;
        private String username;
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
            this.confirmToken = user.verificationToken;
            this.confirmed = user.verified;
            this.locale = Locale.forLanguageTag(user.locale);
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

        public Builder verificationToken(String token) {
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

        public Long getId() {
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

        public long getImageId() {
            return imageId;
        }

        public String getConfirmToken() {
            return confirmToken;
        }

        public boolean isConfirmed() {
            return confirmed;
        }

        public Locale getLocale() {
            return locale;
        }

        public Degree getDegree() {
            return degree;
        }
    }
}
