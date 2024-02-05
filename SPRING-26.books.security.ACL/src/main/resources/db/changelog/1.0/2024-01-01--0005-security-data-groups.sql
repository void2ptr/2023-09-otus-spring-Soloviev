/* Security roles */

--changeset solo: 2024-01-01--003-SECURITY-ROLES-INSERT-roles
insert into groups (id, group_name)
values
     ( 1, 'ROLE_ADMIN' ),
     ( 2, 'ROLE_USER' );

--changeset solo: 2024-01-01--004-SECURITY-ROLES-INSERT-AUTHORITY
insert into group_authorities (group_id, authority)
values
     ( 1, 'ROLE_ADMIN' ),
     ( 2, 'ROLE_ADMIN' ),
     ( 2, 'ROLE_USER' );

--changeset solo: 2024-01-01--005-SECURITY-ROLES-INSERT-group_members
insert into group_members(group_id, username )
values
     ( 1, 'admin' ),
     ( 2, 'admin' ),
     ( 2, 'user' );
