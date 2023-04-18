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
    easy        INTEGER, -- 0-easy, 1-normal, 2-hard
    timeDemanding INTEGER, -- 0-no 1-yes
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

CREATE TABLE IF NOT EXISTS class
(
    idSub       VARCHAR(100) NOT NULL REFERENCES subjects,
    idClass     VARCHAR(100) NOT NULL,

    PRIMARY KEY (idSub,idClass)
);

CREATE TABLE IF NOT EXISTS classProfessors
(
    idSub       VARCHAR(100) NOT NULL,
    idClass     VARCHAR(100) NOT NULL,
    idProf      INTEGER NOT NULL REFERENCES professors,

    FOREIGN KEY (idSub, idClass) REFERENCES class(idsub,idClass),
    PRIMARY KEY (idSub,idClass, idProf)
);

CREATE TABLE IF NOT EXISTS classLocTime
(
    idLocTime   SERIAL PRIMARY KEY,
    idSub       VARCHAR(100) NOT NULL,
    idClass     VARCHAR(100) NOT NULL,
    day         VARCHAR(20) NOT NULL,
    startTime   TIME NOT NULL,
    endTime     TIME NOT NULL,
    class       VARCHAR(20) NOT NULL,
    building    VARCHAR(30) NOT NULL,
    mode        VARCHAR(30) NOT NULL,

    FOREIGN KEY (idSub, idClass) REFERENCES class(idsub,idClass)
);
