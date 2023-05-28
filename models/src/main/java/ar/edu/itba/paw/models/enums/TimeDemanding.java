package ar.edu.itba.paw.models.enums;

public enum TimeDemanding {
    NO_DATA(-1),
    LOW(0),
    MEDIUM(1),
    HIGH(2);

    private final int value;

    TimeDemanding(int value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public static TimeDemanding parse(long timeDemanding){
        for(TimeDemanding t : TimeDemanding.values()){
            if(t.value == timeDemanding){
                return t;
            }
        }
        throw new IllegalArgumentException(String.valueOf(timeDemanding));
    }
}
