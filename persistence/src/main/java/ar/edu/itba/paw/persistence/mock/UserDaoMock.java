package ar.edu.itba.paw.persistence.mock;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class UserDaoMock implements UserDao {
    private static final HashMap<String,User> users = new HashMap<String,User>(){{
        put("a@a.a", new User("a@a.a", "1234", "Juan"));
        put("b@b.b", new User("b@b.b", "1234", "Pedro"));
        put("c@c.c", new User("c@c.c", "1234", "Maria"));
    }};

    @Override
    public User get(String id) {
        return users.get(id);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void insert(User user) {
        users.put(user.getEmail(), user);
    }

    @Override
    public void delete(String id) {
        users.remove(id);
    }

    @Override
    public void update(User user) {
        users.put(user.getEmail(), user);
    }
}
