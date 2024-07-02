package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserSemesterSubject;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserSemesterDto {
    private Long userId;
    private Long dateFinished;
    private Map<String, String> classes;
    private URI planSubjects;
    private URI subjectProgress;        // TODO: remove?

    public static UserSemesterDto fromSemesterEntry(final UriInfo uriInfo, final User user, final List<UserSemesterSubject> userSemesterList){
        return fromSemesterEntry(uriInfo, user, null, userSemesterList);
    }

    public static UserSemesterDto fromSemesterEntry(final UriInfo uriInfo, final User user, final Timestamp dateFinished, final List<UserSemesterSubject> userSemesterList){
        final UserSemesterDto planDto = new UserSemesterDto();

        planDto.userId = user.getId();

        if(dateFinished != null){
            planDto.dateFinished = dateFinished.getTime();
        }

        planDto.classes = new HashMap<>();
        userSemesterList.forEach(s -> planDto.classes.put(s.getSubjectClass().getSubject().getId(), s.getSubjectClass().getClassId()));

        planDto.planSubjects = uriInfo.getBaseUriBuilder().path("subjects").queryParam("plan", String.valueOf(user.getId())).build();   // TODO: fix

        planDto.subjectProgress = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(user.getId())).path("progress").build();

        return planDto;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Map<String, String> getClasses() {
        return classes;
    }

    public void setClasses(Map<String, String> classes) {
        this.classes = classes;
    }

    public URI getPlanSubjects() {
        return planSubjects;
    }

    public void setPlanSubjects(URI planSubjects) {
        this.planSubjects = planSubjects;
    }

    public URI getSubjectProgress() {
        return subjectProgress;
    }

    public void setSubjectProgress(URI subjectProgress) {
        this.subjectProgress = subjectProgress;
    }

    public Long getDateFinished() {
        return dateFinished;
    }

    public void setDateFinished(Long dateFinished) {
        this.dateFinished = dateFinished;
    }
}
