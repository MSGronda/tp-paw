package ar.edu.itba.paw.services.enums;

public enum OperationType{
    Add(1),
    Remove(2),
    Replace(3);

    OperationType(final int value){
        this.value = value;
    }

    final int value;

    public static OperationType parseType(int value) {
        for (OperationType type : OperationType.values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException();
    }

    public int getValue() {
        return value;
    }
}
