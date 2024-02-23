package ru.otus.hw.data;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AclRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public void setGranting(String aclClass, Long objectId, Permission mask, boolean granting) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("acl_class", aclClass);
        params.addValue("object_id_identity", objectId.toString());
        params.addValue("granting", granting);
        params.addValue("mask", mask.getMask());

        jdbc.update("""
                    update acl_entry
                      set granting = :granting
                    where acl_object_identity in (select o.id
                                                    from acl_object_identity o
                                                   where o.object_id_class = (select c.id
                                                                                from acl_class c
                                                                               where c.class = :acl_class
                                                                              )
                                                     and o.object_id_identity = :object_id_identity
                                                   )
                        and mask = :mask
                """, params);
    }

}

