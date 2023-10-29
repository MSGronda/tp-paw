package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.SubjectClass;
import ar.edu.itba.paw.models.User;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class UserSemesterDto {
    private Long userId;
    private Map<String, String> classes;
    private URI planSubjects;

    public static UserSemesterDto fromUser(final UriInfo uriInfo, final User user){
        final UserSemesterDto planDto = new UserSemesterDto();

        planDto.userId = user.getId();
        planDto.classes = new HashMap<>();

        for(final SubjectClass sc : user.getUserSemester()){
            planDto.classes.put(sc.getSubject().getId(), sc.getClassId());
        }

        planDto.planSubjects = uriInfo.getBaseUriBuilder().path("subjects").queryParam("userPlan", String.valueOf(user.getId())).build();

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
}
