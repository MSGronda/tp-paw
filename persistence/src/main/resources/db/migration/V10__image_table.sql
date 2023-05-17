CREATE TABLE images (
    id SERIAL PRIMARY KEY,
    image BYTEA
);

ALTER TABLE users ADD COLUMN image_id INTEGER REFERENCES images(id);


INSERT INTO images (id,image)
SELECT id,image FROM users;

UPDATE users u SET image_id = u.id;

ALTER TABLE users DROP COLUMN image;
