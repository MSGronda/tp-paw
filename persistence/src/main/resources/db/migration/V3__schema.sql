CREATE TABLE IF NOT EXISTS userSubjectProgress
(
    idUser          INTEGER references users,
    idSub           VARCHAR(100) REFERENCES subjects,
    subjectState    INTEGER NOT NULL,
    PRIMARY KEY (idUser,idSub)
);
