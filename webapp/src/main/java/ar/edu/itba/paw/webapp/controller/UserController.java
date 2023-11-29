package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.SubjectClass;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exceptions.*;
import ar.edu.itba.paw.services.AuthUserService;
import ar.edu.itba.paw.services.DegreeService;
import ar.edu.itba.paw.services.ReviewService;
import ar.edu.itba.paw.services.UserService;
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
import java.util.List;

@Path("users")
@Component
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private AuthUserService authUserService;
    @Autowired
    private DegreeService degreeService;
    @Context
    private UriInfo uriInfo;

//    @Autowired
//    public UserController(
//            final UserService userService,
//            final ReviewService reviewService,
//            final AuthUserService authUserService,
//            final DegreeService degreeService
//    ) {
//        this.userService = userService;
//        this.reviewService = reviewService;
//        this.authUserService = authUserService;
//        this.degreeService = degreeService;
//    }

    /*
    * @RequestMapping("/user/{id:\\d+}")
    public ModelAndView user(
            @PathVariable final long id,
            @RequestParam(defaultValue = "1") final int page,
            @RequestParam(defaultValue = "difficulty") final String order,
            @RequestParam(defaultValue = "desc") final String dir
    ) {
        final User user = userService.findById(id).orElseThrow(UserNotFoundException::new);

        if(authUserService.getCurrentUser().getId() == id){
            return new ModelAndView("redirect:/profile");
        }

        final ModelAndView mav = new ModelAndView("user/userProfile");
        return setProfileViewAttributes(mav, user, page, order, dir);
    }

    @RequestMapping("/profile")
    public ModelAndView profile(
            @RequestParam(defaultValue = "1") final int page,
            @RequestParam(defaultValue = "difficulty") final String order,
            @RequestParam(defaultValue = "desc") final String dir
    ) {
        final ModelAndView mav = new ModelAndView("/user/profile");
        return setProfileViewAttributes(mav, authUserService.getCurrentUser(), page, order, dir);
    }

    private ModelAndView setProfileViewAttributes(
            final ModelAndView mav,
            final User user,
            final int page,
            final String orderBy,
            final String dir
    ){
        mav.addObject("reviews", reviewService.getAllUserReviews(user, page, orderBy, dir));
        mav.addObject("totalPages", reviewService.getTotalPagesForUserReviews(user));
        mav.addObject("user", user);
        mav.addObject("currentPage", page);
        mav.addObject("order", orderBy);
        mav.addObject("dir", dir);

        return mav;
    }

    //DONE
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(
            @Valid @ModelAttribute ("UserForm") final UserForm userForm,
            final BindingResult errors,
            final Locale locale
    ) {
        if(errors.hasErrors()){
            return registerForm(userForm);
        }

        final User newUser;
        try {
            newUser = userService.create(
                    userForm.getDegreeId(),
                    userForm.getSubjectIds(),
                    User.builder()
                            .email(userForm.getEmail())
                            .password(userForm.getPassword())
                            .username(userForm.getName())
                            .locale(locale)
                            .build()
            );
        } catch (EmailAlreadyTakenException e){
            final ModelAndView mav = registerForm(userForm);
            mav.addObject("EmailAlreadyUsed", true);
            return mav;
        } catch (DegreeNotFoundException e) {
            throw new InvalidFormException(e);
        }

        final ModelAndView mav = new ModelAndView("redirect:/verification");
        mav.addObject("email", newUser.getEmail());
        return mav;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView registerForm(@ModelAttribute ("UserForm") final UserForm userForm) {
        final ModelAndView mav = new ModelAndView("user/register");
        mav.addObject("degrees", degreeService.getAll());
        return mav;
    }

    @RequestMapping("/verification")
    public ModelAndView confirmPage(@RequestParam final String email) {
        final Optional<User> maybeUser = userService.findUnconfirmedByEmail(email);
        if(!maybeUser.isPresent()) {
            return new ModelAndView("redirect:/login");
        }

        final ModelAndView mav = new ModelAndView("user/verification/checkEmail");
        mav.addObject("user", maybeUser.get());
        return mav;
    }

    @RequestMapping(value = "/verification/resend", method = RequestMethod.POST)
    public ModelAndView resendConfirmationEmail(@RequestParam final String email) {
        final ModelAndView mav = new ModelAndView("redirect:/verification");
        mav.addObject("email", email);

        try {
            userService.resendVerificationEmail(email);
        } catch (UserNotFoundException e) {
            mav.addObject("error", true);
            return mav;
        }

        mav.addObject("resent", true);
        return mav;
    }

    @RequestMapping("/verification/confirm")
    public String confirm(@RequestParam final String token) {
        try {
            userService.confirmUser(token);
        } catch (InvalidTokenException e) {
            return "user/verification/invalidToken";
        }
        return "user/verification/success";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(@RequestParam (value="error", defaultValue = "false") boolean error) {
        final ModelAndView mav = new ModelAndView("user/login");
        mav.addObject("error", error);
        return mav;
    }

    @RequestMapping(value = "/profile/editdata", method = RequestMethod.POST)
    public ModelAndView editProfile(
            @Valid @ModelAttribute ("EditUserDataForm") final EditUserDataForm editUserDataForm,
            final BindingResult errors
    ) {
        if(errors.hasErrors()){
            return editProfileForm(editUserDataForm);
        }

        userService.editProfile(authUserService.getCurrentUser(), editUserDataForm.getUserName());

        return new ModelAndView("redirect:/profile");
    }

    @RequestMapping(value = "/profile/editdata", method = RequestMethod.GET)
    public ModelAndView editProfileForm(@ModelAttribute ("EditUserDataForm") final EditUserDataForm editUserDataForm) {
        final ModelAndView mav = new ModelAndView("user/editUserData");
        mav.addObject("user", authUserService.getCurrentUser());
        return mav;
    }

    @RequestMapping(value = "/profile/editpassword", method = RequestMethod.POST)
    public ModelAndView editPassword(
            @Valid @ModelAttribute ("EditUserPasswordForm") final EditUserPasswordForm editUserPasswordForm,
            final BindingResult errors
    ) {
        if(errors.hasErrors()){
            return editPasswordForm(editUserPasswordForm);
        }

        try{
            userService.changePassword(authUserService.getCurrentUser(), editUserPasswordForm.getNewPassword(), editUserPasswordForm.getOldPassword());
        }catch (OldPasswordDoesNotMatchException e){
            ModelAndView mav = editPasswordForm(editUserPasswordForm);
            mav.addObject("oldPasswordDoesNotMatch", true);
            return mav;
        }

        return new ModelAndView("redirect:/profile");
    }

    @RequestMapping(value = "/profile/editpassword", method = RequestMethod.GET)
    public ModelAndView editPasswordForm(
            @ModelAttribute ("EditUserPasswordForm") final EditUserPasswordForm editUserPasswordForm
    ) {
        return new ModelAndView("user/editUserPassword");
    }

    @RequestMapping(value = "user/{id:\\d+}/moderator")
    public ModelAndView makeModerator(@PathVariable final long id) {
        userService.makeModerator(authUserService.getCurrentUser(), id);

        return new ModelAndView("redirect:/user/" + id);
    }

    @RequestMapping(value = "/recover", method = { RequestMethod.POST })
    public ModelAndView sendRecover(
            @Valid @ModelAttribute ("RecoverPasswordForm") final RecoverPasswordForm recoverPasswordForm,
            final BindingResult errors
    ){
        if(errors.hasErrors()){
            return recoverPassword(recoverPasswordForm);
        }

        try {
            userService.sendPasswordRecoveryEmail(recoverPasswordForm.getEmail());
        } catch (UserNotFoundException e) {
            final ModelAndView mav = recoverPassword(recoverPasswordForm);
            mav.addObject("emailNotFound", true);
            return mav;
        }

        return new ModelAndView("user/recover/emailSent");
    }

    @RequestMapping(value = "/recover", method = { RequestMethod.GET })
    public ModelAndView recoverPassword(
            @ModelAttribute ("RecoverPasswordForm") final RecoverPasswordForm recoverPasswordForm
    ){
        return new ModelAndView("user/recover/index");
    }

    @RequestMapping(value = "/recover/{token}", method = { RequestMethod.POST })
    public ModelAndView recoverPasswordEdit(
            @PathVariable final String token,
            @Valid @ModelAttribute("RecoverPasswordEditForm") final RecoverPasswordEditForm recoverPasswordEditForm,
            final BindingResult errors
    ){
        if(errors.hasErrors()) {
            return recoverPasswordEditForm(token, recoverPasswordEditForm);
        }

        try {
            userService.recoverPassword(token, recoverPasswordEditForm.getPassword());
        } catch (InvalidTokenException e) {
            return new ModelAndView("user/recover/invalidToken");
        }

        return new ModelAndView("user/recover/success");
    }

    @RequestMapping(value = "/recover/{token}", method = { RequestMethod.GET })
    public ModelAndView recoverPasswordEditForm(
            @PathVariable final String token,
            @ModelAttribute("RecoverPasswordEditForm") final RecoverPasswordEditForm form
    ){
        if(!userService.isValidRecoveryToken(token)){
            return new ModelAndView("user/recover/invalidToken");
        }

        final ModelAndView mav = new ModelAndView("user/recover/editPassword");
        mav.addObject("token", token);
        return mav;
    }

    @RequestMapping(value = "/profile/editprofilepicture", method = { RequestMethod.GET })
    public ModelAndView editProfilePictureForm(
            @ModelAttribute ("editProfilePictureForm") final EditProfilePictureForm editProfilePictureForm
    ) {
        final ModelAndView mav = new ModelAndView("user/editProfilePicture");
        mav.addObject("user", authUserService.getCurrentUser());
        return mav;
    }

    @RequestMapping(value="/profile/editprofilepicture", method = {RequestMethod.POST})
    public ModelAndView editProfilePicture(
            @ModelAttribute("editProfilePictureForm") final EditProfilePictureForm editProfilePictureForm,
            final BindingResult errors
    ) {
        if(errors.hasErrors()) {
            return editProfilePictureForm(editProfilePictureForm);
        }

        try {
            userService.updateProfilePicture(
                    authUserService.getCurrentUser(),
                    editProfilePictureForm.getProfilePicture().getBytes()
            );
        } catch (InvalidImageSizeException e) {
            final ModelAndView mav = editProfilePictureForm(editProfilePictureForm);
            mav.addObject("invalidImageSize", true);
            return mav;
        } catch (IOException e) {
            final ModelAndView mav = editProfilePictureForm(editProfilePictureForm);
            mav.addObject("invalidImage", true);
            return mav;
        }

        return new ModelAndView("redirect:/profile");
    }

    @RequestMapping(value = "/subjectProgress", method = RequestMethod.POST)
    @ResponseBody
    public String subjectProgress(
            @Valid @ModelAttribute("SubjectProgressForm") final SubjectProgressForm progressForm
    ) {
        try {
            userService.updateSingleSubjectProgress(
                    authUserService.getCurrentUser(),
                    progressForm.getIdSub(),
                    SubjectProgress.parse(progressForm.getProgress())
            );
        } catch (SubjectNotFoundException e) {
            throw new InvalidFormException(e);
        }

        return "voted";
    }

    @RequestMapping(value = "/select-degree", method = RequestMethod.POST )
    public ModelAndView selectDegreeSubmit(
            @Valid @ModelAttribute ("UpdateDegreeForm") final UpdateDegreeForm updateDegreeForm,
            final BindingResult errors
    ){
        if(errors.hasErrors()){
            return selectDegree(updateDegreeForm);
        }

        userService.updateUserDegreeAndSubjectProgress(
                authUserService.getCurrentUser(),
                degreeService.findById(updateDegreeForm.getDegreeId()).orElseThrow(InvalidFormException::new),
                updateDegreeForm.getSubjectIds()
        );

        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/select-degree", method = RequestMethod.GET )
    public ModelAndView selectDegree(@ModelAttribute ("UpdateDegreeForm") final UpdateDegreeForm updateDegreeForm){
        final User user = authUserService.getCurrentUser();
        if(user.getDegree() != null){
            return new ModelAndView("redirect:/" );
        }

        final ModelAndView mav = new ModelAndView("user/onboarding");
        mav.addObject("degrees", degreeService.getAll());
        mav.addObject("user", user);

        return mav;
    }

    @RequestMapping(value = "/change-degree", method = RequestMethod.POST )
    public ModelAndView changeDegree() {
        final User user = authUserService.getCurrentUser();

        userService.clearDegree(user);

        return new ModelAndView("redirect:/");
    }
    * */

    // = = = = = = = = = = = API = = = = = = = = = = = = =

    //Register ahora se va a encargar de unicamente crear un usuario
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
        final URI userUri = uriInfo.getBaseUriBuilder().path(String.valueOf(newUser.getId())).build();
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
