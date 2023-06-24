package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.SubjectProgress;
import ar.edu.itba.paw.models.exceptions.*;
import ar.edu.itba.paw.persistence.dao.ImageDao;
import ar.edu.itba.paw.persistence.dao.RecoveryDao;
import ar.edu.itba.paw.persistence.dao.UserDao;
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
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final RecoveryDao recDao;
    private final ImageDao imageDao;

    private final RolesService rolesService;
    private final SubjectService subjectService;
    private final DegreeService degreeService;
    private final MailService mailService;

    private final PasswordEncoder passwordEncoder;

    private static final int MAX_IMAGE_SIZE = 1024 * 1024 * 5;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(
            final UserDao userDao,
            final RecoveryDao recDao,
            final ImageDao imageDao,
            final RolesService rolesService,
            final SubjectService subjectService,
            final DegreeService degreeService,
            final MailService mailService,
            final PasswordEncoder passwordEncoder
    ) {
        this.userDao = userDao;
        this.recDao = recDao;
        this.imageDao = imageDao;
        this.rolesService = rolesService;
        this.subjectService = subjectService;
        this.degreeService = degreeService;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Optional<User> findById(final long id) {
        return userDao.findById(id);
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Transactional
    @Override
    public User create(final long degreeId, final String completedSubjectIds, final User user, final byte[] profilePic)
            throws EmailAlreadyTakenException, DegreeNotFoundException {

        final Image image = imageDao.create(profilePic);
        final String confirmToken = generateConfirmToken();

        final User newUser;
        try {
            newUser = userDao.create(
                    User.builderFrom(user)
                            .degree(degreeService.findById(degreeId).orElseThrow(DegreeNotFoundException::new))
                            .password(passwordEncoder.encode(user.getPassword()))
                            .verificationToken(confirmToken)
                            .imageId(image.getId())
            );
        } catch (final EmailAlreadyTakenException e) {
            LOGGER.debug("User email {} already taken", user.getEmail());
            throw e;
        }

        final Role role = rolesService.findByName("USER").orElseThrow(IllegalStateException::new);
        addRole(newUser, role);

        updateSubjectProgress(newUser, completedSubjectIds, SubjectProgress.DONE);

        mailService.sendVerification(newUser, confirmToken);

        return newUser;
    }

    @Transactional
    @Override
    public User create(final long degreeId, final String completedSubjectIds, final User user)
            throws EmailAlreadyTakenException, DegreeNotFoundException {

        final byte[] defaultImg;
        try {
            final File file = ResourceUtils.getFile("classpath:images/default_user.png");
            defaultImg = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            LOGGER.error("Failed to read default image");
            throw new IllegalStateException("Failed to read default image");
        }

        return create(degreeId, completedSubjectIds, user, defaultImg);
    }

    @Transactional
    @Override
    public void resendVerificationEmail(String email) throws UserNotFoundException {
        final User user = userDao.findUnverifiedByEmail(email).orElseThrow(UserNotFoundException::new);

        final String newToken = generateConfirmToken();
        userDao.updateVerificationToken(user, newToken);

        mailService.sendVerification(user, newToken);
    }

    @Transactional
    @Override
    public void updateProfilePicture(final User user, final byte[] newImage) throws InvalidImageSizeException {
        if (newImage.length > MAX_IMAGE_SIZE || newImage.length == 0) {
            throw new InvalidImageSizeException();
        }

        final Image image = imageDao.findById(user.getImageId()).orElseThrow(IllegalStateException::new);
        imageDao.update(image, newImage);
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public Optional<User> findUnconfirmedByEmail(final String email) {
        return userDao.findUnverifiedByEmail(email);
    }

    @Transactional
    @Override
    public void updateSubjectProgress(final User user, final Subject subject, final SubjectProgress progress) {
        userDao.updateSubjectProgress(user, subject, progress);
    }

    @Transactional
    @Override
    public void updateSubjectProgress(final User user, final List<String> subIds, final SubjectProgress progress) {
        if (subIds.isEmpty()) {
            return;
        }
        userDao.updateSubjectProgressList(user, subIds, progress);
    }

    @Transactional
    @Override
    public void updateSubjectProgress(final User user, final String subIds, final SubjectProgress progress) {
        updateSubjectProgress(user, parseJsonList(subIds), progress);
    }

    @Transactional
    @Override
    public void changePassword(
            final User user,
            final String password,
            final String oldPasswordInput
    ) throws OldPasswordDoesNotMatchException {
        if (!passwordEncoder.matches(oldPasswordInput, user.getPassword())) {
            LOGGER.debug("Old password does not match with input");
            throw new OldPasswordDoesNotMatchException();
        }

        userDao.changePassword(user, passwordEncoder.encode(password));
    }

    @Transactional
    @Override
    public void editProfile(final User user, final String username) {
        userDao.changeUsername(user, username);
    }

    @Override
    public boolean isValidRecoveryToken(final String token) {
        return recDao.findUserByToken(token).isPresent();
    }

    @Transactional
    @Override
    public void recoverPassword(final String token, final String newPassword) throws InvalidTokenException {
        final Optional<User> maybeUser = recDao.findUserByToken(token);
        if (!maybeUser.isPresent()) {
            LOGGER.info("Invalid token when trying to recover password");
            throw new InvalidTokenException();
        }

        final User user = maybeUser.get();

        userDao.changePassword(user, passwordEncoder.encode(newPassword));
        recDao.delete(user.getRecoveryToken());
        autoLogin(user);
    }

    @Transactional
    @Override
    public void sendPasswordRecoveryEmail(String email) throws UserNotFoundException {
        final User user = userDao.findByEmail(email).orElseThrow(UserNotFoundException::new);
        mailService.sendRecover(user, createRecoveryToken(user));
    }

    @Transactional
    @Override
    public void confirmUser(final String token) throws InvalidTokenException {
        final Optional<User> optUser = userDao.findByConfirmToken(token);
        if (!optUser.isPresent()) {
            LOGGER.info("Invalid token when trying to confirm user");
            throw new InvalidTokenException();
        }
        final User user = optUser.get();

        userDao.confirmUser(user);
        autoLogin(user);
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

    @Transactional
    @Override
    public void addToCurrentSemester(final User user, final String subjectId, final String classId) {
        final Subject subject = subjectService.findById(subjectId).orElseThrow(SubjectNotFoundException::new);

        final Map<String, SubjectClass> classes = subject.getClassesById();
        if (!classes.containsKey(classId)) {
            LOGGER.debug("No class in subject {} for id {}", subjectId, classId);
            throw new SubjectClassNotFoundException();
        }
        final SubjectClass subjectClass = classes.get(classId);

        userDao.addToCurrentSemester(user, subjectClass);
        LOGGER.info("User {} added to its current semester the subject: {}, class: {}", user.getId(), subjectClass.getSubject().getName(), subjectClass.getClassId());
    }

    @Transactional
    @Override
    public void removeFromCurrentSemester(final User user, final String subjectId, final String classId) throws SubjectNotFoundException, SubjectClassNotFoundException {
        final Subject subject = subjectService.findById(subjectId).orElseThrow(SubjectNotFoundException::new);

        final Map<String, SubjectClass> classes = subject.getClassesById();
        if(!classes.containsKey(classId)){
            LOGGER.warn("No class in subject {} for id {}", subjectId, classId);
            throw new SubjectClassNotFoundException();
        }
        final SubjectClass subjectClass = classes.get(classId);

        userDao.removeFromCurrentSemester(user, subjectClass);

        LOGGER.info("User {} removed from its current semester the subject: {}, class: {}", user.getId(), subjectClass.getSubject().getName(), subjectClass.getClassId());
    }

    @Transactional
    @Override
    public void clearSemester(final User user) {
        userDao.clearSemester(user);
    }

    @Override
    public String getSemesterSubmitRedirectUrl(final User user) {
        final List<String> subjectIds = user.getUserSemester().stream()
                .map(SubjectClass::getSubject)
                .map(Subject::getId)
                .collect(Collectors.toList());

        final StringBuilder sb = new StringBuilder("redirect:/many-reviews?r=");
        int i = 0;
        for (String subIds : subjectIds) {
            sb.append(subIds);
            if (i + 1 < subjectIds.size()) {
                sb.append(" ");
            }
            i++;
        }
        sb.append("&current=0");
        sb.append("&total=").append(i);

        return sb.toString();
    }

    @Transactional
    @Override
    public void finishSemester(final User user) {
        final List<String> userSemesterSubjectIds = user.getUserSemester().stream()
                .map(SubjectClass::getSubject)
                .map(Subject::getId)
                .collect(Collectors.toList());

        updateSubjectProgress(user, userSemesterSubjectIds, SubjectProgress.DONE);
        clearSemester(user);
    }

    @Transactional
    @Override
    public void addRole(final User user, final Role role) {
        userDao.addRole(user, role);
    }

    @Transactional
    @Override
    public void makeModerator(final User requesterUser, final long toMakeModeratorId) throws UserNotFoundException, UnauthorizedException {
        if(!requesterUser.isEditor()) throw new UnauthorizedException();

        final User toMakeModerator = userDao.findById(toMakeModeratorId).orElseThrow(UserNotFoundException::new);
        final Role role = rolesService.findByName(Role.RoleEnum.EDITOR.getName()).orElseThrow(IllegalStateException::new);
        addRole(toMakeModerator, role);
    }

    @Transactional
    @Override
    public void updateUserDegreeAndSubjectProgress(final User user, final Degree degree, final String subjectIds) {
        userDao.updateUserDegree(user, degree);
        updateSubjectProgress(user, parseJsonList(subjectIds), SubjectProgress.DONE);
    }

    private void autoLogin(final User user) {
        final Collection<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(String.format("ROLE_%s", role.getName())))
                .collect(Collectors.toSet());

        final Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
        LOGGER.info("Auto login for user {}", user.getId());
    }

    private String generateConfirmToken() {
        final SecureRandom random = new SecureRandom();
        final byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        return new String(Base64.getUrlEncoder().encode(bytes));
    }

    private String createRecoveryToken(final User user) {
        final SecureRandom random = new SecureRandom();
        final byte[] bytes = new byte[20];
        random.nextBytes(bytes);

        final String token = new String(Base64.getUrlEncoder().encode(bytes));

        recDao.create(token, user);

        return token;
    }

    private List<String> parseJsonList(String stringifyString) {
        String trimmedInput = stringifyString.replace("[", "").replace("]", "");
        String[] elements = trimmedInput.split(",");

        List<String> parsedList = new ArrayList<>();
        if (elements[0].equals("")) {
            return parsedList;
        }
        for (String element : elements) {
            // Remove quotes around each element
            String parsedElement = element.replace("\"", "");
            parsedList.add(parsedElement);
        }

        return parsedList;
    }
}
