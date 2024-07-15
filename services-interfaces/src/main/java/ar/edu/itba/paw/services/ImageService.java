package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Image;

import java.util.List;
import java.util.Optional;

public interface ImageService {
    Optional<Image> findById(final long id);
    List<Image> getAll();

    Image createImage(final byte[] picture);
}
