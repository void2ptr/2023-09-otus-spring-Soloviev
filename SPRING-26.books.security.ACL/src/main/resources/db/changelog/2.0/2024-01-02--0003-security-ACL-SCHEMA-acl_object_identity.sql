-- Security ACL

--changeset solo: 2024-01-02--003-SECURITY-ACL-CREATE-acl_object_identity
CREATE TABLE acl_object_identity (
	id                 bigserial  NOT NULL,
	object_id_class    bigint     NOT NULL, -- links to ACL_CLASS.ID table
	object_id_identity varchar(256) NOT NULL, -- object_id_class.ID store the target object primary key
	parent_object      bigint         NULL, -- specify parent of this Object Identity within this table
	owner_sid          bigint         NULL, -- ID пользователя ASL_SID.ID
	entries_inheriting boolean    NOT NULL, -- whether ACL Entries of this object inherits from the parent object (ACL Entries are defined in ACL_ENTRY table)
	CONSTRAINT acl_object_identity_pk PRIMARY KEY (id),
	CONSTRAINT acl_object_identity_un UNIQUE (object_id_class, object_id_identity)
);
COMMENT ON TABLE acl_object_identity IS 'на какие объекты даем разрешения';

-- Column comments
COMMENT ON COLUMN acl_object_identity.object_id_class IS 'links to ACL_CLASS.ID table';
COMMENT ON COLUMN acl_object_identity.object_id_identity IS 'store the target object primary key object_id_class.ID';
COMMENT ON COLUMN acl_object_identity.parent_object IS 'specify parent of this Object Identity within this table';
COMMENT ON COLUMN acl_object_identity.owner_sid IS 'ID пользователя ASL_SID.ID';
COMMENT ON COLUMN acl_object_identity.entries_inheriting IS 'whether ACL Entries of this object inherits from the parent object (ACL Entries are defined in ACL_ENTRY table)';

-- acl_object_identity foreign keys
ALTER TABLE acl_object_identity ADD CONSTRAINT acl_object_identity_fk_1 FOREIGN KEY (parent_object) REFERENCES acl_object_identity(id);
ALTER TABLE acl_object_identity ADD CONSTRAINT acl_object_identity_fk_2 FOREIGN KEY (object_id_class) REFERENCES acl_class(id);
ALTER TABLE acl_object_identity ADD CONSTRAINT acl_object_identity_fk_3 FOREIGN KEY (owner_sid) REFERENCES acl_sid(id);
