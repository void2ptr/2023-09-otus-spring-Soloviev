insert into authors(id, full_name)
values (1, 'Толстой Лев Николаевич'),
       (2, 'Публий Корнелий Тацит'),
       (3, 'Цезарь Гай Юлий'),
       (4, 'Тит Ливий'),
       (5, 'Аммиан Марцеллин'),
       (6, 'Иосиф Флавий'),
       (7, 'Никколо Макиавелли'),
       (8, 'Аппулей'),
       (9, 'Эзоп'),
       (10, 'Аристофан'),
       (11, 'Эсхил'),
       (12, 'Марциал'),
       (13, 'Ло Гуаньчжун'),
       (14, 'Го Сюн'),
       (15, 'Цао Сюэцин'),
       (16, 'фольклёр');

insert into genres(id, name)
values (1, 'видения'),
       (2, 'новелла'),
       (3, 'ода'),
       (4, 'опус'),
       (5, 'очерк'),
       (6, 'поэма'),
       (7, 'повесть'),
       (8, 'пьеса'),
       (9, 'рассказ'),
       (10, 'роман'),
       (11, 'скетч'),
       (12, 'эпопея'),
       (13, 'эпос'),
       (14, 'эссе'),
       (15, 'комедия'),
       (16, 'эпиграммы'),
       (17, 'хроника'),
       (18, 'мемуар'),
       (19, 'военная проза'),
       (20, 'учебник'),
       (21, 'трагедия');

insert into books(title, author_id)
values ('Война и Мир', 1),
       ('О происхождении, расположении, нравах и населении Германии', 2),
       ('Записки о галльской войне', 3),
       ('История Рима от основания Города', 4),
       ('Деяния', 5),
       ('Ватиканский кодекс', 5),
       ('Иудейская война', 6),
       ('Иудейские древности', 6),
       ('Государь', 7),
       ('Золотой осёл', 8),
       ('Басни', 9),
       ('Птицы', 10),
       ('Прометей', 11),
       ('Зрелища', 12),
       ('Хроники Троецарствия', 13),
       ('Речные Заводи', 14),
       ('Сон в Красном Тереме', 15),
       ('Путешествие на запад', 16);

insert into books_genres(book_id, genre_id)
values (1, 10),   (1, 13),
       (2,  5),   (2, 9),
       (3, 18),   (3, 19),  (3, 17), (3, 20),
       (4, 17),   (4, 13),
       (5, 13),   (5, 17),
       (6, 17),   (6, 20),
       (7, 18),   (7, 19),
       (8, 13),   (8, 10),
       (9, 17),   (9, 20),
       (10, 7),   (10, 15),
       (11, 9),   (11, 15), (11, 20),
       (12, 7),   (12, 15),
       (13, 12),  (13, 21),
       (14, 17),  (14, 20),
       (15, 10),  (15, 17),
       (16, 7),   (16, 17), (16, 18),
       (17, 10),  (17, 21),
       (18, 7),   (18, 15);

insert into comments(book_id, description)
values -- 'Война и Мир'
       ( 1, 'Классика мировой литературы'),
       ( 1, 'Длинная, интересная'),
       ( 1, 'Вдохновляющее произведение, на все времена'),
       -- О происхождении, расположении, нравах и населении Германии
       ( 2, 'Моя настольная книга'),
       ( 2, 'Ничего не изменилось!'),
       ( 2, 'Теперь многое стало понятно...'),
       -- Деяния
       ( 3, 'Автор - безусловно гений!'),
       ( 3, 'Книга - огонь!'),
       ( 3, 'Бестселлер'),
       -- История Рима от основания Города
       ( 4, 'Обязательна к прочтению'),
       ( 4, 'Великолепная книга, должна быть настольной у каждого программиста'),
       ( 4, 'Без этой книги посещение Русского музея в Питере - пустая трата времени'),
       -- Деяния
       ( 5, 'интересно'),
       ( 5, 'очень радует новыми фактами из истории'),
       ( 5, 'отличная книга'),
       -- Иудейская война
       ( 6, 'Нет слов, одни эмоции'),
       ( 6, 'хочешь познать суть мира - прочти Макиавелли'),
       ( 6, 'о мой великий учитель, Макиавелли!'),
       -- Иудейская война
       ( 7, 'классика'),
       ( 7, 'автор - жулик'),
       ( 7, 'весьма познавательно'),
       -- Иудейские древности
       ( 8, 'Шедевр'),
       ( 8, 'Великолепно, автор гений!'),
       ( 8, 'Перечитывал много раз'),
       -- Государь
       ( 9, 'Макиавелли писал книги на все времена'),
       ( 9, 'цинично'),
       ( 9, 'для тех, кто хочет преуспеть в обществе'),
       -- Золотой осёл
       ( 10, 'Да уж'),
       ( 10, 'Эта книга не под запретом?'),
       ( 10, 'Я обязательно напишу в соответствующие инстанции - это надо запретить срочно!'),
       -- Басни
       ( 11, 'После прочтения понимаешь, что Крылов многое "заимствовал"'),
       ( 11, 'Красивый язык'),
       ( 11, 'Веселые ребята были греки, как я погляжу.'),
       -- Птицы
       ( 12, 'Хорошая комедия'),
       ( 12, 'классненько!'),
       ( 12, '"От антидемократических птичек поднимался дымок" - поржал'),
       -- Прометей
       ( 13, 'Читать всем!!!'),
       ( 13, 'Красивейшая легенда'),
       ( 13, 'Жалко его стало'),
       -- Зрелища
       ( 14, 'Какая была тяжелая жизнь раньше'),
       ( 14, 'Узнал много нового про эпиграммы'),
       ( 14, 'Неплохо.'),
       -- Хроники Троецарствия
       ( 15, 'Выдержала проверку временем'),
       ( 15, 'Бестселлер'),
       ( 15, 'Одно из четырех великих китайских классических произведений'),
       -- Речные Заводи
       ( 16, 'Авторство романа до сих пор является предметом дискуссий'),
       ( 16, 'всю книгу доставали сердце и печть врагов, а съели их только в конце...'),
       ( 16, 'Это произведение, поможет Вам пережить любые трудности!'),
       -- Сон в Красном Тереме
       ( 17, 'Классика рулит!'),
       ( 17, 'Книга написана на основании личного опыта'),
       ( 17, 'книга тронула мое сердце'),
       -- Путешествие на запад
       ( 18, 'Цитаты из неей расползлись по всему миру'),
       ( 18, 'Сунь У Кун - лучший!'),
       ( 18, 'Теперь не могу есть хурму, вспоминаю Джу Ба Дзе');

