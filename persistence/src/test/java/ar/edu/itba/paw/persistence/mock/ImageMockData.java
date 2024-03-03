package ar.edu.itba.paw.persistence.mock;

import ar.edu.itba.paw.models.Image;

public class ImageMockData {
    public static final long IMG1_ID = 1;
    public static final byte[] IMG1_DATA = {18,52,86};

    public static Image getImage1() {
        return new Image(IMG1_DATA);
    }
}
