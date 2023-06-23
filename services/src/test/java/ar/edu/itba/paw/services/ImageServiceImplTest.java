package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.persistence.dao.ImageDao;
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
public class ImageServiceImplTest {

    private final static byte[] DATA = {0, 1, 2, 3};
    private final static byte[] DATA2 = {3, 2, 1, 0};

    @Mock
    private ImageDao imageDao;

    @InjectMocks
    private ImageServiceImpl imageService;

    @Test
    public void testFindById() {
        final Image image = new Image(DATA);

        when(imageDao.findById(1)).thenReturn(Optional.of(image));

        final Optional<Image> actual = imageService.findById(1);

        assertTrue(actual.isPresent());
        assertEquals(DATA, actual.get().getImage());
    }

    @Test
    public void testGetAll() {
        final Image image1 = new Image(DATA);
        final Image image2 = new Image(DATA2);

        when(imageDao.getAll()).thenReturn(Arrays.asList(image1, image2));

        final List<Image> actual = imageService.getAll();

        assertEquals(2, actual.size());
        assertEquals(DATA, actual.get(0).getImage());
        assertEquals(DATA2, actual.get(1).getImage());
    }
}
