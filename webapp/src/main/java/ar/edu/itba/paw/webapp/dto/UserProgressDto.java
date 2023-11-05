package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.SubjectProgress;

import javax.ws.rs.core.UriInfo;
import java.util.Map;

public class UserProgressDto {
    private Map<String, SubjectProgress> subjectProgress;

    private Double totalProgress;

    public static UserProgressDto fromUser(final UriInfo uriInfo, final User user){
        final UserProgressDto userProgressDto = new UserProgressDto();

        userProgressDto.subjectProgress = user.getAllSubjectProgress();
        userProgressDto.totalProgress = user.getTotalProgressPercentage();

        return userProgressDto;
    }

    public Map<String, SubjectProgress> getSubjectProgress() {
        return subjectProgress;
    }

    public void setSubjectProgress(Map<String, SubjectProgress> subjectProgress) {
        this.subjectProgress = subjectProgress;
    }

    public Double getTotalProgress() {
        return totalProgress;
    }

    public void setTotalProgress(Double totalProgress) {
        this.totalProgress = totalProgress;
    }
}
