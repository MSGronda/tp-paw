package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class RecoverPasswordEditForm {
    @NotNull
    @Size(min = 8, max = 25)
    private String password;

    private String passwordConfirmation;

    @AssertTrue
    public boolean isPasswordConfirmationEqual() {
        return Objects.equals(password, passwordConfirmation);
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String pass) {
        this.password = pass;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }
}
