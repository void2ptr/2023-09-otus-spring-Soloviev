/* Security ACL */

--changeset solo: 2024-01-02--006-SECURITY-ACL-INSERT-acl_sid
INSERT INTO acl_sid (id, principal, sid)
VALUES
(1, true , 'admin'),
(2, true , 'user'),
(3, false, 'ROLE_ADMIN'),
(4, false, 'ROLE_USER');
