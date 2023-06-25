ALTER TABLE usersemester DROP CONSTRAINT usersemester_idsub_idclass_fkey;
ALTER TABLE usersemester ADD CONSTRAINT usersemester_idsub_idclass_fkey
    FOREIGN KEY (idsub, idclass) REFERENCES class ON DELETE CASCADE;--BORRA

ALTER TABLE classloctime DROP CONSTRAINT classloctime_idsub_idclass_fkey;
ALTER TABLE classloctime ADD CONSTRAINT classloctime_idsub_idclass_fkey
    FOREIGN KEY (idsub, idclass) REFERENCES class(idsub, idclass) ON DELETE CASCADE;--BORRA