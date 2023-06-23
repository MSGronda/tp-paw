ALTER TABLE reviews ADD CONSTRAINT subject_review_delete
    FOREIGN KEY (idsub) REFERENCES subjects ON DELETE CASCADE;--BORRA
ALTER TABLE reviewvote ADD CONSTRAINT review_review_votes_delete
    FOREIGN KEY (idreview) REFERENCES reviews ON DELETE CASCADE;--BORRA
ALTER TABLE classloctime ADD CONSTRAINT subject_classtime_delete
    FOREIGN KEY (idsub) REFERENCES subjects ON DELETE CASCADE;--BORRA
ALTER TABLE classprofessors ADD CONSTRAINT subject_professors_delete
    FOREIGN KEY (idsub) REFERENCES subjects ON DELETE CASCADE;--BORRA
ALTER TABLE prereqsubjects ADD CONSTRAINT review_prereq_delete
    FOREIGN KEY (idsub) REFERENCES subjects ON DELETE CASCADE;--BORRA
ALTER TABLE prereqsubjects ADD CONSTRAINT review_prereqdependency_delete
    FOREIGN KEY (idprereq) REFERENCES subjects ON DELETE CASCADE;--BORRA
ALTER TABLE class ADD CONSTRAINT subject_class_delete
    FOREIGN KEY (idsub) REFERENCES subjects ON DELETE CASCADE;--BORRA