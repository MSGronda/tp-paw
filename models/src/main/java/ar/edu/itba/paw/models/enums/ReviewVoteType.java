package ar.edu.itba.paw.models.enums;

public enum ReviewVoteType {
    UPVOTE(1),
    DOWNVOTE(-1);

    private final int vote;

    ReviewVoteType(int vote){
        this.vote = vote;
    }

    public long getVote() {
        return vote;
    }

    public static ReviewVoteType parse(long vote){
        if(vote == 0) return null;

        for(ReviewVoteType v : ReviewVoteType.values()){
            if(v.vote == vote){
                return v;
            }
        }

        throw new IllegalArgumentException(String.valueOf(vote));
    }
}
