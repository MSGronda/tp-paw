ALTER TABLE reviews DROP CONSTRAINT reviews_idsub_fkey;
ALTER TABLE reviews ADD CONSTRAINT reviews_idsub_fkey
    FOREIGN KEY (idsub) REFERENCES subjects ON DELETE CASCADE;

ALTER TABLE reviewvote DROP CONSTRAINT reviewvote_idreview_fkey;
ALTER TABLE reviewvote ADD CONSTRAINT reviewvote_idreview_fkey
    FOREIGN KEY (idreview) REFERENCES reviews ON DELETE CASCADE;--BORRA

ALTER TABLE classloctime DROP CONSTRAINT classloctime_idsub_idclass_fkey;
ALTER TABLE classloctime ADD CONSTRAINT classloctime_idsub_idclass_fkey
    FOREIGN KEY (idsub) REFERENCES subjects ON DELETE CASCADE;--BORRA

ALTER TABLE classprofessors DROP CONSTRAINT classprofessors_idsub_idclass_fkey;
ALTER TABLE classprofessors ADD CONSTRAINT classprofessors_idsub_idclass_fkey
    FOREIGN KEY (idsub,idclass) REFERENCES class(idsub, idclass) ON DELETE CASCADE;--BORRA

ALTER TABLE prereqsubjects DROP CONSTRAINT prereqsubjects_idsub_fkey;
ALTER TABLE prereqsubjects ADD CONSTRAINT prereqsubjects_idsub_fkey
    FOREIGN KEY (idsub) REFERENCES subjects ON DELETE CASCADE;--BORRA

ALTER TABLE prereqsubjects DROP CONSTRAINT prereqsubjects_idprereq_fkey;
ALTER TABLE prereqsubjects ADD CONSTRAINT prereqsubjects_idprereq_fkey
    FOREIGN KEY (idprereq) REFERENCES subjects ON DELETE CASCADE;--BORRA

ALTER TABLE class DROP CONSTRAINT class_idsub_fkey;
ALTER TABLE class ADD CONSTRAINT class_idsub_fkey
    FOREIGN KEY (idsub) REFERENCES subjects ON DELETE CASCADE;--BORRA