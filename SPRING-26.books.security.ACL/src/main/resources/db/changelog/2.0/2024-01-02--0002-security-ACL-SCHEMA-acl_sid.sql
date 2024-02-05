-- Security ACL

--changeset solo: 2024-01-02--001-SECURITY-ACL-CREATE-acl_sid
CREATE TABLE acl_sid (
	id        bigserial    NOT NULL,
	principal boolean      NOT NULL, -- 0 or 1, для ROLE пароль не нужен, только для USER
	sid       varchar(100) NOT NULL, -- имя пользователя (Authentication.principal.username), или название роли.
	CONSTRAINT acl_sid_pk PRIMARY KEY (id),
	CONSTRAINT acl_sid_un_1 UNIQUE (principal, sid)
);
COMMENT ON TABLE acl_sid IS 'пользователи и роли, которым будут даваться разрешения';

-- Column comments
COMMENT ON COLUMN acl_sid.principal IS '0 or 1, для ROLE пароль не нужен, только для USER';
COMMENT ON COLUMN acl_sid.sid IS 'имя пользователя (Authentication.principal.username), или название роли.';
