package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.persistence.SubjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class SubjectServiceImpl implements SubjectService {
    private final SubjectDao subjectDao;


    private static final Map<String, String > validFilters = new HashMap<>();
    private static final List<String> validOrderBy = Arrays.asList("id", "credits", "subname");
    private static final List<String> validDir = Arrays.asList("asc", "desc");

    @Autowired
    public SubjectServiceImpl(SubjectDao subjectDao) {
        this.subjectDao = subjectDao;
        validFilters.putIfAbsent("department", "[\\p{L} ]+");
        validFilters.putIfAbsent("credits", "\\d+");
    }

    @Override
    public Optional<Subject> findById(String id) {
        return subjectDao.findById(id);
    }

    @Override
    public List<Subject> findByIds(List<String> ids) {
        return subjectDao.findByIds(ids);
    }

    @Override
    public List<Subject> getByName(String name) {
        return subjectDao.getByName(name);
    }

    @Override
    public List<Subject> getByNameFiltered(String name, Map<String,String> filters) {
        Map<String, String> validatedFilters = new HashMap<>();

        // Check if filters, order by and direction are valid
        for(Map.Entry<String, String> filter : filters.entrySet()){
            if(
                (Objects.equals(filter.getKey(), "ob") && validOrderBy.contains(filter.getValue())) ||
               (Objects.equals(filter.getKey(), "dir") && validDir.contains(filter.getValue()) ||
               (validFilters.containsKey(filter.getKey()) && Pattern.matches( validFilters.get(filter.getKey()) ,filter.getValue()))) ||
               (Objects.equals(filter.getKey(), "pageNum") && filter.getValue().matches("[0-9]+"))

            ){
                validatedFilters.put(filter.getKey(), filter.getValue());
            }
        }
        return subjectDao.getByNameFiltered(name, validatedFilters);
    }

    @Override
    public int getTotalPagesForSubjects(String name, Map<String, String> filters){
        Map<String, String> validatedFilters = new HashMap<>();

        // Check if filters, order by and direction are valid
        for(Map.Entry<String, String> filter : filters.entrySet()){
            if(
                    (Objects.equals(filter.getKey(), "ob") && validOrderBy.contains(filter.getValue())) ||
                            (Objects.equals(filter.getKey(), "dir") && validDir.contains(filter.getValue()) ||
                                    (validFilters.containsKey(filter.getKey()) && Pattern.matches( validFilters.get(filter.getKey()) ,filter.getValue()))) ||
                            (Objects.equals(filter.getKey(), "pageNum") && filter.getValue().matches("[0-9]+"))

            ){
                validatedFilters.put(filter.getKey(), filter.getValue());
            }
        }
        return subjectDao.getTotalPagesForSubjects(name,validatedFilters);
    }

    @Override
    public Map<String, Set<String>> getRelevantFilters(List<Subject> subjects) {
        Map<String, Set<String>> relevant = new HashMap<>();

        relevant.put("department", new HashSet<>());
        relevant.put("credits", new HashSet<>());

        for(Subject sub : subjects){
            String dpt = sub.getDepartment();
            int cdts = sub.getCredits();
            if(dpt != null && !dpt.equals(""))
                relevant.get("department").add(dpt);
            if(cdts != 0)
                relevant.get("credits").add(sub.getCredits().toString());
        }
        return relevant;
    }

    @Override
    public List<Subject> getAll() {
        return subjectDao.getAll();
    }

    @Override
    public List<Subject> getAllByDegree(Long idDegree){
        return subjectDao.getAllByDegree(idDegree);
    }

    @Override
    public Map<Long, List<Subject>> getAllGroupedByDegreeId() {
        return subjectDao.getAllGroupedByDegreeId();
    }

    public Map<Long, Map<Integer, List<Subject>>> getAllGroupedByDegIdAndSemester() {
        return subjectDao.getAllGroupedByDegIdAndSemester();
    }

    @Override
    public Map<Long, Map<Integer, List<Subject>>> getAllGroupedByDegIdAndYear(){
        return subjectDao.getAllGroupedByDegIdAndYear();
    }

    @Override
    public Map<Integer, List<Subject>> getInfSubsByYear(Long degreeId){
        Map<Long, Map<Integer, List<Subject>>> all = getAllGroupedByDegIdAndYear();
        return all.get(degreeId);
    }

    @Override
    public Map<Long, List<Subject>> getAllElectivesGroupedByDegId(){
        return subjectDao.getAllElectivesGroupedByDegId();
    }

    @Override
    public List<Subject> getInfElectives(Long degreeId){
        Map<Long, List<Subject>> all = getAllElectivesGroupedByDegId();
        return all.get(degreeId);
    }


    @Transactional
    @Override
    public Subject create(String id, String name, String depto, Set<String> idCorrelativas, Set<Long> idProfesores, Set<Long> idCarreras, Integer creditos){
        return subjectDao.create(id, name, depto, idCorrelativas, idProfesores, idCarreras, creditos);
    }
}
