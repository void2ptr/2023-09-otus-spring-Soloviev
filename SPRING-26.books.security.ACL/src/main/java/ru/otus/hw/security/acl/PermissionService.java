package ru.otus.hw.security.acl;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PermissionService {

    private final MutableAclService aclService;

    private final PlatformTransactionManager transactionManager;

    public void addPermission(boolean isUser, IEntity targetObj, List<Permission> permissions) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (isUser) {
            // User
            permissions.forEach(permission ->
                    this.addPermissionForUser(targetObj, permission, authentication.getName()));
        } else {
            // Group
            authentication.getAuthorities().forEach(authority -> permissions.forEach(permission ->
                    this.addPermissionForAuthority(targetObj, permission, authority.getAuthority())));
        }
    }

    @Transactional
    public void addPermissionForUser(IEntity targetObj, Permission permission, String username) {
        final Sid sid = new PrincipalSid(username);
        addPermissionForSid(targetObj, permission, sid);
    }

    @Transactional
    public void addPermissionForAuthority(IEntity targetObj, Permission permission, String authority) {
        final Sid sid = new GrantedAuthoritySid(authority);
        addPermissionForSid(targetObj, permission, sid);
    }

    @Transactional
    private void addPermissionForSid(IEntity targetObj, Permission permission, Sid sid) {
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
                final ObjectIdentity oi = new ObjectIdentityImpl(targetObj.getClass(), targetObj.getId());
                MutableAcl acl;
                try {
                    acl = (MutableAcl) aclService.readAclById(oi);
                } catch (final NotFoundException nfe) {
                    acl = aclService.createAcl(oi);
                }
                acl.insertAce(acl.getEntries()
                        .size(), permission, sid, true);
                aclService.updateAcl(acl);
            }
        });
    }
}
