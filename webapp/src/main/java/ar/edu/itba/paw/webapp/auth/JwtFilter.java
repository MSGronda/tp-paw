package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        final String header = req.getHeader(HttpHeaders.AUTHORIZATION);

        if(header == null || header.isEmpty()) {
            filterChain.doFilter(req, res);
            return;
        }

        if (header.startsWith("Bearer ")) {
            bearerAuth(req, res, filterChain);
            return;
        }

        if (header.startsWith("Basic ")) {
            basicAuth(req, res, filterChain);
            return;
        }

        filterChain.doFilter(req, res);
    }

    private void bearerAuth(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        final String header = req.getHeader(HttpHeaders.AUTHORIZATION);

        final String token = header.split(" ")[1];
        if (!jwtUtils.validate(token)) {
            filterChain.doFilter(req, res);
            return;
        }

        final String email = jwtUtils.getEmail(token);
        final UserDetails user;
        try {
            user = userDetailsService.loadUserByUsername(email);
        } catch (UsernameNotFoundException e) {
            filterChain.doFilter(req, res);
            return;
        }

        if (jwtUtils.isRefreshToken(token)) {
            final User uniUser = userService.findByEmail(email).orElse(null);
            if (uniUser == null) {
                filterChain.doFilter(req, res);
                return;
            }

            res.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtUtils.generateToken(email));
        }

        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities()
        );

        authentication.setDetails(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(req, res);
    }

    private void basicAuth(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        final String header = req.getHeader(HttpHeaders.AUTHORIZATION);
        final String encodedCredentials = header.split(" ")[1];

        final String[] credentials;
        try {
            credentials = new String(Base64.getDecoder().decode(encodedCredentials)).split(":");
        } catch (IllegalArgumentException e) {
            filterChain.doFilter(req, res);
            return;
        }

        if (credentials.length != 2) {
            filterChain.doFilter(req, res);
            return;
        }

        final String email = credentials[0];
        final String password = credentials[1];

        final UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(email, password);

        try {
            authenticationManager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            filterChain.doFilter(req, res);
            return;
        }

        final User user = userService.findByEmail(email).orElse(null);
        if(user == null) {
            filterChain.doFilter(req, res);
            return;
        }

        res.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtUtils.generateToken(email));
        res.setHeader("X-Refresh", "Bearer " + jwtUtils.generateRefreshToken(email));

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(req, res);
    }
}
