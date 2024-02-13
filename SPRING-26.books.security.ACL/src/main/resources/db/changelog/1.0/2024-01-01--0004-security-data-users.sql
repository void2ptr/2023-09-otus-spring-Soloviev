/* security users */

--changeset solo: 2024-01-01--004-SECURITY-USER-INSERT-users
insert into users(username, password, account_non_expired, account_non_locked, credentials_non_expired, enabled)
values
     ( 'admin', '$2a$10$InGthJc0x34rVxfWUJbIjOIv5DxAJGaWUTyKxaNmrew5FU.vAmxaC', true, true, true, true ),
     ( 'user' , '$2a$10$InGthJc0x34rVxfWUJbIjOIv5DxAJGaWUTyKxaNmrew5FU.vAmxaC', true, true, true, true ),
     ( 'anonymous', ''                                                        , false, false, false, false );

--changeset solo: 2024-01-01--002-SECURITY-USER-INSERT-authorities
insert into authorities (username, authority)
values
     ( 'admin'    , 'ROLE_ADMIN' ),
     ( 'admin'    , 'ROLE_USER' ),
     ( 'user'     , 'ROLE_USER' ),
     ( 'anonymous', 'ROLE_ANONYMOUS' );
