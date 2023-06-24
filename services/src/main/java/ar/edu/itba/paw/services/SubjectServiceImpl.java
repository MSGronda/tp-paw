package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.OrderDir;
import ar.edu.itba.paw.models.enums.SubjectFilterField;
import ar.edu.itba.paw.models.enums.SubjectOrderField;
import ar.edu.itba.paw.models.exceptions.InvalidPageNumberException;
import ar.edu.itba.paw.persistence.dao.SubjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl implements SubjectService {
    private final SubjectDao subjectDao;

    @Autowired
    public SubjectServiceImpl(final SubjectDao subjectDao) {
        this.subjectDao = subjectDao;
    }

    @Override
    public Optional<Subject> findById(final String id) {
        return subjectDao.findById(id);
    }

    @Override
    public List<Subject> findAllThatUserCanDo(User user) {
        return subjectDao.findAllThatUserCanDo(user);
    }

    @Override
    public List<Subject> findAllThatUserHasNotDone(User user){
        return subjectDao.findAllThatUserHasNotDone(user);
    }

    @Override
    public List<Subject> findAllThatUserHasDone(User user) {
        return subjectDao.findAllThatUserHasDone(user);
    }

    @Override
    public List<Subject> findAllThatUserCouldUnlock(User user){
        return subjectDao.findAllThatUserCouldUnlock(user);
    }

    @Override
    public List<Subject> search(final User user, final String name, final int page) {
        return subjectDao.search(user, name, page);
    }

    @Override
    public List<Subject> search(
            final User user,
            final String name,
            final int page,
            final String orderBy,
            final String dir,
            final Integer credits,
            final String department
    ) {
        if(page < 1 || page > getTotalPagesForSearch(user, name, credits, department)) throw new InvalidPageNumberException();

        final OrderDir orderDir = OrderDir.parse(dir);
        final SubjectOrderField orderField = SubjectOrderField.parse(orderBy);

        return subjectDao.search(user, name, page, getFilterMap(credits, department), orderField, orderDir);
    }

    @Override
    public int getTotalPagesForSearch(final User user, final String name){
        return subjectDao.getTotalPagesForSearch(user, name, null);
    }

    @Override
    public int getTotalPagesForSearch(final User user, final String name, final Integer credits, final String department){
        return subjectDao.getTotalPagesForSearch(user, name, getFilterMap(credits, department));
    }

    @Override
    public Map<String, List<String>> getRelevantFiltersForSearch(final String name) {
        return subjectDao.getRelevantFiltersForSearch(name, null)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().name(),
                        Map.Entry::getValue
                ));
    }

    @Override
    public Map<String, List<String>> getRelevantFiltersForSearch(final String name, final Integer credits, final String department) {
        return subjectDao.getRelevantFiltersForSearch(name, getFilterMap(credits, department))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().name(),
                        Map.Entry::getValue
                ));
    }

    @Override
    public List<Subject> getAll() {
        return subjectDao.getAll();
    }

    @Override
    public Map<User, Set<Subject>> getAllUserUnreviewedNotificationSubjects() {
        return subjectDao.getAllUserUnreviewedNotificationSubjects();
    }

    @Override
    public void updateUnreviewedNotificationTime() {
        subjectDao.updateUnreviewedNotificationTime();
    }

    private Map<SubjectFilterField, String> getFilterMap(final Integer credits, final String department) {
        final Map<SubjectFilterField, String> filters = new HashMap<>();
        if(credits != null) filters.put(SubjectFilterField.CREDITS, credits.toString());
        if(department != null && !department.isEmpty()) filters.put(SubjectFilterField.DEPARTMENT, department);
        return filters;
    }
}
