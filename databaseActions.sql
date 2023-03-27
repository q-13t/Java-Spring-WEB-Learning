SELECT * FROM fruits_db.fruit;
SELECT * FROM fruits_db.order;
SELECT * FROM fruits_db.users;
SELECT * FROM fruits_db.authorities;

SELECT a.username, a.authority
From fruits_db.order o
inner join fruits_db.users u on o.username =  u.USERNAME
inner join fruits_db.authorities a on a.USERNAME = u.USERNAME
where o.id =1;