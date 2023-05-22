package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.dao.ImageDao;
import ar.edu.itba.paw.persistence.dao.UserDao;
import ar.edu.itba.paw.persistence.exceptions.UserEmailAlreadyTakenPersistenceException;
import ar.edu.itba.paw.services.exceptions.InvalidImageSizeException;
import ar.edu.itba.paw.services.exceptions.OldPasswordDoesNotMatchException;
import ar.edu.itba.paw.services.exceptions.UserEmailAlreadyTakenException;
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

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

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

    @InjectMocks
    private UserServiceImpl us;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RolesService rolesService;

    @Mock
    private ImageDao imageDao;

    @Test
    public void testCreate() throws UserEmailAlreadyTakenPersistenceException, UserEmailAlreadyTakenException, IOException {
        //1. Precondiciones
        User user = User.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .username(USERNAME)
                .build();

        User serviceUser = User.builderFrom(user)
                .password(PASSWORD_ENCRYPTED)
                .imageId(IMAGEID)
                .build();

        User daoUser = User.builderFrom(serviceUser)
                .id(ID)
                .build();

        when(userDao.create(eq(serviceUser)))
                .thenReturn(daoUser);
        when(passwordEncoder.encode(eq(PASSWORD))).thenReturn(PASSWORD_ENCRYPTED);
        when(imageDao.insertAndReturnKey(eq("asdf".getBytes()))).thenReturn(IMAGEID);
        when(rolesService.findByName(eq("USER"))).thenReturn(Optional.of(new Role(ID, "USER")));

        // 2. Execute class under test
        User newUser = us.create(user, "asdf".getBytes());

        // 3. Meaningful assertions
        Assert.assertNotNull(newUser);
        Assert.assertEquals(EMAIL, newUser.getEmail());
        Assert.assertEquals(PASSWORD_ENCRYPTED, newUser.getPassword());
        Assert.assertEquals(USERNAME, newUser.getUsername());
    }

    @Test(expected = UserEmailAlreadyTakenException.class)
    public void testCreateAlreadyExists() throws UserEmailAlreadyTakenPersistenceException, UserEmailAlreadyTakenException, IOException {
        //1. Precondiciones
        User user = User.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .username(USERNAME)
                .build();

        User serviceUser = User.builderFrom(user)
                .password(PASSWORD_ENCRYPTED)
                .imageId(IMAGEID)
                .build();

        when(userDao.create(eq(serviceUser)))
                .thenThrow(UserEmailAlreadyTakenPersistenceException.class);
        when(imageDao.insertAndReturnKey(eq("asdf".getBytes()))).thenReturn(IMAGEID);
        when(passwordEncoder.encode(eq(PASSWORD))).thenReturn(PASSWORD_ENCRYPTED);

        // 2. Execute class under test
        us.create(user, "asdf".getBytes());
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

        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(ID, user.get().getId());
    }

    @Test
    public void testChangePassword() throws OldPasswordDoesNotMatchException {
        when(passwordEncoder.matches(eq(PASSWORD), eq(PASSWORD_ENCRYPTED))).thenReturn(true);
        when(passwordEncoder.encode(eq(NEWPASSWORD))).thenReturn(NEWPASSWORDENCRYPTED);

        us.changePassword(ID, NEWPASSWORD, PASSWORD, PASSWORD_ENCRYPTED);

        //Funciona como esperado si no larga excepcion
    }

    @Test(expected = OldPasswordDoesNotMatchException.class)
    public void testChangePasswordOldPasswordDoesNotMatch() throws OldPasswordDoesNotMatchException {
        when(passwordEncoder.matches(eq(PASSWORD), eq(PASSWORD_ENCRYPTED))).thenReturn(false);

        us.changePassword(ID, NEWPASSWORD, PASSWORD, PASSWORD_ENCRYPTED);

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
