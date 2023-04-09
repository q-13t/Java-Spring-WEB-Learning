-- select all
SELECT * FROM fruits_db.fruit;
SELECT * FROM fruits_db.order;
SELECT * FROM fruits_db.users;
SELECT * FROM fruits_db.authorities;

SELECT * FROM users u
INNER JOIN authorities a ON a.username = u.username;

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


SET GLOBAL FOREIGN_KEY_CHECKS=1;
update authorities set username = "Nyan" Where username = "quack";

update users u
Inner JOIN authorities a ON a.username = u.username
set 
	u.username = "Nyan",
	a.username = "Nyan",
    u.password = "abc",
    a.authority = "ROLE_ADMIN"
where u.username =  "quack";

SET FOREIGN_KEY_CHECKS=0;

update fruit set image = "Kiwi" where id = 8;

SELECT * from fruit where ID in (2,5,8);

select password from users where USERNAME = "Nyan";

INSERT INTO fruits_db.order(id,fruit_id,amount,username,address) VALUE(?,?,?,?,?);