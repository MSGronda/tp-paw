package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exceptions.SubjectNotFoundException;
import ar.edu.itba.paw.models.exceptions.UnauthorizedException;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.services.exceptions.SubjectIdAlreadyExistsException;
import ar.edu.itba.paw.webapp.form.EditSubjectForm;
import ar.edu.itba.paw.webapp.form.SubjectForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
public class SubjectController {
    private final SubjectService subjectService;
    private final ReviewService reviewService;
    private final DegreeService degreeService;
    private final AuthUserService authUserService;
    private final ProfessorService professorService;

    @Autowired
    public SubjectController(
            final SubjectService subjectService,
            final ReviewService reviewService,
            final DegreeService degreeService,
            final AuthUserService authUserService,
            final ProfessorService professorService
    ) {
        this.subjectService = subjectService;
        this.reviewService = reviewService;
        this.degreeService = degreeService;
        this.authUserService = authUserService;
        this.professorService = professorService;
    }

    @RequestMapping("/subject/{id:\\d+\\.\\d+}")
    public ModelAndView subjectInfo(
            @PathVariable final String id,
            @RequestParam(name = "page", defaultValue = "1") final int page,
            @RequestParam(defaultValue = "name") final String order,
            @RequestParam(defaultValue = "asc") final String dir
    ) {
        final Subject subject = subjectService.findById(id).orElseThrow(SubjectNotFoundException::new);
        final User user = authUserService.getCurrentUser();

        final ModelAndView mav = new ModelAndView("subjects/subject_info");
        mav.addObject("totalPages", reviewService.getTotalPagesForSubjectReviews(subject));
        mav.addObject("reviews", reviewService.getAllSubjectReviews(subject, page, order, dir));
        mav.addObject("degree", degreeService.findParentDegree(subject, user));
        mav.addObject("year", degreeService.findSubjectYearForParentDegree(subject, user));
        mav.addObject("didReview", reviewService.didUserReview(subject, user));
        mav.addObject("progress", user.getSubjectProgress(subject));
        mav.addObject("user", user);
        mav.addObject("subject", subject);
        mav.addObject("currentPage", page);
        mav.addObject("order", order);
        mav.addObject("dir", dir);

        return mav;
    }

    @RequestMapping(value = "/create-subject", method = {RequestMethod.GET} )
    public ModelAndView createSubjectForm(@ModelAttribute("subjectForm") final SubjectForm subjectForm) {

        ModelAndView mav = new ModelAndView("moderator-tools/createSubject");
        List<Subject> subjects = subjectService.getAll();
        List<Professor> professors = professorService.getAll();
        List<Degree> degrees = degreeService.getAll();
        mav.addObject("subjects", subjects);
        mav.addObject("professors", professors);
        mav.addObject("degrees", degrees);
        return mav;
    }
    @RequestMapping(value = "/create-subject", method = {RequestMethod.POST} )
    public ModelAndView createSubject(@Valid @ModelAttribute("subjectForm") final SubjectForm subjectForm,
                                      final BindingResult errors) {
        if(errors.hasErrors()) {
            return createSubjectForm(subjectForm);
        }
        Subject newSub;
        try{
            newSub = subjectService.create(Subject.builder()
                    .id(subjectForm.getId())
                    .name(subjectForm.getName())
                    .department(subjectForm.getDepartment())
                    .credits(subjectForm.getCredits()),
                    subjectForm.getDegreeIds(),
                    subjectForm.getSemesters(),
                    subjectForm.getRequirementIds(),
                    subjectForm.getProfessors(),
                    subjectForm.getClassCodes(),
                    subjectForm.getClassProfessors(),
                    subjectForm.getClassDays(),
                    subjectForm.getClassStartTimes(),
                    subjectForm.getClassEndTimes(),
                    subjectForm.getClassBuildings(),
                    subjectForm.getClassRooms(),
                    subjectForm.getClassModes()
            );
        } catch (SubjectIdAlreadyExistsException e) {
            ModelAndView mav = createSubjectForm(subjectForm);
            mav.addObject("subjectCodeRepeated", true);
            return mav;
        }
        return new ModelAndView("redirect:/subject/"+newSub.getId());
    }

    @RequestMapping("/subject/{id:\\d+\\.\\d+}/delete-subject")
    public ModelAndView deleteSubject(
            @PathVariable final String id
    ) throws UnauthorizedException, SubjectNotFoundException {

        subjectService.delete(authUserService.getCurrentUser(), id);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/subject/{id:\\d+\\.\\d+}/edit", method = {RequestMethod.GET})
    public ModelAndView editSubjectForm(@PathVariable final String id, @ModelAttribute("subjectForm") final EditSubjectForm editSubjectForm,
                                        final BindingResult errors) {
        final Subject subject = subjectService.findById(id).orElseThrow(SubjectNotFoundException::new);
        List<Subject> subjects = subjectService.getAll();
        List<Professor> professors = professorService.getAll();
        List<Degree> degrees = degreeService.getAll();
        final ModelAndView mav = new ModelAndView("moderator-tools/editSubject");
        mav.addObject("subject", subject);
        mav.addObject("allSubjects", subjects);
        mav.addObject("professors", professors);
        mav.addObject("degrees", degrees);
        return mav;
    }

    @RequestMapping(value = "/subject/{id:\\d+\\.\\d+}/edit", method = {RequestMethod.POST})
    public ModelAndView editSubject(@PathVariable final String id, @Valid @ModelAttribute("editSubjectForm") final EditSubjectForm editSubjectForm,
                                    final BindingResult errors) {
        subjectService.edit(
                id,
                editSubjectForm.getCredits(),
                editSubjectForm.getDegreeIds(),
                editSubjectForm.getSemesters(),
                editSubjectForm.getRequirementIds(),
                editSubjectForm.getProfessors(),
                editSubjectForm.getClassIds(),
                editSubjectForm.getClassCodes(),
                editSubjectForm.getClassProfessors(),
                editSubjectForm.getClassDays(),
                editSubjectForm.getClassStartTimes(),
                editSubjectForm.getClassEndTimes(),
                editSubjectForm.getClassBuildings(),
                editSubjectForm.getClassRooms(),
                editSubjectForm.getClassModes()
        );
        return new ModelAndView("redirect:/subject/"+id);
    }



}
