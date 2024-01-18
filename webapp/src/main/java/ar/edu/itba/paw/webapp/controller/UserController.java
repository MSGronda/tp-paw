package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exceptions.*;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.dto.UserProgressDto;
import ar.edu.itba.paw.webapp.dto.UserSemesterDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.form.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("users")
@Component
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthUserService authUserService;
    @Context
    private UriInfo uriInfo;

    // Register ahora se va a encargar de unicamente crear un usuario
    // Posteriormente se va a hacer un patch para cargar el degree y los subjects mediante un interceptor
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
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
    public Response getCurrentUser(){
        final User user = authUserService.getCurrentUser();
        return Response.ok(UserDto.fromUser(uriInfo, user)).build();
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

        if(!user.hasSemester()){
            return Response.noContent().build();
        }

        return Response.ok(UserSemesterDto.fromUser(uriInfo, user)).build();
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
        return Response.ok(UserSemesterDto.fromUser(uriInfo, currentUser)).build();
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
        return Response.ok(UserSemesterDto.fromUser(uriInfo, currentUser)).build();
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
