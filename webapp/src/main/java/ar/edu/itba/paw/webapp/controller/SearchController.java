package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


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
            @RequestParam(name = "pageNum", defaultValue = "1") final int page,
            @RequestParam(name = "ob", defaultValue = "name") final String orderBy,
            @RequestParam(name = "dir", defaultValue = "asc") final String dir,
            @RequestParam(name = "credits", required = false) final Integer credits,
            @RequestParam(name = "department", required = false) final String department
    ) {
        final ModelAndView mav = new ModelAndView("subjects/search");
        mav.addObject("subjects", subjectService.search(query, page, orderBy, dir, credits, department));
        mav.addObject("subjectProgress", authUserService.getCurrentUser().getAllSubjectProgress());
        mav.addObject("relevantFilters", subjectService.getRelevantFiltersForSearch(query, credits, department));
        mav.addObject("totalPages", subjectService.getTotalPagesForSearch(query, credits, department));
        mav.addObject("query", query);
        mav.addObject("currentPage", page);
        return mav;
    }
}
