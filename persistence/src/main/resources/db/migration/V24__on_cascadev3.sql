ALTER TABLE usersubjectprogress DROP CONSTRAINT usersubjectprogress_idsub_fkey;
ALTER TABLE usersubjectprogress ADD CONSTRAINT usersubjectprogress_idsub_fkey
    FOREIGN KEY (idsub) REFERENCES subjects(id) ON DELETE CASCADE;--BORRA
