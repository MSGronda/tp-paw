package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.SubjectProgress;
import ar.edu.itba.paw.persistence.dao.ImageDao;
import ar.edu.itba.paw.persistence.dao.RecoveryDao;
import ar.edu.itba.paw.persistence.dao.UserDao;
import ar.edu.itba.paw.persistence.exceptions.UserEmailAlreadyTakenPersistenceException;
import ar.edu.itba.paw.services.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final RecoveryDao recDao;
    private final ImageDao imageDao;
    private final RolesService rolesService;
    private final PasswordEncoder passwordEncoder;

    private static final int MAX_IMAGE_SIZE = 1024 * 1024 * 5;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(
            final UserDao userDao,
            final RecoveryDao recDao,
            final ImageDao imageDao,
            final RolesService rolesService,
            final PasswordEncoder passwordEncoder
    ) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.recDao = recDao;
        this.imageDao = imageDao;
        this.rolesService = rolesService;
    }


    @Override
    public Optional<User> findById(Long id) {
        return userDao.findById(id);
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Transactional
    @Override
    public User create(final User user, final byte[] profilePic) throws UserEmailAlreadyTakenException {
        long imageId = imageDao.insertAndReturnKey(profilePic);

        final String confirmToken = generateConfirmToken();

        User newUser;
        try {
            newUser = userDao.create(
                    User.builderFrom(user)
                            .password(passwordEncoder.encode(user.getPassword()))
                            .confirmToken(confirmToken)
                            .imageId(imageId)
                            .build()
            );
        } catch (final UserEmailAlreadyTakenPersistenceException e) {
            LOGGER.info("User {} failed to create", user.getEmail());
            throw new UserEmailAlreadyTakenException();
        }

        Optional<Role> maybeRole = rolesService.findByName("USER");
        if(!maybeRole.isPresent()){
            throw new IllegalStateException("USER role not found");
        }
        Role role = maybeRole.get();
        addRole(newUser, role);


        return newUser;
    }

    @Transactional
    @Override
    public User create(final User user) throws UserEmailAlreadyTakenException, IOException {
        File file = ResourceUtils.getFile("classpath:images/default_user.png");
        byte[] defaultImg = Files.readAllBytes(file.toPath());
        return create(user, defaultImg);
    }

    @Transactional
    @Override
    public String regenerateConfirmToken(final User user) {
        String newToken = generateConfirmToken();

        userDao.updateConfirmToken(user, newToken);

        return newToken;
    }

    @Transactional
    @Override
    public void updateProfilePicture(final User user, final byte[] image) throws InvalidImageSizeException {
        if(image.length > MAX_IMAGE_SIZE || image.length == 0){
            throw new InvalidImageSizeException();
        }
        imageDao.updateImage(user.getImageId(), image);
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public Optional<User> findUnconfirmedByEmail(final String email) {
        return userDao.findUnconfirmedByEmail(email);
    }

    @Transactional
    @Override
    public void deleteSubjectProgress(final User user, final Subject subject){
        userDao.deleteSubjectProgress(user, subject);
    }

    @Transactional
    @Override
    public void updateSubjectProgress(final User user, final Subject subject, final SubjectProgress progress) {
        if(progress != SubjectProgress.PENDING)
            userDao.updateSubjectProgress(user, subject, progress);
        else
            userDao.deleteSubjectProgress(user, subject);
    }

    @Transactional
    @Override
    public void changePassword(
            final User user,
            final String password,
            final String oldPassword,
            final String userOldPassword
    ) throws OldPasswordDoesNotMatchException {

        if(!passwordEncoder.matches(oldPassword, userOldPassword)){
            LOGGER.warn("Old password does not match with input. Update failed");
            throw new OldPasswordDoesNotMatchException();
        }
        userDao.changePassword(user, passwordEncoder.encode(password));
    }

    @Transactional
    @Override
    public void editProfile(final User user, final String username) {
        userDao.changeUsername(user, username);
    }

    @Transactional
    @Override
    public String generateRecoveryToken(final String email){
        final Optional<User> optUser = findByEmail(email);
        if(!optUser.isPresent()){
            LOGGER.warn("Generation of recovery token failed. User not found");
            throw new UserEmailNotFoundException();
        }
        final User user = optUser.get();

        final SecureRandom random = new SecureRandom();
        final byte[] bytes = new byte[20];
        random.nextBytes(bytes);

        final String token = new String(Base64.getUrlEncoder().encode(bytes));

        recDao.create(token, user.getId());

        return token;
    }

    @Override
    public boolean isValidRecoveryToken(final String token) {
        return recDao.findUserIdByToken(token).isPresent();
    }

    @Transactional
    @Override
    public void recoverPassword(final String token, final String newPassword) throws InvalidTokenException {
        final Optional<Long> optUserId = recDao.findUserIdByToken(token);
        if(!optUserId.isPresent()){
            LOGGER.info("Invalid token when trying to recover password");
            throw new InvalidTokenException();
        }

        final long userId = optUserId.get();
        final User user = userDao.findById(userId).orElseThrow(IllegalStateException::new);

        userDao.changePassword(user, passwordEncoder.encode(newPassword));
        recDao.delete(token);
        autoLogin(userId);
    }

    @Transactional
    @Override
    public void confirmUser(final String token) throws InvalidTokenException {
        final Optional<User> optUser = userDao.findByConfirmToken(token);
        if(!optUser.isPresent()){
            LOGGER.info("Invalid token when trying to confirm user");
            throw new InvalidTokenException();
        }
        final User user = optUser.get();

        userDao.confirmUser(user);
        autoLogin(user.getId());
    }

    @Transactional
    @Override
    public void setLocale(final User user, final Locale locale) {
        userDao.setLocale(user, locale);
    }

    @Async
    @Transactional
    @Override
    public void setLocaleAsync(final User user, final Locale locale) {
        setLocale(user, locale);
    }

    private void autoLogin(final long userId) {
        final Optional<User> maybeUser = findById(userId);
        if(!maybeUser.isPresent()){
            LOGGER.info("No registered user with id {}", userId);
            throw new UserEmailNotFoundException();
        }
        User user = maybeUser.get();
        final Collection<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(String.format("ROLE_%s", Role.RoleEnum.USER.getName())));
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
        LOGGER.info("Auto login for user {}", userId);
    }

    private String generateConfirmToken() {
        final SecureRandom random = new SecureRandom();
        final byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        return new String(Base64.getUrlEncoder().encode(bytes));
    }

    @Transactional
    @Override
    public void addRole(final User user, final Role role) {
        userDao.addRole(user, role);
    }

    @Transactional
    @Override
    public void updateRoles(final User user, final Role role) {
        userDao.updateRoles(user, role);
    }
}
