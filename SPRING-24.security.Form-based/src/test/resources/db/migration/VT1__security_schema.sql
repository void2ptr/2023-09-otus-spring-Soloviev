/* Security */
create table users (
    id                      bigserial,
    username                varchar(255),
    password                varchar(1024),
    account_non_expired     boolean default true,
    account_non_locked      boolean default true,
    credentials_non_expired boolean default true,
    enabled                 boolean default true,
    primary key (id)
);

create table roles (
    id bigserial,
    role varchar(255),
    primary key (id)
);

create table users_roles (
    id bigserial,
    user_id bigint references users(id) on delete cascade,
    role_id bigint references roles(id) on delete cascade,
    primary key (id)
);
