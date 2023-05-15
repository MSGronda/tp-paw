ALTER TABLE usersubjectprogress
    ADD COLUMN notiftime TIMESTAMP;

ALTER TABLE users
    ADD COLUMN locale VARCHAR(32);
