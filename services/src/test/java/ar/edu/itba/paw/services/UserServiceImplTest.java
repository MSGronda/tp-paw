package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exceptions.EmailAlreadyTakenException;
import ar.edu.itba.paw.models.exceptions.InvalidImageSizeException;
import ar.edu.itba.paw.models.exceptions.OldPasswordDoesNotMatchException;
import ar.edu.itba.paw.persistence.dao.ImageDao;
import ar.edu.itba.paw.persistence.dao.UserDao;
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
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/*
@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
    private static final long ID = 1;
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String PASSWORD_ENCRYPTED = "a;lskdfjas;lkdfjasdf";

    private static final String NEWPASSWORD = "newPassword";

    private static final String NEWPASSWORDENCRYPTED = "fdfkdjadkfa;sd";


    private static final String USERNAME = "username";

    private static final long IMAGEID = 3;

    private static final File IMAGE = new File("src/test/resources/large_image.jpg");


    @Mock
    private UserDao userDao;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RolesService rolesService;

    @Mock
    private ImageDao imageDao;

    @Mock
    private DegreeService degreeService;

    @SuppressWarnings("unused")
    @Mock
    private MailService mailService;

    @InjectMocks
    private UserServiceImpl us;

    @Test
    public void testCreate() throws EmailAlreadyTakenException {
        final User user = User.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .username(USERNAME)
                .build();

        final Image image = new Image("asdf".getBytes());
        final Degree degree = new Degree("degree");

        final User.Builder serviceUser = User.builderFrom(user)
                .degree(degree)
                .password(PASSWORD_ENCRYPTED)
                .imageId(IMAGEID);

        final User.Builder daoUser = User.builderFrom(serviceUser.build())
                .id(ID);

        when(degreeService.findById(eq(0L))).thenReturn(Optional.of(degree));
        when(userDao.create(any()))
                .thenReturn(daoUser.build());
        when(passwordEncoder.encode(eq(PASSWORD))).thenReturn(PASSWORD_ENCRYPTED);
        when(imageDao.create(eq(image.getImage()))).thenReturn(image);
        when(rolesService.findByName(eq("USER"))).thenReturn(Optional.of(new Role(ID, "USER")));

        //final User newUser = us.create(0, "", user, image.getImage());

//        Assert.assertNotNull(newUser);
//        assertEquals(EMAIL, newUser.getEmail());
//        assertEquals(PASSWORD_ENCRYPTED, newUser.getPassword());
//        assertEquals(USERNAME, newUser.getUsername());
    }

    @Test(expected = EmailAlreadyTakenException.class)
    public void testCreateAlreadyExists() throws EmailAlreadyTakenException {
        final User user = User.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .username(USERNAME)
                .build();

        final Image image = new Image("asdf".getBytes());
        final Degree degree = new Degree("degree");

        when(userDao.create(any()))
                .thenThrow(EmailAlreadyTakenException.class);
        when(passwordEncoder.encode(eq(PASSWORD))).thenReturn(PASSWORD_ENCRYPTED);
        when(imageDao.create(eq(image.getImage()))).thenReturn(image);
        when(degreeService.findById(eq(0L))).thenReturn(Optional.of(degree));


        // 2. Execute class under test
//        us.create(0,"", user, image.getImage());
    }

    @Test
    public void testFindById() {
        when(userDao.findById(eq(ID))).thenReturn(Optional.of(
                User.builder()
                        .id(ID)
                        .email(EMAIL)
                        .password(PASSWORD)
                        .username(USERNAME)
                        .build()
        ));

        Optional<User> user = us.findById(ID);

        assertTrue(user.isPresent());
        assertEquals(ID, user.get().getId());
        assertEquals(EMAIL, user.get().getEmail());
        assertEquals(PASSWORD, user.get().getPassword());
        assertEquals(USERNAME, user.get().getUsername());
    }

    @Test
    public void testChangePassword() throws OldPasswordDoesNotMatchException {
        final User user = User.builder()
                .email(EMAIL)
                .password(PASSWORD_ENCRYPTED)
                .username(USERNAME)
                .id(ID)
                .build();

        when(passwordEncoder.matches(eq(PASSWORD), eq(PASSWORD_ENCRYPTED))).thenReturn(true);
        when(passwordEncoder.encode(eq(NEWPASSWORD))).thenReturn(NEWPASSWORDENCRYPTED);

        us.changePassword(user, NEWPASSWORD, PASSWORD);

        //Funciona como esperado si no larga excepcion
    }

    @Test(expected = OldPasswordDoesNotMatchException.class)
    public void testChangePasswordOldPasswordDoesNotMatch() throws OldPasswordDoesNotMatchException {
        final User user = User.builder()
                .email(EMAIL)
                .password(PASSWORD_ENCRYPTED)
                .username(USERNAME)
                .id(ID)
                .build();

        us.changePassword(user, NEWPASSWORD, PASSWORD);

        //Funciona como esperado si larga excepcion
    }

    @Test(expected = InvalidImageSizeException.class)
    public void testUpdateProfilePictureNoImage() throws InvalidImageSizeException {
        User user = User.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .username(USERNAME)
                .id(ID)
                .build();

        us.updateProfilePicture(user, new byte[]{});
    }


    @Test(expected = InvalidImageSizeException.class)
    public void testUpdateProfilePictureLargeImage() throws InvalidImageSizeException, IOException {
        User user = User.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .username(USERNAME)
                .id(ID)
                .build();

        byte[] image = Files.readAllBytes(IMAGE.toPath());
        us.updateProfilePicture(user, image);
    }
}

*/
