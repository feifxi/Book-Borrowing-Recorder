CREATE DATABASE g25db;
USE g25db;

CREATE TABLE books (
	id VARCHAR(5),
    title VARCHAR(30),
    author VARCHAR(30),
    description VARCHAR(85),
    availableCopies INT
);

CREATE TABLE customers (
	id VARCHAR(5),
    name VARCHAR(30),
    email VARCHAR(30)
);

CREATE TABLE borrowrecords (
	id VARCHAR(6),	
	cid VARCHAR(5),
    bid VARCHAR(5),
    borrowDate DATE,
    returnDate DATE
);

SHOW TABLES;

/*------------------------------------------------------------------------------------------*/

DROP TABLE books;
DROP TABLE customers;
DROP TABLE borrowrecords;

SELECT * FROM books;
SELECT * FROM customers;
SELECT * FROM borrowrecords;