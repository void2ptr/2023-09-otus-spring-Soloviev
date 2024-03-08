insert into author(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genre(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6');

insert into book(title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);

insert into book_genre(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);

insert into comment(book_id, description)
values (1, 'Comment_1'), ( 1, 'Comment_2'), (1, 'Comment_3'),
       (2, 'Comment_1'), ( 2, 'Comment_2'), (2, 'Comment_3'),
       (3, 'Comment_1'), ( 3, 'Comment_2'), (3, 'Comment_3');
