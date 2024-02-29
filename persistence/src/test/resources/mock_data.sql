INSERT INTO degrees(id, degname, totalcredits)
VALUES (1, 'Ing. Informatica', 240);

INSERT INTO users(id, email, pass, username, image_id, confirmtoken, confirmed, locale, degreeid)
VALUES(1, 'invalid@mail.com', 'aaaa', 'Test User', null, 'aaaa', true, 'EN', 1);

INSERT INTO subjects(id, subname, department, credits)
VALUES ('11.15', 'Test Subject', 'Informatica', 6);

INSERT INTO subjects(id, subname, department, credits)
VALUES ('11.16', 'Test Subject 2', 'Informatica', 3);

INSERT INTO subjects(id, subname, department, credits)
VALUES ('11.18', 'Test Subject 4', 'Informatica', 5);


INSERT INTO subjectsdegrees(idsub, semester, iddeg)
VALUES ('11.15', 1, 1);

INSERT INTO subjectsdegrees(idsub, semester, iddeg)
VALUES ('11.16', 2, 1);

INSERT INTO usersubjectprogress(iduser, idsub, subjectstate)
VALUES (1, '11.15', 1);