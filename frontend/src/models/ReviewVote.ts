export interface ReviewVote {
    reviewId: number,
    userId: number,
    vote: number
}

export enum VoteValue {
    UpVote = 1,
    DownVote = -1,
}
