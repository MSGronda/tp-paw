package ar.edu.itba.paw.persistence.dao.hibernate;

import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.dao.UserDao;
import ar.edu.itba.paw.persistence.exceptions.UserEmailAlreadyTakenPersistenceException;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;

@Transactional(propagation = Propagation.REQUIRED)
@Repository
public class UserHibernateDao implements UserDao {
    @PersistenceContext
    private EntityManager em;


    @Override
    public Optional<User> findById(Long id) {
        TypedQuery<User> query = em.createQuery("from User u where u.id = :id and u.confirmed = true", User.class);
        query.setParameter("id", id);
        final List<User> list = query.getResultList();
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public List<User> getAll() {
        return em.createQuery("from User u where u.confirmed = true", User.class).getResultList();
    }

    @Override
    public User create(User user) throws UserEmailAlreadyTakenPersistenceException {
        if (!user.getConfirmToken().isPresent())
            throw new IllegalArgumentException("Confirm token must be present");

        em.persist(user);
        return user;
    }

    @Override
    public Optional<User> getUserWithEmail(String email) {
        return em.createQuery("from User u where u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList()
                .stream().findFirst();
    }

    @Override
    public Optional<User> getUnconfirmedUserWithEmail(String email) {
        final TypedQuery<User> query = em.createQuery("from User u where u.email = :email and u.confirmed = false", User.class);
        query.setParameter("email", email);
        final List<User> list = query.getResultList();
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public Integer deleteUserProgressForSubject(Long id, String idSub) {
        final User u = em.find(User.class, id);
        final Map<String,Integer> sp = u.getSubjectProgress();
        sp.remove(idSub);

        em.merge(u);
        return 1;
    }

    @Override
    public Integer updateSubjectProgress(Long id, String idSub, Integer newProgress) {
        final User u = em.find(User.class, id);
        final Map<String,Integer> sp = u.getSubjectProgress();
        sp.put(idSub, newProgress);

        em.merge(u);

        return 1;
    }

    @Override
    public Optional<Integer> getUserSubjectProgress(Long id, String idSub) {
        final User u = em.find(User.class, id);
        if(u == null) return Optional.empty();
        return Optional.ofNullable(u.getSubjectProgress().get(idSub));
    }

    @Override
    public Map<String, Integer> getUserAllSubjectProgress(Long id) {
        final User u = em.find(User.class, id);
        if(u == null) return Collections.emptyMap();

        final Map<String,Integer> p = u.getSubjectProgress();
        Hibernate.initialize(p);
        return p;
    }

    @Override
    public void changePassword(Long userId, String password) {
        final User u = em.find(User.class, userId);
        u.setPassword(password);
        em.merge(u);
    }

    @Override
    public void editProfile(Long userId, String username) {
        final User u = em.find(User.class, userId);
        u.setUsername(username);
        em.merge(u);
    }

    @Override
    public List<Role> getUserRoles(Long userId) {
        final User u = em.find(User.class, userId);
        final List<Role> roles = u.getRoles();
        Hibernate.initialize(roles);
        return roles;
    }

    @Override
    public Integer addIdToUserRoles(Long roleId, Long userId) {
        final User u = em.find(User.class, userId);
        final Role r = em.find(Role.class, roleId);

        List<Role> roles = u.getRoles();
        if(roles == null) roles = new ArrayList<>();
        roles.add(r);

        em.merge(u);
        return 1;
    }

    @Override
    public Integer updateUserRoles(Long roleId, Long userId) {
        final User u = em.find(User.class, userId);
        final Role r = em.find(Role.class, roleId);
        final List<Role> roles = new ArrayList<>();
        roles.add(r);
        u.setRoles(roles);

        em.merge(u);

        return 1;
    }

    @Override
    public Optional<User> findUserByConfirmToken(String token) {
        return em.createQuery("from User u where u.confirmToken = :token", User.class)
                .setParameter("token", token)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Override
    public void confirmUser(long userId) {
        final User u = em.find(User.class, userId);
        u.setConfirmed(true);
        em.merge(u);
    }

    @Override
    public void setLocale(long userId, Locale locale) {
        final User u = em.find(User.class, userId);
        u.setLocale(locale);
        em.merge(u);
    }

    @Override
    public void updateConfirmToken(long userId, String token) {
        final User u = em.find(User.class, userId);
        u.setConfirmToken(token);
        em.merge(u);
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
