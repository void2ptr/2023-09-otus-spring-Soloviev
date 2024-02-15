--changeset solo: 2024-01-03--031-APP-INIT-authors
insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

--changeset solo: 2024-01-03--032-APP-INIT-genres
insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6');

--changeset solo: 2024-01-03--033-APP-INIT-books
insert into books(title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);

--changeset solo: 2024-01-03--034-APP-INIT-books_genres
insert into books_genres(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);

--changeset solo: 2024-01-03--035-APP-INIT-comments
insert into comments(book_id, description)
values (1, 'Comment_1'), ( 1, 'Comment_2'), (1, 'Comment_3'),
       (2, 'Comment_1'), ( 2, 'Comment_2'), (2, 'Comment_3'),
       (3, 'Comment_1'), ( 3, 'Comment_2'), (3, 'Comment_3');

