--changeset solo: 2024-01-03--001-APP-SCHEMA-authors
create table authors (
    id bigserial,
    full_name varchar(255),
    primary key (id)
);
--rollback DROP TABLE authors;

--changeset solo: 2024-01-03--002-APP-SCHEMA-genres
create table genres (
    id bigserial,
    name varchar(255),
    primary key (id)
);
--rollback DROP TABLE genres;

--changeset solo: 2024-01-03--003-APP-SCHEMA-books
create table books (
    id bigserial,
    title varchar(255),
    author_id bigint references authors (id) on delete cascade,
    primary key (id)
);
--rollback DROP TABLE books;

--changeset solo: 2024-01-03--004-APP-SCHEMA-books_genres
create table books_genres (
    book_id bigint references books(id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade,
    primary key (book_id, genre_id)
);
--rollback DROP TABLE books_genres;

--changeset solo: 2024-01-03--004-APP-SCHEMA-comments
create table comments (
    id bigserial,
    book_id bigint references books(id) on delete cascade,
    description varchar(255),
    primary key (id)
);
--rollback DROP TABLE comments;
