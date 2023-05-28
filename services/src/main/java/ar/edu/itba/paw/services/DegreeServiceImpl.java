package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.DegreeYear;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.persistence.dao.DegreeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DegreeServiceImpl implements DegreeService {
    private final DegreeDao degreeDao;

    @Autowired
    public DegreeServiceImpl(final DegreeDao degreeDao) {
        this.degreeDao = degreeDao;
    }

    @Override
    public Optional<Degree> findById(final Long id) {
        return degreeDao.findById(id);
    }
    @Override
    public Optional<Degree> findByName(final String name) {
        return degreeDao.findByName(name);
    }

    public int findSubjectYearForDegree(final Subject subject, final Degree degree) {
        Optional<Integer> semester = degreeDao.findSubjectSemesterForDegree(subject, degree);
        int sem = semester.orElse(-1);
        return (int) Math.ceil(sem / 2.0);
    }

    public Map<Integer, Integer> getTotalCreditsPerYear(final Degree degree){
        Map<Integer, Integer> resp = new HashMap<>();
        for(DegreeYear year : degree.getYears()){
            int total = 0;
            for(Subject s : year.getSubjects()){
                total += s.getCredits();
            }
            resp.put(year.getNumber(), total);
        }
        return resp;
    }

    @Override
    public List<Degree> getAll() {
        return degreeDao.getAll();
    }
}
