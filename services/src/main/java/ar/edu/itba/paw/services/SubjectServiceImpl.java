package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.dao.SubjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl implements SubjectService {
    private final SubjectDao subjectDao;


    private static final Map<String, String > validFilters = new HashMap<>();
    private static final List<String> validOrderBy = Arrays.asList("id", "credits", "subname");
    private static final List<String> validDir = Arrays.asList("asc", "desc");

    @Autowired
    public SubjectServiceImpl(final SubjectDao subjectDao) {
        this.subjectDao = subjectDao;
        validFilters.putIfAbsent("department", "[\\p{L} ]+");
        validFilters.putIfAbsent("credits", "\\d+");
    }

    @Override
    public Optional<Subject> findById(final String id) {
        return subjectDao.findById(id);
    }

    @Override
    public List<Subject> findByIds(final List<String> ids) {
        return subjectDao.findByIds(ids);
    }

    @Override
    public List<Subject> getByName(final String name) {
        return subjectDao.getByName(name);
    }

    @Override
    public List<Subject> getByNameFiltered(final String name, final Map<String,String> filters) {

        // Given that pagination does not work "properly" when joining multiple tables
        // we must first get the ids of the subjects (which is paginated) and then get
        // the other relevant information (professors, prerequisits)

        List<Subject> subjectsWithoutPreReqs = subjectDao.getByNameFiltered(name, filterValidation(filters));

        List<String> ids = subjectsWithoutPreReqs.stream().map(Subject::getId).collect(Collectors.toList());

        return subjectDao.findByIds(ids);
    }

    @Override
    public int getTotalPagesForSubjects(final String name, final Map<String,String> filters){
        return subjectDao.getTotalPagesForSubjects(name,filterValidation(filters));
    }

    @Override
    public List<String> getSubjectsIds(List<Subject> subjects){
        List<String> toReturn = new ArrayList<>();
        for(Subject sub: subjects){
            toReturn.add(sub.getId());
        }
        return toReturn;
    }


    // In order to obtain ALL of the relevant filters for a certain search,
    @Override
    public Map<String, Set<String>> getRelevantFilters(final String name, final Map<String,String> filters) {
        Map<String, Set<String>> map =  subjectDao.getRelevantFiltersForSearchByName(name, filters);
        map.get("department").remove("");
        return map;
    }

    @Override
    public List<Subject> getAll() {
        return subjectDao.getAll();
    }

    @Override
    public List<Subject> getAllByDegree(final Long idDegree){
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
    public Map<Integer, List<Subject>> getInfSubsByYear(final Long degreeId){
        Map<Long, Map<Integer, List<Subject>>> all = getAllGroupedByDegIdAndYear();
        return all.get(degreeId);
    }

    @Override
    public Map<Long, List<Subject>> getAllElectivesGroupedByDegId(){
        return subjectDao.getAllElectivesGroupedByDegId();
    }

    @Override
    public List<Subject> getInfElectives(final Long degreeId){
        Map<Long, List<Subject>> all = getAllElectivesGroupedByDegId();
        return all.get(degreeId);
    }

    @Override
    public Map<User, Set<Subject>> getAllUserUnreviewedNotifSubjects() {
        return subjectDao.getAllUserUnreviewedNotIfSubjects();
    }

    @Override
    public void updateUnreviewedNotIfTime() {
        subjectDao.updateUnreviewedNotIfTime();
    }

    @Override
    public int checkPageNum(Map<String,String> params){
        if(params.isEmpty()){
            return 1;
        } else {
            if(!params.getOrDefault("pageNum","1").matches("[0-9]+")){
                return 1;
            } else {
                return Integer.parseInt(params.getOrDefault("pageNum","0")) + 1;
            }
        }
    }

    @Override
    public String checkOrder(Map<String,String> params){
        if(params.isEmpty()){
           return "semester";
        } else {
            return params.getOrDefault("order","semester");
        }
    }

    @Override
    public String checkDir(Map<String,String> params){
        if(params.isEmpty()){
            return "desc";
        } else {
            return params.getOrDefault("dir","desc");
        }
    }

    private Map<String,String> filterValidation(final Map<String,String> filters){
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
        return validatedFilters;
    }


}
