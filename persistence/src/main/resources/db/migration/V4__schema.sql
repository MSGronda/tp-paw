CREATE TABLE IF NOT EXISTS reviewVote
(
    idUser          INTEGER references users,
    idReview        INTEGER REFERENCES reviews,
    vote            INTEGER NOT NULL,
    PRIMARY KEY (idUser,idReview)
);