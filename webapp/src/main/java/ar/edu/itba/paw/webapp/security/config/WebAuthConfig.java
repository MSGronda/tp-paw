package ar.edu.itba.paw.webapp.security.config;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.webapp.security.filters.JwtFilter;
import ar.edu.itba.paw.webapp.security.handlers.UniAccessDeniedHandler;
import ar.edu.itba.paw.webapp.security.handlers.UniAuthenticationEntryPoint;
import ar.edu.itba.paw.webapp.security.services.UniUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
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
@ComponentScan({"ar.edu.itba.paw.webapp.security.services",
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
        http
                .cors().and().csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new UniAuthenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler())
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().headers()
                .cacheControl().disable()
                .and().authorizeRequests()

                // TODO COMPLETE (!!!)
//                .antMatchers("/login","/register", "/recover/**", "/verification/**").anonymous()
//                .antMatchers("/user/{id:\\d+}/moderator", "/degrees", "/create-subject", "/subject/{id:\\d+\\.\\d+}/delete-subject", "/subject/{id:\\d+\\.\\d+}/edit").hasRole(Role.RoleEnum.EDITOR.getName())
//                .antMatchers("/").permitAll()
                .antMatchers("/**").authenticated()
                .and().addFilterBefore(jwtFilter, FilterSecurityInterceptor.class);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "favicon.ico");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedOrigins(Collections.singletonList("*"));
        cors.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        cors.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        cors.setExposedHeaders(Arrays.asList("X-JWT", "X-Refresh-Token", "X-Content-Type-Options", "X-XSS-Protection", "X-Frame-Options",
                "authorization", "Location",
                "Content-Disposition", "Link"));
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
