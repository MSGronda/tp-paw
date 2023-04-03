package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloWorldController {
    private final UserService userService;

    @Autowired
    public HelloWorldController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/")
    public ModelAndView home() {
        return new ModelAndView("helloworld/index");
    }

    @RequestMapping("/hello")
    public ModelAndView helloWorld() {
        ModelAndView mav = new ModelAndView("helloworld/hello");
        mav.addObject("user", userService.createUser("a@a.a", "1234", "A A"));
        return mav;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(@RequestParam String email, @RequestParam String password, @RequestParam String nombre) {
        User user = userService.createUser(email, password, nombre);
        ModelAndView mav = new ModelAndView("helloworld/hello");
        mav.addObject("user", user);

        return mav;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView registerForm() {
        return new ModelAndView("helloworld/register");
    }

    @RequestMapping("/{id:\\d+}")
    public ModelAndView profile(@PathVariable long id) {
        ModelAndView mav = new ModelAndView("helloworld/profile");
        mav.addObject("userId", id);

        return mav;
    }
}
