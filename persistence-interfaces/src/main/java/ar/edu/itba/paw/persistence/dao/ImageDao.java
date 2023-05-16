package ar.edu.itba.paw.persistence.dao;

import ar.edu.itba.paw.models.Image;

public interface ImageDao extends RWDao<Long, Image> {
    void insert(final byte[] image);
    Long insertAndReturnKey(final byte[] image);

    void updateImage(long id, byte[] image);
}
