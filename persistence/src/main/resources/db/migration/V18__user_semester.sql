CREATE TABLE IF NOT EXISTS userSemester
(
    idUser      INTEGER,
    idSub       VARCHAR(100),
    idClass     VARCHAR(100),
    FOREIGN KEY (idSub, idClass) REFERENCES class(idsub, idclass),
    FOREIGN KEY (idUser) REFERENCES users(id),
    PRIMARY KEY (idUser,idSub,idClass)
);