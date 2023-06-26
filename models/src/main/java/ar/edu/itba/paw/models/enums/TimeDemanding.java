package ar.edu.itba.paw.models.enums;

public enum TimeDemanding {
    LOW(0),
    MEDIUM(1),
    HIGH(2),
    NO_DATA(-1);

    private final int value;

    TimeDemanding(int value) {
        this.value = value;
    }

    public long getIntValue() {
        return value;
    }

    public static TimeDemanding parse(final Long timeDemanding){
        if(timeDemanding == null)
            return NO_DATA;

        for(TimeDemanding t : TimeDemanding.values()){
            if(t.value == timeDemanding){
                return t;
            }
        }
        throw new IllegalArgumentException(String.valueOf(timeDemanding));
    }
}
