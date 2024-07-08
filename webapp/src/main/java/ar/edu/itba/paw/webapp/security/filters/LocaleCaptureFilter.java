package ar.edu.itba.paw.webapp.security.filters;

import ar.edu.itba.paw.services.AuthUserService;
import ar.edu.itba.paw.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LocaleCaptureFilter extends OncePerRequestFilter {
    @Lazy
    @Autowired
    private AuthUserService authUserService;
    
    @Lazy
    @Autowired
    private UserService userService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        if(authUserService.isAuthenticated()) {
            userService.setLocale(authUserService.getCurrentUser(), req.getLocale());
        }
        
        filterChain.doFilter(req, res);
    }
}
