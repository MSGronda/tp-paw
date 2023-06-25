package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.SubjectClass;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.SubjectProgress;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.models.exceptions.EmailAlreadyTakenException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Locale;

import static org.junit.Assert.*;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class UserJpaDaoTest {
    private final static String USERNAME = "username";
    private final static String PASSWORD = "password";
    private final static String EMAIL = "e@mail.com";
    private final static String VERIFICATION_TOKEN = "token";

    private final static String USERNAME2 = "username2";
    private final static String PASSWORD2 = "password2";
    private final static String EMAIL2 = "e2@mail.com";
    private final static String CONFIRM_TOKEN2 = "token2";

    private final static String SUBJECT_ID = "10.10";
    private final static String SUBJECT_NAME = "subject";
    private final static String SUBJECT_DEPARTMENT = "department";
    private final static int SUBJECT_CREDITS = 3;

    private final static String SUBJECT_CLASS_ID = "A";


    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserJpaDao userJpaDao;

    @Before
    public void clear() {
        em.createQuery("DELETE FROM User").executeUpdate();
        em.createQuery("DELETE FROM Subject").executeUpdate();
        em.createQuery("DELETE FROM SubjectClass").executeUpdate();
        em.createQuery("DELETE FROM Role").executeUpdate();
    }

    @Test
    public void create() throws EmailAlreadyTakenException {
        final User user = userJpaDao.create(
                User.builder()
                        .email(EMAIL)
                        .username(USERNAME)
                        .password(PASSWORD)
                        .verificationToken(VERIFICATION_TOKEN)
        );

        assertNotNull(user);
        assertEquals(user, em.find(User.class, user.getId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNoConfirmToken() throws EmailAlreadyTakenException {
        userJpaDao.create(
                User.builder()
                        .email(EMAIL)
                        .username(USERNAME)
                        .password(PASSWORD)
        );
    }


    @Test(expected = EmailAlreadyTakenException.class)
    public void createExistingEmail() throws EmailAlreadyTakenException {
        em.persist(
                User.builder()
                        .email(EMAIL)
                        .username(USERNAME)
                        .password(PASSWORD)
                        .confirmed(true)
                        .build()
        );

        userJpaDao.create(
                User.builder()
                        .email(EMAIL)
                        .username(USERNAME2)
                        .password(PASSWORD2)
                        .verificationToken(CONFIRM_TOKEN2)
        );
    }

    @Test
    public void findByEmail() {
        final User user = User.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .confirmed(true)
                .build();

        em.persist(user);

        assertEquals(user, userJpaDao.findByEmail(EMAIL).get());
    }

    @Test
    public void findByEmailUserNotConfirmed() {
        em.persist(
                User.builder()
                        .email(EMAIL)
                        .username(USERNAME)
                        .password(PASSWORD)
                        .verificationToken(VERIFICATION_TOKEN)
                        .build()
        );

        assertFalse(userJpaDao.findByEmail(EMAIL).isPresent());
    }

    @Test
    public void findByEmailUnconfirmed() {
        final User user = User.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .verificationToken(VERIFICATION_TOKEN)
                .build();

        em.persist(user);

        assertEquals(user, userJpaDao.findUnverifiedByEmail(EMAIL).get());
    }

    @Test
    public void findByConfirmToken() {
        final User user = User.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .verificationToken(VERIFICATION_TOKEN)
                .build();

        em.persist(user);

        assertEquals(
                em.createQuery("from User where verificationToken = :verificationToken", User.class)
                        .setParameter("verificationToken", VERIFICATION_TOKEN)
                        .getSingleResult(),
                userJpaDao.findByConfirmToken(VERIFICATION_TOKEN).get()
        );
    }

    @Test
    public void updateSubjectProgress() {
        final Subject subject = Subject.builder()
                .id(SUBJECT_ID)
                .name(SUBJECT_NAME)
                .credits(SUBJECT_CREDITS)
                .department(SUBJECT_DEPARTMENT)
                .build();

        final User user = User.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .build();

        em.persist(subject);
        em.persist(user);

        userJpaDao.updateSubjectProgress(user, subject, SubjectProgress.DONE);
        assertEquals(SubjectProgress.DONE, user.getAllSubjectProgress().get(subject.getId()));

        userJpaDao.updateSubjectProgress(user, subject, SubjectProgress.PENDING);
        assertEquals(SubjectProgress.PENDING, user.getSubjectProgress(subject));
    }

    @Test
    public void changePassword() {
        final User user = User.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .confirmed(true)
                .build();

        em.persist(user);

        userJpaDao.changePassword(user, PASSWORD2);
        assertEquals(PASSWORD2, user.getPassword());
    }

    @Test
    public void changeUsername() {
        final User user = User.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .confirmed(true)
                .build();

        em.persist(user);

        userJpaDao.changeUsername(user, USERNAME2);
        assertEquals(USERNAME2, user.getUsername());
    }

    @Test
    public void confirmUser() {
        final User user = User.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .verificationToken(VERIFICATION_TOKEN)
                .build();

        em.persist(user);

        userJpaDao.confirmUser(user);
        assertTrue(user.isVerified());
    }

    @Test
    public void setLocale() {
        final User user = User.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .build();

        em.persist(user);

        userJpaDao.setLocale(user, Locale.ENGLISH);
        assertEquals(Locale.ENGLISH, user.getLocale());
    }

    @Test
    public void addRole() {
        final Role.RoleEnum userRoleEnum = Role.RoleEnum.USER;
        final Role userRole = new Role(userRoleEnum.getId(), userRoleEnum.getName());
        em.persist(userRole);

        final User user = User.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .build();

        em.persist(user);

        userJpaDao.addRole(user, userRole);
        assertTrue(user.getRoles().contains(userRole));
    }

    @Test
    public void updateConfirmToken() {
        final User user = User.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .verificationToken(VERIFICATION_TOKEN)
                .build();

        em.persist(user);

        userJpaDao.updateVerificationToken(user, CONFIRM_TOKEN2);
        assertEquals(CONFIRM_TOKEN2, user.getVerificationToken().get());
    }

    @Test
    public void addToCurrentSemester() {
        final Subject subject = Subject.builder()
                .id(SUBJECT_ID)
                .name(SUBJECT_NAME)
                .credits(SUBJECT_CREDITS)
                .department(SUBJECT_DEPARTMENT)
                .build();

        final SubjectClass subClass = new SubjectClass(SUBJECT_CLASS_ID, subject);

        subject.getClasses().add(subClass);

        em.persist(subject);

        final User user = User.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .build();

        em.persist(user);

        userJpaDao.addToCurrentSemester(user, subClass);
        assertTrue(user.getUserSemester().contains(subClass));
    }

    @Test
    public void removeFromCurrentSemester() {
        final Subject subject = Subject.builder()
                .id(SUBJECT_ID)
                .name(SUBJECT_NAME)
                .credits(SUBJECT_CREDITS)
                .department(SUBJECT_DEPARTMENT)
                .build();

        final SubjectClass subClass = new SubjectClass(SUBJECT_CLASS_ID, subject);

        subject.getClasses().add(subClass);

        em.persist(subject);

        final User user = User.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .confirmed(true)
                .build();

        user.getUserSemester().add(subClass);

        em.persist(user);

        userJpaDao.removeFromCurrentSemester(user, subClass);
        assertFalse(user.getUserSemester().contains(subClass));
    }
}
