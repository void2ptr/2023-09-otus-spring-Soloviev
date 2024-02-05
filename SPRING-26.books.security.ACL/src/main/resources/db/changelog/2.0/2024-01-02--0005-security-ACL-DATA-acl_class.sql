/* Security ACL */

--changeset solo: 2024-01-02--005-SECURITY-ACL-INSERT-acl_class
INSERT INTO acl_class (id, class)
VALUES
(1, 'ru.otus.hw.model.Author'),
(2, 'ru.otus.hw.model.Book'),
(3, 'ru.otus.hw.model.Comment'),
(4, 'ru.otus.hw.model.Genre');
