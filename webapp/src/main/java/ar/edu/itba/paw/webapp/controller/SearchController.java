package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.SubjectFilterField;
import ar.edu.itba.paw.models.enums.SubjectProgress;
import ar.edu.itba.paw.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public ModelAndView search(@RequestParam Map<String, String> params) {
        final User user;
        if(!authUserService.isAuthenticated())
            user = null;
        else
            user = authUserService.getCurrentUser();

        final String query = params.getOrDefault("q","");
        final int page = Integer.parseInt(params.getOrDefault("pageNum", "1"));
        final String orderBy = params.getOrDefault("ob","name");
        final String dir = params.getOrDefault("dir","asc");

        final Map<String, String> filters = new HashMap<>();
        for(Map.Entry<String, String> entry : params.entrySet()) {
            final String key = entry.getKey();
            if(key.equals("q") || key.equals("pageNum") || key.equals("ob") || key.equals("dir"))
                continue;

            filters.put(key, entry.getValue());
        }

        final int totalPages = subjectService.getTotalPagesForSearch(query, params);

        if(page < 1 || page > totalPages)
            return new ModelAndView("redirect:/404");

        final List<Subject> subjects = subjectService.search(query, page, filters, orderBy, dir);
        final Map<SubjectFilterField, List<String>> relevantFilters = subjectService.getRelevantFiltersForSearch(query, filters);

        Map<String, SubjectProgress> progress = user == null ? new HashMap<>() : user.getSubjectProgress();

        ModelAndView mav = new ModelAndView("subjects/search");
        mav.addObject("subjects", subjects);
        mav.addObject("query", query);
        mav.addObject("subjectProgress", progress);
        mav.addObject("relevantFilters", relevantFilters);
        mav.addObject("totalPages", totalPages);
        mav.addObject("currentPage", page);

        return mav;
    }
}
