package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.SubjectProgress;
import ar.edu.itba.paw.models.exceptions.*;
import ar.edu.itba.paw.persistence.dao.ImageDao;
import ar.edu.itba.paw.persistence.dao.RecoveryDao;
import ar.edu.itba.paw.persistence.dao.UserDao;
import ar.edu.itba.paw.services.enums.UserSemesterEditType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Optional<User> getRelevantUser(final Long available, final Long unLockable, final Long done, final Long future, final Long plan){
        if(available != null){
            return findById(available);
        }
        if(unLockable != null){
            return findById(unLockable);
        }
        if(done != null){
            return findById(done);
        }
        if(future != null){
            return findById(future);
        }
        if(plan != null){
            return findById(plan);
        }
        return findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }


    @Override
    public Optional<User> findById(final long id) {
        return userDao.findById(id);
    }

    @Transactional
    @Override
    public User create(final User user, final byte[] profilePic)
            throws EmailAlreadyTakenException, DegreeNotFoundException {

        final Image image = imageDao.create(profilePic);
        final String confirmToken = generateConfirmToken();

        final User newUser;
        try {
            newUser = userDao.create(
                    User.builderFrom(user)
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

        //updateMultipleSubjectProgress(newUser, completedSubjectIds, SubjectProgress.DONE);

        mailService.sendVerification(newUser, confirmToken);

        return newUser;
    }

    @Transactional
    @Override
    public User create(final User user)
            throws EmailAlreadyTakenException, DegreeNotFoundException {

        final byte[] defaultImg;
        try {
            final File file = ResourceUtils.getFile("classpath:images/default_user.png");
            defaultImg = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            LOGGER.error("Failed to read default image");
            throw new IllegalStateException("Failed to read default image");
        }

        return create(user, defaultImg);
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
    public void updateSingleSubjectProgress(final User user, final Subject subject, final SubjectProgress progress) {
        if (progress == SubjectProgress.DONE) {
            for (final SubjectClass c : subject.getClasses()) {
                if (user.getUserSemester().contains(c)) {
                    userDao.removeFromCurrentSemester(user, c);
                }
            }
        }

        userDao.updateSubjectProgress(user, subject, progress);
    }

    @Transactional
    @Override
    public void updateSingleSubjectProgress(final User user, final String subjectId, final SubjectProgress progress) {
        final Subject subject = subjectService.findById(subjectId).orElseThrow(SubjectNotFoundException::new);

        updateSingleSubjectProgress(user, subject, progress);
    }

    @Transactional
    @Override
    public void updateMultipleSubjectProgress(final User user, final List<String> subIds, final SubjectProgress progress) {
        if (subIds.isEmpty()) {
            return;
        }
        userDao.updateSubjectProgressList(user, subIds, progress);
    }

    @Transactional
    @Override
    public void updateMultipleSubjectProgress(final User user, final String subIds, final SubjectProgress progress) {
        updateMultipleSubjectProgress(user, parseJsonList(subIds), progress);
    }

    @Transactional
    @Override
    public void updateMultipleSubjectProgress(
            final User currentUser,
            final Long userId,
            final List<String> passedSubjects,
            final List<String> notPassedSubject
    ){
        if(currentUser.getId() != userId){
            throw new UnauthorizedException();
        }
        if(!passedSubjects.isEmpty()){
            userDao.updateSubjectProgressList(currentUser, passedSubjects, SubjectProgress.DONE);
        }
        if(!notPassedSubject.isEmpty()){
            userDao.updateSubjectProgressList(currentUser, notPassedSubject, SubjectProgress.PENDING);
        }

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
        if(!user.getLocale().equals(locale)){
            userDao.setLocale(user, locale);
            LOGGER.debug("Set locale for user {} to '{}'", user.getId(), locale);
        }
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

        if(semesterAlreadyContainsSubject(user, subjectClass)){
            throw new UserSemesterAlreadyContainsSubjectException();
        }

        userDao.addToCurrentSemester(user, subjectClass);
        LOGGER.info("User {} added to its current semester the subject: {}, class: {}", user.getId(), subjectClass.getSubject().getName(), subjectClass.getClassId());
    }

    private boolean semesterAlreadyContainsSubject(final User user, final SubjectClass subjectClass){
        for(final SubjectClass sb : user.getUserSemester()){
            if(Objects.equals(sb.getSubject().getId(), subjectClass.getSubject().getId())){
                return true;
            }
        }
        return false;
    }

    @Transactional
    @Override
    public void createUserSemester(final User currentUser, final Long userId, final List<String> subjectIds, final List<String> classIds){
        if(userId != currentUser.getId()){
            throw new UnauthorizedException();
        }
        if(subjectIds.size() != classIds.size()){
            throw new InvalidUserSemesterIds();
        }
        if(!currentUser.getUserSemester().isEmpty()){
            throw new UserAlreadyHasSemesterException();
        }

        for(int i=0; i<subjectIds.size(); i++){
            addToCurrentSemester(currentUser, subjectIds.get(i), classIds.get(i));
        }
    }

    @Transactional
    @Override
    public void editUserSemester(
            final User currentUser,
            final Long userId,
            final UserSemesterEditType type,
            final List<String> subjectIds,
            final List<String> classIds
    ){
        switch (type){
            case ADD_SUBJECT:
                if(subjectIds == null || classIds == null || subjectIds.size() != classIds.size()){
                    throw new InvalidUserSemesterIds();
                }
                for(int i=0; i<subjectIds.size(); i++){
                    addToCurrentSemester(currentUser, subjectIds.get(i), classIds.get(i));
                }
                break;
            case REMOVE_SUBJECT:
                if(subjectIds == null || classIds == null || subjectIds.size() != classIds.size()){
                    throw new InvalidUserSemesterIds();
                }
                for(int i=0; i<subjectIds.size(); i++){
                    removeFromCurrentSemester(currentUser, subjectIds.get(i), classIds.get(i));
                }
                break;
            case FINISH_SEMESTER:

                // Debera hacer un PATCH a {id}/progress para marcar como curzadas las materias

                userDao.clearSemester(currentUser);
        }
    }

    @Transactional
    @Override
    public void removeFromCurrentSemester(final User user, final String subjectId, final String classId) throws SubjectNotFoundException, SubjectClassNotFoundException {
        final Subject subject = subjectService.findById(subjectId).orElseThrow(SubjectNotFoundException::new);

        final Map<String, SubjectClass> classes = subject.getClassesById();
        if (!classes.containsKey(classId)) {
            LOGGER.debug("No class in subject {} for id {}", subjectId, classId);
            throw new SubjectClassNotFoundException();
        }
        final SubjectClass subjectClass = classes.get(classId);

        userDao.removeFromCurrentSemester(user, subjectClass);

        LOGGER.info("User {} removed from its current semester the subject: {}, class: {}", user.getId(), subjectClass.getSubject().getName(), subjectClass.getClassId());
    }

    @Transactional
    @Override
    public void deleteUserSemester(final User currentUser, final Long userId){
        if(currentUser.getId() != userId){
            throw new UnauthorizedException();
        }
        userDao.clearSemester(currentUser);
    }

    @Transactional
    @Override
    public void replaceUserSemester(final User currentUser, final Long userId, final List<String> subjectIds, final List<String> classIds){
        if(currentUser.getId() != userId){
            throw new UnauthorizedException();
        }
        if(subjectIds.size() != classIds.size()){
            throw new InvalidUserSemesterIds();
        }
        userDao.clearSemester(currentUser);

        for(int i=0; i<subjectIds.size(); i++){
            addToCurrentSemester(currentUser, subjectIds.get(i), classIds.get(i));
        }
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
    public void finishSemester(final User user, final List<String> subjectIds) {
        if(!validFinishedSemesterSubjectIds(user, subjectIds)){
            throw new InvalidUserSemesterIds();
        }
        updateMultipleSubjectProgress(user, subjectIds, SubjectProgress.DONE);
        userDao.clearSemester(user);
    }

    private boolean validFinishedSemesterSubjectIds(final User user, final List<String> subjectIds){
        for(String idSub : subjectIds) {
            boolean valid = false;
            for(SubjectClass subject : user.getUserSemester()){
                if (subject.getSubject().getId().equals(idSub)) {
                    valid = true;
                    break;
                }
            }
            if(!valid){
                return false;
            }
        }
        return true;
    }

    @Transactional
    @Override
    public void addRole(final User user, final Role role) {
        userDao.addRole(user, role);
    }

    @Transactional
    @Override
    public void makeModerator(final User requesterUser, final long toMakeModeratorId) throws UserNotFoundException, UnauthorizedException {
        if (!requesterUser.isEditor()) throw new UnauthorizedException();

        final User toMakeModerator = userDao.findById(toMakeModeratorId).orElseThrow(UserNotFoundException::new);
        final Role role = rolesService.findByName(Role.RoleEnum.EDITOR.getName()).orElseThrow(IllegalStateException::new);
        addRole(toMakeModerator, role);
    }

    @Transactional
    @Override
    public void updateUserDegreeAndSubjectProgress(final User user, final Degree degree, final String subjectIds) {
        userDao.updateUserDegree(user, degree);
        updateMultipleSubjectProgress(user, parseJsonList(subjectIds), SubjectProgress.DONE);
    }

    @Transactional
    @Override
    public void clearDegree(final User user) {
        userDao.clearSemester(user);
        userDao.updateUserDegree(user, null);
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

    @Transactional
    @Override
    public void updateUser(final Long userId, final User user, final String username, final String oldPassword, final String newPassword, final Long degreeId, final List<String> subjectIds) throws OldPasswordDoesNotMatchException{
        if( user.getId() != userId) {
            throw new ProfileNotOwnedException();
        }
        if (username != null ){
            editProfile(user, username);
        }
        if( oldPassword != null && newPassword != null) {
            changePassword(user, newPassword, oldPassword);
        }
        if(degreeId != null && subjectIds != null) {
            updateUserDegreeAndSubjectProgress(user, degreeService.findById(degreeId).orElseThrow(DegreeNotFoundException::new), subjectIds.toString());
        }
    }
}
