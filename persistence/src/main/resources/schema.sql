CREATE TABLE IF NOT EXISTS users
(
    id          SERIAL PRIMARY KEY,
    email       VARCHAR(100) NOT NULL UNIQUE,
    pass        VARCHAR(100),
--     pass        VARCHAR(100) NOT NULL,

    username    VARCHAR(100)
--         username    VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS professors
(
    id          SERIAL PRIMARY KEY,
    profName    VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS subjects
(
    id          VARCHAR(100) PRIMARY KEY,
    subName     VARCHAR(100) NOT NULL,
    department  VARCHAR(100) NOT NULL,
    credits     INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS degrees
(
    id          SERIAL PRIMARY KEY,
    degName     VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS reviews
(
    id          SERIAL PRIMARY KEY,
    idUser      INTEGER NOT NULL REFERENCES users(id),
    userEmail   VARCHAR(100) NOT NULL references users(email),
    idSub       VARCHAR(100) NOT NULL REFERENCES subjects,
    score INTEGER,
--     score       INTEGER NOT NULL,
    easy BOOLEAN,
    timeDemanding BOOLEAN,
    revText     TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS professorsSubjects
(
    idProf  INTEGER NOT NULL REFERENCES professors,
    idSub   VARCHAR(100) NOT NULL REFERENCES subjects,

    PRIMARY KEY (idProf, idSub)
);

CREATE TABLE IF NOT EXISTS subjectsDegrees
(
    idSub   VARCHAR(100) NOT NULL REFERENCES subjects,
    semester INTEGER, -- puede ser null por las correlativas
    idDeg   INTEGER NOT NULL REFERENCES degrees,

    PRIMARY KEY (idSub, idDeg)
);

CREATE TABLE IF NOT EXISTS prereqSubjects
(
    idSub       VARCHAR(100) NOT NULL REFERENCES subjects,
    idPrereq    VARCHAR(100) NOT NULL REFERENCES subjects,

    PRIMARY KEY (idSub, idPrereq)
);
