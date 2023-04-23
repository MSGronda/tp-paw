package ar.edu.itba.paw.services;

import java.io.IOException;

public interface ImageService {
    void updateProfilePicture(long id, byte[] image);

    byte[] getProfilePicture(long id) throws IOException;
}
