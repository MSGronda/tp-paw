INSERT INTO userroles(roleid, userid)
SELECT 1, id userid FROM users WHERE id NOT IN (SELECT userid FROM userroles);
