ALTER TABLE usersemester ADD id SERIAL;
AlTER TABLE usersemester DROP CONSTRAINT usersemester_pkey;
ALTER TABLE usersemester ADD CONSTRAINT usersemester_pkey PRIMARY KEY (id);