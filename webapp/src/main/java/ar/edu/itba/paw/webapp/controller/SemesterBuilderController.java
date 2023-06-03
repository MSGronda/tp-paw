package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.form.ReviewForm;
import ar.edu.itba.paw.webapp.form.UserSemesterForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.*;

import static java.lang.Long.parseLong;

@Controller
public class SemesterBuilderController {
    private final AuthUserService authUserService;
    private final SubjectService subjectService;
    private final UserService userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(SemesterBuilderController.class);

    @Autowired
    public SemesterBuilderController(AuthUserService authUserService, SubjectService subjectService, UserService userService) {
        this.authUserService = authUserService;
        this.subjectService = subjectService;
        this.userService = userService;
    }

    @RequestMapping("/builder")
    public ModelAndView subjectInfo() {
        User user;
        if(authUserService.isAuthenticated()) {
           user = authUserService.getCurrentUser();
        }
        else{
            return new ModelAndView("user/login");
        }

        ModelAndView mav = new ModelAndView("builder/semester-builder");

        List<Subject> availableSubjects = subjectService.findAllThatUserCanDo(user);
        List<Subject> unlockableSubjects = subjectService.findAllThatUserCouldUnlock(user);
        List<Subject> doneSubjects = subjectService.findAllThatUserHasDone(user);

        mav.addObject("user", user);
        mav.addObject("availableSubjects", availableSubjects);
        mav.addObject("unlockableSubjects", unlockableSubjects);
        mav.addObject("doneSubjects", doneSubjects);

        return mav;
    }

    @RequestMapping(value = "/builder/add", method = RequestMethod.POST)
    public ModelAndView addSubjectToSemester(@Valid @ModelAttribute("ReviewForm") final UserSemesterForm semesterForm, final BindingResult errors){
        User user;
        if(authUserService.isAuthenticated()) {
            user = authUserService.getCurrentUser();
        }
        else{
            LOGGER.info("User is not logged in");
            return new ModelAndView("user/login");
        }
        if(errors.hasErrors()){
            LOGGER.warn("Subject builder adding form has errors");
            return new ModelAndView("redirect:/builder/");
        }

        final Optional<Subject> maybeSubject = subjectService.findById(semesterForm.getIdSub());

        if(!maybeSubject.isPresent()){
            LOGGER.warn("No subject for id {}", semesterForm.getIdSub());
            return new ModelAndView("redirect:/builder/");
        }

        final Map<String, SubjectClass> classes = maybeSubject.get().getClassesById();

        if(!classes.containsKey(semesterForm.getIdClass())){
            LOGGER.warn("No class in subject {} for id {}", semesterForm.getIdSub(), semesterForm.getIdClass());
            return new ModelAndView("redirect:/builder/");
        }
        final SubjectClass subjectClass = classes.get(semesterForm.getIdClass());

        LOGGER.info("User {} added to its current semester the subject: {}, class: {}", user.getId(), subjectClass.getSubject().getName(), subjectClass.getClassId());

        userService.addToCurrentSemester(user, subjectClass);

        return new ModelAndView("redirect:/builder/");
    }

    // Justificacion: codigo repetido que no vale modularizar al tener que retornar MAV y SubjecClass
    @RequestMapping(value = "/builder/remove", method = RequestMethod.POST)
    public ModelAndView removeSubjectToSemester(@Valid @ModelAttribute("ReviewForm") final UserSemesterForm semesterForm, final BindingResult errors){
        User user;
        if(authUserService.isAuthenticated()) {
            user = authUserService.getCurrentUser();
        }
        else{
            LOGGER.info("User is not logged in");
            return new ModelAndView("user/login");
        }
        if(errors.hasErrors()){
            LOGGER.warn("Subject builder adding form has errors");
            return new ModelAndView("redirect:/builder/");
        }

        final Optional<Subject> maybeSubject = subjectService.findById(semesterForm.getIdSub());

        if(!maybeSubject.isPresent()){
            LOGGER.warn("No subject for id {}", semesterForm.getIdSub());
            return new ModelAndView("redirect:/builder/");
        }

        final Map<String, SubjectClass> classes = maybeSubject.get().getClassesById();

        if(!classes.containsKey(semesterForm.getIdClass())){
            LOGGER.warn("No class in subject {} for id {}", semesterForm.getIdSub(), semesterForm.getIdClass());
            return new ModelAndView("redirect:/builder/");
        }
        final SubjectClass subjectClass = classes.get(semesterForm.getIdClass());

        LOGGER.info("User {} removed to its current semester the subject: {}, class: {}", user.getId(), subjectClass.getSubject().getName(), subjectClass.getClassId());

        userService.removeFromCurrentSemester(user, subjectClass);

        return new ModelAndView("redirect:/builder/");
    }
}