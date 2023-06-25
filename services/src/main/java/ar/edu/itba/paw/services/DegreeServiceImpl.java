package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.DegreeSubject;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.persistence.dao.DegreeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Service
public class DegreeServiceImpl implements DegreeService {
    private final DegreeDao degreeDao;

    @Autowired
    public DegreeServiceImpl(final DegreeDao degreeDao) {
        this.degreeDao = degreeDao;
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
    public OptionalInt findSubjectYearForDegree(final Subject subject, final Degree degree) {
        final OptionalInt semester = degreeDao.findSubjectSemesterForDegree(subject, degree);
        if(!semester.isPresent()) return OptionalInt.empty();

        return OptionalInt.of((int)Math.ceil(semester.getAsInt() / 2.0));
    }

    @Override
    public List<Degree> getAll() {
        return degreeDao.getAll();
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

        List<Degree> degreesToRemove = new ArrayList<>();
        List<Integer> semesterToRemove = new ArrayList<>();

        List<Degree> degreeToUpdate = new ArrayList<>();
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
                                degreeToUpdate.add(degree);
                                semesterToUpdate.add(semesters.get(i));
                            } else {
                                //semester es el mismo, eliminar
                                degreesToRemove.add(degree);
                                semesterToRemove.add(semesters.get(i));
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
        degreeDao.updateDeleteSubjectToDegrees(subject, degreesToRemove, semesterToRemove);

    }

}
