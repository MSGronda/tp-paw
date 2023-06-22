package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.OrderDir;
import ar.edu.itba.paw.models.enums.SubjectFilterField;
import ar.edu.itba.paw.models.enums.SubjectOrderField;
import ar.edu.itba.paw.models.exceptions.InvalidPageNumberException;
import ar.edu.itba.paw.persistence.dao.SubjectDao;
import ar.edu.itba.paw.persistence.exceptions.SubjectIdAlreadyExistsPersistenceException;
import ar.edu.itba.paw.services.exceptions.SubjectIdAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl implements SubjectService {
    private final SubjectDao subjectDao;

    private final DegreeService degreeService;

    private final ProfessorService professorService;

    @Autowired
    public SubjectServiceImpl(final SubjectDao subjectDao, DegreeService degreeService, ProfessorService professorService) {
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
        List<String> degreeIdsList = parseJsonList(degreeIds, false);
        List<String> semestersList = parseJsonList(semesters, false);
        degreeService.addSubjectToDegrees(
                sub,
                degreeIdsList.stream().map(Long::parseLong).collect(java.util.stream.Collectors.toList()),
                semestersList.stream().map(Integer::parseInt).collect(java.util.stream.Collectors.toList())
        );

        //se agregan profesores a professorSubjects
        List<String> professorsList = parseJsonList(professors, true);
        professorService.addSubjectToProfessors(
                sub,
                professorsList
        );

        //se agregan las correlativas
        List<String> correlativesList = parseJsonList(requirementIds, false);
        subjectDao.addPrerequisites( sub, correlativesList);



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
}
