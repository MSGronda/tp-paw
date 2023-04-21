package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.persistence.SubjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SubjectServiceImpl implements SubjectService {
    private final SubjectDao subjectDao;

    private static final String orderByName = "subname";
    private static final String orderByCredits = "credits";
    private static final String orderById = "id";



    @Autowired
    public SubjectServiceImpl(SubjectDao subjectDao) {
        this.subjectDao = subjectDao;
    }

    @Override
    public Optional<Subject> findById(String id) {
        return subjectDao.findById(id);
    }

    @Override
    public List<Subject> getByName(String name) {
        return subjectDao.getByName(name);
    }

    @Override
    public Map<String, String> findPrerequisitesName(String id){
        return subjectDao.findPrerequisitesName(id);
    }

    @Override
    public List<Subject> getByNameOrderBy(String name, String ob) {
        switch (ob){
            case "credits": return subjectDao.getByNameOrderedBy(name, orderByCredits);
            case "id": return subjectDao.getByNameOrderedBy(name, orderById);
            case "name":default: return subjectDao.getByNameOrderedBy(name, orderByName);
        }
    }

    @Override
    public List<Subject> getByNameFiltered(String name, Map<String,String> filters, String ob) {
        return subjectDao.getByNameFiltered(name, filters, ob);
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

    @Override
    public List<Map<String, Integer>> getCardData(Set<Integer> years, Map<Integer, List<Subject>> infSubsByYear, ReviewService rs){
        Map<String, Integer> reviewCount = new HashMap<>();
        Map<String, Integer> subjectDifficulty = new HashMap<>();
        Map<String, Integer> subjectTime = new HashMap<>();

        for( int year : years ){
            for( Subject subject : infSubsByYear.get(year)){
                populateMaps(rs, reviewCount, subjectDifficulty, subjectTime, subject);
            }
        }
        List<Map<String, Integer>> toRet = new ArrayList<>();
        toRet.add(reviewCount);
        toRet.add(subjectDifficulty);
        toRet.add(subjectTime);
        return toRet;
    }

    @Override
    public List<Map<String, Integer>> getElectiveCardData(List<Subject> infElectives, ReviewService rs){
        Map<String, Integer> reviewCount = new HashMap<>();
        Map<String, Integer> subjectDifficulty = new HashMap<>();
        Map<String, Integer> subjectTime = new HashMap<>();

        for( Subject subject : infElectives){
            populateMaps(rs, reviewCount, subjectDifficulty, subjectTime, subject);
        }

        List<Map<String, Integer>> toRet = new ArrayList<>();
        toRet.add(reviewCount);
        toRet.add(subjectDifficulty);
        toRet.add(subjectTime);
        return toRet;
    }

    private void populateMaps(ReviewService rs, Map<String, Integer> reviewCount, Map<String, Integer> subjectDifficulty, Map<String, Integer> subjectTime, Subject subject) {
        reviewCount.put(subject.getId(), rs.getAllBySubject(subject.getId()).size());

        Optional<Integer> maybeDifficulty =  rs.getDifficultyBySubject(subject.getId());
        int difficulty;
        difficulty = maybeDifficulty.orElse(-1);
        subjectDifficulty.put(subject.getId(), difficulty );

        Optional<Integer> maybeTime = rs.getTimeBySubject(subject.getId());
        int time = maybeTime.orElse(-1);
        subjectTime.put(subject.getId(), time);
    }

    @Override
    public Subject create(String id, String name, String depto, Set<String> idCorrelativas, Set<Long> idProfesores, Set<Long> idCarreras, Integer creditos){
        return subjectDao.create(id, name, depto, idCorrelativas, idProfesores, idCarreras, creditos);
    }
}
