package ar.edu.itba.paw.persistence.dao.jpa;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.SubjectProgress;
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
        if( findByEmail(user.getEmail()).isPresent() )
            throw new UserEmailAlreadyTakenPersistenceException();
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
    public void deleteSubjectProgress(final User user, final Subject subject) {
        final Map<String, SubjectProgress> sp = user.getSubjectProgress();
        sp.remove(subject.getId());
    }

    @Override
    public void updateSubjectProgress(final User user, final Subject subject, final SubjectProgress progress) {
        final Map<String, SubjectProgress> sp = user.getSubjectProgress();
        sp.put(subject.getId(), progress);
    }

    @Override
    public void setAllSubjectProgress(final User user, final Map<String, SubjectProgress> progressMap) {
        final Map<String, SubjectProgress> sp = user.getSubjectProgress();
        sp.clear();
        sp.putAll(progressMap);
    }

    @Override
    public void updateUserDegree(final User user, final Degree degree){
        user.setDegree(degree);
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
        final Set<Role> roles = user.getRoles();
        roles.add(role);
    }

    @Override
    public void updateRoles(final User user, final Role role) {
        final Set<Role> roles = user.getRoles();

        roles.clear();
        roles.add(role);
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
        //do nothing
    }

    @Override
    public void delete(Long aLong) {
        //do nothing
    }

    @Override
    public void update(User user) {
        //do nothing
    }

    @Override
    public void updateSubjectProgressList(final User user, final List<String> subjectIdList){
        Map<String, SubjectProgress> userSubjectProgress = user.getSubjectProgress();
        if( userSubjectProgress == null)
            userSubjectProgress = new HashMap<>();
        for( String subjectId : subjectIdList){
            userSubjectProgress.put(subjectId, SubjectProgress.DONE);
        }
        user.setSubjectProgress(userSubjectProgress);
    }

    @Override
    public void addToCurrentSemester(final User user, final SubjectClass subjectClass){
        final Set<SubjectClass> semester =  user.getUserSemester();

        semester.add(subjectClass);
    }

    @Override
    public void removeFromCurrentSemester(final User user, final SubjectClass subjectClass){
        final Set<SubjectClass> semester =  user.getUserSemester();

        semester.remove(subjectClass);
    }
}
