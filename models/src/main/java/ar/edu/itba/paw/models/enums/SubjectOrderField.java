package ar.edu.itba.paw.models.enums;

public enum SubjectOrderField {
    ID("id", "id"),
    NAME("subname", "subname"),
    CREDITS("credits", "credits"),
    DIFFICULTY("difficulty", "difficulty"),
    TIME("timeDemanding", "timeDemanding");

    private final String fieldName;
    private final String tableColumn;

    SubjectOrderField(String fieldName, String tableColumn) {
        this.fieldName = fieldName;
        this.tableColumn = tableColumn;
    }

    public static SubjectOrderField parse(String orderBy) {
        for (SubjectOrderField field : SubjectOrderField.values()) {
            if (field.name().equalsIgnoreCase(orderBy)) {
                return field;
            }
        }
        return null;
    }

    public String getTableColumn() {
        return tableColumn;
    }

    public String getFieldName() {
        return fieldName;
    }
}
