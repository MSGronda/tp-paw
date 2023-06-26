package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class EditUserDataForm {

    @NotBlank
    @Size(max = 20)
    @Pattern(regexp = "^\\p{L}(\\p{L}|\\s|_)*$")
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
