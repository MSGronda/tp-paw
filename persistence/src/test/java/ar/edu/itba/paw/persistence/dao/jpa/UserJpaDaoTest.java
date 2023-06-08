package ar.edu.itba.paw.persistence.dao.jpa;

import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.SubjectClass;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.SubjectProgress;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.exceptions.EmailAlreadyTakenException;
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
import java.util.Map;

import static org.junit.Assert.*;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class UserJpaDaoTest {
    private final static String USERNAME = "username";
    private final static String PASSWORD = "password";
    private final static String EMAIL = "e@mail.com";
    private final static String CONFIRM_TOKEN = "token";

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
                        .confirmToken(CONFIRM_TOKEN)
                        .build()
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
                        .build()
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
                        .confirmToken(CONFIRM_TOKEN2)
                        .build()
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
                        .confirmToken(CONFIRM_TOKEN)
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
                .confirmToken(CONFIRM_TOKEN)
                .build();

        em.persist(user);

        assertEquals(user, userJpaDao.findUnconfirmedByEmail(EMAIL).get());
    }

    @Test
    public void findByConfirmToken() {
        final User user = User.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .confirmToken(CONFIRM_TOKEN)
                .build();

        em.persist(user);

        assertEquals(
                em.createQuery("from User where confirmToken = :confirmToken", User.class)
                        .setParameter("confirmToken", CONFIRM_TOKEN)
                        .getSingleResult(),
                userJpaDao.findByConfirmToken(CONFIRM_TOKEN).get()
        );
    }

    @Test
    public void deleteSubjectProgress() {
        final Subject subject = Subject.builder()
                .id(SUBJECT_ID)
                .name(SUBJECT_NAME)
                .credits(SUBJECT_CREDITS)
                .department(SUBJECT_DEPARTMENT)
                .build();

        em.persist(subject);

        final User user = User.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .build();

        Map<String, SubjectProgress> subjectProgress = user.getSubjectProgress();
        subjectProgress.put(subject.getId(), SubjectProgress.DONE);
        user.setSubjectProgress(subjectProgress);

        em.persist(user);

        userJpaDao.deleteSubjectProgress(user, subject);

        assertFalse(user.getSubjectProgress().containsKey(subject.getId()));
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
        assertEquals(SubjectProgress.DONE, user.getSubjectProgress().get(subject.getId()));

        userJpaDao.updateSubjectProgress(user, subject, SubjectProgress.PENDING);
        assertEquals(SubjectProgress.PENDING, user.getSubjectProgress().get(subject.getId()));
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
                .confirmToken(CONFIRM_TOKEN)
                .build();

        em.persist(user);

        userJpaDao.confirmUser(user);
        assertTrue(user.isConfirmed());
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
        assertEquals(Locale.ENGLISH, user.getLocale().get());
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
    public void updateRoles() {
        final Role.RoleEnum userRoleEnum = Role.RoleEnum.USER;
        final Role userRole = new Role(userRoleEnum.getId(), userRoleEnum.getName());
        em.persist(userRole);

        final Role.RoleEnum editorRoleEnum = Role.RoleEnum.EDITOR;
        final Role editorRole = new Role(editorRoleEnum.getId(), editorRoleEnum.getName());
        em.persist(editorRole);

        final User user = User.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .build();

        user.getRoles().add(userRole);

        em.persist(user);

        userJpaDao.updateRoles(user, editorRole);

        assertTrue(user.getRoles().contains(editorRole));
        assertFalse(user.getRoles().contains(userRole));
    }

    @Test
    public void updateConfirmToken() {
        final User user = User.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .confirmToken(CONFIRM_TOKEN)
                .build();

        em.persist(user);

        userJpaDao.updateConfirmToken(user, CONFIRM_TOKEN2);
        assertEquals(CONFIRM_TOKEN2, user.getConfirmToken().get());
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
