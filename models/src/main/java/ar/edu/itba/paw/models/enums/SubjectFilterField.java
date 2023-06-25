package ar.edu.itba.paw.models.enums;

public enum SubjectFilterField {
    DEPARTMENT("department"),
    CREDITS("credits"),
    DIFFICULTY("difficulty"),
    TIME("timeDemanding");

    private final String column;

    SubjectFilterField(String column) {
        this.column = column;
    }

    public String getColumn() {
        return column;
    }

    public static SubjectFilterField fromString(final String string) {
        for (final SubjectFilterField field : SubjectFilterField.values()) {
            if (field.name().equalsIgnoreCase(string)) {
                return field;
            }
        }
        return null;
    }
}
