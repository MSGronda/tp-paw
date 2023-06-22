package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class EditUserPasswordForm {

    private String oldPassword;

    @NotNull
    @Size(min = 8, max = 25)
    private String newPassword;

    private String newPasswordConfirmation;

    @AssertTrue
    public boolean isPasswordConfirmationEqual() {
        return Objects.equals(newPassword, newPasswordConfirmation);
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    public String getNewPassword(){
        return newPassword;
    }

    public void setNewPasswordConfirmation(String passwordConfirmation) {
        this.newPasswordConfirmation = passwordConfirmation;
    }
    public String getNewPasswordConfirmation() {
        return newPasswordConfirmation;
    }

    public String getOldPassword() {
        return oldPassword;
    }
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
