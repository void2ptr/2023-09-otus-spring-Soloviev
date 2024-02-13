package ru.otus.hw.security.acl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PermissionService {

    private final MutableAclService aclService;

    public void addPermission(boolean isUser, Class<?> clazz, Long id, List<Permission> permissions) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (isUser) {
            // User
            permissions.forEach(permission ->
                    this.addPermissionForUser(clazz, id, permission, authentication.getName()));
        } else {
            // Group
            authentication.getAuthorities().forEach(authority -> permissions.forEach(permission ->
                    this.addPermissionForAuthority(clazz, id, permission, authority.getAuthority())));
        }
    }

    public void addPermissionForUser(Class<?> clazz, Long id, Permission permission, String username) {
        final Sid sid = new PrincipalSid(username);
        addPermissionForSid(clazz, id, permission, sid);
    }

    public void addPermissionForAuthority(Class<?> clazz, Long id, Permission permission, String authority) {
        final Sid sid = new GrantedAuthoritySid(authority);
        addPermissionForSid(clazz, id, permission, sid);
    }

    @Transactional
    private void addPermissionForSid(Class<?> clazz, Long id, Permission permission, Sid sid) {
            ObjectIdentity oi = new ObjectIdentityImpl(clazz, id);
            MutableAcl acl;
            try {
                acl = (MutableAcl) aclService.readAclById(oi);
            } catch (NotFoundException nfe) {
                acl = aclService.createAcl(oi);
            }
            acl.insertAce(acl.getEntries().size(), permission, sid, true);
            aclService.updateAcl(acl);
    }
}
