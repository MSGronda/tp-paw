package ar.edu.itba.paw.persistence.dao.jpa;

import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.dao.UserDao;
import ar.edu.itba.paw.persistence.exceptions.UserEmailAlreadyTakenPersistenceException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;

@Repository
public class UserJpaDao implements UserDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public User create(User user) throws UserEmailAlreadyTakenPersistenceException {
        if (!user.getConfirmToken().isPresent())
            throw new IllegalArgumentException("Confirm token must be present");

        em.persist(user);
        return user;
    }

    @Override
    public List<User> getAll() {
        return em.createQuery("from User u where u.confirmed = true", User.class).getResultList();
    }

    @Override
    public Optional<User> findById(Long id) {
        TypedQuery<User> query = em.createQuery("from User u where u.id = :id and u.confirmed = true", User.class);
        query.setParameter("id", id);
        final List<User> list = query.getResultList();
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return em.createQuery("from User u where u.email = :email and u.confirmed = true", User.class)
                .setParameter("email", email)
                .getResultList()
                .stream().findFirst();
    }

    @Override
    public Optional<User> findUnconfirmedByEmail(String email) {
        return em.createQuery("from User u where u.email = :email and u.confirmed = false", User.class)
                .setParameter("email", email)
                .getResultList()
                .stream().findFirst();
    }

    @Override
    public void deleteSubjectProgress(final User user, final String idSub) {
        final Map<String, Integer> sp = user.getSubjectProgress();
        sp.remove(idSub);
    }

    @Override
    public void updateSubjectProgress(final User user, final String idSub, final Integer newProgress) {
        final Map<String, Integer> sp = user.getSubjectProgress();
        sp.put(idSub, newProgress);
    }

    @Override
    public void changePassword(final User user, final String password) {
        user.setPassword(password);
    }

    @Override
    public void changeUsername(final User user, final String username) {
        user.setUsername(username);
    }

    @Override
    public void addRole(final User user, final Role role) {
        List<Role> roles = user.getRoles();
        if (roles == null) roles = new ArrayList<>();
        roles.add(role);
    }

    @Override
    public void updateRoles(final User user, final Role role) {
        final List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);
    }

    @Override
    public Optional<User> findByConfirmToken(String token) {
        return em.createQuery("from User u where u.confirmToken = :token", User.class)
                .setParameter("token", token)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Override
    public void confirmUser(final User user) {
        user.setConfirmed(true);
    }

    @Override
    public void setLocale(final User user, final Locale locale) {
        user.setLocale(locale);
    }

    @Override
    public void updateConfirmToken(final User user, final String token) {
        user.setConfirmToken(token);
    }

    @Override
    public void insert(User user) {

    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public void update(User user) {

    }
}