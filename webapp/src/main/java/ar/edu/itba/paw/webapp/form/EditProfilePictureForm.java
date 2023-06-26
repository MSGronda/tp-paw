package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.constraints.annotations.ImageType;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;


public class EditProfilePictureForm {

    @NotNull
    @ImageType(types = {"image/png", "image/jpeg", "image/gif"})
    private MultipartFile profilePicture;

    public void setProfilePicture(MultipartFile profilePicture) {
        this.profilePicture = profilePicture;
    }

    public MultipartFile getProfilePicture() {
        return profilePicture;
    }

}
