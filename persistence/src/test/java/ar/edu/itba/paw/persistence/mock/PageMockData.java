package ar.edu.itba.paw.persistence.mock;

import ar.edu.itba.paw.models.enums.OrderDir;
import ar.edu.itba.paw.models.enums.ReviewOrderField;
import ar.edu.itba.paw.models.enums.SubjectOrderField;

public class PageMockData {
    public static final int DEFAULT_PAGE = 1;
    public static final OrderDir DEFAULT_DIR = OrderDir.ASCENDING;
    public static final ReviewOrderField DEFAULT_ORDER_REVIEW = ReviewOrderField.DIFFICULTY;

    public static final SubjectOrderField DEFAULT_ORDER_SUBJECT = SubjectOrderField.ID;
}
