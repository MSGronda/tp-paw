package ar.edu.itba.paw.models;

import java.util.Objects;

public class User {
    private final long id;
    private final String email, password, username;

    public User(UserBuilder builder) {
        this.id = builder.id;
        this.email = builder.email;
        this.password = builder.password;
        this.username = builder.username;
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
    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getId() == user.getId() && Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getPassword() ,user.getPassword()) && Objects.equals(getUsername(), user.getUsername());
    }

    public static class UserBuilder {
        private long id;
        private final String email;
        private String password, username;

        public UserBuilder(String email, String password, String username) {
            this.email = email;
            this.password = password;
            this.username = username;
        }
        public UserBuilder id(long id) {
            this.id = id;
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
