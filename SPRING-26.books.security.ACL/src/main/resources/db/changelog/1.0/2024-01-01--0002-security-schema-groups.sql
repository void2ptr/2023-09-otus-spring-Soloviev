/* Security roles */

--changeset solo: 2024-01-01--003-SECURITY-CREATE-groups
CREATE TABLE groups (
    id         bigserial   NOT NULL,
    group_name varchar(50) NOT NULL,
    CONSTRAINT groups_pk PRIMARY KEY (id)
);

--changeset solo: 2024-01-01--004-SECURITY-CREATE-group_authorities
CREATE TABLE group_authorities (
    group_id  bigserial   NOT NULL, -- link to groups.id
    authority varchar(50) NOT NULL,
    CONSTRAINT group_authorities_group_fk FOREIGN KEY (group_id) REFERENCES groups(id)
);

--changeset solo: 2024-01-01--005-SECURITY-CREATE-group_members
CREATE TABLE group_members (
    id       bigserial   NOT NULL,
    username varchar(50) NOT NULL, -- link to users.username
    group_id bigint      NOT NULL, -- link to groups.id
    CONSTRAINT group_members_pk PRIMARY KEY (id),
    CONSTRAINT group_members_group_fk FOREIGN KEY(group_id) REFERENCES groups(id)
);
