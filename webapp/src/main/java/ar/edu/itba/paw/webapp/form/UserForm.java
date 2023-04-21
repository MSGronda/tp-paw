package ar.edu.itba.paw.webapp.form;


import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserForm {
    @Email
    @NotBlank
    private String email;

    @Size(min = 8, max = 25)
    private String password;

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotNull
    @Size(min = 8, max = 25)
    private String passwordConfirmation;

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
        checkConfirmPassword();
    }
    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }
    private void checkConfirmPassword() {
        if(!this.password.equals(passwordConfirmation)){
            this.passwordConfirmation = null;
        }
    }
}
