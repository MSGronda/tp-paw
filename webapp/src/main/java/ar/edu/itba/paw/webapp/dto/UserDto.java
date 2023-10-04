package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.User;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class UserDto {
    private Long id;
    private String email;
    private String username;

    //TODO - Image

    //private Locale locale
    private URI degree;
    //TODO - ROLES ?
    private URI reviews;

    //TODO - ReviewVote

    private URI userSemester;

    //TODO - Recovery Token ?

    private int creditsDone;

    public static UserDto fromUser(final UriInfo uriInfo, final User user){
        if( user == null)
            return null;
        UserDto userDto = new UserDto();
        userDto.id = user.getId();
        userDto.email = user.getEmail();
        userDto.username = user.getUsername();
        userDto.degree = uriInfo.getBaseUriBuilder().path("degrees").path(String.valueOf(user.getDegree().getId())).build();
        userDto.reviews = uriInfo.getBaseUriBuilder().path("reviews").queryParam("userId", user.getId()).build();
        //TODO - CHECK THIS
        userDto.userSemester = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(user.getId())).path("plan").build();
        userDto.creditsDone = user.getCreditsDone();
        return userDto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public URI getDegree() {
        return degree;
    }

    public void setDegree(URI degree) {
        this.degree = degree;
    }

    public URI getReviews() {
        return reviews;
    }

    public void setReviews(URI reviews) {
        this.reviews = reviews;
    }

    public URI getUserSemester() {
        return userSemester;
    }

    public void setUserSemester(URI userSemester) {
        this.userSemester = userSemester;
    }

    public int getCreditsDone() {
        return creditsDone;
    }

    public void setCreditsDone(int creditsDone) {
        this.creditsDone = creditsDone;
    }
}
