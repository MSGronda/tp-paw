package ar.edu.itba.paw.webapp.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;

public class UnconditionalCacheFilter extends OncePerRequestFilter {
    public static final long MAX_AGE = Duration.ofDays(365).getSeconds();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(HttpMethod.GET.matches(request.getMethod())) {
            response.setHeader(HttpHeaders.CACHE_CONTROL, String.format("public, max-age=%d, inmutable", MAX_AGE));
        }
        filterChain.doFilter(request,response);
    }
}
