package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.SubjectProgress;
import ar.edu.itba.paw.persistence.dao.ImageDao;
import ar.edu.itba.paw.persistence.dao.RecoveryDao;
import ar.edu.itba.paw.persistence.dao.UserDao;
import ar.edu.itba.paw.persistence.exceptions.EmailAlreadyTakenException;
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
import java.util.stream.Collectors;

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
    public User create(final User user, final byte[] profilePic) throws ar.edu.itba.paw.services.exceptions.UserEmailAlreadyTakenException {
        final Image image = imageDao.create(profilePic);

        final String confirmToken = generateConfirmToken();

        User newUser;
        try {
            newUser = userDao.create(
                    User.builderFrom(user)
                            .password(passwordEncoder.encode(user.getPassword()))
                            .confirmToken(confirmToken)
                            .imageId(image.getId())
                            .build()
            );
        } catch (final EmailAlreadyTakenException e) {
            LOGGER.info("User {} failed to create", user.getEmail());
            throw new ar.edu.itba.paw.services.exceptions.UserEmailAlreadyTakenException();
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
    public User create(final User user) throws ar.edu.itba.paw.services.exceptions.UserEmailAlreadyTakenException, IOException {
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
    public void updateProfilePicture(final User user, final byte[] newImage) throws InvalidImageSizeException {
        if(newImage.length > MAX_IMAGE_SIZE || newImage.length == 0){
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
    public void updateSubjectProgressWithSubList( final User user, final String subIds){
        List<String> subjectIdList = parseString(subIds);
        if(subjectIdList.isEmpty()){
            return;
        }
        userDao.updateSubjectProgressList(user, subjectIdList);
    }

    private List<String> parseString(String stringifyString){
        String trimmedInput = stringifyString.replace("[", "").replace("]", "");
        String[] elements = trimmedInput.split(",");

        List<String> parsedList = new ArrayList<>();
        if(elements[0].equals("")){
            return parsedList;
        }
        for (String element : elements) {
            // Remove quotes around each element
            String parsedElement = element.replace("\"", "");
            parsedList.add(parsedElement);
        }

        return parsedList;
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

        recDao.create(token, user);

        return token;
    }

    @Override
    public boolean isValidRecoveryToken(final String token) {
        return recDao.findUserByToken(token).isPresent();
    }

    @Transactional
    @Override
    public void recoverPassword(final String token, final String newPassword) throws InvalidTokenException {
        final Optional<User> maybeUser = recDao.findUserByToken(token);
        if(!maybeUser.isPresent()){
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
    public void confirmUser(final String token) throws InvalidTokenException {
        final Optional<User> optUser = userDao.findByConfirmToken(token);
        if(!optUser.isPresent()){
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

    @Override
    public Map<Integer, Double> getUserProgressionPerYear(Degree degree, User user){
        Map<Integer, Double> progress = new LinkedHashMap<>();

        for(DegreeYear year: degree.getYears()){
            int credits = 0;
            int totalCredits = 0;
            for(Subject sub : year.getSubjects()){
                if(user.getSubjectProgress().containsKey(sub.getId())){
                    credits += sub.getCredits();
                }
                totalCredits += sub.getCredits();
            }
            if (totalCredits == 0) totalCredits = 1;
            progress.put(year.getNumber(), 100.0 * credits / totalCredits);
        }

        int credits = 0;
        int totalCredits = 0;
        for(Subject sub : degree.getElectives()){
            if(user.getSubjectProgress().containsKey(sub.getId())){
                credits += sub.getCredits();
            }
            totalCredits += sub.getCredits();
        }
        if(totalCredits == 0) totalCredits = 1;

        progress.put(-1, 100.0 * credits / totalCredits);
        return progress;
    }

    @Transactional
    @Override
    public void addToCurrentSemester(User user, SubjectClass subjectClass) {
        userDao.addToCurrentSemester(user,subjectClass);
    }

    @Transactional
    @Override
    public void removeFromCurrentSemester(User user, SubjectClass subjectClass) {
        userDao.removeFromCurrentSemester(user,subjectClass);
    }

    @Transactional
    @Override
    public void clearSemester(final User user) {
        userDao.clearSemester(user);
    }

    @Override
    public boolean canReviewGivenSubjectList(final String subjectIds){
        return !subjectIds.equals("[]");
    }

    @Override
    public String generateSemesterReviewUrl(final String subjectIds){
        List<String> subjectIdList = parseString(subjectIds);

        StringBuilder sb = new StringBuilder("?r=");
        int i=0;
        for(String subIds : subjectIdList){
            sb.append(subIds);
            if(i + 1 < subjectIdList.size()){
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
    public void addRole(final User user, final Role role) {
        userDao.addRole(user, role);
    }

    @Transactional
    @Override
    public void updateRoles(final User user, final Role role) {
        userDao.updateRoles(user, role);
    }

    private void autoLogin(final User user) {
        final Collection<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(String.format("ROLE_%s", role.getName())))
                .collect(Collectors.toSet());

        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
        LOGGER.info("Auto login for user {}", user.getId());
    }

    private String generateConfirmToken() {
        final SecureRandom random = new SecureRandom();
        final byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        return new String(Base64.getUrlEncoder().encode(bytes));
    }

    @Transactional
    @Override
    public void updateUserDegree(final User user, final Degree degree){
        userDao.updateUserDegree(user, degree);

    }

    @Transactional
    @Override
    public void setAllSubjectProgress(final User user, final String subjectIdsStringify){
        List<String> subjectIds = parseString(subjectIdsStringify);
        Map<String, SubjectProgress> progressMap = new HashMap<>();

        for(String subjectId : subjectIds){
            progressMap.put(subjectId, SubjectProgress.DONE);
        }

        userDao.setAllSubjectProgress(user, progressMap);
    }
}
