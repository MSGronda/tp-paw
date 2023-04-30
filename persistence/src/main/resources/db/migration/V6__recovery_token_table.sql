CREATE TABLE IF NOT EXISTS recoveryToken(
    token VARCHAR(255) PRIMARY KEY,
    userid INT UNIQUE NOT NULL references users(id),
    created TIMESTAMP NOT NULL DEFAULT now()
);
