--changeset solo: 2024-01-03--011-APP-SCHEMA-authors
DELETE FROM authors;
ALTER SEQUENCE authors_id_seq RESTART WITH 1;

--changeset solo: 2024-01-03--012-APP-SCHEMA-genres
DELETE FROM genres;
ALTER SEQUENCE genres_id_seq RESTART WITH 1;

--changeset solo: 2024-01-03--013-APP-SCHEMA-books
DELETE FROM books;
ALTER SEQUENCE books_id_seq RESTART WITH 1;

--changeset solo: 2024-01-03--014-APP-SCHEMA-books_genres
DELETE FROM books_genres;

--changeset solo: 2024-01-03--015-APP-SCHEMA-comments
DELETE FROM comments;
ALTER SEQUENCE comments_id_seq RESTART WITH 1;
