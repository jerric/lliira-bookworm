DROP TABLE category_books IF EXISTS;
DROP TABLE book_authors IF EXISTS;
DROP TABLE books IF EXISTS;
DROP TABLE categories IF EXISTS;
DROP TABLE authors IF EXISTS;


CREATE TABLE books (
	id	INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(200) NOT NULL,
	publish_date DATE,
	description TEXT,
	PRIMARY KEY (id)
);

CREATE INDEX books_ix01 ON books (name);

CREATE TABLE authors (
	id INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(200) NOT NULL,
	description TEXT,
	PRIMARY KEY (id)
);

CREATE INDEX authors_ix01 ON authors (name);

CREATE TABLE categories (
	id	INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(50) NOT NULL,
	parent_id INT,
	description TEXT,
	PRIMARY KEY (id),
	UNIQUE (parent_id, name)
);

CREATE TABLE book_authors (
	id	INT NOT NULL AUTO_INCREMENT,
	book_id INT NOT NULL,
	author_id INT NOT NULL,
	PRIMARY KEY (id),
	UNIQUE (book_id, author_id),
	FOREIGN KEY (book_id) REFERENCES books (id),
	FOREIGN KEY (author_id) REFERENCES authors (id)
);

CREATE INDEX book_authors_ix01 ON book_authors (author_id);

CREATE TABLE category_books (
	id	INT NOT NULL AUTO_INCREMENT,
	category_id INT NOT NULL,
	book_id INT NOT NULL,
	PRIMARY KEY (id),
	UNIQUE (category_id, book_id),
	FOREIGN KEY (category_id) REFERENCES categories (id),
	FOREIGN KEY (book_id) REFERENCES books (id)
);

CREATE INDEX category_books_ix01 ON category_books (book_id);
