package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.SubjectProgress;
import ar.edu.itba.paw.models.exceptions.EmailAlreadyTakenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.util.*;

@Repository
public class UserJpaDao implements UserDao {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserJpaDao.class);
    @PersistenceContext
    private EntityManager em;

    @Override
    public User create(final User.Builder userBuilder) throws EmailAlreadyTakenException {
        final User user = userBuilder.build();

        if (!user.getVerificationToken().isPresent()){
            LOGGER.warn("Attempted to create user without verification token for user: {}!", user.getUsername());   // Username es un atributo publico
            throw new IllegalArgumentException("Confirm token must be present");
        }
        if(findByEmail(user.getEmail()).isPresent() || findUnverifiedByEmail(user.getEmail()).isPresent()){
            LOGGER.warn("Attempted to create user with existing mail for user: {}!", user.getUsername());   // Username es un atributo publico
            throw new EmailAlreadyTakenException();
        }

        em.persist(user);
        LOGGER.info("Created user with id: {}", user.getId());
        return user;
    }

    @Override
    public List<User> getAll() {
        return em.createQuery("from User u where u.verified = true", User.class).getResultList();
    }

    @Override
    public Optional<User> findById(final long id) {
        TypedQuery<User> query = em.createQuery("from User u where u.id = :id and u.verified = true", User.class);
        query.setParameter("id", id);
        final List<User> list = query.getResultList();
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }
    @Override
    public Optional<User> findByEmail(final String email) {
        return em.createQuery("from User u where u.email = :email and u.verified = true", User.class)
                .setParameter("email", email)
                .getResultList()
                .stream().findFirst();
    }

    @Override
    public Optional<User> findUnverifiedByEmail(final String email) {
        return em.createQuery("from User u where u.email = :email and u.verified = false", User.class)
                .setParameter("email", email)
                .getResultList()
                .stream().findFirst();
    }

    @Override
    public void updateSubjectProgress(final User user, final Subject subject, final SubjectProgress progress) {
        final Map<String, SubjectProgress> sp = user.getAllSubjectProgress();
        if(progress == SubjectProgress.DONE) {
            sp.put(subject.getId(), progress);
        } else {
            sp.remove(subject.getId());
        }
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
    public void changeImage(final User user, final Image image) {
        user.setImageId(image.getId());
    }

    @Override
    public void addRole(final User user, final Role role) {
        final Set<Role> roles = user.getRoles();
        roles.add(role);
    }

    @Override
    public Optional<User> findByConfirmToken(final String token) {
        return em.createQuery("from User u where u.verificationToken = :token", User.class)
                .setParameter("token", token)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Override
    public void confirmUser(final User user) {
        user.setVerified(true);
    }

    @Override
    public void setLocale(final User user, final String locale) {
        user.setLocale(locale);
        em.merge(user);
    }

    @Override
    public void updateVerificationToken(final User user, final String token) {
        user.setVerificationToken(token);
    }

    @Override
    public void updateSubjectProgressList(final User user, final List<String> subjectIdList, final SubjectProgress progress){
        Map<String, SubjectProgress> userSubjectProgress = user.getAllSubjectProgress();
        if( userSubjectProgress == null)
            userSubjectProgress = new HashMap<>();
        for( String subjectId : subjectIdList){
            if (progress == SubjectProgress.DONE) {
                userSubjectProgress.put(subjectId, progress);
            } else {
                userSubjectProgress.remove(subjectId);
            }
        }
        user.setAllSubjectProgress(userSubjectProgress);
    }

    @Override
    public void addToCurrentSemester(final User user, final SubjectClass subjectClass){
        final List<UserSemesterSubject> semester =  user.getUserSemester();

        semester.add(new UserSemesterSubject(user, subjectClass));
    }

    @Override
    public void removeFromCurrentSemester(final User user, final SubjectClass subjectClass){
        final List<UserSemesterSubject> semester =  user.getUserSemester();

        semester.removeIf(s -> s.isActive() && s.getSubjectClass().equals(subjectClass));
    }

    @Override
    public void clearCurrentSemester(final User user){
        final List<UserSemesterSubject> semester =  user.getUserSemester();

        semester.removeIf(UserSemesterSubject::isActive);
    }

    @Override
    public void finishSemester(final User user){
        final List<UserSemesterSubject> semester =  user.getUserSemester();

        final Timestamp now = new Timestamp(System.currentTimeMillis());

        semester.forEach(s -> {
            if(s.isActive()) {
                s.setDateFinished(now);
            }
        });
    }
}
