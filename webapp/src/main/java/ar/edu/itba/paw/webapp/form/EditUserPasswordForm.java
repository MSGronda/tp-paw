package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EditUserPasswordForm {

    @NotNull
    @Size(min = 8, max = 25)
    private String editPassword;
    @NotNull
    @Size(min = 8, max = 25)
    private String passwordEditConfirmation;

    public void setEditPassword(String editPassword) {
        this.editPassword = editPassword;
    }

    public String getEditPassword(){
        return editPassword;
    }
    public void setPasswordEditConfirmation(String passwordConfirmation) {
        this.passwordEditConfirmation = passwordConfirmation;
        checkConfirmEditPassword();
    }
    public String getPasswordEditConfirmation() {
        return passwordEditConfirmation;
    }
    private void checkConfirmEditPassword() {
        if(!this.editPassword.equals(passwordEditConfirmation)){
            this.passwordEditConfirmation = null;
        }
    }
}
