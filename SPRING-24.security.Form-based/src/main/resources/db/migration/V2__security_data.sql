-- Security
insert into users(username, password, account_non_expired, account_non_locked, credentials_non_expired, enabled)
values
     ( 'admin', '$2a$10$InGthJc0x34rVxfWUJbIjOIv5DxAJGaWUTyKxaNmrew5FU.vAmxaC', true, true, true, true ),
     ( 'user' , '$2a$10$InGthJc0x34rVxfWUJbIjOIv5DxAJGaWUTyKxaNmrew5FU.vAmxaC', true, true, true, true ),
     ( 'anonymous', ''                                                        , false, false, false, false );

insert into roles(role)
values
     ( 'ROLE_ADMIN' ),
     ( 'ROLE_USER' ),
     ( 'ROLE_ANONYMOUS' );

insert into users_roles(user_id, role_id)
values
     ( 1, 1 ),
     ( 2, 2 ),
     ( 3, 3 );
