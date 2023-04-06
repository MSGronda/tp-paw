CREATE TABLE IF NOT EXISTS users
(
    id   SERIAL PRIMARY KEY,
    email    VARCHAR(100) NOT NULL UNIQUE,
    pass VARCHAR(100) NOT NULL,
    username VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS professor
(
    id SERIAL PRIMARY KEY,
    profName VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS professorSubjects
(
    idProf SERIAL NOT NULL,
    idSub SERIAL NOT NULL,

    PRIMARY KEY (idProf, idSub),
    FOREIGN KEY (idProf) REFERENCES professor
);