create table authors (
    id bigserial,
    full_name varchar(255),
    primary key (id)
);

create table genres (
    id bigserial,
    name varchar(255),
    primary key (id)
);

create table books (
    id bigserial,
    title varchar(255),
    author_id bigint references authors (id) on delete cascade,
    primary key (id)
);

create table books_genres (
    book_id bigint references books(id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade,
    primary key (book_id, genre_id)
);

create table comments (
    id bigserial,
    book_id bigint references books(id) on delete cascade,
    description varchar(255),
    primary key (id)
);

/* Security */
create table users (
    id bigserial,
    username varchar(255),
    password varchar(1024),
    primary key (id)
);

create table roles (
    id bigserial,
    role varchar(255),
    primary key (id)
);

create table users_roles (
    id bigserial,
    user_id bigint references users(id) on delete cascade,
    role_id bigint references roles(id) on delete cascade,
    primary key (id)
);
