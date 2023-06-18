package ar.edu.itba.paw.webapp.interceptors;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.AuthUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Lazy
@Component
public class DegreeSelectInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(DegreeSelectInterceptor.class);

    private final AuthUserService authUserService;

    @Autowired
    public DegreeSelectInterceptor(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        if(!authUserService.isAuthenticated()) return true;

        final String degreeSelectUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/select-degree")
                .build()
                .toUriString();

        final User user = authUserService.getCurrentUser();
        if(user.getDegree() != null || request.getRequestURL().toString().equals(degreeSelectUrl)) {
            LOGGER.debug("User {} has already selected a degree or is in selector", user.getId());
            return true;
        }

        response.sendRedirect(degreeSelectUrl);
        LOGGER.info("Redirecting user {} to degree selection", user.getId());
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView mav) {
        // Do nothing
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {
        // Do nothing
    }
}
