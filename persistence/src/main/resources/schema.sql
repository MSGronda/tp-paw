CREATE TABLE IF NOT EXISTS users
(
    id   SERIAL PRIMARY KEY,
    email    VARCHAR(100) NOT NULL UNIQUE,
    pass VARCHAR(100) NOT NULL,
    username VARCHAR(100) NOT NULL
);
