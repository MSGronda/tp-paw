package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.*;

@Controller
public class UserController {
    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/profile/{id:\\d+}")
    public ModelAndView profile(@PathVariable long id) {
        final Optional<User> maybeUser = userService.findById(id);
        if(!maybeUser.isPresent()) {
            throw new UserNotFoundException();
        }

        final User user = maybeUser.get();
        ModelAndView mav = new ModelAndView("user/profile");
        mav.addObject("user", user);

        return mav;
    }

    @RequestMapping(value = "/register", method = { RequestMethod.POST })
    public ModelAndView register(@Valid @ModelAttribute ("UserForm") final UserForm userForm,
                                 final BindingResult errors) throws SQLException {
        if(errors.hasErrors()){
            return registerForm(userForm);
        }
        //TODO - check if email is already in use
        Optional<User> maybeUser = userService.getUserWithEmail(userForm.getEmail());
        if(maybeUser.isPresent()){
            //user already exists
            //TODO - aplicar internacionalizacion            
            errors.rejectValue("email", "UserForm.email.alreadyExists", "An account with this email already exists");
            return registerForm(userForm);
        }
        User.UserBuilder user = new User.UserBuilder(userForm.getEmail(), userForm.getPassword(), userForm.getName());
        final User newUser = userService.create(user);

        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(value = "/register", method = { RequestMethod.GET })
    public ModelAndView registerForm(@ModelAttribute ("UserForm") final UserForm userForm) {
        return new ModelAndView("user/register");
    }

    @RequestMapping(value = "/login", method = { RequestMethod.GET })
    public ModelAndView login(@RequestParam (value="error", required = false) String error) {
        ModelAndView mav = new ModelAndView("/login/login");
        mav.addObject("error", error);
        return mav;
    }

    @ModelAttribute("loggedUser")
    public User loggedUser(final HttpSession session) {
        Long userId = (Long) session.getAttribute("userId2");
        if(userId== null)
            return null;
        return userService.findById(userId.longValue()).orElseGet(() -> null);
    }
}
