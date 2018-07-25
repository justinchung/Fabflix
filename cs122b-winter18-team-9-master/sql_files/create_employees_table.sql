use moviedb;

DROP TABLE IF EXISTS employees;
CREATE TABLE employees (
	email varchar(50) NOT NULL,
	password varchar(20) NOT NULL,
	fullname varchar(100),
	PRIMARY KEY(email) 
);
