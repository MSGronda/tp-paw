package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exceptions.InvalidFormException;
import ar.edu.itba.paw.models.exceptions.SubjectClassNotFoundException;
import ar.edu.itba.paw.models.exceptions.SubjectNotFoundException;
import ar.edu.itba.paw.services.AuthUserService;
import ar.edu.itba.paw.services.SubjectService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.form.UserSemesterFinishForm;
import ar.edu.itba.paw.webapp.form.UserSemesterForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import javax.validation.Valid;

@Controller
public class SemesterBuilderController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SemesterBuilderController.class);

    private final AuthUserService authUserService;
    private final SubjectService subjectService;
    private final UserService userService;

    @Autowired
    public SemesterBuilderController(
            final AuthUserService authUserService,
            final SubjectService subjectService,
            final UserService userService
    ) {
        this.authUserService = authUserService;
        this.subjectService = subjectService;
        this.userService = userService;
    }

    @RequestMapping("/builder")
    public ModelAndView subjectInfo() {
        final User user = authUserService.getCurrentUser();

        final ModelAndView mav = new ModelAndView("builder/semester-builder");
        mav.addObject("user", user);
        mav.addObject("availableSubjects", subjectService.findAllThatUserCanDo(user));
        mav.addObject("unlockableSubjects", subjectService.findAllThatUserCouldUnlock(user));
        mav.addObject("doneSubjects", subjectService.findAllThatUserHasDone(user));
        return mav;
    }

    @RequestMapping(value ="/builder/finish", method = RequestMethod.GET)
    public ModelAndView finishSemester(
            @Valid @ModelAttribute("UserSemesterFinishForm") final UserSemesterFinishForm semesterForm
    ){
        final ModelAndView mav = new ModelAndView("builder/finish-semester");
        mav.addObject("user", authUserService.getCurrentUser());
        return mav;
    }

    @RequestMapping(value ="/builder/finish", method = RequestMethod.POST)
    public ModelAndView finishSemesterSubmit(
            @Valid @ModelAttribute("UserSemesterFinishForm") final UserSemesterFinishForm semesterForm,
            final BindingResult errors
    ){
        if(errors.hasErrors()){
            return finishSemester(semesterForm);
        }
        User user = authUserService.getCurrentUser();
        String url = userService.getSemesterSubmitRedirectUrl(user);
        userService.finishSemester(user);
        return new ModelAndView(url);
    }

    @RequestMapping(value = "/builder/add", method = RequestMethod.POST)
    public ModelAndView addSubjectToSemester(
            @Valid @ModelAttribute("UserSemesterForm") final UserSemesterForm semesterForm,
            final BindingResult errors
    ){
        if(errors.hasErrors()){
            LOGGER.debug("Subject builder adding form has errors");
            throw new InvalidFormException();
        }

        try {
            userService.addToCurrentSemester(authUserService.getCurrentUser(), semesterForm.getIdSub(), semesterForm.getIdClass());
        } catch (SubjectNotFoundException | SubjectClassNotFoundException e) {
            LOGGER.debug("form has invalid subject or class");
            throw new InvalidFormException(e);
        }

        return new ModelAndView("redirect:/builder");
    }

    @RequestMapping(value = "/builder/remove", method = RequestMethod.POST)
    public ModelAndView removeSubjectToSemester(
            @Valid @ModelAttribute("UserSemesterForm") final UserSemesterForm semesterForm,
            final BindingResult errors
    ){
        if(errors.hasErrors()){
            LOGGER.debug("Subject builder adding form has errors");
            throw new InvalidFormException();
        }

        try {
            userService.removeFromCurrentSemester(authUserService.getCurrentUser(), semesterForm.getIdSub(), semesterForm.getIdClass());
        } catch (SubjectNotFoundException | SubjectClassNotFoundException e) {
            LOGGER.debug("form has invalid subject or class");
            throw new InvalidFormException();
        }

        return new ModelAndView("redirect:/builder");
    }
}
