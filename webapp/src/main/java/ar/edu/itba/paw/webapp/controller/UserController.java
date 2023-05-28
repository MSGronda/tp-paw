package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.SubjectProgress;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.services.exceptions.*;
import ar.edu.itba.paw.webapp.auth.UniUserDetailsService;
import ar.edu.itba.paw.webapp.exceptions.RoleNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.SubjectNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.*;
import ar.edu.itba.paw.webapp.form.EditUserDataForm;
import ar.edu.itba.paw.webapp.form.EditUserPasswordForm;
import ar.edu.itba.paw.webapp.form.RecoverPasswordForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import ar.edu.itba.paw.webapp.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
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
            @RequestParam(required = false, defaultValue = "date") final String orderBy,
            @RequestParam(required = false, defaultValue = "desc") final String dir
    ) {
        final Optional<User> maybeUser = userService.findById(id);
        if(!maybeUser.isPresent()) {
            throw new UserNotFoundException();
        }

        if( authUserService.isAuthenticated() && authUserService.getCurrentUser().getId() == id){
            return new ModelAndView("redirect:/profile");
        }

        final User user = maybeUser.get();
        ModelAndView mav = new ModelAndView("user/userProfile");

        return setProfileData(mav, user, page, orderBy, dir);
    }

    @RequestMapping("/profile")
    public ModelAndView profile(
            @RequestParam(required = false, defaultValue = "1") final int page,
            @RequestParam(required = false, defaultValue = "date") final String orderBy,
            @RequestParam(required = false, defaultValue = "desc") final String dir
    ) {
        ModelAndView mav = new ModelAndView("/user/profile");
        User user = authUserService.getCurrentUser();
        return setProfileData(mav, user, page, orderBy, dir);
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

        User newUser;
        try {
            newUser = userService.create(
                    User.builder()
                            .email(userForm.getEmail())
                            .password(userForm.getPassword())
                            .username(userForm.getName())
                            .build()
            );
        }catch (UserEmailAlreadyTakenException e){
            ModelAndView mav = registerForm(userForm);
            mav.addObject("EmailAlreadyUsed", true);
            return mav;
        }

        final String token = newUser.getConfirmToken().orElseThrow(IllegalStateException::new);

        final String baseUrl = Utils.getBaseUrl();
        final String verifUrl = baseUrl + "/verification/confirm?token=" + token;
        final String logoUrl = baseUrl + "/img/uni.png";
        mailService.sendVerification(newUser.getEmail(), verifUrl, logoUrl, locale);

        userService.updateSubjectProgressWithSubList(newUser, userForm.getSubjectIds());

        return new ModelAndView("redirect:/verification?email=" + newUser.getEmail());
    }

    @RequestMapping(value = "/register", method = { RequestMethod.GET })
    public ModelAndView registerForm(@ModelAttribute ("UserForm") final UserForm userForm) {
        List<Degree> degreeList = degreeService.getAll();
//        Map<Long, Map<Integer, List<Subject>>> degreeMapAndYearSubjects = subjectService.getAllGroupedByDegIdAndYear();
//        Map<Long, List<Subject>> degreeMapAndYearElectives = subjectService.getAllElectivesGroupedByDegId();

        ModelAndView mav = new ModelAndView("user/register");
        mav.addObject("degrees", degreeList);
//        mav.addObject("degreeMapAndYearSubjects", degreeMapAndYearSubjects);
//        mav.addObject("degreeMapAndYearElectives", degreeMapAndYearElectives);
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
    public ModelAndView resendConfirmationEmail(@RequestParam final String email, final Locale locale) {
        final Optional<User> maybeUser = userService.findUnconfirmedByEmail(email);
        if(!maybeUser.isPresent()) {
            return new ModelAndView("redirect:/verification?error=true&email=" + email);
        }

        final User user = maybeUser.get();
        final String token = userService.regenerateConfirmToken(user);

        final String baseUrl = Utils.getBaseUrl();
        final String verifUrl = baseUrl + "/verification/confirm?token=" + token;
        final String logoUrl = baseUrl + "/img/uni.png";
        mailService.sendVerification(user.getEmail(), verifUrl, logoUrl, locale);

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

        User user = authUserService.getCurrentUser();
        userService.editProfile(user, editUserDataForm.getUserName());
        return new ModelAndView("redirect:/profile");
    }

    @RequestMapping(value = "/profile/editdata", method = { RequestMethod.GET })
    public ModelAndView editProfileForm(@ModelAttribute ("EditUserDataForm") final EditUserDataForm editUserDataForm) {
        ModelAndView mav = new ModelAndView("user/editUserData");
        User user = authUserService.getCurrentUser();
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

        User user = authUserService.getCurrentUser();
        try{
            userService.changePassword(user, editUserPasswordForm.getEditPassword(), editUserPasswordForm.getOldPassword(), user.getPassword());
        }catch (OldPasswordDoesNotMatchException e){
            ModelAndView mav = editPasswordForm(editUserPasswordForm);
            mav.addObject("oldPasswordDoesNotMatch", true);
            return mav;
        }
        return new ModelAndView("redirect:/profile");
    }
    @RequestMapping(value = "/profile/editpassword", method = { RequestMethod.GET })
    public ModelAndView editPasswordForm(@ModelAttribute ("EditUserPasswordForm") final EditUserPasswordForm editUserPasswordForm) {
        return new ModelAndView("user/editUserPassword");
    }

    @RequestMapping(value = "user/{id:\\d+}/moderator")
    public ModelAndView makeModerator(@PathVariable long id) {
        if(!authUserService.isCurrentUserEditor())
            return new ModelAndView("redirect:/error");

        Optional<Role> maybeRole = rolesService.findByName(Role.RoleEnum.EDITOR.getName());
        if(!maybeRole.isPresent()){
            throw new RoleNotFoundException();
        }
        Role role = maybeRole.get();

        final User toMakeMod = userService.findById(id).orElseThrow(UserNotFoundException::new);
        userService.updateRoles(toMakeMod, role);

        return new ModelAndView("redirect:/user/" + id);
    }

    @RequestMapping(value = "/recover", method = { RequestMethod.POST })
    public ModelAndView sendRecover(
            @Valid @ModelAttribute ("RecoverPasswordForm") final RecoverPasswordForm recoverPasswordForm,
            final BindingResult errors,
            final Locale locale
    ){
        if( errors.hasErrors()){
            return recoverPassword(recoverPasswordForm);
        }

        final String email = recoverPasswordForm.getEmail();

        String token;
        try {
            token = userService.generateRecoveryToken(email);
        } catch (UserEmailNotFoundException e) {
            ModelAndView mav = recoverPassword(recoverPasswordForm);
            mav.addObject("emailNotFound", true);
            return mav;
        }

        final String baseUrl = Utils.getBaseUrl();
        final String logoUrl = baseUrl + "/img/uni.png";
        final String recoverUrl = baseUrl + "/recover/" + token;
        mailService.sendRecover(email, recoverUrl, logoUrl, locale);

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

        ModelAndView mav = new ModelAndView("user/recover/editPassword");
        mav.addObject("token", token);
        return mav;
    }

    @RequestMapping(value = "/profile/editprofilepicture", method = { RequestMethod.GET })
    public ModelAndView editProfilePictureForm(@ModelAttribute ("editProfilePictureForm") final EditProfilePictureForm editProfilePictureForm) {
        ModelAndView mav = new ModelAndView("user/editProfilePicture");
        User user = authUserService.getCurrentUser();
        mav.addObject("user", user);
        return mav;
    }

    @RequestMapping(value="/profile/editprofilepicture", method = {RequestMethod.POST})
    public ModelAndView editProfilePicture(@ModelAttribute("editProfilePictureForm") final EditProfilePictureForm editProfilePictureForm,
                                           //CommonsMultipartFile profilePicture,
                                           final BindingResult errors) throws SQLException, IOException {
        if(errors.hasErrors()) {
            return editProfilePictureForm(editProfilePictureForm);
        }
        User user = authUserService.getCurrentUser();
        try {
            userService.updateProfilePicture(user, editProfilePictureForm.getProfilePicture().getBytes());
        }catch (InvalidImageSizeException e) {
            ModelAndView mav = editProfilePictureForm(editProfilePictureForm);
            mav.addObject("invalidImageSize", true);
            return mav;
        }
        return new ModelAndView("redirect:/profile");
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
}
