package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.DegreeSubject;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.dao.DegreeDao;
import ar.edu.itba.paw.persistence.dao.SubjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DegreeServiceImpl implements DegreeService {
    private final DegreeDao degreeDao;
    private final SubjectDao subjectDao;

    @Autowired
    public DegreeServiceImpl(
            final DegreeDao degreeDao,
            final SubjectDao subjectDao
    ) {
        this.degreeDao = degreeDao;
        this.subjectDao = subjectDao;
    }

    @Override
    public Optional<Degree> findById(final long id) {
        return degreeDao.findById(id);
    }

    @Override
    public Optional<Degree> findByName(final String name) {
        return degreeDao.findByName(name);
    }

    @Override
    public Optional<Degree> findParentDegree(final Subject subject, final User user) {
        final Set<Degree> subjectDegrees = subject.getDegrees();

        if(user != null) {
            final Degree userDegree = user.getDegree();
            if(subjectDegrees.contains(userDegree)) {
                return Optional.of(userDegree);
            }
        }

        return subjectDegrees.stream().findFirst();
    }

    @Override
    public OptionalInt findSubjectYearForParentDegree(final Subject subject, final User user) {
        final Optional<Degree> maybeDegree = findParentDegree(subject, user);
        if(!maybeDegree.isPresent()) return OptionalInt.empty();
        final Degree degree = maybeDegree.get();

        final OptionalInt semester = degreeDao.findSubjectSemesterForDegree(subject, degree);
        if(!semester.isPresent()) return OptionalInt.empty();

        return OptionalInt.of((int)Math.ceil(semester.getAsInt() / 2.0));
    }

    @Override
    public List<Degree> getAll() {
        return degreeDao.getAll();
    }

    @Override
    public Map<String, List<String>> getRelevantFiltersForDegree(Degree degree) {
        return subjectDao.getRelevantFiltersForSearch(degree,"", null, null)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().name(),
                        Map.Entry::getValue
                ));
    }

    @Transactional
    @Override
    public void create(final Degree.Builder builder) {
        degreeDao.create(builder.build());
    }

    @Transactional
    @Override
    public void addSubjectToDegrees(final Subject subject, final List<Long> degreeIds, final List<Integer> semesters) {
        degreeDao.addSubjectToDegrees(subject, degreeIds, semesters);
    }

    @Transactional
    @Override
    public void updateSubjectToDegrees(final Subject subject, final List<Long> degreeIds, final List<Integer> semesters) {
        List<Degree> degreesToAdd = new ArrayList<>();
        List<Integer> semestersToAdd = new ArrayList<>();

        List<DegreeSubject> degreesToRemove = new ArrayList<>();

        List<DegreeSubject> degreeToUpdate = new ArrayList<>();
        List<Integer> semesterToUpdate = new ArrayList<>();

        // itero por la lista de degrees
        for (int i = 0; i < degreeIds.size(); i++) {
            Optional<Degree> maybeDegree = degreeDao.findById(degreeIds.get(i));

            if (maybeDegree.isPresent()) {
                Degree degree = maybeDegree.get();

                //si contiene el degree, 2 casos: si es el mismo semetre -> eliminar, sino actualizar
                if (subject.getDegrees().contains(degree)) {

                    //itero por los degreeSubjects para encontrar el indicado
                    for (DegreeSubject degreeSubject : degree.getDegreeSubjects()) {
                        if (degreeSubject.getSubject().equals(subject)) {
                            if (degreeSubject.getSemester() != semesters.get(i)) {
                                //semester se cambio, actualizar
                                degreeToUpdate.add(degreeSubject);
                                semesterToUpdate.add(semesters.get(i));
                            } else {
                                //semester es el mismo, eliminar
                                degreesToRemove.add(degreeSubject);
                            }
                        }
                    }
                } else {
                    //si no tiene degree, agregar nuevo degreeSubject
                    degreesToAdd.add(degree);
                    semestersToAdd.add(semesters.get(i));
                }
            }
        }
        degreeDao.updateInsertSubjectToDegrees(subject, degreesToAdd, semestersToAdd);
        degreeDao.updateUpdateSubjectToDegrees(subject, degreeToUpdate, semesterToUpdate);
        degreeDao.updateDeleteSubjectToDegrees(subject, degreesToRemove);

    }

    @Transactional
    @Override
    public void addSemestersToDegree(final Degree degree, final Map<Integer, List<String>> semesterSubjects) {

        for (Map.Entry<Integer, List<String>> entry : semesterSubjects.entrySet()) {

            for (String subjectId : entry.getValue()) {

                Optional<Subject> maybeSubject = subjectDao.findById(subjectId);

                if (maybeSubject.isPresent()) {
                    Subject subject = maybeSubject.get();
                    DegreeSubject degreeSubject = new DegreeSubject(degree, subject, entry.getKey());

                    if (!degree.getDegreeSubjects().contains(degreeSubject)) {
                        degree.getDegreeSubjects().add(degreeSubject);
                    }

                }
            }
        }
    }


    @Transactional
    @Override
    public void delete(final Degree degree) {
        degreeDao.delete(degree);
    }
}
