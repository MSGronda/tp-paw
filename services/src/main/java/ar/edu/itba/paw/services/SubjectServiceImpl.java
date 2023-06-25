package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.SubjectClass;
import ar.edu.itba.paw.models.SubjectClassTime;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.*;
import ar.edu.itba.paw.models.exceptions.InvalidPageNumberException;
import ar.edu.itba.paw.models.exceptions.SubjectNotFoundException;
import ar.edu.itba.paw.models.exceptions.UnauthorizedException;
import ar.edu.itba.paw.persistence.dao.SubjectDao;
import ar.edu.itba.paw.persistence.exceptions.SubjectIdAlreadyExistsPersistenceException;
import ar.edu.itba.paw.services.exceptions.SubjectIdAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Time;
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
//        if (elements[0].equals("") || elements[0].equals("[") || elements[0].equals("[]")){
//            return resultList;
//        }
        for( int i = 0; i < elements.length ; i++){
            List<String> classProf = new ArrayList<>();

            if( elements[i].equals("") || elements[i].equals("[") || elements[i].equals("[]")){
                resultList.add(classProf);
                continue;
            }

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
            final String department,
            final Integer difficulty,
            final Integer time
    ) {
        if(page < 1 || page > getTotalPagesForSearch(user, name, credits, department, difficulty, time)) throw new InvalidPageNumberException();

        final OrderDir orderDir = OrderDir.parse(dir);
        final SubjectOrderField orderField = SubjectOrderField.parse(orderBy);

        if(user.isEditor()){
            return subjectDao.searchAll(name, page, getFilterMap(credits, department, difficulty, time), orderField, orderDir);
        }
        return subjectDao.search(user, name, page, getFilterMap(credits, department, difficulty, time), orderField, orderDir);
    }

    @Override
    public int getTotalPagesForSearch(final User user, final String name){
        if(user.isEditor()){
            return subjectDao.getTotalPagesForSearchAll(name, null);
        }
        return subjectDao.getTotalPagesForSearch(user, name, null);
    }

    @Override
    public int getTotalPagesForSearch(
            final User user,
            final String name,
            final Integer credits,
            final String department,
            final Integer difficulty,
            final Integer time
    ){
        if(user.isEditor()){
            return subjectDao.getTotalPagesForSearchAll(name, getFilterMap(credits, department, difficulty, time));
        }
        return subjectDao.getTotalPagesForSearch(user, name, getFilterMap(credits, department, difficulty, time));
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
    public Map<String, List<String>> getRelevantFiltersForSearch(
            final String name,
            final Integer credits,
            final String department,
            final Integer difficulty,
            final Integer time
    ) {
        return subjectDao.getRelevantFiltersForSearch(name, getFilterMap(credits, department, difficulty, time))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().name(),
                        Map.Entry::getValue
                ));
    }

    @Override
    public Map<String, List<String>> getRelevantFiltersForSearch(final User user, final String name) {
        if(user.isEditor()){
            return subjectDao.getRelevantFiltersForSearch(name, null)
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            e -> e.getKey().name(),
                            Map.Entry::getValue
                    ));
        }
        return subjectDao.getRelevantFiltersForSearch(user.getDegree(), name, null)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().name(),
                        Map.Entry::getValue
                ));
    }

    @Override
    public Map<String, List<String>> getRelevantFiltersForSearch(
            final User user,
            final String name,
            final Integer credits,
            final String department,
            final Integer difficulty,
            final Integer time
    ) {
        if(user.isEditor()){
            return subjectDao.getRelevantFiltersForSearch(name, getFilterMap(credits, department, difficulty, time))
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            e -> e.getKey().name(),
                            Map.Entry::getValue
                    ));
        }
        else{
            return subjectDao.getRelevantFiltersForSearch(user.getDegree(), name, getFilterMap(credits, department, difficulty, time))
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            e -> e.getKey().name(),
                            Map.Entry::getValue
                    ));
        }
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

    private Map<SubjectFilterField, String> getFilterMap(
            final Integer credits,
            final String department,
            final Integer difficulty,
            final Integer time
    ) {
        final Map<SubjectFilterField, String> filters = new HashMap<>();
        if(credits != null) filters.put(SubjectFilterField.CREDITS, credits.toString());
        if(department != null && !department.isEmpty()) filters.put(SubjectFilterField.DEPARTMENT, department);

        if(difficulty != null)
            filters.put(SubjectFilterField.DIFFICULTY, String.valueOf(Difficulty.parse(difficulty.longValue()).getValue()));

        if(time != null)
            filters.put(SubjectFilterField.TIME, String.valueOf(TimeDemanding.parse(time.longValue()).getIntValue()));

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
        if (!optionalSubject.isPresent()) {
            return;
        }

        Subject sub = optionalSubject.get();
        if (credits != sub.getCredits()) {
            subjectDao.editCredits(sub, credits);
        }

        if (!degreeIds.isEmpty() && !semesters.isEmpty()) {
            final List<String> degreeIdsList = parseJsonList(degreeIds, false);
            final List<String> semestersList = parseJsonList(semesters, false);
            //este metodo se encarga de separar los degrees a partir de si ya existen o si fueron modificados
            //llama al dao despues por cada caso, insertar, modificar o eliminar
            degreeService.updateSubjectToDegrees(
                    sub,
                    degreeIdsList.stream().map(Long::parseLong).collect(java.util.stream.Collectors.toList()),
                    semestersList.stream().map(Integer::parseInt).collect(java.util.stream.Collectors.toList())
            );
        }

        if (!professors.isEmpty()) {
            final List<String> professorsList = parseJsonList(professors, true);
            professorService.updateSubjectToProfessors(
                    sub,
                    professorsList
            );
        }
        if (!requirementIds.isEmpty()) {
            final List<String> correlativesList = parseJsonList(requirementIds, false);
            //este metodo se encarga de separar las correlativas que se tienen que agregar o sacar
            //despues llama al dao por cada caso
            prepareCorrelativesToUpdate(sub, correlativesList);
        }

        if (!classCodes.isEmpty() && !classIds.isEmpty()) {
            final List<String> classesList = parseJsonList(classCodes, false);
            final List<String> classesIdsList = parseJsonList(classIds, false);
            prepareClassesToUpdate(sub, classesIdsList, classesList);

            final List<List<String>> classProfessorsList = parseJsonListOfLists(classProfessors);
            professorService.updateProfessorsToClasses(sub, classesIdsList, classesList, classProfessorsList);

            prepareClassLocTimeToUpdate(sub,classesIdsList,classesList,classStartTimes,classEndTime,classBuildings,classModes,classDays,classRooms);
        }
    }

    private void prepareClassLocTimeToUpdate(final Subject subject,final List<String> classIdsList,final List<String> classCodes,final String classStartTimes,final String classEndTimes,final String classBuildings,final String classModes,final String classDays,final String classRooms) {
        final List<String> startTimes = parseJsonList(classStartTimes, false);
        final List<String> endTimes = parseJsonList(classEndTimes, false);
        final List<String> buildings = parseJsonList(classBuildings, false);
        final List<String> modes = parseJsonList(classModes, false);
        final List<String> days = parseJsonList(classDays, false);
        final List<String> rooms = parseJsonList(classRooms, false);
        List<Integer> parsedDays = days.stream().map(Integer::parseInt).collect(Collectors.toList());
        List<LocalTime> parsedStartTime = startTimes.stream().map(LocalTime::parse).collect(Collectors.toList());
        List<LocalTime> parsedEndTime = endTimes.stream().map(LocalTime::parse).collect(Collectors.toList());
        for( int i = 0 ; i < classIdsList.size() ; i++){
            //si classId es -1, es una nueva comision
            //se crea
            if( Integer.parseInt(classIdsList.get(i)) < 0 ){
                createClassLocTime(subject,classCodes,i,startTimes.stream().map(LocalTime::parse).collect(Collectors.toList())
                        ,endTimes.stream().map(LocalTime::parse).collect(Collectors.toList())
                        ,days.stream().map(Integer::parseInt).collect(Collectors.toList()),rooms,buildings,modes);
            } else {//class id NO es -1
                //si code es -1, borrar con subjectClassTime id
                long key = Long.parseLong(classIdsList.get(i));
                if(classCodes.get(i).equals("-1")){//chequiar si es la ultima classLocTime que cada, si lo es borro la comi
                    subjectDao.deleteClassLocTime(key);
                } else {
                    //si no es -1 el class code, se modifica
                    subjectDao.updateClassLocTime(key,parsedDays.get(i),rooms.get(i),buildings.get(i),modes.get(i),parsedStartTime.get(i),parsedEndTime.get(i));
                }
            }
        }
    }

    private void createClassLocTime(final Subject subject,
                             final List<String> classCodes,
                             final int index,
                             final List<LocalTime> startTimes,
                             final List<LocalTime> endTimes,
                             final List<Integer> days,
                             final List<String> rooms,
                             final List<String> buildings,
                             final List<String> modes)
    {
        //se itera por subject Class de la subject para encontrar la comision
        //es necesaria para agregarla a la nueva SubjectClassTime
        for( SubjectClass subjectClass : subject.getClasses()){
            //si es la comision indicada
            if( subjectClass.getClassId().equals(classCodes.get(index))){
                subjectDao.createClassLocTime(subjectClass,days.get(index),endTimes.get(index),startTimes.get(index),rooms.get(index), buildings.get(index), modes.get(index));
            }
        }
    }

    private void prepareCorrelativesToUpdate(final Subject subject, final List<String> correlativesList){
        List<Subject> prereqToAdd = new ArrayList<>();
        List<Subject> prereqToRemove = new ArrayList<>();

        for( String prereqId : correlativesList ){
            Optional<Subject> maybeSubject = findById(prereqId);
            if( maybeSubject.isPresent()){
                Subject prereq = maybeSubject.get();
                if( subject.getPrerequisites().contains(prereq)){
                    prereqToRemove.add(prereq);
                }else{
                    prereqToAdd.add(prereq);
                }
            }
        }

        subjectDao.updatePrerequisitesToAdd(subject, prereqToAdd);
        subjectDao.updatePrerequisitesToRemove(subject, prereqToRemove);
    }

    private void prepareClassesToUpdate( final Subject subject, final List<String> classesIdsList, final List<String> classesCodeList){
        List<String> classesToAdd = new ArrayList<>();
        for(int i = 0 ; i < classesIdsList.size() ; i++){
            if( Integer.parseInt(classesIdsList.get(i)) < 0){
                boolean hasSubjectClass = false;
                for( SubjectClass subjectClass : subject.getClasses() ){
                    if( subjectClass.getClassId().equals(classesCodeList.get(i)) ){
                        hasSubjectClass = true;
                        break;
                    }
                }
                if( !hasSubjectClass ){
                    classesToAdd.add(classesCodeList.get(i));
                }
            }
        }
        subjectDao.updateClassesToSubject(subject, classesToAdd);
    }
}
