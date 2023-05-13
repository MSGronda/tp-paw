ALTER TABLE users ADD COLUMN confirmtoken VARCHAR(100) UNIQUE;
ALTER TABLE users ADD COLUMN confirmed BOOLEAN DEFAULT FALSE;

UPDATE users SET confirmed=true
WHERE confirmtoken IS NULL;

CREATE UNIQUE INDEX users_confirmtoken_idx ON users (confirmtoken);
