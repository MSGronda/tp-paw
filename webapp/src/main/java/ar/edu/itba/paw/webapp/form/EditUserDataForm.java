package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.validation.constraints.Size;

public class EditUserDataForm {

    @NotBlank
    @Size(max = 50)
    private String userName;

    private CommonsMultipartFile profileImage;


    public CommonsMultipartFile getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(CommonsMultipartFile profileImage) {
        this.profileImage = profileImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
