package ar.edu.itba.paw.models;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class User {
    private final Long id;
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
        private Long imageId;
        private Map<String, Integer> subjectProgress;
        private String confirmToken;
        private boolean confirmed;
        private Locale locale;


        private Builder() {

        }

        private Builder(final User user) {
            this.id = user.id;
            this.email = user.email;
            this.password = user.password;
            this.username = user.username;
            this.imageId = user.imageId;
            this.subjectProgress = user.subjectProgress;
            this.confirmToken = user.confirmToken;
            this.confirmed = user.confirmed;
            this.locale = user.locale;
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

        public Builder subjectProgress(final Map<String, Integer> subjectProgress){
            this.subjectProgress = subjectProgress;
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

        public User build() {
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
