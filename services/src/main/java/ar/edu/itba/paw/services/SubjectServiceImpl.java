package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.*;
import ar.edu.itba.paw.models.exceptions.*;
import ar.edu.itba.paw.persistence.dao.SubjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl implements SubjectService {
    private final SubjectDao subjectDao;
    private final DegreeService degreeService;
    private final ProfessorService professorService;

    private final ReviewService reviewService;

    @Autowired
    public SubjectServiceImpl(
            final SubjectDao subjectDao,
            final DegreeService degreeService,
            final ProfessorService professorService,
            @Lazy final ReviewService reviewService
    ){
        this.subjectDao = subjectDao;
        this.degreeService = degreeService;
        this.professorService = professorService;
        this.reviewService = reviewService;
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
        if(!user.isEditor())
            throw new UnauthorizedException();

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
    public List<Subject> get(
            final User user,
            final Long degree,
            final Long semester,
            final Long available,
            final Long unLockable,
            final Long done,
            final Long future,
            final Long plan,
            final String query,
            final Integer credits,
            final String department,
            final Integer difficulty,
            final Integer timeDemand,
            final Long userReviews,
            final int page,
            final String orderBy,
            final String dir
    ){
        if(degree != null && semester != null){
            final Degree deg = degreeService.findById(degree).orElseThrow(DegreeNotFoundException::new);
            final List<DegreeSemester> semesters =  deg.getSemesters();

            if(semesters.size() < semester)
                throw new SemesterNotFoundException();

            return semesters.get(Math.toIntExact(semester) - 1).getSubjects();
        }
        if(available != null){
            return findAllThatUserCanDo(user, page, orderBy, dir);
        }
        if(unLockable != null){
            return findAllThatUserCouldUnlock(user, page, orderBy, dir);
        }
        if(done != null){
            return findAllThatUserHasDone(user, page, orderBy, dir);
        }
        if(future != null){
            return findAllThatUserHasNotDone(user, page, orderBy, dir);
        }
        if(plan != null){
            return getUserSemester(user);
        }
        if(query != null){
            return search(user, query, page, orderBy, dir, credits, department, difficulty, timeDemand);
        }
        if(userReviews != null){
            return getSubjectsThatUserReviewed(userReviews, page);
        }
        return getAll(page,orderBy, dir);
    }

    public List<Subject> getUserSemester(final User user){
        final List<Subject> subjects = new ArrayList<>();

        for(final SubjectClass sc : user.getUserSemester()){
            subjects.add(sc.getSubject());
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
    public List<Subject> findAllThatUserCanDo(final User user, final int page, final String orderBy, final String dir) {
        final OrderDir orderDir = OrderDir.parse(dir);
        final SubjectOrderField orderField = SubjectOrderField.parse(orderBy);
        return subjectDao.findAllThatUserCanDo(user, page, orderField, orderDir);
    }

    @Override
    public List<Subject> findAllThatUserHasNotDone(final User user, final int page, final String orderBy, final String dir){
        final OrderDir orderDir = OrderDir.parse(dir);
        final SubjectOrderField orderField = SubjectOrderField.parse(orderBy);
        return subjectDao.findAllThatUserHasNotDone(user, page, orderField, orderDir);
    }

    @Override
    public List<Subject> findAllThatUserHasDone(final User user, final int page, final String orderBy, final String dir) {
        final OrderDir orderDir = OrderDir.parse(dir);
        final SubjectOrderField orderField = SubjectOrderField.parse(orderBy);
        return subjectDao.findAllThatUserHasDone(user, page, orderField, orderDir);
    }

    @Override
    public List<Subject> findAllThatUserCouldUnlock(final User user, final int page, final String orderBy, final String dir){
        final OrderDir orderDir = OrderDir.parse(dir);
        final SubjectOrderField orderField = SubjectOrderField.parse(orderBy);
        return subjectDao.findAllThatUserCouldUnlock(user, page, orderField, orderDir);
    }

    @Override
    public List<Subject> search(
            final User user,
            final String name,
            final int page,
            final String orderBy,
            final String dir,
            final Integer credits,
            final String department,
            final Integer difficulty,
            final Integer time
    ) {
        if(page < 1 || page > getTotalPagesForSearch(user, name, credits, department, difficulty, time, orderBy))
            throw new InvalidPageNumberException();

        final OrderDir orderDir = OrderDir.parse(dir);
        final SubjectOrderField orderField = SubjectOrderField.parse(orderBy);

        if(user.isEditor()){
            return subjectDao.searchAll(name, page, getFilterMap(credits, department, difficulty, time), orderField, orderDir);
        }
        return subjectDao.search(user, name, page, getFilterMap(credits, department, difficulty, time), orderField, orderDir);
    }

    @Override
    public int getTotalPagesForSearch(
            final User user,
            final String name,
            final Integer credits,
            final String department,
            final Integer difficulty,
            final Integer time,
            final String orderBy
    ){
        if(user.isEditor()){
            return subjectDao.getTotalPagesForSearchAll(
                    name,
                    getFilterMap(credits, department, difficulty, time),
                    SubjectOrderField.parse(orderBy)
            );
        }
        return subjectDao.getTotalPagesForSearch(
                user,
                name,
                getFilterMap(credits, department, difficulty, time),
                SubjectOrderField.parse(orderBy)
        );
    }

    @Override
    public Map<String, List<String>> getRelevantFiltersForSearch(
            final User user,
            final String name,
            final Integer credits,
            final String department,
            final Integer difficulty,
            final Integer time,
            final String orderBy
    ) {
        if(user.isEditor()){
            return subjectDao.getRelevantFiltersForSearch(
                    name, getFilterMap(credits, department, difficulty, time),
                    SubjectOrderField.parse(orderBy)
                    )
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            e -> e.getKey().name(),
                            Map.Entry::getValue
                    ));
        }
        else {
            return subjectDao.getRelevantFiltersForSearch(
                    user.getDegree(),
                            name,
                            getFilterMap(credits, department, difficulty, time),
                            SubjectOrderField.parse(orderBy)
                    )
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            e -> e.getKey().name(),
                            Map.Entry::getValue
                    ));
        }
    }

    protected Map<SubjectFilterField, String> getFilterMap(
            final Integer credits,
            final String department,
            final Integer difficulty,
            final Integer time
    ) {
        final Map<SubjectFilterField, String> filters = new HashMap<>();

        if(credits != null)
            filters.put(SubjectFilterField.CREDITS, credits.toString());
        if(department != null && !department.isEmpty())
            filters.put(SubjectFilterField.DEPARTMENT, department);
        if(difficulty != null)
            filters.put(SubjectFilterField.DIFFICULTY, String.valueOf(Difficulty.parse(difficulty.longValue()).getValue()));
        if(time != null)
            filters.put(SubjectFilterField.TIME, String.valueOf(TimeDemanding.parse(time.longValue()).getIntValue()));

        return filters;
    }

    @Override
    public Map<User, Set<Subject>> getAllUserUnreviewedNotificationSubjects() {
        return subjectDao.getAllUserUnreviewedNotificationSubjects();
    }

    @Override
    public void updateUnreviewedNotificationTime() {
        subjectDao.updateUnreviewedNotificationTime();
    }

    @Override
    public List<Subject> getSubjectsThatUserReviewed(final Long userId, final Integer page){
        List<Subject> subjectList = new ArrayList<>();
        if( userId != null && page != 0){
            List<Review> reviews = reviewService.get(userId, null, page, "difficulty", "desc" );
            for( Review review : reviews){
                subjectList.add(review.getSubject());
            }
        }

        return subjectList;
    }
}
