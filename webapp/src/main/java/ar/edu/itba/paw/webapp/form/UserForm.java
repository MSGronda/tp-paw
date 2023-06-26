package ar.edu.itba.paw.webapp.form;


import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

public class UserForm {
    @Email
    @NotBlank
    private String email;

    @NotNull
    @Size(min = 8, max = 25)
    private String password;

    @NotBlank
    @Size(max = 20)
    @Pattern(regexp = "^\\p{L}(\\p{L}|\\s|_)*$")
    private String name;

    private String passwordConfirmation;

    @NotNull
    private long degreeId;

    private String subjectIds;

    @AssertTrue
    public boolean isPasswordConfirmationEqual() {
        return Objects.equals(password, passwordConfirmation);
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public long getDegreeId() {
        return degreeId;
    }

    public void setDegreeId(long degreeId) {
        this.degreeId = degreeId;
    }

    public String getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(String subjectIds) {
        this.subjectIds = subjectIds;
    }
}
