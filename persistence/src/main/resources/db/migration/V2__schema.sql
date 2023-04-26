
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
    day         INTEGER NOT NULL,
    startTime   TIME NOT NULL,
    endTime     TIME NOT NULL,
    class       VARCHAR(100) NOT NULL,
    building    VARCHAR(100) NOT NULL,
    mode        VARCHAR(100) NOT NULL,

    FOREIGN KEY (idSub, idClass) REFERENCES class(idsub,idClass)
);

ALTER TABLE users
ADD image BYTEA;

