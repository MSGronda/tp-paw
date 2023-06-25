package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Degree;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.persistence.dao.DegreeDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DegreeServiceImplTest {
    private static final String NAME = "name";
    private static final String NAME2 = "name2";

    @Mock
    private DegreeDao degreeDao;

    @InjectMocks
    private DegreeServiceImpl degreeService;

    @Test
    public void findById() {
        final Degree degree = new Degree(NAME);

        when(degreeDao.findById(1)).thenReturn(Optional.of(degree));

        final Optional<Degree> actual = degreeService.findById(1);

        assertTrue(actual.isPresent());
        assertEquals(NAME, actual.get().getName());
    }

    @Test
    public void findByName() {
        final Degree degree = new Degree(NAME);

        when(degreeDao.findByName(NAME)).thenReturn(Optional.of(degree));

        final Optional<Degree> actual = degreeService.findByName(NAME);

        assertTrue(actual.isPresent());
        assertEquals(NAME, actual.get().getName());
    }

    @Test
    public void getAll() {
        final Degree degree1 = new Degree(NAME);
        final Degree degree2 = new Degree(NAME2);

        when(degreeDao.getAll()).thenReturn(Arrays.asList(degree1, degree2));

        final List<Degree> actual = degreeService.getAll();

        assertEquals(2, actual.size());
        assertEquals(NAME, actual.get(0).getName());
        assertEquals(NAME2, actual.get(1).getName());
    }

    @Test
    public void findSubjectYearForDegree() {
        final Degree degree = new Degree(NAME);
        final Subject subject = Subject.builder()
                .id("00.01")
                .name("Subject")
                .build();
        subject.getDegrees().add(degree);

        when(degreeDao.findSubjectSemesterForDegree(eq(subject), eq(degree))).thenReturn(OptionalInt.of(2));

        final OptionalInt actual = degreeService.findSubjectYearForParentDegree(subject, null);

        assertTrue(actual.isPresent());
        assertEquals(1, actual.getAsInt());
    }
}
