package ar.edu.itba.paw.models.enums;

public enum Difficulty {
    NO_DATA(-1),
    EASY(0),
    MEDIUM(1),
    HARD(2);

    private final int value;

    Difficulty(int value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public static Difficulty parse(final Long difficulty){
        if(difficulty == null)
            return NO_DATA;

        for(Difficulty d : Difficulty.values()){
            if(d.value == difficulty){
                return d;
            }
        }
        throw new IllegalArgumentException(String.valueOf(difficulty));
    }
}
