-- Security ACL

--changeset solo: 2024-01-02--002-SECURITY-ACL-CREATE-acl_class
CREATE TABLE acl_class (
	id    bigserial    NOT NULL,
	class varchar(100) NOT NULL, -- запись о классе объектов
	CONSTRAINT acl_class_pk PRIMARY KEY (id),
	CONSTRAINT acl_class_un UNIQUE (class)
);
COMMENT ON TABLE acl_class IS 'записи о классах объектов (.model.ClassName)';

-- Column comments
COMMENT ON COLUMN acl_class.class IS 'запись о классе объектов';
