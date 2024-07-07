package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.*;
import ar.edu.itba.paw.models.exceptions.*;
import ar.edu.itba.paw.models.utils.SubjectSearchParams;
import ar.edu.itba.paw.persistence.dao.SubjectDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl implements SubjectService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubjectServiceImpl.class);
    private final SubjectDao subjectDao;
    private final DegreeService degreeService;
    private final ProfessorService professorService;

    @Autowired
    public SubjectServiceImpl(
            final SubjectDao subjectDao,
            final DegreeService degreeService,
            final ProfessorService professorService
    ){
        this.subjectDao = subjectDao;
        this.degreeService = degreeService;
        this.professorService = professorService;
    }

    @Transactional
    @Override
    public Subject create(
            final Subject.Builder builder,
            final List<Long> degreeIds,
            final List<Integer> semesters,
            final List<String> requirementIds,
            final List<String> professors
    ) {
        final Subject sub = subjectDao.create(builder.build());

        degreeService.addSubjectToDegrees(sub, degreeIds, semesters);

        professorService.addSubjectToProfessors(sub, professors);

        subjectDao.addPrerequisites(sub, requirementIds);

        return sub;
    }

    @Override
    @Transactional
    public void addClass(
            final Subject subject,
            final String classCode,
            final List<String> professors,
            final List<Integer> days,
            final List<LocalTime> startTimes,
            final List<LocalTime> endTimes,
            final List<String> locations,
            final List<String> buildings,
            final List<String> modes
    ){
        final SubjectClass subjectClass = subjectDao.addClassToSubject(subject, classCode);

        professorService.addProfessorsToClass(subjectClass, professors);

        subjectDao.addClassTimesToClass(
                subjectClass,
                days,
                startTimes,
                endTimes,
                locations,
                buildings,
                modes
        );
    }

    @Override
    @Transactional
    public void addClasses(
            final Subject subject,
            final List<String> codes,
            final List<List<String>> professors,
            final List<List<Integer>> days,
            final List<List<LocalTime>> startTimes,
            final List<List<LocalTime>> endTimes,
            final List<List<String>> locations,
            final List<List<String>> buildings,
            final List<List<String>> modes
    ){
        // TODO: ojo con los .size()
        for(int i=0; i<codes.size(); i++){
            addClass(
                    subject,
                    codes.get(i),
                    professors.get(i),
                    days.get(i),
                    startTimes.get(i),
                    endTimes.get(i),
                    locations.get(i),
                    buildings.get(i),
                    modes.get(i)
            );
        }
    }

    @Transactional
    @Override
    public void delete(final User user, final String subjectId) {
        if(!user.isEditor()){
            LOGGER.warn("Unauthorized user with id: {} attempted to delete subject with id: {}", user.getId(), subjectId);
            throw new UnauthorizedException();
        }

        final Subject subject = findById(subjectId).orElseThrow(SubjectNotFoundException::new);
        subjectDao.delete(subject);
    }

    @Override
    @Transactional
    public Subject editSubject(
            final Subject subject,
            final String name,
            final String department,
            final int credits,
            final List<String> requirementIds
    ){
        final Set<Subject> prereqs = new HashSet<>();
        requirementIds.forEach(id -> findById(id).ifPresent(prereqs::add));
        return subjectDao.editSubject(
                subject,
                name,
                department,
                credits,
                prereqs
        );
    }

    @Override
    @Transactional
    public void setClasses(
            final Subject subject,
            final List<String> codes,
            final List<List<String>> professors,
            final List<List<Integer>> days,
            final List<List<LocalTime>> startTimes,
            final List<List<LocalTime>> endTimes,
            final List<List<String>> locations,
            final List<List<String>> buildings,
            final List<List<String>> modes
    ){

        subjectDao.removeSubjectClassIfNotPresent(subject, codes);

        // TODO: ojo con los .size()
        for(int i=0; i<codes.size(); i++){
            final Optional<SubjectClass> maybeSubjectClass = subject.getClassById(codes.get(i));

            if(!maybeSubjectClass.isPresent()){
                addClass(
                        subject,
                        codes.get(i),
                        professors.get(i),
                        days.get(i),
                        startTimes.get(i),
                        endTimes.get(i),
                        locations.get(i),
                        buildings.get(i),
                        modes.get(i)
                );
            }
            else{
                final SubjectClass subjectClass = maybeSubjectClass.get();
                professorService.replaceClassProfessors(subjectClass, professors.get(i));
                subjectDao.replaceClassTimes(
                        subjectClass,
                        days.get(i),
                        startTimes.get(i),
                        endTimes.get(i),
                        locations.get(i),
                        buildings.get(i),
                        modes.get(i)
                );
            }
        }
    }
    @Override
    public List<Subject> superSearch(final SubjectSearchParams params, final int page, final String orderBy,final String dir){
        if(page < 1 || page > superSearchTotalPages(params))
            throw new InvalidPageNumberException();

        return subjectDao.superSearch(params, page, SubjectOrderField.parse(orderBy), OrderDir.parse(dir));
    }
    @Override
    public int superSearchTotalPages(final SubjectSearchParams params){
        return subjectDao.superSearchTotalPages(params);
    }

    @Override
    public Map<String, List<String>> superSearchRelevantFilters(final SubjectSearchParams params){
        return subjectDao.superSearchRelevantFilters(params)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey().name(),Map.Entry::getValue));
    }


    public List<Subject> getUserSemester(final User user, final Long planFinishedDate){
        final List<Subject> subjects = new ArrayList<>();

        for(final UserSemesterSubject us : user.getUserSemester()){
            if(
                (planFinishedDate == null && us.isActive()) ||
                (!us.isActive() && Objects.equals(planFinishedDate, us.getDateFinished().getTime()))
            ){
                subjects.add(us.getSubjectClass().getSubject());
            }
        }

        return subjects;
    }

    @Override
    public Optional<Subject> findById(final String id) {
        return subjectDao.findById(id);
    }

    @Override
    public List<Subject> getAll(final int page, final String orderBy, final String dir) {
        final OrderDir orderDir = OrderDir.parse(dir);
        final SubjectOrderField orderField = SubjectOrderField.parse(orderBy);
        return subjectDao.getAll(page, orderField, orderDir);
    }



    @Override
    public Map<User, Set<Subject>> getAllUserUnreviewedNotificationSubjects() {
        return subjectDao.getAllUserUnreviewedNotificationSubjects();
    }

    @Override
    public void updateUnreviewedNotificationTime() {
        subjectDao.updateUnreviewedNotificationTime();
    }

}
