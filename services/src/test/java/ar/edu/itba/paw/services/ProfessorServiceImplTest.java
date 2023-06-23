package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.persistence.dao.ProfessorDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProfessorServiceImplTest {

    private static final String NAME = "name";
    private static final String NAME2 = "name2";

    @Mock
    private ProfessorDao professorDao;

    @InjectMocks
    private ProfessorServiceImpl professorService;

    @Test
    public void testFindById() {
        final Professor professor1 = new Professor(NAME);
        final Professor professor2 = new Professor(NAME2);

        when(professorDao.findById(1)).thenReturn(Optional.of(professor1));
        when(professorDao.findById(2)).thenReturn(Optional.of(professor2));

        final Optional<Professor> actual1 = professorService.findById(1);
        final Optional<Professor> actual2 = professorService.findById(2);

        assertTrue(actual1.isPresent());
        assertTrue(actual2.isPresent());
        assertEquals(NAME, actual1.get().getName());
        assertEquals(NAME2, actual2.get().getName());
    }

    @Test
    public void testGetAll() {
        final Professor professor1 = new Professor(NAME);
        final Professor professor2 = new Professor(NAME2);

        when(professorDao.getAll()).thenReturn(Arrays.asList(professor1, professor2));

        final List<Professor> actual = professorService.getAll();

        assertEquals(2, actual.size());
        assertEquals(NAME, actual.get(0).getName());
        assertEquals(NAME2, actual.get(1).getName());
    }
}
