CREATE TABLE IF NOT EXISTS userSubjectProgress
(
    idUser          INTEGER references users,
    idSub           VARCHAR(100) REFERENCES subjects,
    subjectState    INTEGER NOT NULL,
    PRIMARY KEY (idUser,idSub)
);

CREATE TABLE IF NOT EXISTS subjectReviewStatistics
(
    idSub           VARCHAR(100) PRIMARY KEY  REFERENCES subjects(id),
    reviewCount     INTEGER NOT NULL,
    easyCount       INTEGER NOT NULL,
    mediumCount     INTEGER NOT NULL,
    hardCount       INTEGER NOT NULL,
    notTimeDemandingCount INTEGER NOT NULL,
    timeDemandingCount INTEGER NOT NULL
);

