package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {
    @Override
    public User create(String email, String password, String nombre) {
        return new User(email, password, nombre);
    }
}
