package ar.edu.itba.paw.webapp.form;

import org.springframework.web.multipart.MultipartFile;


public class EditProfilePictureForm {

    private MultipartFile profilePicture;

    public void setProfilePicture(MultipartFile profilePicture) {
        this.profilePicture = profilePicture;
    }

    public MultipartFile getProfilePicture() {
        return profilePicture;
    }

}
