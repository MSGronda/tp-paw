package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.ReviewService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.services.exceptions.OldPasswordDoesNotMatchException;
import ar.edu.itba.paw.services.exceptions.UserEmailAlreadyTakenException;
import ar.edu.itba.paw.webapp.auth.UniAuthUser;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.EditUserDataForm;
import ar.edu.itba.paw.webapp.form.EditUserPasswordForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.*;

@Controller
public class UserController {
    private final UserService userService;
    private final ReviewService reviewService;


    @Autowired
    public UserController(UserService userService, ReviewService reviewService) {
        this.userService = userService;
        this.reviewService = reviewService;
    }

    @RequestMapping("/profile/{id:\\d+}")
    public ModelAndView profile(@PathVariable long id) {
        final Optional<User> maybeUser = userService.findById(id);
        if(!maybeUser.isPresent()) {
            throw new UserNotFoundException();
        }

        final User user = maybeUser.get();
        final List<Review> userReviews = reviewService.getAllUserReviewsWithSubjectName(user.getId());

        ModelAndView mav = new ModelAndView("user/profile");
        mav.addObject("user", user);
        mav.addObject("reviews", userReviews);

        return mav;
    }

    @RequestMapping(value = "/register", method = { RequestMethod.POST })
    public ModelAndView register(@Valid @ModelAttribute ("UserForm") final UserForm userForm,
                                 final BindingResult errors) {
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

    @ModelAttribute("loggedUser")
    public User loggedUser(){
        Object maybeUniAuthUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if( maybeUniAuthUser.toString().equals("anonymousUser")){
            return null;
        }
        final UniAuthUser userDetails = (UniAuthUser) maybeUniAuthUser ;
        return userService.getUserWithEmail(userDetails.getUsername()).orElse(null);
    }
}
