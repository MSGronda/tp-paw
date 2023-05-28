package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.OrderDir;
import ar.edu.itba.paw.models.enums.SubjectFilterField;
import ar.edu.itba.paw.models.enums.SubjectOrderField;
import ar.edu.itba.paw.persistence.dao.SubjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public List<Subject> findAllThatHasNotDone(User user){
        return subjectDao.findAllThatHasNotDone(user);
    }

    @Override
    public List<Subject> findAllThatHasDone(User user) {
        return subjectDao.findAllThatHasDone(user);
    }

    @Override
    public List<Subject> search(final String name, final int page) {
        return subjectDao.search(name, page);
    }

    @Override
    public List<Subject> search(
            final String name,
            final int page,
            final Map<String,String> filters,
            final String orderBy,
            final String dir
    ) {
        final OrderDir orderDir = OrderDir.parse(dir);
        final SubjectOrderField orderField = SubjectOrderField.parse(orderBy);

        return subjectDao.search(name, page, parseFilters(filters), orderField, orderDir);
    }

    @Override
    public int getTotalPagesForSearch(final String name){
        return subjectDao.getTotalPagesForSearch(name, null);
    }

    @Override
    public int getTotalPagesForSearch(final String name, final Map<String,String> filters){
        return subjectDao.getTotalPagesForSearch(name, parseFilters(filters));
    }

    @Override
    public Map<SubjectFilterField, List<String>> getRelevantFiltersForSearch(final String name) {
        return subjectDao.getRelevantFiltersForSearch(name, null);
    }

    @Override
    public Map<SubjectFilterField, List<String>> getRelevantFiltersForSearch(final String name, final Map<String,String> filters) {
        return subjectDao.getRelevantFiltersForSearch(name, parseFilters(filters));
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

    private Map<SubjectFilterField, String> parseFilters(Map<String, String> filters) {
        if (filters == null) {
            return null;
        }

        Map<SubjectFilterField, String> parsedFilters = new HashMap<>();
        for (Map.Entry<String, String> entry : filters.entrySet()) {
            SubjectFilterField filterField = SubjectFilterField.fromString(entry.getKey());
            if (filterField != null) {
                parsedFilters.put(filterField, entry.getValue());
            }
        }

        return parsedFilters;
    }
}
