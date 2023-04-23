package ar.edu.itba.paw.services;

import ar.edu.itba.paw.persistence.ImageDao;
import org.springframework.util.ResourceUtils;
import org.apache.commons.io.IOUtils;
import java.io.*;
import java.util.Objects;
import java.util.Optional;

public class ImageServiceImpl implements ImageService{
    private final ImageDao imageDao;

    public ImageServiceImpl(final ImageDao imageDao) {
        this.imageDao = imageDao;
    }

    @Override
    public void updateProfilePicture(long id, byte[] image) {
        if (image.length > 0)
            imageDao.updateProfilePicture(id, image);
    }

    @Override
    public byte[] getProfilePicture(long id) throws IOException {
        Optional<byte[]> optionalBytes = imageDao.getProfilePicture(id);
        if(!optionalBytes.isPresent()) {
            File file = ResourceUtils.getFile("classpath:images/default_user.png");
            InputStream fileStream = new FileInputStream(file);
            return IOUtils.toByteArray(Objects.requireNonNull(fileStream));
        }
        return optionalBytes.get();
    }
}
