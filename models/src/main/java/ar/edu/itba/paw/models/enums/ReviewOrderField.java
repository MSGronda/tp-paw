package ar.edu.itba.paw.models.enums;

public enum ReviewOrderField {
    DIFFICULTY("easy", "difficulty"),
    TIMEDEMANDING("timedemanding", "timeDemanding");

    private String tableColumn;
    private String fieldName;

    ReviewOrderField(String tableColumn, String fieldName) {
        this.tableColumn = tableColumn;
        this.fieldName = fieldName;
    }

    public static ReviewOrderField parse(String orderBy) {
        for(ReviewOrderField f : ReviewOrderField.values()){
            if(f.name().equalsIgnoreCase(orderBy)){
                return f;
            }
        }
        return DIFFICULTY;
    }

    public String getTableColumn() {
        return tableColumn;
    }

    public String getFieldName() {
        return fieldName;
    }
}
