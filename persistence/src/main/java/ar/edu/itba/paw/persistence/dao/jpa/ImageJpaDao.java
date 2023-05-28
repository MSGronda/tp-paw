package ar.edu.itba.paw.persistence.dao.jpa;

import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.persistence.dao.ImageDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class ImageJpaDao implements ImageDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Image create(byte[] image) {
        final Image i = new Image(image);
        em.persist(i);
        return i;
    }

    @Override
    public void update(final Image image, byte[] newImage) {
        image.setImage(newImage);
    }

    @Override
    public Optional<Image> findById(Long id) {
        return Optional.ofNullable(em.find(Image.class, id));
    }

    @Override
    public List<Image> getAll() {
        return em.createQuery("from Image", Image.class)
                .getResultList();
    }
}
