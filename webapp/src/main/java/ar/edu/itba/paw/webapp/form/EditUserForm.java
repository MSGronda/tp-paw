package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

public class EditUserForm {
    @Size(max = 20)
    @Pattern(regexp = "^\\p{L}(\\p{L}|\\s|_)*$")
    private String userName;
    private String oldPassword;
    @Size(min = 8, max = 25)
    private String newPassword;
    private Long degreeId;
    private List<String> subjectIds;
    private Long imageId;
    private Boolean makeModerator;

    public void setMakeModerator(Boolean makeModerator) {
        this.makeModerator = makeModerator;
    }
    public Boolean getMakeModerator() {
        return makeModerator;
    }

    public Long getImageId() {
        return imageId;
    }
    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Long getDegreeId() {
        return degreeId;
    }

    public void setDegreeId(Long degreeId) {
        this.degreeId = degreeId;
    }

    public List<String> getSubjectIds() {
        return subjectIds;
    }
    public void setSubjectIds(List<String> subjectIds) {
        this.subjectIds = subjectIds;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    public String getNewPassword(){
        return newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
