-- select all
SELECT * FROM fruits_db.fruit;
SELECT * FROM fruits_db.order;
SELECT * FROM fruits_db.users;
SELECT * FROM fruits_db.authorities;

-- Checker QUERIES
SELECT a.username, a.authority
From fruits_db.order o
inner join fruits_db.users u on o.username =  u.USERNAME
inner join fruits_db.authorities a on a.USERNAME = u.USERNAME
where o.id =1;

SELECT COUNT(*) col FROM dual WHERE 1=0;

SELECT u.username,u.password,a.authority FROM users u
INNER JOIN authorities a ON a.username = u.username
WHERE u.username="user";

-- delete non admin/user USERS-- 
DELETE FROM authorities where username NOT IN("admin","user");
DELETE FROM users where username NOT IN("admin","user");
SELECT * FROM users u
INNER JOIN authorities a ON a.username = u.username;


INSERT INTO users(username,password,enabled) values("admin","admin","Y");
INSERT INTO authorities(username,authority) values("abc","ROLE_USER");