package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.NotEmpty;

public class ConfirmUserForm {
    @NotEmpty
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
