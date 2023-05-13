package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.services.exceptions.InvalidTokenException;
import ar.edu.itba.paw.services.exceptions.OldPasswordDoesNotMatchException;
import ar.edu.itba.paw.services.exceptions.UserEmailAlreadyTakenException;
import ar.edu.itba.paw.services.exceptions.UserEmailNotFoundException;
import ar.edu.itba.paw.webapp.auth.UniUserDetailsService;
import ar.edu.itba.paw.webapp.exceptions.RoleNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.*;
import ar.edu.itba.paw.webapp.form.EditUserDataForm;
import ar.edu.itba.paw.webapp.form.EditUserPasswordForm;
import ar.edu.itba.paw.webapp.form.RecoverPasswordForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.sql.SQLException;
import java.util.*;

@Controller
public class UserController {
    private final UserService userService;
    private final ReviewService reviewService;
    private final MailService mailService;

    private final DegreeService degreeService;

    private final AuthUserService authUserService;
    private final AuthenticationManager authManager;

    private final RolesService rolesService;

    private final UniUserDetailsService uniUserDetailsService;


    @Autowired
    public UserController(UserService userService, ReviewService reviewService, MailService mailService, DegreeService degreeService, AuthUserService authUserService, RolesService rolesService, UniUserDetailsService uniUserDetailsService, AuthenticationManager authManager) {
        this.userService = userService;
        this.reviewService = reviewService;
        this.mailService = mailService;
        this.degreeService = degreeService;
        this.authUserService = authUserService;
        this.rolesService = rolesService;
        this.uniUserDetailsService = uniUserDetailsService;
        this.authManager = authManager;
    }

    @RequestMapping("/user/{id:\\d+}")
    public ModelAndView user(@PathVariable long id) {
        final Optional<User> maybeUser = userService.findById(id);
        if(!maybeUser.isPresent()) {
            throw new UserNotFoundException();
        }

        if( authUserService.isAuthenticated() && authUserService.getCurrentUser().getId() == id){
            return new ModelAndView("redirect:/profile");
        }

        final User user = maybeUser.get();
        ModelAndView mav = new ModelAndView("user/userProfile");

        return setProfileData(user, mav);
    }

    @RequestMapping("/profile")
    public ModelAndView profile() {
        ModelAndView mav = new ModelAndView("/user/profile");
        User user = authUserService.getCurrentUser();
        return setProfileData(user, mav);
    }

    private ModelAndView setProfileData(User user, ModelAndView mav){
        final List<Review> userReviews = reviewService.getAllUserReviewsWithSubjectName(user.getId());
        final Map<Long, Integer> userVotes = reviewService.userReviewVoteByIdUser(user.getId());

        UserDetails userDetails = uniUserDetailsService.loadUserByUsername(user.getEmail());
        Boolean isEditor = userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EDITOR"));

        mav.addObject("editor", isEditor);
        mav.addObject("user", user);
        mav.addObject("reviews", userReviews);
        mav.addObject("userVotes",userVotes);

        return mav;
    }

    @RequestMapping(value = "/register", method = { RequestMethod.POST })
    public ModelAndView register(@Valid @ModelAttribute ("UserForm") final UserForm userForm,
                                 final BindingResult errors, final Locale locale) throws IOException {
        if(errors.hasErrors()){
            return registerForm(userForm);
        }

        User.UserBuilder user = new User.UserBuilder(userForm.getEmail(), userForm.getPassword(), userForm.getName());

        try {
            final User newUser = userService.create(user);
        }catch (UserEmailAlreadyTakenException e){

//            errors.rejectValue("email", "UserForm.email.alreadyExists", "An account with this email already exists");
            ModelAndView mav = registerForm(userForm);
            mav.addObject("EmailAlreadyUsed", true);
            return mav;
        }

        final String baseUrl = Helpers.getBaseUrl();

        Map<String,Object> mailModel = new HashMap<>();
        mailModel.put("logoUrl", baseUrl + "/img/uni.png");
        mailModel.put("url", baseUrl + "/confirm/" + user.getConfirmToken());
        mailService.sendMail(user.getEmail(), "Email confirmation", "confirmation", mailModel, locale);

        return new ModelAndView("user/confirm/checkEmail");
    }

    @RequestMapping(value = "/register", method = { RequestMethod.GET })
    public ModelAndView registerForm(@ModelAttribute ("UserForm") final UserForm userForm) {
        return new ModelAndView("user/register");
    }

    @RequestMapping("/confirm/{token}")
    public String confirm(HttpServletRequest request, @PathVariable String token) {
        User user;
        try {
            user = userService.confirmUser(token);
        } catch (InvalidTokenException e) {
            return "user/confirm/invalidToken";
        }

        // Auto-login
//        final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getEmail(), null);
//        final Authentication auth = authManager.authenticate(authToken);
//        final SecurityContext ctx = SecurityContextHolder.getContext();
//        ctx.setAuthentication(auth);
//        final HttpSession session = request.getSession(true);
//        session.setAttribute("SPRING_SECURITY_CONTEXT_KEY", ctx);

        return "user/confirm/success";
    }

    @RequestMapping(value = "/login", method = { RequestMethod.GET })
    public ModelAndView login(@RequestParam (value="error", required = false) String error) {
        ModelAndView mav = new ModelAndView("user/login");
        mav.addObject("error", error);
        return mav;
    }

    @RequestMapping(value = "/profile/editdata", method = { RequestMethod.POST })
    public ModelAndView editProfile(@Valid @ModelAttribute ("EditUserDataForm") final EditUserDataForm editUserDataForm,
                                    final BindingResult errors) throws SQLException {
        if(errors.hasErrors()){
            return editProfileForm(editUserDataForm);
        }

        User user = authUserService.getCurrentUser();
        userService.editProfile(user.getId(), editUserDataForm.getUserName());
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
    public ModelAndView editPassword(@Valid @ModelAttribute ("EditUserPasswordForm") final EditUserPasswordForm editUserPasswordForm,
                                    final BindingResult errors) throws SQLException {
        if(errors.hasErrors()){
            return editPasswordForm(editUserPasswordForm);
        }

        User user = authUserService.getCurrentUser();
        try{
            userService.changePassword(user.getId(), editUserPasswordForm.getEditPassword(), editUserPasswordForm.getOldPassword(), user.getPassword());
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
        Optional<Roles> maybeRole = rolesService.findByName("EDITOR");
        if(!maybeRole.isPresent()){
            throw new RoleNotFoundException();
        }
        Roles role = maybeRole.get();

        if(userService.updateUserRoles(role.getId(), id) == 0)
            return new ModelAndView("redirect:/error");
        return new ModelAndView("redirect:/user/" + id);
    }

    @RequestMapping(value = "/recover", method = { RequestMethod.POST })
    public ModelAndView sendEmail(@Valid @ModelAttribute ("RecoverPasswordForm") final RecoverPasswordForm recoverPasswordForm,
                                  final BindingResult errors,
                                  final Locale locale){
        if( errors.hasErrors()){
            return recoverPassword(recoverPasswordForm);
        }

        final String email = recoverPasswordForm.getEmail();

        String token;
        try {
            token = userService.sendRecoveryMail(email);
        } catch (UserEmailNotFoundException e) {
            ModelAndView mav = recoverPassword(recoverPasswordForm);
            mav.addObject("emailNotFound", true);
            return mav;
        }

        final String baseUrl = Helpers.getBaseUrl();

        Map<String,Object> mailModel = new HashMap<>();
        mailModel.put("logoUrl", baseUrl + "/img/uni.png");
        mailModel.put("url", baseUrl + "/recover/" + token);
        mailService.sendMail(email, "Uni: Recover password", "recovery", mailModel, locale);

        return new ModelAndView("user/recover/emailSent");
    }

    @RequestMapping(value = "/recover", method = { RequestMethod.GET })
    public ModelAndView recoverPassword(@ModelAttribute ("RecoverPasswordForm") final RecoverPasswordForm recoverPasswordForm){
        return new ModelAndView("user/recover/index");
    }

    @RequestMapping(value = "/recover/{token}", method = { RequestMethod.POST })
    public ModelAndView recoverPasswordEdit(@PathVariable String token, @Valid @ModelAttribute("RecoverPasswordEditForm") final RecoverPasswordEditForm recoverPasswordEditForm,
                                            final BindingResult errors){
        if(errors.hasErrors()) {
            return recoverPasswordEditForm(token, recoverPasswordEditForm);
        }

        try {
            userService.recoverPassword(token, recoverPasswordEditForm.getPassword());
        } catch (InvalidTokenException e) {
            ModelAndView mav = new ModelAndView("user/recover/index");
            mav.addObject("invalidToken", true);
            return mav;
        }

        return new ModelAndView("user/recover/success");
    }

    @RequestMapping(value = "/recover/{token}", method = { RequestMethod.GET })
    public ModelAndView recoverPasswordEditForm(@PathVariable String token, @ModelAttribute("RecoverPasswordEditForm") final RecoverPasswordEditForm form){
        if(!userService.isValidRecoveryToken(token)){
            ModelAndView mav = new ModelAndView("user/recover/index");
            mav.addObject("invalidToken", true);
            return mav;
        }

        ModelAndView mav = new ModelAndView("user/recover/editPassword");
        mav.addObject("token", token);
        return mav;
    }

    @ModelAttribute("degrees")
    public List<Degree> degrees(){
        return degreeService.getAll();
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

        userService.updateProfilePicture(user, editProfilePictureForm.getProfilePicture().getBytes());
        return new ModelAndView("redirect:/profile");
    }

    @RequestMapping(value = "/subjectProgress", method = RequestMethod.POST)
    @ResponseBody
    public String subjectProgress(@Valid @ModelAttribute("SubjectProgressForm") final SubjectProgressForm progressForm
    ) {
        if( !authUserService.isAuthenticated()){
            return "invalid parameters"; // we do not give any information on the inner workings
        }
        int resp = 0, progressValue = progressForm.getProgress();
        User user = authUserService.getCurrentUser();
        if(progressValue != 0)
            resp = userService.updateSubjectProgress(user.getId(), progressForm.getIdSub(),progressValue);
        else
            resp = userService.deleteUserProgressForSubject(user.getId(), progressForm.getIdSub());

        if(resp != 1){
            return "invalid parameters"; // we do not give any information on the inner workings
        }
        return "voted";
    }
}
