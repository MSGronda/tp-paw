package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
        ModelAndView mav = new ModelAndView("helloworld/profile");
        mav.addObject("user", user);

        return mav;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(
        @RequestParam String username,
        @RequestParam String email,
        @RequestParam String password
    ) throws SQLException {
        final User newUser = userService.create(email, password, username);

        return new ModelAndView("redirect:/profile/" + newUser.getId());
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView registerForm() {
        return new ModelAndView("helloworld/register");
    }

}
