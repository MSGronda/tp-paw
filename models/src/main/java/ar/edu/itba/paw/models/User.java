package ar.edu.itba.paw.models;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class User {
    private final long id;
    private final String email, password, username;
    private final Long imageId;
    private final Map<String, Integer> subjectProgress;
    private final String confirmToken;
    private final boolean confirmed;
    private final Locale locale;


    private User(final Builder builder) {
        this.id = builder.id;
        this.email = builder.email;
        this.password = builder.password;
        this.username = builder.username;
        this.imageId = builder.imageId;
        this.subjectProgress = builder.subjectProgress;
        this.confirmToken = builder.confirmToken;
        this.locale = builder.locale;
        this.confirmed = builder.confirmed;
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

    public Map<String, Integer> getSubjectProgress() {
        return subjectProgress;
    }

    public Optional<Locale> getLocale(){
        return Optional.ofNullable(locale);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(username, user.username) && Objects.equals(imageId, user.imageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static Builder builder(final String email, final String password, final String username) {
        return new Builder(email, password, username);
    }

    public static class Builder {
        private long id;
        private final String email;
        private String password, username;
        private long imageId;
        private Map<String, Integer> subjectProgress;
        private String confirmToken;
        private boolean confirmed;
        private Locale locale;


        private Builder(final String email, final String password, final String username) {
            this.email = email;
            this.password = password;
            this.username = username;
        }
        public Builder id(final long id) {
            this.id = id;
            return this;
        }
        public Map<String, Integer> getSubjectProgress(){
            return this.subjectProgress;
        }
        public Builder subjectProgress(final Map<String, Integer> subjectProgress){
            this.subjectProgress = subjectProgress;
            return this;
        }
        public Long getImageId(){
            return this.imageId;
        }
        public Builder imageId(long imageId) {
            this.imageId = imageId;
            return this;
        }
        public Long getId() {
            return id;
        }
        public String getEmail(){
            return email;
        }


        public Builder username(String username) {
            this.username = username;
            return this;
        }
        public String getUsername(){
            return username;
        }


        public Builder password(String password) {
            this.password = password;
            return this;
        }
        public String getPassword(){
            return password;
        }

        public Optional<String> getConfirmToken(){
            return Optional.ofNullable(confirmToken);
        }

        public Builder confirmToken(String token) {
            this.confirmToken = token;
            return this;
        }

        public boolean getConfirmed() {
            return confirmed;
        }

        public Builder confirmed(boolean confirmed) {
            this.confirmed = confirmed;
            return this;
        }

        public Locale getLocale(){
            return locale;
        }
        public Builder locale(Locale locale) {
            this.locale = locale;
            return this;
        }

        public User build() {
            if(this.username == null) {
                throw new NullPointerException("Property \"name\" is null");
            }
            if(this.email == null) {
                throw new NullPointerException("Property \"email\" is null");
            }
            if(this.password == null) {
                throw new NullPointerException("Property \"password\" is null");
            }
            return new User(this);
        }
    }

    public enum SubjectProgressEnum {

        PENDING(0),
        DONE(1);

        private final int progress;
        SubjectProgressEnum(int progress){
            this.progress = progress;
        }
        public int getProgress() {
            return progress;
        }
        public static SubjectProgressEnum getByInt(int progress){
            for(SubjectProgressEnum p : SubjectProgressEnum.values()){
                if(p.progress == progress){
                    return p;
                }
            }
            throw new IllegalArgumentException(String.valueOf(progress));
        }
    }
}
