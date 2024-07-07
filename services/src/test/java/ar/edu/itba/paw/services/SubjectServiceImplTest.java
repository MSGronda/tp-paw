package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.OrderDir;
import ar.edu.itba.paw.models.enums.SubjectFilterField;
import ar.edu.itba.paw.models.enums.SubjectOrderField;
import ar.edu.itba.paw.persistence.dao.SubjectDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalTime;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SubjectServiceImplTest {
    private final Subject testSubject = Subject.builder().id("31.08").name("Sistemas de Representación").department("Ciencias Exactas y Naturales").credits(3).build();
    private final Subject testSubject2 = Subject.builder().id("72.34").name("Algebra").department("Matematica").credits(9).build();
    private final Degree testDegree = Degree.builder().id(1).build();
    private final DegreeSubject testDegreeSubject = new DegreeSubject(testDegree, testSubject, 1);

    private static final long userId = 1;
    private final User testUser = User.builder().id(userId).build();

    // Pagination and order
    private static final int defaultPage = 1;
    private static final String defaultOrderBy = "name";
    private static final String defaultDir = "asc";

    @Mock
    private SubjectDao subjectDao;
    @Mock
    private DegreeService degreeService;
    @Mock
    private ProfessorService professorService;
    @InjectMocks
    private SubjectServiceImpl subjectService;


    @Test
    public void testSubjectCreation() {
        final List<Long> degreeIds = new ArrayList<>(Collections.singletonList(1L));
        final List<Integer> semesterIds = new ArrayList<>(Collections.singletonList(1));
        final List<String> requirementIds = new ArrayList<>(Collections.singletonList(testSubject2.getId()));
        final List<String> professors = new ArrayList<>();
        when(subjectDao.create(testSubject)).thenReturn(testSubject);

        final Subject createdSubject = subjectService.create(Subject.builderFrom(testSubject), degreeIds, semesterIds, requirementIds, professors);

        assertEquals(testSubject, createdSubject);
    }

    @Test
    public void testCreateSubjectWithExistingId() {
        final List<Long> degreeIds = new ArrayList<>(Collections.singletonList(1L));
        final List<Integer> semesterIds = new ArrayList<>(Collections.singletonList(1));
        final List<String> requirementIds = new ArrayList<>(Collections.singletonList(testSubject2.getId()));
        final List<String> professors = new ArrayList<>();
        when(subjectDao.create(testSubject)).thenReturn(testSubject);

        final Subject createdSubject = subjectService.create(Subject.builderFrom(testSubject), degreeIds, semesterIds, requirementIds, professors);

        assertEquals(testSubject, createdSubject);
    }

    @Test
    public void testEditSubject() {
        final String name = "Diseño";
        final String department = "Departamento de Diseño";
        final int credits = 6;
        final List<String> requirementIds = new ArrayList<>(Collections.singletonList(testSubject2.getId()));
        final Set<Subject> prereqs = new HashSet<>(Collections.singletonList(testSubject2));

        when(subjectService.findById(testSubject2.getId())).thenReturn(Optional.of(testSubject2));
        when(subjectDao.editSubject(testSubject, name, department, credits, prereqs))
                .thenReturn(Subject.builder().id(testSubject.getId()).name(name).department(department).credits(credits).prerequisites(prereqs).build());

        final Subject editedSubject = subjectService.editSubject(testSubject, name, department, credits, requirementIds);

        assertEquals(testSubject.getId(), editedSubject.getId());
        assertEquals(name, editedSubject.getName());
        assertEquals(department, editedSubject.getDepartment());
        assertEquals(credits, editedSubject.getCredits().intValue());
    }

    // Tests viejos
    @Test
    public void testUpdateUnreviewedNotificationTime() {
        subjectService.updateUnreviewedNotificationTime();
        verify(subjectDao, times(1)).updateUnreviewedNotificationTime();
    }

    @Test
    public void testGetAllUserUnreviewedNotificationSubjects() {
        when(subjectDao.getAllUserUnreviewedNotificationSubjects()).thenReturn(new HashMap<>());
        final Map<?,?> map = subjectService.getAllUserUnreviewedNotificationSubjects();
        assertEquals(0, map.size());
    }
}
