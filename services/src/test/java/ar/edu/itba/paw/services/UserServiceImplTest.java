package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.UserDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.SQLException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
    private static final long ID = 1;
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String USERNAME = "username";

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl us;

    @Test
    public void testCreate() throws SQLException {
        //1. Precondiciones
        when(userDao.create(eq(EMAIL), eq(PASSWORD), eq(USERNAME)))
            .thenReturn(new User(0, EMAIL, PASSWORD, USERNAME));

        // 2. Execute class under test
        User newUser = us.create(EMAIL, PASSWORD, USERNAME);

        // 3. Meaningful assertions
        Assert.assertNotNull(newUser);
        Assert.assertEquals(EMAIL, newUser.getEmail());
        Assert.assertEquals(PASSWORD, newUser.getPassword());
        Assert.assertEquals(USERNAME, newUser.getUsername());
    }

    @Test(expected = SQLException.class)
    public void testCreateAlreadyExists() throws SQLException {
        //1. Precondiciones
        when(userDao.create(eq(EMAIL), eq(PASSWORD), eq(USERNAME)))
            .thenThrow(SQLException.class);

        // 2. Execute class under test
        User newUser = us.create(EMAIL, PASSWORD, USERNAME);
    }

    @Test
    public void testFindById() {
        when(userDao.findById(eq(ID))).thenReturn(Optional.of(new User(ID, EMAIL, PASSWORD, USERNAME)));

        Optional<User> user = us.findById(ID);

        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(ID, user.get().getId());
    }


}
