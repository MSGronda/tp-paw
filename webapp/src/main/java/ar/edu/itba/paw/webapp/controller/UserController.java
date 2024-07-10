package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserSemesterSubject;
import ar.edu.itba.paw.models.exceptions.*;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.controller.utils.PATCH;
import ar.edu.itba.paw.webapp.dto.UserProgressDto;
import ar.edu.itba.paw.webapp.dto.UserSemesterDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.form.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("users")
@Component
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;
    @Autowired
    private AuthUserService authUserService;
    @Context
    private UriInfo uriInfo;

    // Register ahora se va a encargar de unicamente crear un usuario
    // Posteriormente se va a hacer un patch para cargar el degree y los subjects mediante un interceptor
    @POST
    @Consumes("application/vnd.user.register.v1+json")
    public Response registerUser(
            @Valid @ModelAttribute("userForm") final UserForm userForm){
        final User newUser = userService.create(
                User.builder()
                        .email(userForm.getEmail())
                        .password(userForm.getPassword())
                        .username(userForm.getName())
                        .locale(LocaleContextHolder.getLocale())
                        .build()
        );
        final URI userUri = uriInfo.getBaseUriBuilder().path("/users").path(String.valueOf(newUser.getId())).build();
        return Response.created(userUri).build();
    }
    
    @POST
    @Consumes("application/vnd.user.confirm.v1+json")
    public Response confirm(@Valid ConfirmUserForm form) {
        try {
            userService.confirmUser(form.getToken());
            return Response.ok().build();
        } catch (InvalidTokenException e) {
            LOGGER.info("Invalid confirmation token '{}'", form.getToken());
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
    
    @POST
    @Consumes("application/vnd.user.recover.request.v1+json")
    public Response requestRecover(@Valid RecoverPasswordRequestForm form) {
        try {
            userService.sendPasswordRecoveryEmail(form.getEmail());
        } catch(UserNotFoundException e) {
            LOGGER.info("Invalid email for recovery '{}'", form.getEmail());
        }
        
        return Response.accepted().build();
    }
    
    @POST
    @Consumes("application/vnd.user.recover.v1+json")
    public Response recover(@Valid RecoverPasswordForm form) {
        try {
            userService.recoverPassword(form.getToken(), form.getPassword());
            return Response.ok().build();
        } catch (InvalidTokenException e) {
            LOGGER.info("Invalid token for recovery '{}'", form.getToken());
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editUser(@PathParam("id") final Long id, @Valid final EditUserForm editUserForm){
        userService.updateUser(id, authUserService.getCurrentUser(),
                editUserForm.getUserName(),
                editUserForm.getOldPassword(),
                editUserForm.getNewPassword(),
                editUserForm.getDegreeId(),
                editUserForm.getSubjectIds()
        );
        return Response.ok().build();
    }
    
    @POST
    @Path("/{id}/picture")
    @Consumes({"image/jpeg", "image/png"})
    public Response updatePicture(@PathParam("id") final Long id, final byte[] picture){
        final User user = userService.findById(id).orElseThrow(UserNotFoundException::new);
        userService.updateProfilePicture(user, picture);
        
        final URI imgUri = uriInfo.getBaseUriBuilder().path("image").path(String.valueOf(user.getImageId())).build();
        return Response.created(imgUri).build();
    }

    @GET
    @Produces("application/vnd.user.v1+json")
    public Response getUsers(
            @QueryParam("subjectId") final String subjectId,
            @QueryParam("page") @DefaultValue("0") final Integer page
    ){
        final List<User> users = userService.getUsers(subjectId, page);
        if( users.isEmpty() ){
            return Response.noContent().build();
        }
        final List<UserDto> userDtos = users.stream().map(u -> UserDto.fromUser(uriInfo, u)).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<UserDto>>(userDtos){}).build();
    }


    @GET
    @Path("/{id}")
    @Produces("application/vnd.user.v1+json")
    public Response getUser(@PathParam("id") final Long id){
        final User user = userService.findById(id).orElseThrow(UserNotFoundException::new);
        return Response.ok(UserDto.fromUser(uriInfo, user)).build();
    }

    @GET
    @Path("/{id}/plan")
    @Produces("application/vnd.user-plan.v1+json")
    public Response getUserSemester(@PathParam("id") final Long id){
        final User user = userService.findById(id).orElseThrow(UserNotFoundException::new);

        final Map<Timestamp, List<UserSemesterSubject>> semesters = userService.getUserSemesters(user);

        if(semesters.isEmpty())
            return Response.noContent().build();

        final List<UserSemesterDto> semesterDtos = semesters.entrySet().stream().map(e ->
                UserSemesterDto.fromSemesterEntry(uriInfo, user, e.getKey(), e.getValue())
        ).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<UserSemesterDto>>(semesterDtos){}).build();
    }

    @POST
    @Path("/{id}/plan")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/vnd.user-plan.v1+json")
    public Response createUserSemester(
            @PathParam("id") final Long id,
            @Valid final UserSemesterForm userSemesterForm
    ){
        final User currentUser = authUserService.getCurrentUser();
        userService.createUserSemester(currentUser, id, userSemesterForm.getIdSub(), userSemesterForm.getIdClass());

        return Response.ok(UserSemesterDto.fromSemesterEntry(uriInfo, currentUser, userService.getCurrentUserSemester(currentUser))).build();
    }

    @PUT
    @Path("/{id}/plan")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/vnd.user-plan.v1+json")
    public Response replaceUserSemester(
            @PathParam("id") final Long id,
            @Valid final UserSemesterForm userSemesterForm
    ){
        final User currentUser = authUserService.getCurrentUser();
        userService.replaceUserSemester(currentUser, id, userSemesterForm.getIdSub(), userSemesterForm.getIdClass());
        return Response.accepted().build();
    }

    @PATCH
    @Path("/{id}/plan")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response alterUserSemester(
            @PathParam("id") final Long id,
            @Valid final UserSemesterEditForm userSemesterEditForm
    ){
        final User currentUser = authUserService.getCurrentUser();
        userService.editUserSemester(
                currentUser,
                id,
                userSemesterEditForm.getEditType(),
                userSemesterEditForm.getSubjectIds(),
                userSemesterEditForm.getClassIds()
        );

        return Response.accepted().build();
    }

    @DELETE
    @Path("/{id}/plan")
    public Response deleteUserSemester(
            @PathParam("id") final Long id
    ){
        final User currentUser = authUserService.getCurrentUser();
        userService.deleteUserSemester(currentUser, id);

        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/progress")
    @Produces("application/vnd.user-progress.v1+json")
    public Response getUserProgress(
        @PathParam("id") final Long id
    ){
        final User user = userService.findById(id).orElseThrow(UserNotFoundException::new);

        return Response.ok(UserProgressDto.fromUser(uriInfo, user)).build();
    }

    @PATCH
    @Path("/{id}/progress")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCompletedSubjects(
        @PathParam("id") final Long id,
        @Valid final UserProgressForm userProgressForm
    ){
        final User currentUser = authUserService.getCurrentUser();

        userService.updateMultipleSubjectProgress(currentUser, id, userProgressForm.getNewPassedSubjects(), userProgressForm.getNewNotPassedSubjects());

        return Response.accepted().build();
    }
}
