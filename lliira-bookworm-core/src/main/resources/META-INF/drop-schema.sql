DROP INDEX users_ix01 ON users;

DROP INDEX books_ix01 ON books;
DROP INDEX authors_ix01 ON authors;
DROP INDEX book_authors_ix01 ON book_authors;
DROP INDEX category_books_ix01 ON category_books;

DROP INDEX calibre_series_ix01 ON calibre_series;
DROP INDEX calibre_books_ix01 ON calibre_books;
DROP INDEX calibre_authors_ix01 ON calibre_authors;

DROP INDEX goodreads_books_ix01 ON goodreads_books;


DROP TABLE IF EXISTS goodreads_authors;
DROP TABLE IF EXISTS goodreads_books;

DROP TABLE IF EXISTS calibre_authors;
DROP TABLE IF EXISTS calibre_books;
DROP TABLE IF EXISTS calibre_series;
DROP TABLE IF EXISTS calibre_libraries;

DROP TABLE IF EXISTS category_books;
DROP TABLE IF EXISTS book_authors;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS authors;

DROP TABLE IF EXISTS users;

