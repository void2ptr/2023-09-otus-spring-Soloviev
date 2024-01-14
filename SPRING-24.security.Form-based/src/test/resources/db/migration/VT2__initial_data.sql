insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6');

insert into books(title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);

insert into books_genres(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);

insert into comments(book_id, description)
values (1, 'Comment_1'), ( 1, 'Comment_2'), (1, 'Comment_3'),
       (2, 'Comment_1'), ( 2, 'Comment_2'), (2, 'Comment_3'),
       (3, 'Comment_1'), ( 3, 'Comment_2'), (3, 'Comment_3');

-- Security
insert into users(username, password)
values
     ( 'admin', '$2a$10$InGthJc0x34rVxfWUJbIjOIv5DxAJGaWUTyKxaNmrew5FU.vAmxaC' ),
     ( 'user' , '$2a$10$InGthJc0x34rVxfWUJbIjOIv5DxAJGaWUTyKxaNmrew5FU.vAmxaC' );

insert into roles(role)
values
     ( 'ROLE_ADMIN' ),
     ( 'ROLE_USER' ),
     ( 'ROLE_ANON' );

insert into users_roles(user_id, role_id)
values
     ( 1, 1 ),
     ( 2, 2 );
