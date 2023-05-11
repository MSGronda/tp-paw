package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.Roles;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class UniUserDetailsService implements UserDetailsService {
    @Autowired
    private final UserService us;

    private final Pattern BCRYPT_PATTERN = Pattern
            .compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");

    @Autowired
    public UniUserDetailsService(final UserService us) {
        this.us = us;
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        final User user = us.getUserWithEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user for email" + email));

//        if( !BCRYPT_PATTERN.matcher(user.getPassword()).matches()){
//            //TODO - update password with hashed version
//            us.changePassword(email, user.getPassword());
//            return loadUserByUsername(email);
//        }

        //TODO: implement logic to grant roles required authorities

        final List<Roles> userRoles = us.getUserRoles(user.getId());

        final Collection<GrantedAuthority> authorities = new HashSet<>();
        userRoles.forEach(roles -> authorities.add(new SimpleGrantedAuthority(String.format("ROLE_%s", roles.getName()))));
        return new UniAuthUser(email, user.getPassword(), authorities);
    }
}
