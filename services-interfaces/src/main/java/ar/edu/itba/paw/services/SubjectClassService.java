package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.SubjectClass;

import java.util.List;
import java.util.Optional;

public interface SubjectClassService extends BaseService<String, SubjectClass>{
    List<SubjectClass> getBySubIdRaw(String s);
    List<SubjectClass> getBySubId(String s);

}
