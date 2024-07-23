package ar.edu.itba.paw.webapp.security.config;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.webapp.security.filters.JwtFilter;
import ar.edu.itba.paw.webapp.security.filters.LocaleCaptureFilter;
import ar.edu.itba.paw.webapp.security.handlers.UniAccessDeniedHandler;
import ar.edu.itba.paw.webapp.security.handlers.UniAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;


@EnableWebSecurity
@ComponentScan({
        "ar.edu.itba.paw.webapp.security.services",
        "ar.edu.itba.paw.webapp.security.filters",
    })
@Configuration
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private Environment environment;

    @Autowired
    private JwtFilter jwtFilter;
    
    @Autowired
    private LocaleCaptureFilter localeCaptureFilter;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new UniAccessDeniedHandler();
    }

    @Bean
    public UniAuthenticationEntryPoint authenticationEntryPoint() {
        return new UniAuthenticationEntryPoint();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .cors().and()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new UniAuthenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler())
                .and().headers()
                .cacheControl().disable()
                .and().authorizeRequests()

                // Users
                .antMatchers(HttpMethod.POST, "/api/users").anonymous()
                .antMatchers(HttpMethod.GET, "/api/users").authenticated()
                .antMatchers(HttpMethod.PATCH, "/api/users/{id}").authenticated()
                .antMatchers(HttpMethod.GET, "/api/users/{id}").authenticated()

                // User plan
                .antMatchers(HttpMethod.GET, "/api/users/{id}/plan").authenticated()
                .antMatchers(HttpMethod.POST, "/api/users/{id}/plan").authenticated()
                .antMatchers(HttpMethod.PUT, "/api/users/{id}/plan").authenticated()
                .antMatchers(HttpMethod.PATCH, "/api/users/{id}/plan").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/users/{id}/plan").authenticated()

                // User progress
                .antMatchers(HttpMethod.GET, "/api/users/{id}/progress").authenticated()
                .antMatchers(HttpMethod.PATCH, "/api/users/{id}/progress").authenticated()

                // Subjects
                .antMatchers(HttpMethod.GET, "/api/subjects").authenticated()
                .antMatchers(HttpMethod.POST, "/api/subjects").hasRole(Role.RoleEnum.EDITOR.getName())
                .antMatchers(HttpMethod.GET, "/api/subjects/{id}").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/subjects/{id}").hasRole(Role.RoleEnum.EDITOR.getName())
                .antMatchers(HttpMethod.PUT, "/api/subjects/{id}").hasRole(Role.RoleEnum.EDITOR.getName())

                // Degree
                .antMatchers(HttpMethod.GET, "/api/degrees").authenticated()
                .antMatchers(HttpMethod.POST, "/api/degrees").hasRole(Role.RoleEnum.EDITOR.getName())
                .antMatchers(HttpMethod.GET, "/api/degrees/{id}").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/degrees/{id}").hasRole(Role.RoleEnum.EDITOR.getName())
                .antMatchers(HttpMethod.PUT, "/api/degrees/{id}").hasRole(Role.RoleEnum.EDITOR.getName())

                // Departments
                .antMatchers(HttpMethod.GET, "/api/departments").authenticated()

                // Degree semesters
                .antMatchers(HttpMethod.GET, "/api/degrees/{degreeId}/semesters").authenticated()
                .antMatchers(HttpMethod.GET, "/api/degrees/{degreeId}/semesters/{id}").authenticated()
                .antMatchers(HttpMethod.POST, "/api/degrees/{degreeId}/semesters").hasRole(Role.RoleEnum.EDITOR.getName())
                .antMatchers(HttpMethod.PATCH, "/api/degrees/{degreeId}/semesters").hasRole(Role.RoleEnum.EDITOR.getName())
                .antMatchers(HttpMethod.DELETE, "/api/degrees/{degreeId}/semesters/{id}").hasRole(Role.RoleEnum.EDITOR.getName())

                // Review
                .antMatchers(HttpMethod.GET, "/api/reviews").authenticated()
                .antMatchers(HttpMethod.POST, "/api/reviews").authenticated()
                .antMatchers(HttpMethod.GET, "/api/reviews/{id}").authenticated()
                .antMatchers(HttpMethod.PUT, "/api/reviews/{id}").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/reviews/{id}").authenticated()

                // Review votes
                .antMatchers(HttpMethod.GET, "/api/reviews/{id}/votes").authenticated()
                .antMatchers(HttpMethod.POST, "/api/reviews/{id}/votes").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/reviews/{id}/votes/{userId}").authenticated()

                // Professor
                .antMatchers(HttpMethod.GET, "/api/professors").authenticated()
                .antMatchers(HttpMethod.GET, "/api/professors/{id}").authenticated()
                .antMatchers(HttpMethod.POST, "/api/professors").hasRole(Role.RoleEnum.EDITOR.getName())

                // Images
                .antMatchers(HttpMethod.GET, "/api/images/{id}").permitAll()
                .antMatchers(HttpMethod.POST, "/api/images").authenticated()

                .antMatchers("/api/**").authenticated()
                .antMatchers("/**").permitAll()
                .and()
                    .addFilterBefore(jwtFilter, FilterSecurityInterceptor.class)
                    .addFilterAfter(localeCaptureFilter, FilterSecurityInterceptor.class);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/static/**","/css/**", "/js/**", "/static/**", "/assets/**", "favicon.ico");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedOrigins(Collections.singletonList("*"));
        cors.setAllowedMethods(Collections.singletonList("*"));
        cors.setAllowedHeaders(Collections.singletonList("*"));
        cors.setExposedHeaders(Arrays.asList("X-Refresh", "X-Auth", "Authorization", "Location", "Content-Disposition", "Link"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }

    private class AuthFailureHandler implements AuthenticationFailureHandler {
        @Override
        public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res, AuthenticationException e) throws IOException {
            final String email = req.getParameter("email");
            final String pass = req.getParameter("password");
            final String baseUrl = req.getContextPath();

            final Optional<User> maybeUser = userService.findUnconfirmedByEmail(email);
            if(!maybeUser.isPresent() || !passwordEncoder().matches(pass, maybeUser.get().getPassword())){
                res.sendRedirect(baseUrl + "/login?error=true");
                return;
            }

            final User user = maybeUser.get();
            res.sendRedirect(baseUrl + "/verification?email=" + user.getEmail());
        }
    }
}
