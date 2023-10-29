package ar.edu.itba.paw.services.enums;

public enum UserSemesterEditType {
    ADD_SUBJECT(1),
    REMOVE_SUBJECT(2),
    FINISH_SEMESTER(3);
    private final int value;

    UserSemesterEditType(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static UserSemesterEditType parse(final Long type){
        for(UserSemesterEditType editType : UserSemesterEditType.values()){
            if(editType.value == type){
                return editType;
            }
        }
        throw new IllegalArgumentException(String.valueOf(type));
    }
}