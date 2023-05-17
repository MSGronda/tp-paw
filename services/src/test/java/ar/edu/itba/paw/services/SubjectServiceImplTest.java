package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.persistence.dao.SubjectDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SubjectServiceImplTest {

    private static final String ID = "31.08";

    private static final String NAME = "Sistemas de Representación";

    private static final String DEPARTMENT = "Ciencias Exactas y Naturales";

    private static final Integer CREDITS = 3;

    private static final String ID2 = "72.34";

    private static final String NAME2 = "Algebra";

    private static final String DEPARTMENT2 = "Matematica";

    private static final Integer CREDITS2 = 9;

    @Mock
    private SubjectDao subjectDao;

    @InjectMocks
    private SubjectServiceImpl subjectService;

    @Test
    public void testFindById(){
        when(subjectDao.findById(eq(ID))).thenReturn(Optional.of(
                Subject.builder()
                        .id(ID)
                        .name(NAME)
                        .department(DEPARTMENT)
                        .credits(CREDITS)
                        .build()
        ));

        Optional<Subject> subject = subjectService.findById(ID);

        Assert.assertTrue(subject.isPresent());
        Assert.assertEquals(ID, subject.get().getId());
        Assert.assertEquals(NAME, subject.get().getName());
        Assert.assertEquals(DEPARTMENT, subject.get().getDepartment());
        Assert.assertEquals(CREDITS, subject.get().getCredits());

    }

    @Test
    public void testFindByName(){
        ArrayList<Subject> arrayList = new ArrayList<>();
        arrayList.add(
                Subject.builder()
                        .id(ID)
                        .name(NAME)
                        .department(DEPARTMENT)
                        .credits(CREDITS)
                        .build()
        );
        when(subjectDao.getByName(eq(NAME))).thenReturn(arrayList);

        List<Subject> subjects = subjectService.getByName(NAME);

        Assert.assertNotEquals(0, subjects.size());
        Assert.assertTrue(subjects.stream().findFirst().isPresent());
        Assert.assertEquals(ID, subjects.stream().findFirst().get().getId());
        Assert.assertEquals(DEPARTMENT, subjects.stream().findFirst().get().getDepartment());
    }

    @Test
    public void testFindByNameFiltered(){
        Map<String, String> filterMap = new HashMap<>();
        List<Subject> subjectList = new ArrayList<>();
        subjectList.add(
                Subject.builder()
                        .id(ID)
                        .name(NAME)
                        .department(DEPARTMENT)
                        .credits(CREDITS)
                        .build()
        );
        when(subjectDao.getByNameFiltered(eq(NAME), eq(filterMap))).thenReturn(subjectList);

        //FindByNameFiltered should ignore a faulty param
        Map<String, String> filterMap2 = new HashMap<>();
        filterMap2.put("ob", "asdfdfa");
        filterMap2.put("dir", "adsfasdf");
        List<Subject> subjects = subjectService.getByNameFiltered(NAME, filterMap2);

        Assert.assertNotEquals(0, subjects.size());
        Assert.assertTrue(subjects.stream().findFirst().isPresent());
        Assert.assertEquals(ID, subjects.stream().findFirst().get().getId());
        Assert.assertEquals(DEPARTMENT, subjects.stream().findFirst().get().getDepartment());
    }
}
