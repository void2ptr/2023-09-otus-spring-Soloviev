--changeset solo: 2024-01-02--0080-SECURITY-CLEAN-acl_entry
delete from public.acl_entry cascade;

--changeset solo: 2024-01-01--0081-SECURITY-CLEAN-acl_entry
ALTER SEQUENCE public.acl_entry_id_seq RESTART WITH 1;


--changeset solo: 2024-01-02--0011-SECURITY-CLEAN acl_object_identity
delete from public.acl_object_identity cascade;
commit;

--changeset solo: 2024-01-01--0012-SECURITY-EXEC-acl_object_identity
ALTER SEQUENCE acl_object_identity_id_seq RESTART WITH 1;
commit;