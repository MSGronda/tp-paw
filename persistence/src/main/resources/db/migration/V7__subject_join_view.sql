CREATE MATERIALIZED VIEW joinedsubjects AS
    SELECT id,subname,department,credits,idprereq,idprof,semester,iddeg  FROM subjects s
        LEFT JOIN prereqsubjects prereq ON s.id = prereq.idsub
        LEFT JOIN professorssubjects prof ON s.id = prof.idsub
        LEFT JOIN subjectsdegrees deg ON s.id = deg.idsub;

REFRESH MATERIALIZED VIEW joinedsubjects;
