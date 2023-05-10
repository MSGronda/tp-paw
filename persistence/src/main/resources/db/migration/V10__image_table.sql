CREATE TABLE images (
    id SERIAL PRIMARY KEY,
    image BYTEA
);

ALTER TABLE users ADD COLUMN image_id INTEGER REFERENCES images(id);


INSERT INTO images (image)
SELECT image FROM users;

UPDATE users u SET image_id = (
    SELECT i.id
    FROM images i
    WHERE u.image = i.image
);


ALTER TABLE users DROP COLUMN image;
