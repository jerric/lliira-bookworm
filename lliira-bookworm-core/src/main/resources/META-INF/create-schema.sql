/*
  Core schema
*/
CREATE TABLE IF NOT EXISTS users (
    id  INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    registered_time TIMESTAMP NOT NULL,
    is_active BOOLEAN NOT NULL,
    description TEXT
);

CREATE TABLE IF NOT EXISTS books (
	id	INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
    sorted_name VARCHAR(255) NOT NULL,
	publish_date DATE,
	description TEXT
);

CREATE TABLE IF NOT EXISTS authors (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	description TEXT
);

CREATE TABLE IF NOT EXISTS categories (
	id	INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(100) NOT NULL,
	parent_id INT,
	sibling_index INT NOT NULL,
	description TEXT,
	UNIQUE (parent_id, sibling_index)
);

CREATE TABLE IF NOT EXISTS book_authors (
	id	INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	book_id INT NOT NULL REFERENCES books (id),
	author_id INT NOT NULL REFERENCES authors (id),
	UNIQUE (book_id, author_id)
);

CREATE TABLE IF NOT EXISTS category_books (
	id	INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	category_id INT NOT NULL REFERENCES categories (id),
	book_id INT NOT NULL REFERENCES books (id),
	sibling_index DECIMAL(5,2) NOT NULL,
	UNIQUE (category_id, book_id),
	UNIQUE (category_id, sibling_index)
);


/*
  Calibre Schema
*/
CREATE TABLE IF NOT EXISTS calibre_libraries (
	id	INT NOT NULL AUTO_INCREMENT,
    uuid VARCHAR(50) NOT NULL,
    path VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (path)
);

CREATE TABLE IF NOT EXISTS calibre_series (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	calibre_id INT NOT NULL,
    library_id INT NOT NULL REFERENCES calibre_libraries (id),
    category_id INT REFERENCES categories (id),
    UNIQUE (library_id, calibre_id)
);

CREATE TABLE IF NOT EXISTS calibre_books (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    calibre_id INT NOT NULL,
    library_id INT NOT NULL REFERENCES calibre_libraries (id),
    book_id INT NOT NULL REFERENCES books (id),
    UNIQUE (library_id, calibre_id)
);

CREATE TABLE IF NOT EXISTS calibre_authors (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    calibre_id INT NOT NULL,
    library_id INT NOT NULL REFERENCES calibre_libraries (id),
    author_id INT NOT NULL REFERENCES authors (id),
    UNIQUE (library_id, calibre_id)
);


/*
  Goodreads schema
*/
CREATE TABLE IF NOT EXISTS goodreads_books (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    goodread_id INT NOT NULL UNIQUE,
    book_id INT NOT NULL REFERENCES books (id),
    rating DECIMAL(3,2) NOT NULL,
    rating_count INT NOT NULL
);

CREATE TABLE IF NOT EXISTS goodreads_authors (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    goodread_id INT NOT NULL UNIQUE,
    author_id INT NOT NULL REFERENCES authors (id)
);


/*
  create indexes
*/
CREATE INDEX users_ix01 ON users (email, password);

CREATE INDEX books_ix01 ON books (name);
CREATE INDEX authors_ix01 ON authors (name);
CREATE INDEX book_authors_ix01 ON book_authors (author_id, book_id);
CREATE INDEX category_books_ix01 ON category_books (book_id, category_id);

CREATE INDEX calibre_series_ix01 ON calibre_series (category_id);
CREATE INDEX calibre_books_ix01 ON calibre_books (book_id);
CREATE INDEX calibre_authors_ix01 ON calibre_authors (author_id);

CREATE INDEX goodreads_books_ix01 ON goodreads_books (rating, rating_count);
