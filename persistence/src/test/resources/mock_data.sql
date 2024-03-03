INSERT INTO degrees(id, degname, totalcredits)
VALUES (1, 'Ing. Informatica', 240);

INSERT INTO degrees(id, degname, totalcredits)
VALUES (2, 'Ing. Mecanica', 240);

INSERT INTO images(id, image)
VALUES(1, X'123456');

INSERT INTO users(id, email, pass, username, image_id, confirmtoken, confirmed, locale, degreeid)
VALUES(1, 'invalid@mail.com', 'aaaa', 'Test User', 1, 'aaaa', true, 'EN', 1);

INSERT INTO users(id, email, pass, username, image_id, confirmtoken, confirmed, locale, degreeid)
VALUES(2, 'invalid2@mail.com', 'bbbb', 'Test User 2', 1, 'bbbb', false, 'EN', 1);

INSERT INTO subjects(id, subname, department, credits)
VALUES ('11.15', 'Test Subject', 'Informatica', 6);

INSERT INTO subjects(id, subname, department, credits)
VALUES ('11.16', 'Test Subject 2', 'Informatica', 3);

INSERT INTO subjects(id, subname, department, credits)
VALUES ('11.18', 'Test Subject 4', 'Informatica', 5);

INSERT INTO class(idsub, idclass)
VALUES ('11.16', 'A');

INSERT INTO subjectsdegrees(idsub, semester, iddeg)
VALUES ('11.15', 1, 1);

INSERT INTO subjectsdegrees(idsub, semester, iddeg)
VALUES ('11.16', 2, 1);

INSERT INTO usersubjectprogress(iduser, idsub, subjectstate)
VALUES (1, '11.15', 1);

INSERT INTO reviews(id, iduser, idsub, easy, timedemanding, revtext, useranonymous)
VALUES (1, 1, '11.15', 0, 0, 'Very Easy', false);

INSERT INTO reviews(id, iduser, idsub, easy, timedemanding, revtext, useranonymous)
VALUES (2, 2, '11.16', 0, 1, 'Real Easy', false);

INSERT INTO professors (id, profname)
VALUES (1, 'Paula Daurat');

INSERT INTO roles (id, name)
VALUES (1, 'USER');

INSERT INTO roles (id, name)
VALUES (2, 'EDITOR');