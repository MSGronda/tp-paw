package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
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

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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

    @Mock
    private User user;

    @Mock
    private Degree userDegree;

    @InjectMocks
    private SubjectServiceImpl subjectService;

    @Test
    public void testFindById() {
        when(subjectDao.findById(eq(ID))).thenReturn(Optional.of(
                Subject.builder()
                        .id(ID)
                        .name(NAME)
                        .department(DEPARTMENT)
                        .credits(CREDITS)
                        .build()
        ));

        final Optional<Subject> subject = subjectService.findById(ID);

        assertTrue(subject.isPresent());
        assertEquals(ID, subject.get().getId());
        assertEquals(NAME, subject.get().getName());
        assertEquals(DEPARTMENT, subject.get().getDepartment());
        assertEquals(CREDITS, subject.get().getCredits());
    }

    @Test
    public void testGetAll() {
        final Subject subject1 = Subject.builder()
                .id(ID)
                .name(NAME)
                .department(DEPARTMENT)
                .credits(CREDITS)
                .build();

        final Subject subject2 = Subject.builder()
                .id(ID2)
                .name(NAME2)
                .department(DEPARTMENT2)
                .credits(CREDITS2)
                .build();

        final List<Subject> resultList = Arrays.asList(subject1, subject2);

        when(subjectDao.getAll()).thenReturn(resultList);

        final List<Subject> subjects = subjectService.getAll();

        assertEquals(2, subjects.size());
        assertTrue(subjects.contains(subject1));
        assertTrue(subjects.contains(subject2));
    }

    @Test
    public void testFindByName() {
        final ArrayList<Subject> arrayList = new ArrayList<>();
        arrayList.add(
                Subject.builder()
                        .id(ID)
                        .name(NAME)
                        .department(DEPARTMENT)
                        .credits(CREDITS)
                        .build()
        );

        Mockito.lenient().when(userDegree.getId()).thenReturn(1L);
        Mockito.lenient().when(user.getDegree()).thenReturn(userDegree);

        when(subjectDao.search(eq(user), eq(NAME), eq(1))).thenReturn(arrayList);
        final List<Subject> subjects = subjectService.search(user, NAME, 1);

        assertEquals(1, subjects.size());
        assertEquals(ID, subjects.get(0).getId());
        assertEquals(NAME, subjects.get(0).getName());
        assertEquals(DEPARTMENT, subjects.get(0).getDepartment());
    }

    @Test
    public void testFindByNameFiltered() {
        final Map<SubjectFilterField, String> filterMap = new HashMap<>();
        filterMap.put(SubjectFilterField.DEPARTMENT, DEPARTMENT);

        final SubjectOrderField orderBy = SubjectOrderField.NAME;
        final OrderDir dir = OrderDir.ASCENDING;

        final Subject subject1 = Subject.builder()
                .id(ID)
                .name(NAME)
                .department(DEPARTMENT)
                .credits(CREDITS)
                .build();

        final List<Subject> resultList = Collections.singletonList(subject1);

        Mockito.lenient().when(userDegree.getId()).thenReturn(1L);
        Mockito.lenient().when(user.getDegree()).thenReturn(userDegree);

        when(subjectDao.search(eq(user), eq(NAME), eq(1), eq(filterMap), eq(orderBy), eq(dir))).thenReturn(resultList);
        when(subjectDao.getTotalPagesForSearch(eq(user), eq(NAME), eq(filterMap), eq(orderBy))).thenReturn(1);

        final List<Subject> actual = subjectService.search(user, NAME, 1, "name", "asc", null, DEPARTMENT, null, null);

        assertEquals(1, actual.size());
        assertEquals(ID, actual.get(0).getId());
        assertEquals(DEPARTMENT, actual.get(0).getDepartment());
        assertEquals(NAME, actual.get(0).getName());
        assertEquals(CREDITS, actual.get(0).getCredits());
    }

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
