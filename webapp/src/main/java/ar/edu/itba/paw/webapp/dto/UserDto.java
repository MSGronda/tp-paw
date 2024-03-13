package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.User;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

public class UserDto {
    private Long id;
    private String email;
    private String username;

    private URI image;

    //private Locale locale
    private URI degree;

    private List<String> roles;

    private URI reviews;

    //TODO - ReviewVote

    private URI userSemester;

    //TODO - Recovery Token ?

    private int creditsDone;

    public static UserDto fromUser(final UriInfo uriInfo, final User user){
        if( user == null)
            return null;
        final UserDto userDto = new UserDto();
        userDto.id = user.getId();
        userDto.email = user.getEmail();
        userDto.username = user.getUsername();
        userDto.image = uriInfo.getBaseUriBuilder().path("images").path(String.valueOf(user.getImageId())).build();
        userDto.roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
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

    public URI getImage() {
        return image;
    }

    public void setImage(URI image) {
        this.image = image;
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

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
