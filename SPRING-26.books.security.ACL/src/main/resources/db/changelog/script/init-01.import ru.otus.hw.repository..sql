-- Не проверять: fill table genrator
/*
DELETE FROM acl_entry;
commit;
delete from acl_object_identity;
commit;
*/
DO $$ DECLARE
    rec record;
begin
  -- new
  INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
  values (1  , 0    , null         , 1        , false),
         (2  , 0    , null         , 1        , false),
         (3  , 0    , null         , 1        , false),
         (4  , 0    , null         , 1        , false);
    -- exist
    FOR rec in select cl.id  cl_id
                    , d.id   ob_id
--                    , sid.id sid
                FROM acl_class cl
--                    , acl_sid  sid
                    , (         SELECT a.id, 1 as clazz FROM authors    a
                      union all SELECT b.id, 2 as clazz FROM books      b
                      union all SELECT c.id, 3 as clazz FROM "comments" c
                      union all SELECT g.id, 4 as clazz FROM genres     g
                      ) as d
                where d.clazz = cl.id
                order by 1,2
    LOOP
      INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
      values                          (rec.cl_id      , rec.ob_id         , null         , 3        , false);
    END LOOP;
    commit;
END;
$$ LANGUAGE plpgsql;
