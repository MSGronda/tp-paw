package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Image;

import java.util.List;
import java.util.Optional;

public interface ImageDao {
    List<Image> getAll();
    Optional<Image> findById(final long id);

    Image create(final byte[] image);
    Image update(final Image image, final byte[] newImage);
}
