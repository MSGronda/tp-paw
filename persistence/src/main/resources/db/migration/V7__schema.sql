CREATE TABLE IF NOT EXISTS roles
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS userRoles
(
    roleId INTEGER NOT NULL REFERENCES roles(id),
    userId INTEGER NOT NULL REFERENCES users(id),

    PRIMARY KEY (roleId, userId)
);

INSERT INTO roles (name) VALUES ('USER');
INSERT INTO roles (name) VALUES ('EDITOR');