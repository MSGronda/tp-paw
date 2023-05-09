CREATE TABLE images (
    id SERIAL PRIMARY KEY,
    image BYTEA
);

ALTER TABLE users ADD COLUMN image_id INTEGER REFERENCES images(id);


INSERT INTO images (image)
SELECT image FROM users;

UPDATE users SET image_id = images.id
FROM images
WHERE images.image = users.image;


ALTER TABLE users DROP COLUMN image;
