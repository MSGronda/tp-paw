package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.ImageDao;
import ar.edu.itba.paw.persistence.UserDao;
import ar.edu.itba.paw.persistence.exceptions.UserEmailAlreadyTakenPersistenceException;
import ar.edu.itba.paw.services.exceptions.UserEmailAlreadyTakenException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.access.method.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
    private static final long ID = 1;
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String PASSWORD_ENCRYPTED = "a;lskdfjas;lkdfjasdf";

    private static final String USERNAME = "username";

    private static final long IMAGEID = 3;

    @Mock
    private UserDao userDao;

    @Mock
    private User.UserBuilder userBuilder;

    @InjectMocks
    private UserServiceImpl us;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ImageDao imageDao;

    @Test
    public void testCreate() throws UserEmailAlreadyTakenPersistenceException, UserEmailAlreadyTakenException, IOException {
        //1. Precondiciones
        User.UserBuilder userBuilder = new User.UserBuilder(EMAIL, PASSWORD, USERNAME);
        when(userDao.create(eq(userBuilder)))
            .thenReturn(new User.UserBuilder(EMAIL, PASSWORD, USERNAME).build());
        when(imageDao.insertAndReturnKey(eq("asdf".getBytes()))).thenReturn(IMAGEID);

        // 2. Execute class under test
        User newUser = us.create(userBuilder, "asdf".getBytes());

        // 3. Meaningful assertions
        Assert.assertNotNull(newUser);
        Assert.assertEquals(EMAIL, newUser.getEmail());
        Assert.assertEquals(PASSWORD, newUser.getPassword());
        Assert.assertEquals(USERNAME, newUser.getUsername());
    }
    @Test(expected = UserEmailAlreadyTakenException.class)
    public void testCreateAlreadyExists() throws UserEmailAlreadyTakenPersistenceException, UserEmailAlreadyTakenException, IOException {
        //1. Precondiciones
        User.UserBuilder userBuilder = new User.UserBuilder(EMAIL, PASSWORD, USERNAME);
        when(userDao.create(eq(userBuilder)))
                .thenThrow(UserEmailAlreadyTakenPersistenceException.class);
        when(imageDao.insertAndReturnKey(eq("asdf".getBytes()))).thenReturn(IMAGEID);
        when(passwordEncoder.encode(eq(PASSWORD))).thenReturn(PASSWORD_ENCRYPTED);

        // 2. Execute class under test
        us.create(userBuilder, "asdf".getBytes());
    }
    @Test
    public void testFindById() {
        when(userDao.findById(eq(ID))).thenReturn(Optional.of(new User.UserBuilder(EMAIL, PASSWORD, USERNAME).id(ID).build()));

        Optional<User> user = us.findById(ID);

        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(ID, user.get().getId());
    }


}
