/* Security users */

--changeset solo: 2024-01-01--001-SECURITY-USERS-CREATE-users
CREATE TABLE users (
    username                varchar(128),
    password                varchar(128),
    account_non_expired     boolean default true,
    account_non_locked      boolean default true,
    credentials_non_expired boolean default true,
    enabled                 boolean default true,
    primary key (username)
);

--changeset solo: 2024-01-01--001-SECURITY-USERS-CREATE-authorities
CREATE TABLE authorities (
	username  varchar(128) NOT NULL, -- link to users.username
	authority varchar(128) NOT NULL, -- ROLE_ADMIN, ROLE_USER
	CONSTRAINT authorities_pk PRIMARY KEY (username, authority),
	CONSTRAINT authorities_users_fk FOREIGN KEY (username) REFERENCES users(username)
);
