package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.DegreeService;
import ar.edu.itba.paw.services.MailService;
import ar.edu.itba.paw.services.ReviewService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.services.exceptions.InvalidTokenException;
import ar.edu.itba.paw.services.exceptions.OldPasswordDoesNotMatchException;
import ar.edu.itba.paw.services.exceptions.UserEmailAlreadyTakenException;
import ar.edu.itba.paw.services.exceptions.UserEmailNotFoundException;
import ar.edu.itba.paw.webapp.auth.UniAuthUser;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.*;
import ar.edu.itba.paw.webapp.form.EditUserDataForm;
import ar.edu.itba.paw.webapp.form.EditUserPasswordForm;
import ar.edu.itba.paw.webapp.form.RecoverPasswordForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.*;

@Controller
public class UserController {
    private final UserService userService;
    private final ReviewService reviewService;
    private final MailService mailService;

    private final DegreeService degreeService;


    @Autowired
    public UserController(UserService userService, ReviewService reviewService, MailService mailService, DegreeService degreeService) {
        this.userService = userService;
        this.reviewService = reviewService;
        this.mailService = mailService;
        this.degreeService = degreeService;
    }

    @RequestMapping("/profile/{id:\\d+}")
    public ModelAndView profile(@PathVariable long id) {
        final Optional<User> maybeUser = userService.findById(id);
        if(!maybeUser.isPresent()) {
            throw new UserNotFoundException();
        }

        final User user = maybeUser.get();
        final List<Review> userReviews = reviewService.getAllUserReviewsWithSubjectName(user.getId());
        final Map<Long, Integer> userVotes = reviewService.userReviewVoteByIdUser(user.getId());

        ModelAndView mav = new ModelAndView("user/profile");
        mav.addObject("user", user);
        mav.addObject("reviews", userReviews);
        mav.addObject("userVotes",userVotes);

        return mav;
    }

    @RequestMapping(value = "/register", method = { RequestMethod.POST })
    public ModelAndView register(@Valid @ModelAttribute ("UserForm") final UserForm userForm,
                                 final BindingResult errors) throws IOException {
        if(errors.hasErrors()){
            return registerForm(userForm);
        }

        User.UserBuilder user = new User.UserBuilder(userForm.getEmail(), userForm.getPassword(), userForm.getName());
        File file = ResourceUtils.getFile("classpath:images/default_user.png");
        byte[] imgData = Files.readAllBytes(file.toPath());
        user.image(imgData).build();
        try {
            final User newUser = userService.create(user);
        }catch (UserEmailAlreadyTakenException e){

//            errors.rejectValue("email", "UserForm.email.alreadyExists", "An account with this email already exists");
            ModelAndView mav = registerForm(userForm);
            mav.addObject("EmailAlreadyUsed", true);
            return mav;
        }

        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(value = "/register", method = { RequestMethod.GET })
    public ModelAndView registerForm(@ModelAttribute ("UserForm") final UserForm userForm) {
        return new ModelAndView("user/register");
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

        User user = loggedUser();
        userService.editProfile(user.getId(), editUserDataForm.getUserName());
        return new ModelAndView("redirect:/profile/" + user.getId());
    }
    @RequestMapping(value = "/profile/editdata", method = { RequestMethod.GET })
    public ModelAndView editProfileForm(@ModelAttribute ("EditUserDataForm") final EditUserDataForm editUserDataForm) {
        return new ModelAndView("user/editUserData");
    }
    @RequestMapping(value = "/profile/editpassword", method = { RequestMethod.POST })
    public ModelAndView editPassword(@Valid @ModelAttribute ("EditUserPasswordForm") final EditUserPasswordForm editUserPasswordForm,
                                    final BindingResult errors) throws SQLException {
        if(errors.hasErrors()){
            return editPasswordForm(editUserPasswordForm);
        }

        User user = loggedUser();
        try{
            userService.changePassword(user.getId(), editUserPasswordForm.getEditPassword(), editUserPasswordForm.getOldPassword(), user.getPassword());
        }catch (OldPasswordDoesNotMatchException e){
            ModelAndView mav = editPasswordForm(editUserPasswordForm);
            mav.addObject("oldPasswordDoesNotMatch", true);
            return mav;
        }
        return new ModelAndView("redirect:/profile/" + user.getId());
    }
    @RequestMapping(value = "/profile/editpassword", method = { RequestMethod.GET })
    public ModelAndView editPasswordForm(@ModelAttribute ("EditUserPasswordForm") final EditUserPasswordForm editUserPasswordForm) {
        return new ModelAndView("user/editUserPassword");
    }

    @RequestMapping(value = "/recover", method = { RequestMethod.POST })
    public ModelAndView sendEmail(@Valid @ModelAttribute ("RecoverPasswordForm") final RecoverPasswordForm recoverPasswordForm,
                                  final BindingResult errors,
                                  HttpServletRequest request){
        if( errors.hasErrors()){
            return recoverPassword(recoverPasswordForm);
        }

        final String token;
        try {
            token = userService.generateRecoveryToken(recoverPasswordForm.getEmail());
        } catch (UserEmailNotFoundException e) {
            ModelAndView mav = recoverPassword(recoverPasswordForm);
            mav.addObject("emailNotFound", true);
            return mav;
        }

        final String baseUrl =
                ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        Map<String,Object> mailModel = new HashMap<>();
        mailModel.put("url", baseUrl + "/recover/" + token);
        mailService.sendMail(recoverPasswordForm.getEmail(), "Uni: Recover password", "recovery", mailModel);

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
        if(!userService.isValidToken(token)){
            ModelAndView mav = new ModelAndView("user/recover/index");
            mav.addObject("invalidToken", true);
            return mav;
        }

        ModelAndView mav = new ModelAndView("user/recover/editPassword");
        mav.addObject("token", token);
        return mav;
    }

    @ModelAttribute("loggedUser")
    public User loggedUser(){
        Object maybeUniAuthUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if( maybeUniAuthUser.toString().equals("anonymousUser")){
            return null;
        }
        final UniAuthUser userDetails = (UniAuthUser) maybeUniAuthUser ;
        return userService.getUserWithEmail(userDetails.getUsername()).orElse(null);
    }

    @ModelAttribute("degrees")
    public List<Degree> degrees(){
        return degreeService.getAll();
    }

    @RequestMapping(value = "/profile/{id:\\d+}", method = { RequestMethod.GET },
        produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public byte[] profilePicture(@PathVariable long id)throws IOException {
        return userService.findByIdWithImage(id).get().getImage();
    }

    @RequestMapping(value = "/profile/editprofilepicture", method = { RequestMethod.GET })
    public ModelAndView editProfilePictureForm(@ModelAttribute ("editProfilePictureForm") final EditProfilePictureForm editProfilePictureForm) {
        return new ModelAndView("user/editProfilePicture");
    }
    @RequestMapping(value="/profile/editprofilepicture", method = {RequestMethod.POST})
    public ModelAndView editProfilePicture(@ModelAttribute("editProfilePictureForm") final EditProfilePictureForm editProfilePictureForm,
                                           //CommonsMultipartFile profilePicture,
                                           final BindingResult errors) throws SQLException, IOException {
        if(errors.hasErrors()) {
            return editProfilePictureForm(editProfilePictureForm);
        }
        User user = loggedUser();

        userService.updateProfilePicture(user.getId(), editProfilePictureForm.getProfilePicture().getBytes());
        return new ModelAndView("redirect:/profile/" + user.getId());
    }

    @RequestMapping(value = "/subjectProgress", method = RequestMethod.POST)
    @ResponseBody
    public String subjectProgress(@Valid @ModelAttribute("SubjectProgressForm") final SubjectProgressForm progressForm
    ) {
        if( loggedUser() == null){
            return "invalid parameters"; // we do not give any information on the inner workings
        }
        int resp = 0, progressValue = progressForm.getProgress();
        if(progressValue != 0)
            resp = userService.updateSubjectProgress(loggedUser().getId(), progressForm.getIdSub(),progressValue);
        else
            resp = userService.deleteUserProgressForSubject(loggedUser().getId(), progressForm.getIdSub());

        if(resp != 1){
            return "invalid parameters"; // we do not give any information on the inner workings
        }
        return "voted";
    }
}
