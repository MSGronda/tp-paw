package ar.edu.itba.paw.webapp.interceptors;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.AuthUserService;
import ar.edu.itba.paw.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Lazy
@Component
public class LocaleInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocaleInterceptor.class);

    private final AuthUserService authUserService;
    private final UserService userService;

    @Autowired
    public LocaleInterceptor(AuthUserService authUserService, UserService userService) {
        this.authUserService = authUserService;
        this.userService = userService;
    }

    @Override
    public boolean preHandle(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse, final Object o) {
        return true;
    }

    @Override
    public void postHandle(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse, final Object o, final ModelAndView modelAndView) {
        // Do nothing
    }

    @Override
    public void afterCompletion(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse, final Object o, final Exception e) {
        if(!authUserService.isAuthenticated()) return;

        userService.setLocale(authUserService.getCurrentUser(), httpServletRequest.getLocale());
    }
}
