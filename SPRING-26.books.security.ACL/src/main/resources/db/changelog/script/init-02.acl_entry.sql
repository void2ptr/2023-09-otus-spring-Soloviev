-- Не проверять: fill table genrator
DO $$ DECLARE
    rec record;
    ace_order integer;
begin
    FOR rec IN select o.id obj_id
                    , s.id sid
                    , ord.id mask
                  from acl_object_identity o
                     , acl_sid             s
                     , (         select 1 id
                       union all select 2 id
                       union all select 4 id
                       union all select 8 id
                     ) as ord
                order by 1, 2, 3
    loop
        --
        select COALESCE(max(e.ace_order), 0) + 1 ord
          into ace_order
          from acl_entry e
        where e.acl_object_identity = rec.obj_id;
        --
        INSERT INTO acl_entry (acl_object_identity, ace_order, sid    , mask    , granting, audit_success, audit_failure)
        values                (rec.obj_id         , ace_order, rec.sid, rec.mask, true    , true         , true         );
    END LOOP;
    commit;
END;
$$ LANGUAGE plpgsql;
