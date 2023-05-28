package ar.edu.itba.paw.models.enums;

public enum SubjectProgress {
    PENDING(0),
    DONE(1);

    private final int value;

    SubjectProgress(int value){
        this.value = value;
    }
    public long getValue() {
        return value;
    }
    public static SubjectProgress parse(long progress){
        for(SubjectProgress p : SubjectProgress.values()){
            if(p.value == progress){
                return p;
            }
        }
        throw new IllegalArgumentException(String.valueOf(progress));
    }
}
