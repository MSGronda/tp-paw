package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.exceptions.InvalidImageSizeException;
import ar.edu.itba.paw.persistence.dao.ImageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {
    private static final int MAX_IMAGE_SIZE = 1024 * 1024 * 5;

    private final ImageDao imageDao;

    @Autowired
    public ImageServiceImpl(final ImageDao imageDao) {
        this.imageDao = imageDao;
    }

    @Override
    public Optional<Image> findById(final long id) {
        return imageDao.findById(id);
    }

    @Override
    public List<Image> getAll() {
        return imageDao.getAll();
    }

    @Transactional
    @Override
    public Image createImage(final byte[] picture){
        if (picture.length > MAX_IMAGE_SIZE || picture.length == 0) {
            throw new InvalidImageSizeException();
        }

        return imageDao.create(picture);
    }
}
