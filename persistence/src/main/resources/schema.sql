CREATE TABLE IF NOT EXISTS users
(
    id          SERIAL PRIMARY KEY,
    email       VARCHAR(100) NOT NULL UNIQUE,
    pass        VARCHAR(100) NOT NULL,
    username    VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS professors
(
    id          SERIAL PRIMARY KEY,
    profName    VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS subjects
(
    id          SERIAL PRIMARY KEY,
    subName     VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS degrees
(
    id          SERIAL PRIMARY KEY,
    degName     VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS review
(
    id          SERIAL PRIMARY KEY,
    idUser      INTEGER NOT NULL REFERENCES users,
    idSub       INTEGER NOT NULL REFERENCES subjects,
    score       INTEGER NOT NULL,
    revText     TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS professorsSubjects
(
    idProf  INTEGER NOT NULL REFERENCES professors,
    idSub   INTEGER NOT NULL REFERENCES subjects,

    PRIMARY KEY (idProf, idSub)
);

CREATE TABLE IF NOT EXISTS subjectsDegrees
(
    idSub   INTEGER NOT NULL REFERENCES subjects,
    idDeg   INTEGER NOT NULL REFERENCES degrees,

    PRIMARY KEY (idSub, idDeg)
);

CREATE TABLE IF NOT EXISTS prereqSubjects
(
    idSub       INTEGER NOT NULL REFERENCES subjects,
    idPrereq    INTEGER NOT NULL REFERENCES subjects,

    PRIMARY KEY (idSub, idPrereq)
);
