-- Security ACL

--changeset solo: 2024-01-02--004-SECURITY-ACL-CREATE-acl_entry
CREATE TABLE acl_entry (
	id                  bigserial NOT NULL,
	acl_object_identity bigint    NOT NULL, -- specify the object identity, links to ACL_OBJECT_IDENTITY table
	ace_order           int       NOT NULL, -- порядок обработки разрешений
	sid                 bigint    NOT NULL, -- кому дается разрешение
	mask                integer   NOT NULL, -- какое именно разрешение 1 — READ, 2 — WRITE, 4 — CREATE (см. класс BasePermission)
	granting            boolean   NOT NULL, -- value 1 means granting, value 0 means denying
	audit_success       boolean   NOT NULL, -- for auditing purpose
	audit_failure       boolean   NOT NULL, -- for auditing purpose
	CONSTRAINT acl_entry_pk PRIMARY KEY (id),
	CONSTRAINT acl_entry_un UNIQUE (acl_object_identity, ace_order),
	CONSTRAINT acl_entry_fk_1 FOREIGN KEY (acl_object_identity) REFERENCES acl_object_identity(id),
	CONSTRAINT acl_entry_fk_2 FOREIGN KEY (sid) REFERENCES acl_sid(id)
);
COMMENT ON TABLE acl_entry IS 'разрешения (какие, кому, на что)';

-- Column comments

COMMENT ON COLUMN acl_entry.acl_object_identity IS 'specify the object identity, links to ACL_OBJECT_IDENTITY table';
COMMENT ON COLUMN acl_entry.ace_order IS 'порядок обработки разрешений';
COMMENT ON COLUMN acl_entry.sid IS 'кому дается разрешение';
COMMENT ON COLUMN acl_entry.mask IS 'какое именно разрешение 1 — READ, 2 — WRITE, 4 — CREATE (см. класс BasePermission)';
COMMENT ON COLUMN acl_entry.granting IS 'value 1 means granting, value 0 means denying';
COMMENT ON COLUMN acl_entry.audit_success IS 'for auditing purpose';
COMMENT ON COLUMN acl_entry.audit_failure IS 'for auditing purpose';
