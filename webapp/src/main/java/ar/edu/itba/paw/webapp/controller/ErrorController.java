package ar.edu.itba.paw.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {
    @RequestMapping("/403")
    public String forbidden() {
        return "error/unauthorized";
    }

    @RequestMapping("/404")
    public String notFound() {
        return "error/page_not_found";
    }

    @RequestMapping("/400")
    public String badRequest() {
        return "error/bad_request";
    }

    @RequestMapping("/500")
    public String internalServerError() {
        return "error/internal_error";
    }
}
