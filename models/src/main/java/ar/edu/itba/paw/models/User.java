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
    private final Optional<String> confirmToken;
    private final Locale locale;


    public User(final UserBuilder builder) {
        this.id = builder.id;
        this.email = builder.email;
        this.password = builder.password;
        this.username = builder.username;
        this.imageId = builder.imageId;
        this.subjectProgress = builder.subjectProgress;
        this.confirmToken = builder.confirmToken;
        this.locale = builder.locale;
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
        return confirmToken;
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

    public static class UserBuilder {
        private long id;
        private final String email;
        private String password, username;
        private long imageId;
        private Map<String, Integer> subjectProgress;
        private Optional<String> confirmToken;
        private Locale locale;


        public UserBuilder(final String email, final String password, final String username) {
            this.email = email;
            this.password = password;
            this.username = username;
            this.confirmToken = Optional.empty();
        }
        public UserBuilder id(final long id) {
            this.id = id;
            return this;
        }
        public Map<String, Integer> getSubjectProgress(){
            return this.subjectProgress;
        }
        public UserBuilder subjectProgress(final Map<String, Integer> subjectProgress){
            this.subjectProgress = subjectProgress;
            return this;
        }
        public Long getImageId(){
            return this.imageId;
        }
        public UserBuilder imageId(long imageId) {
            this.imageId = imageId;
            return this;
        }
        public Long getId() {
            return id;
        }
        public String getEmail(){
            return email;
        }


        public UserBuilder username(String username) {
            this.username = username;
            return this;
        }
        public String getUsername(){
            return username;
        }


        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }
        public String getPassword(){
            return password;
        }

        public Optional<String> getConfirmToken(){
            return confirmToken;
        }

        public UserBuilder confirmToken(String token) {
            this.confirmToken = Optional.of(token);
            return this;
        }

        public Locale getLocale(){
            return locale;
        }
        public UserBuilder locale(Locale locale) {
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
}
