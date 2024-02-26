package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.DegreeSemester;
import ar.edu.itba.paw.models.DegreeSubject;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.persistence.dao.DegreeDao;
import ar.edu.itba.paw.persistence.dao.SubjectDao;
import ar.edu.itba.paw.services.enums.OperationType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class DegreeServiceImplTest {
    private final Subject testSubject = Subject.builder().id("11.15").build();
    private final Subject testSubject2 = Subject.builder().id("11.16").build();
    private final Degree testDegree = Degree.builder().id(1).name("Ing. Informatica").totalCredits(240).build();

    @Mock
    private SubjectDao subjectDao;
    @InjectMocks
    private DegreeServiceImpl degreeService;

    // Tests nuevos
    @Test
    public void testAddSemesters() {
        final Map<Integer, List<String>> semesters = new HashMap<>();
        final int firstSemester = 1;
        final int secondSemester = 2;
        semesters.put(firstSemester, new ArrayList<>(Collections.singletonList(testSubject.getId())));
        semesters.put(secondSemester, new ArrayList<>(Collections.singletonList(testSubject2.getId())));
        when(subjectDao.findById(testSubject.getId())).thenReturn(Optional.of(testSubject));
        when(subjectDao.findById(testSubject2.getId())).thenReturn(Optional.of(testSubject2));

        degreeService.addSemestersToDegree(testDegree, semesters);

        assertEquals(2, testDegree.getDegreeSubjects().size());
        assertEquals(testDegree.getSemesters().get(firstSemester - 1).getSubjects(), new ArrayList<>(Collections.singletonList(testSubject)));
        assertEquals(testDegree.getSemesters().get(secondSemester - 1).getSubjects(), new ArrayList<>(Collections.singletonList(testSubject2)));
    }

    @Test
    public void testDeleteSemester() {
        final int semester = 1;
        testDegree.getDegreeSubjects().add(new DegreeSubject(testDegree, testSubject, semester));

        degreeService.deleteSemesterFromDegree(testDegree, semester);

        assertEquals(0, testDegree.getSemesters().size());
    }

    @Test
    public void testAddSubjectToSemester() {
        final int semester = 2;
        final String subjectId = testSubject.getId();
        when(subjectDao.findById(subjectId)).thenReturn(Optional.of(testSubject));

        degreeService.editDegreeSemester(testDegree, new AbstractMap.SimpleEntry<>(OperationType.Add, new AbstractMap.SimpleEntry<>(semester, subjectId)));

        final DegreeSemester expected = new DegreeSemester(2, new ArrayList<>(Collections.singletonList(testSubject)));
        assertEquals(testDegree.getSemesters(), new ArrayList<>(Collections.singletonList(expected)));
    }

    @Test
    public void testRemoveSubjectFromSemester() {
        final int semester = 1;
        final String subjectId = testSubject2.getId();
        testDegree.getDegreeSubjects().add(new DegreeSubject(testDegree, testSubject2, semester));
        when(subjectDao.findById(subjectId)).thenReturn(Optional.of(testSubject2));

        degreeService.editDegreeSemester(testDegree, new AbstractMap.SimpleEntry<>(OperationType.Remove, new AbstractMap.SimpleEntry<>(semester, subjectId)));

        assertTrue(testDegree.getSemesters().isEmpty());
    }

    // El resto son todos pasamanos.
}


