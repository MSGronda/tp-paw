package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;


@Controller
public class SearchController {
    private final SubjectService subjectService;
    private final AuthUserService authUserService;

    @Autowired
    public SearchController(SubjectService subjectService, AuthUserService authUserService) {
        this.subjectService = subjectService;
        this.authUserService = authUserService;
    }

    @RequestMapping("/search")
    public ModelAndView search(
            @RequestParam(name = "q", defaultValue = "") final String query,
            @RequestParam(name = "page", defaultValue = "1") final int page,
            @RequestParam(name = "ob", defaultValue = "name") final String orderBy,
            @RequestParam(name = "dir", defaultValue = "asc") final String dir,
            @RequestParam(name = "credits", required = false) final Integer credits,
            @RequestParam(name = "department", required = false) final String department,
            @RequestParam(name = "difficulty", required = false) final Integer difficulty,
            @RequestParam(name = "time", required = false) final Integer time
    ) {
        final User user = authUserService.getCurrentUser();
        final ModelAndView mav = new ModelAndView("subjects/search");

        final Map<String, List<String>> filters = subjectService.getRelevantFiltersForSearch(
                user,
                query,
                credits,
                department,
                difficulty,
                time,
                orderBy
        );

        final List<Subject> result = subjectService.search(
                user,
                query,
                page,
                orderBy,
                dir,
                credits,
                department,
                difficulty,
                time
        );

        final int totalPages = subjectService.getTotalPagesForSearch(
                user,
                query,
                credits,
                department,
                difficulty,
                time,
                orderBy
        );

        mav.addObject("subjects", result);
        mav.addObject("subjectProgress", user.getAllSubjectProgress());
        mav.addObject("relevantFilters", filters);
        mav.addObject("totalPages", totalPages);
        mav.addObject("query", query);
        mav.addObject("currentPage", page);
        return mav;
    }
}
