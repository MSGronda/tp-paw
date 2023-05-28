package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Image;

public interface ImageDao extends ReadableDao<Long, Image> {
    Image create(final byte[] image);
    void update(final Image image, final byte[] newImage);
}
