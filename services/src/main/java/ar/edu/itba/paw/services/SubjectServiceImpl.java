package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.OrderDir;
import ar.edu.itba.paw.models.enums.SubjectFilterField;
import ar.edu.itba.paw.models.enums.SubjectOrderField;
import ar.edu.itba.paw.models.exceptions.InvalidPageNumberException;
import ar.edu.itba.paw.models.exceptions.SubjectNotFoundException;
import ar.edu.itba.paw.models.exceptions.UnauthorizedException;
import ar.edu.itba.paw.persistence.dao.SubjectDao;
import ar.edu.itba.paw.persistence.exceptions.SubjectIdAlreadyExistsPersistenceException;
import ar.edu.itba.paw.services.exceptions.SubjectIdAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Autowired
    public SubjectServiceImpl(final SubjectDao subjectDao, DegreeService degreeService, ProfessorService professorService){
        this.subjectDao = subjectDao;
        this.degreeService = degreeService;
        this.professorService = professorService;
    }

    @Transactional
    @Override
    public Subject create(final Subject.Builder builder,
                          final String degreeIds,
                          final String semesters,
                          final String requirementIds,
                          final String professors,
                          final String classCodes,
                          final String classProfessors,
                          final String classDays,
                          final String classStartTimes,
                          final String classEndTime,
                          final String classBuildings,
                          final String classRooms,
                          final String classModes) throws SubjectIdAlreadyExistsException {
        Subject sub;
        try{
            sub = subjectDao.create(builder.build());
        } catch (final SubjectIdAlreadyExistsPersistenceException e){
            throw new SubjectIdAlreadyExistsException();
        }
        // parsear y llamar a los demas services

        //se agrega a subjectDegrees
        final List<String> degreeIdsList = parseJsonList(degreeIds, false);
        final List<String> semestersList = parseJsonList(semesters, false);
        degreeService.addSubjectToDegrees(
                sub,
                degreeIdsList.stream().map(Long::parseLong).collect(java.util.stream.Collectors.toList()),
                semestersList.stream().map(Integer::parseInt).collect(java.util.stream.Collectors.toList())
        );

        //se agregan profesores a professorSubjects
        final List<String> professorsList = parseJsonList(professors, true);
        professorService.addSubjectToProfessors(
                sub,
                professorsList
        );

        //se agregan las correlativas
        final List<String> correlativesList = parseJsonList(requirementIds, false);
        subjectDao.addPrerequisites( sub, correlativesList);

        //se crean las comisiones
        final List<String> classesList = parseJsonList(classCodes, false);
        final Set<String> uniqueClassesList = new HashSet<>(classesList);
        subjectDao.addClassesToSubject(sub, uniqueClassesList);

        //se agregan los profesores a las comisiones
        final List<List<String>> classProfessorsList = parseJsonListOfLists(classProfessors);
        professorService.addProfessorsToClasses(sub, classesList, classProfessorsList);

        //se agregan los datos a las comisiones
        final List<String> startTimes = parseJsonList(classStartTimes, false);
        final List<String> endTimes = parseJsonList(classEndTime, false);
        final List<String> buildings = parseJsonList(classBuildings, false);
        final List<String> modes = parseJsonList(classModes, false);
        final List<String> days = parseJsonList(classDays, false);
        final List<String> rooms = parseJsonList(classRooms, false);
        subjectDao.addSubjectClassTimes(sub,
                classesList,
                startTimes.stream().map(LocalTime::parse).collect(Collectors.toList()),
                endTimes.stream().map(LocalTime::parse).collect(Collectors.toList()),
                buildings,
                modes,
                days.stream().map(Integer::parseInt).collect(Collectors.toList()),
                rooms
        );

        return sub;
    }

    private List<String> parseJsonList(String stringifyString, boolean everySecondComma) {
        String trimmedInput = stringifyString.replace("[", "").replace("]", "");
        String[] elements = trimmedInput.split(",");

        List<String> parsedList = new ArrayList<>();
        if (elements[0].equals("")) {
            return parsedList;
        }
        if( !everySecondComma){
            for (String element : elements) {
                // Remove quotes around each element
                String parsedElement = element.replace("\"", "");
                parsedList.add(parsedElement);
            }
        }else{
            for (int i = 0; i < elements.length; i+=2) {
                // Remove quotes around each element
                String parsedElement = elements[i].replace("\"", "");
                String parsedElement2 = elements[i+1].replace("\"", "");
                parsedElement += "," + parsedElement2;
                parsedList.add(parsedElement);
            }
        }


        return parsedList;
    }

    private List<List<String>> parseJsonListOfLists(String classProfessors){
        List<List<String>> resultList = new ArrayList<>();

        // Remove the square brackets at the start and end of the input string
        String cleanedInput = classProfessors.substring(1, classProfessors.length() - 1);

        String[] elements = cleanedInput.split("],");
        if (elements[0].equals("") || elements[0].equals("[") || elements[0].equals("[]")){
            return resultList;
        }
        for( int i = 0; i < elements.length ; i++){
            List<String> classProf = new ArrayList<>();

            String trimmedInput = elements[i].replace("[", "").replace("]", "");
            String[] elements2 = trimmedInput.split(",");
            for( int j = 0 ; j < elements2.length ; j+=2 ){
                String parsedElement = elements2[j].replace("\"", "");
                String parsedElement2 = elements2[j+1].replace("\"", "");

                parsedElement += "," + parsedElement2;
                classProf.add(parsedElement);
            }
            resultList.add(classProf);
        }
        return resultList;
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
    public List<Subject> search(final String name, final int page) {
        return subjectDao.search(name, page);
    }

    @Override
    public List<Subject> search(
            final String name,
            final int page,
            final String orderBy,
            final String dir,
            final Integer credits,
            final String department
    ) {
        if(page < 1 || page > getTotalPagesForSearch(name, credits, department)) throw new InvalidPageNumberException();

        final OrderDir orderDir = OrderDir.parse(dir);
        final SubjectOrderField orderField = SubjectOrderField.parse(orderBy);

        return subjectDao.search(name, page, getFilterMap(credits, department), orderField, orderDir);
    }

    @Override
    public int getTotalPagesForSearch(final String name){
        return subjectDao.getTotalPagesForSearch(name, null);
    }

    @Override
    public int getTotalPagesForSearch(final String name, final Integer credits, final String department){
        return subjectDao.getTotalPagesForSearch(name, getFilterMap(credits, department));
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

    @Transactional
    @Override
    public void delete(final User user, final String subjectId) throws UnauthorizedException, SubjectNotFoundException {
        final Subject subject = findById(subjectId).orElseThrow(SubjectNotFoundException::new);
        delete(user, subject);
    }

    @Transactional
    @Override
    public void delete(final User user, final Subject subject) throws UnauthorizedException {
        if( !user.isEditor())
            throw new UnauthorizedException();

        subjectDao.delete(subject);
    }

    @Transactional
    @Override
    public void edit(
            final String id,
            final int credits,
            final String degreeIds,
            final String semesters,
            final String requirementIds,
            final String professors,
            final String classIds,
            final String classCodes,
            final String classProfessors,
            final String classDays,
            final String classStartTimes,
            final String classEndTime,
            final String classBuildings,
            final String classRooms,
            final String classModes) {

        Optional<Subject> optionalSubject = findById(id);
        if(!optionalSubject.isPresent()) {
            return;
        }

        Subject sub = optionalSubject.get();
        subjectDao.editCredits(sub, credits);

        final List<String> degreeIdsList = parseJsonList(degreeIds, false);
        final List<String> semestersList = parseJsonList(semesters, false);
        degreeService.updateSubjectToDegrees(
                sub,
                degreeIdsList.stream().map(Long::parseLong).collect(java.util.stream.Collectors.toList()),
                semestersList.stream().map(Integer::parseInt).collect(java.util.stream.Collectors.toList())
        );

        final List<String> professorsList = parseJsonList(professors, true);
        professorService.updateSubjectToProfessors(
                sub,
                professorsList
        );

        final List<String> correlativesList = parseJsonList(requirementIds, false);
        subjectDao.updatePrerequisites( sub, correlativesList);

        final List<String> classesList = parseJsonList(classCodes, false);
        final List<String> classesIdsList = parseJsonList(classIds, false);
        subjectDao.updateClassesToSubject(sub, classesIdsList, classesList);


        final List<List<String>> classProfessorsList = parseJsonListOfLists(classProfessors);
        professorService.updateProfessorsToClasses(sub, classesIdsList, classesList, classProfessorsList);

        final List<String> startTimes = parseJsonList(classStartTimes, false);
        final List<String> endTimes = parseJsonList(classEndTime, false);
        final List<String> buildings = parseJsonList(classBuildings, false);
        final List<String> modes = parseJsonList(classModes, false);
        final List<String> days = parseJsonList(classDays, false);
        final List<String> rooms = parseJsonList(classRooms, false);
        subjectDao.updateSubjectClassTimes(
                sub,
                classesIdsList,
                classesList,
                startTimes.stream().map(LocalTime::parse).collect(Collectors.toList()),
                endTimes.stream().map(LocalTime::parse).collect(Collectors.toList()),
                buildings,
                modes,
                days.stream().map(Integer::parseInt).collect(Collectors.toList()),
                rooms
        );
    }
}
