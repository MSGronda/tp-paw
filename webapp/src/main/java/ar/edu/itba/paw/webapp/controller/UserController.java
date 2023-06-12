package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.SubjectProgress;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.services.exceptions.*;
import ar.edu.itba.paw.webapp.exceptions.RoleNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.SubjectNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UnauthorizedException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.*;
import ar.edu.itba.paw.webapp.form.EditUserDataForm;
import ar.edu.itba.paw.webapp.form.EditUserPasswordForm;
import ar.edu.itba.paw.webapp.form.RecoverPasswordForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import ar.edu.itba.paw.webapp.Utils;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.sql.SQLException;
import java.util.*;

@Controller
public class UserController {
    private final UserService userService;
    private final SubjectService subjectService;
    private final ReviewService reviewService;
    private final MailService mailService;
    private final AuthUserService authUserService;
    private final RolesService rolesService;
    private final DegreeService degreeService;

    private static final String REDIRECT_PROFILE= "redirect:/profile";
    private static final String UNI_IMAGE = "/img/uni.png";

    @Autowired
    public UserController(
            UserService userService,
            SubjectService subjectService,
            ReviewService reviewService,
            MailService mailService,
            AuthUserService authUserService,
            RolesService rolesService,
            DegreeService degreeService
    ) {
        this.userService = userService;
        this.subjectService = subjectService;
        this.reviewService = reviewService;
        this.mailService = mailService;
        this.authUserService = authUserService;
        this.rolesService = rolesService;
        this.degreeService = degreeService;
    }

    @RequestMapping("/user/{id:\\d+}")
    public ModelAndView user(
            @PathVariable long id,
            @RequestParam(required = false, defaultValue = "1") final int page,
            @RequestParam(required = false, defaultValue = "easy") final String order,
            @RequestParam(required = false, defaultValue = "desc") final String dir
    ) {
        final Optional<User> maybeUser = userService.findById(id);
        if(!maybeUser.isPresent()) {
            throw new UserNotFoundException();
        }

        if( authUserService.isAuthenticated() && authUserService.getCurrentUser().getId() == id){
            return new ModelAndView(REDIRECT_PROFILE);
        }

        final User user = maybeUser.get();
        ModelAndView mav = new ModelAndView("user/userProfile");

        return setProfileData(mav, user, page, order, dir);
    }

    @RequestMapping("/profile")
    public ModelAndView profile(
            @RequestParam(required = false, defaultValue = "1") final int page,
            @RequestParam(required = false, defaultValue = "easy") final String order,
            @RequestParam(required = false, defaultValue = "desc") final String dir
    ) {
        ModelAndView mav = new ModelAndView("/user/profile");
        User user = authUserService.getCurrentUser();
        return setProfileData(mav, user, page, order, dir);
    }

    private ModelAndView setProfileData(
            final ModelAndView mav,
            final User user,
            final int page,
            final String orderBy,
            final String dir
    ){
        final List<Review> userReviews = reviewService.getAllUserReviews(user, page, orderBy, dir);
        final int totalPages = reviewService.getTotalPagesForUserReviews(user);

        final Map<Review, ReviewVote> userVotes = user.getVotesByReview();

        if(page > totalPages || page < 1) return new ModelAndView("redirect:/404");

        mav.addObject("user", user);
        mav.addObject("reviews", userReviews);
        mav.addObject("totalPages", totalPages);
        mav.addObject("currentPage", page);
        mav.addObject("userVotes", userVotes);
        mav.addObject("order", orderBy);
        mav.addObject("dir", dir);

        return mav;
    }

    @RequestMapping(value = "/register", method = { RequestMethod.POST })
    public ModelAndView register(@Valid @ModelAttribute ("UserForm") final UserForm userForm,
                                 final BindingResult errors, final Locale locale) throws IOException {
        if(errors.hasErrors()){
            return registerForm(userForm);
        }

        final User newUser;
        try {
            newUser = userService.create(
                    User.builder()
                            .email(userForm.getEmail())
                            .password(userForm.getPassword())
                            .username(userForm.getName())
                            .degree(degreeService.findById(userForm.getDegreeId()).orElseThrow(IllegalStateException::new))
                            .locale(locale)
                            .build()
            );
        }catch (UserEmailAlreadyTakenException e){
            ModelAndView mav = registerForm(userForm);
            mav.addObject("EmailAlreadyUsed", true);
            return mav;
        }

        final String token = newUser.getConfirmToken().orElseThrow(IllegalStateException::new);
        mailService.sendVerification(newUser, token);

        userService.updateSubjectProgressWithSubList(newUser, userForm.getSubjectIds());

        return new ModelAndView("redirect:/verification?email=" + newUser.getEmail());
    }

    @RequestMapping(value = "/register", method = { RequestMethod.GET })
    public ModelAndView registerForm(@ModelAttribute ("UserForm") final UserForm userForm) {
        final ModelAndView mav = new ModelAndView("user/register");
        mav.addObject("degrees", degreeService.getAll());
        return mav;
    }

    @RequestMapping("/verification")
    public ModelAndView confirmPage(@RequestParam final String email, final HttpServletRequest req, final HttpServletResponse res) {
        final Optional<User> maybeUser = userService.findUnconfirmedByEmail(email);
        if(!maybeUser.isPresent()) {
            return new ModelAndView("redirect:/login");
        }

        final User user = maybeUser.get();

        final ModelAndView mav = new ModelAndView("user/verification/checkEmail");
        mav.addObject("user", user);
        return mav;
    }

    @RequestMapping(value = "/verification/resend", method = { RequestMethod.POST })
    public ModelAndView resendConfirmationEmail(@RequestParam final String email) {
        final Optional<User> maybeUser = userService.findUnconfirmedByEmail(email);
        if(!maybeUser.isPresent()) {
            return new ModelAndView("redirect:/verification?error=true&email=" + email);
        }

        final User user = maybeUser.get();
        final String token = userService.regenerateConfirmToken(user);
        mailService.sendVerification(user, token);

        return new ModelAndView("redirect:/verification?resent=true&email=" + email);
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

    @RequestMapping(value = "/login", method = { RequestMethod.GET })
    public ModelAndView login(@RequestParam (value="error", required = false) String error) {
        ModelAndView mav = new ModelAndView("user/login");
        mav.addObject("error", error != null);
        return mav;
    }

    @RequestMapping(value = "/profile/editdata", method = { RequestMethod.POST })
    public ModelAndView editProfile(
            @Valid @ModelAttribute ("EditUserDataForm") final EditUserDataForm editUserDataForm,
            final BindingResult errors
    ) {
        if(errors.hasErrors()){
            return editProfileForm(editUserDataForm);
        }

        final User user = authUserService.getCurrentUser();
        userService.editProfile(user, editUserDataForm.getUserName());
        return new ModelAndView(REDIRECT_PROFILE);
    }

    @RequestMapping(value = "/profile/editdata", method = { RequestMethod.GET })
    public ModelAndView editProfileForm(@ModelAttribute ("EditUserDataForm") final EditUserDataForm editUserDataForm) {
        final ModelAndView mav = new ModelAndView("user/editUserData");
        final User user = authUserService.getCurrentUser();
        mav.addObject("user", user);
        return mav;
    }

    @RequestMapping(value = "/profile/editpassword", method = { RequestMethod.POST })
    public ModelAndView editPassword(
            @Valid @ModelAttribute ("EditUserPasswordForm") final EditUserPasswordForm editUserPasswordForm,
            final BindingResult errors
    ) {
        if(errors.hasErrors()){
            return editPasswordForm(editUserPasswordForm);
        }

        final User user = authUserService.getCurrentUser();
        try{
            userService.changePassword(user, editUserPasswordForm.getEditPassword(), editUserPasswordForm.getOldPassword(), user.getPassword());
        }catch (OldPasswordDoesNotMatchException e){
            ModelAndView mav = editPasswordForm(editUserPasswordForm);
            mav.addObject("oldPasswordDoesNotMatch", true);
            return mav;
        }
        return new ModelAndView(REDIRECT_PROFILE);
    }
    @RequestMapping(value = "/profile/editpassword", method = { RequestMethod.GET })
    public ModelAndView editPasswordForm(@ModelAttribute ("EditUserPasswordForm") final EditUserPasswordForm editUserPasswordForm) {
        return new ModelAndView("user/editUserPassword");
    }

    @RequestMapping(value = "user/{id:\\d+}/moderator")
    public ModelAndView makeModerator(@PathVariable long id) {
        if(!authUserService.isAuthenticated() || !authUserService.isCurrentUserEditor())
            throw new UnauthorizedException();

        final Optional<Role> maybeRole = rolesService.findByName(Role.RoleEnum.EDITOR.getName());
        if(!maybeRole.isPresent()){
            throw new RoleNotFoundException();
        }
        final Role role = maybeRole.get();

        final User toMakeMod = userService.findById(id).orElseThrow(UserNotFoundException::new);
        userService.updateRoles(toMakeMod, role);

        return new ModelAndView("redirect:/user/" + id);
    }

    @RequestMapping(value = "/recover", method = { RequestMethod.POST })
    public ModelAndView sendRecover(
            @Valid @ModelAttribute ("RecoverPasswordForm") final RecoverPasswordForm recoverPasswordForm,
            final BindingResult errors
    ){
        if( errors.hasErrors()){
            return recoverPassword(recoverPasswordForm);
        }

        final String email = recoverPasswordForm.getEmail();

        final Optional<User> maybeUser = userService.findByEmail(email);
        if(!maybeUser.isPresent()) {
            ModelAndView mav = recoverPassword(recoverPasswordForm);
            mav.addObject("emailNotFound", true);
            return mav;
        }
        final User user = maybeUser.get();

        final String token = userService.generateRecoveryToken(user);
        mailService.sendRecover(user, token);

        return new ModelAndView("user/recover/emailSent");
    }

    @RequestMapping(value = "/recover", method = { RequestMethod.GET })
    public ModelAndView recoverPassword(@ModelAttribute ("RecoverPasswordForm") final RecoverPasswordForm recoverPasswordForm){
        return new ModelAndView("user/recover/index");
    }

    @RequestMapping(value = "/recover/{token}", method = { RequestMethod.POST })
    public ModelAndView recoverPasswordEdit(
            @PathVariable String token,
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
    public ModelAndView recoverPasswordEditForm(@PathVariable String token, @ModelAttribute("RecoverPasswordEditForm") final RecoverPasswordEditForm form){
        if(!userService.isValidRecoveryToken(token)){
            return new ModelAndView("user/recover/invalidToken");
        }

        final ModelAndView mav = new ModelAndView("user/recover/editPassword");
        mav.addObject("token", token);
        return mav;
    }

    @RequestMapping(value = "/profile/editprofilepicture", method = { RequestMethod.GET })
    public ModelAndView editProfilePictureForm(@ModelAttribute ("editProfilePictureForm") final EditProfilePictureForm editProfilePictureForm) {
        final ModelAndView mav = new ModelAndView("user/editProfilePicture");
        final User user = authUserService.getCurrentUser();
        mav.addObject("user", user);
        return mav;
    }

    @RequestMapping(value="/profile/editprofilepicture", method = {RequestMethod.POST})
    public ModelAndView editProfilePicture(@ModelAttribute("editProfilePictureForm") final EditProfilePictureForm editProfilePictureForm,
                                           //CommonsMultipartFile profilePicture,
                                           final BindingResult errors) throws IOException {
        if(errors.hasErrors()) {
            return editProfilePictureForm(editProfilePictureForm);
        }
        final User user = authUserService.getCurrentUser();
        try {
            userService.updateProfilePicture(user, editProfilePictureForm.getProfilePicture().getBytes());
        }catch (InvalidImageSizeException e) {
            final ModelAndView mav = editProfilePictureForm(editProfilePictureForm);
            mav.addObject("invalidImageSize", true);
            return mav;
        }
        return new ModelAndView(REDIRECT_PROFILE);
    }

    @RequestMapping(value = "/subjectProgress", method = RequestMethod.POST)
    @ResponseBody
    public String subjectProgress(@Valid @ModelAttribute("SubjectProgressForm") final SubjectProgressForm progressForm
    ) {
        if( !authUserService.isAuthenticated()){
            return "invalid parameters"; // we do not give any information on the inner workings
        }

        final User user = authUserService.getCurrentUser();
        final Subject subject = subjectService.findById(progressForm.getIdSub()).orElseThrow(SubjectNotFoundException::new);

        userService.updateSubjectProgress(user, subject, SubjectProgress.parse(progressForm.getProgress()));

        return "voted";
    }

    @RequestMapping(value = "/select-degree", method = RequestMethod.POST )
    public ModelAndView selectDegreeSubmit(@Valid @ModelAttribute ("UpdateDegreeForm") final UpdateDegreeForm updateDegreeForm,
                                           final BindingResult errors){
        if(errors.hasErrors()){
            return selectDegree(updateDegreeForm);
        }

        User user = authUserService.getCurrentUser();

        userService.updateUserDegree(user, degreeService.findById(updateDegreeForm.getDegreeId()).orElseThrow(IllegalStateException::new));

        userService.setAllSubjectProgress(user, updateDegreeForm.getSubjectIds() );

        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/select-degree", method = RequestMethod.GET )
    public ModelAndView selectDegree(@ModelAttribute ("UpdateDegreeForm") final UpdateDegreeForm updateDegreeForm){
        List<Degree> degreeList = degreeService.getAll();
        User user = authUserService.getCurrentUser();

        if(user.getDegree() != null){
            return new ModelAndView("redirect:/" );
        }

        ModelAndView mav = new ModelAndView("user/onboarding");
        mav.addObject("degrees", degreeList);
        mav.addObject("user", user);

        return mav;
    }
}
