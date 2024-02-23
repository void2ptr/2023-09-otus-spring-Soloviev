--changeset solo: 2024-01-03--021-APP-SCHEMA-authors
create table authors (
    id bigserial,
    full_name varchar(255),
    primary key (id)
);

--changeset solo: 2024-01-03--022-APP-SCHEMA-genres
create table genres (
    id bigserial,
    name varchar(255),
    primary key (id)
);

--changeset solo: 2024-01-03--023-APP-SCHEMA-books
create table books (
    id bigserial,
    title varchar(255),
    author_id bigint references authors (id) on delete cascade,
    primary key (id)
);

--changeset solo: 2024-01-03--024-APP-SCHEMA-books_genres
create table books_genres (
    book_id bigint references books(id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade,
    primary key (book_id, genre_id)
);

--changeset solo: 2024-01-03--025-APP-SCHEMA-comments
create table comments (
    id bigserial,
    book_id bigint references books(id) on delete cascade,
    description varchar(255),
    primary key (id)
);
