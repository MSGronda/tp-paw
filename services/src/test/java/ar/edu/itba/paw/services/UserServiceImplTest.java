package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exceptions.*;
import ar.edu.itba.paw.persistence.dao.ImageDao;
import ar.edu.itba.paw.persistence.dao.UserDao;
import ar.edu.itba.paw.services.enums.UserSemesterEditType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private static final String encoded_password = "$2a$12$rlg1jQUSQXWYGoXoMP8cXOFSpSwxdwNTcdXyg984yhvn7z6liaFFK";
    private final User testUser = User.builder().id(1).email("invalid@mail.com").password("test_password1").username("testUser").locale(Locale.ENGLISH).build();

    @Mock
    private ImageDao imageDao;
    @Mock
    private UserDao userDao;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RolesService rolesService;
    @SuppressWarnings("unused")
    @Mock
    private MailService mailService;
    @Mock
    private SubjectService subjectService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testCreate() throws IOException {
        final byte[] defaultImageBytes = Files.readAllBytes(new File("src\\test\\resources\\default_user.png").toPath());
        final Image defaultImage = new Image(defaultImageBytes);
        defaultImage.setId(1L);
        final Role role = new Role(Role.RoleEnum.USER.getId(), Role.RoleEnum.USER.getName());
        when(imageDao.create(defaultImageBytes)).thenReturn(defaultImage);
        when(rolesService.findByName("USER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(testUser.getPassword())).thenReturn(encoded_password);
        final String confirmationToken = "asodjkfaosk34";
        final User completedUser = User.builderFrom(testUser).password(passwordEncoder.encode(testUser.getPassword())).verificationToken(confirmationToken).imageId(defaultImage.getId()).build();
        completedUser.getRoles().add(role);
        when(userDao.create(any())).thenReturn(completedUser);


        final User createdUser = userService.create(testUser, defaultImageBytes);

        assertEquals(createdUser, testUser);
        assertEquals(createdUser.getRoles(), new HashSet<>(Collections.singleton(role)));
    }

    @Test
    public void testChangePassword() throws OldPasswordDoesNotMatchException {
        final String newPassword = "new_password1";
        final String encodedNewPassword = "$2a$12$2cNTr5J1Zousbl9LJk8rueLpWa9Uayaknlc0L21YN1ttWbPdvo4si";
        when(passwordEncoder.matches("test_password1", testUser.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedNewPassword);

        userService.changePassword(testUser, newPassword, testUser.getPassword());
    }

    @Test(expected = OldPasswordDoesNotMatchException.class)
    public void testChangePasswordOldPasswordDoesNotMatch() throws OldPasswordDoesNotMatchException {
        final String wrongPassword = "wrong_password";
        when(passwordEncoder.matches(wrongPassword, testUser.getPassword())).thenReturn(false);

        userService.changePassword(testUser, "new_password1", wrongPassword);
    }


    @Test(expected = InvalidImageSizeException.class)
    public void testUpdateProfilePictureLargeImage() throws InvalidImageSizeException, IOException {
        byte[] bytes = Files.readAllBytes(new File("src\\test\\resources\\large_image.jpg").toPath());

        imageDao.create(bytes);
    }

    @Test
    public void testAddSubjectToUserSemester() {
        final String classId = "A";
        final Subject testSubject = Subject.builder().id("31.08").name("Sistemas de Representación").department("Ciencias Exactas y Naturales").credits(3).build();
        testSubject.getClasses().add(new SubjectClass(classId, testSubject));

        when(subjectService.findById(testSubject.getId())).thenReturn(Optional.of(testSubject));

        userService.addToCurrentSemester(testUser,  testSubject.getId(), classId);
    }

    @Test(expected = UserSemesterAlreadyContainsSubjectException.class)
    public void testAddSubjectAlreadyPresentSubject() {
        final String classId = "A";
        final Subject testSubject = Subject.builder().id("31.08").name("Sistemas de Representación").department("Ciencias Exactas y Naturales").credits(3).build();
        final SubjectClass subjectClass = new SubjectClass(classId, testSubject);
        final UserSemesterSubject userSemester = new UserSemesterSubject(testUser, subjectClass);
        testSubject.getClasses().add(subjectClass);
        testUser.getUserSemester().add(userSemester);

        when(subjectService.findById(testSubject.getId())).thenReturn(Optional.of(testSubject));

        userService.addToCurrentSemester(testUser,  testSubject.getId(), classId);
    }

    @Test
    public void testRemoveSubjectFromUserSemester() {
        final String classId = "A";
        final Subject testSubject = Subject.builder().id("31.08").name("Sistemas de Representación").department("Ciencias Exactas y Naturales").credits(3).build();
        final SubjectClass subjectClass = new SubjectClass(classId, testSubject);
        final UserSemesterSubject userSemester = new UserSemesterSubject(testUser, subjectClass);
        testSubject.getClasses().add(subjectClass);
        testUser.getUserSemester().add(userSemester);
        when(subjectService.findById(testSubject.getId())).thenReturn(Optional.of(testSubject));

        userService.removeFromCurrentSemester(testUser,  testSubject.getId(), classId);
    }

    @Test(expected = InvalidUserSemesterIds.class)
    public void testInvalidEditUserSemester() {
        final List<String> subjectIds = new ArrayList<>(Collections.singletonList("11.15"));
        final List<String> classIds = new ArrayList<>();

        userService.editUserSemester(testUser, testUser.getId(), UserSemesterEditType.ADD_SUBJECT, subjectIds, classIds);

        Assert.fail("InvalidUserSemesterIds must be thrown");
    }

}
