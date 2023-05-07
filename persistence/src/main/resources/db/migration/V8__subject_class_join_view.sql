CREATE MATERIALIZED VIEW joinedSubjectClass AS
SELECT id,subname,department,credits, c.idclass, cl.day, cl.starttime, cl.endtime, cl.class, cl.building, cl.mode FROM subjects s
      LEFT JOIN class c ON s.id = c.idsub
      LEFT JOIN classloctime cl ON c.idclass = cl.idclass AND cl.idsub = s.id;

REFRESH MATERIALIZED VIEW joinedSubjectClass;


