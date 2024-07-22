package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exceptions.DegreeNotFoundException;
import ar.edu.itba.paw.models.exceptions.SemesterNotFoundException;
import ar.edu.itba.paw.models.exceptions.SubjectNotFoundException;
import ar.edu.itba.paw.models.utils.DegreeSearchParams;
import ar.edu.itba.paw.models.utils.SubjectSearchParams;
import ar.edu.itba.paw.persistence.dao.DegreeDao;
import ar.edu.itba.paw.persistence.dao.SubjectDao;
import ar.edu.itba.paw.services.enums.OperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DegreeServiceImpl implements DegreeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DegreeServiceImpl.class);
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

    @Transactional
    @Override
    public Degree create(final Degree.Builder builder) {
        final Degree degree = builder.build();
        degreeDao.create(degree);

        return degree;
    }

    @Transactional
    @Override
    public void addSubjectToDegrees(final Subject subject, final List<Long> degreeIds, final List<Integer> semesters) {
        degreeDao.addSubjectToDegrees(subject, degreeIds, semesters);
    }

    @Transactional
    @Override
    public void addSemestersToDegree(final Degree degree, final Map<Integer, List<String>> semesterSubjects) {

        for (Map.Entry<Integer, List<String>> entry : semesterSubjects.entrySet()) {
            if(entry != null) {
                for (String subjectId : entry.getValue()) {

                    Optional<Subject> maybeSubject = subjectDao.findById(subjectId);

                    if (maybeSubject.isPresent()) {
                        final Subject subject = maybeSubject.get();
                        DegreeSubject degreeSubject = new DegreeSubject(degree, subject, entry.getKey());

                        if (!degree.getDegreeSubjects().contains(degreeSubject)) {
                            degree.getDegreeSubjects().add(degreeSubject);
                            LOGGER.info("Added subject with id: {} to degree with id: {}", subject.getId(), degree.getId());
                        }

                    }
                }
            }
        }
    }

    @Transactional
    @Override
    public void deleteSemesterFromDegree(final Degree degree, final int semesterId){
        final List<DegreeSubject> subjects = degree.getDegreeSubjects();

        final List<DegreeSubject> subjectsToDelete = new ArrayList<>();
        subjects.forEach(subject -> {
            if(subject.getSemester() == semesterId){
                subjectsToDelete.add(subject);
            }
        });

        subjectsToDelete.forEach(subjects::remove);
    }

    @Transactional
    @Override
    public void delete(final Degree degree) {
        degreeDao.delete(degree);
    }

    @Transactional
    @Override
    public void editName(final Degree degree, final String name){
        degreeDao.editName(degree, name);
    }

    @Transactional
    @Override
    public void editTotalCredits(final Degree degree, final int totalCredits){
        degreeDao.editTotalCredits(degree, totalCredits);
    }

    @Override
    public List<DegreeSemester> getDegreeSemesters(final Long degreeId, final String subjectId){
        final Degree degree = degreeDao.findById(degreeId).orElseThrow(DegreeNotFoundException::new);
        if(subjectId == null){
            return degree.getSemesters();
        } else {
            final Subject subject = subjectDao.findById(subjectId).orElseThrow(SubjectNotFoundException::new);

            OptionalInt semesterId = degreeDao.findSubjectSemesterForDegree(subject, degree);

            if(!semesterId.isPresent()) return Collections.emptyList();

            return new ArrayList<>(Collections.singletonList(degree.getSemesters().get(semesterId.getAsInt())));
        }
    }

    @Transactional
    @Override
    public void editDegreeSemester(
            final Degree degree,
            final AbstractMap.SimpleEntry<OperationType, AbstractMap.SimpleEntry<Integer, String>> op
    ){
        final Subject subject = subjectDao.findById(op.getValue().getValue()).orElseThrow(SubjectNotFoundException::new);
        final DegreeSubject ds = new DegreeSubject(degree, subject, op.getValue().getKey());
        if(op.getKey() == OperationType.Add){
            if(!degree.getDegreeSubjects().contains(ds)){
                degree.getDegreeSubjects().add(ds);
                LOGGER.info("Added subject with id: {} to degree with id: {}", subject.getId(), degree.getId());
            }
        }
        else if(op.getKey() == OperationType.Remove){
            degree.getDegreeSubjects().remove(ds);
            LOGGER.info("Removed subject with id: {} from degree with id: {}", subject.getId(), degree.getId());
        }
    }

    @Transactional
    @Override
    public void replaceSubjectDegrees(final Subject subject, final List<Long> degreeIds, final List<Integer> semesters) {
        degreeDao.replaceSubjectDegrees(subject, degreeIds, semesters);
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
    public List<Degree> searchDegrees(final DegreeSearchParams params) {
        return degreeDao.searchDegrees(params);
    }

}
