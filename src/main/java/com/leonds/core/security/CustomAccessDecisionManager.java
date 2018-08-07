package com.leonds.core.security;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author Leon
 */
public class CustomAccessDecisionManager implements AccessDecisionManager {

    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        if (configAttributes == null) {
            return;
        }

        for (ConfigAttribute configAttribute : configAttributes) {
            //访问所请求资源所需要的权限
            String needPermission = configAttribute.getAttribute();
            if ("NO_PERM".equals(needPermission)) {
                return;
            }
//            System.out.println("needPermission is " + needPermission);
//            System.out.println("user is " + authentication.getAuthorities().toString());
            //用户所拥有的权限authentication
            for (GrantedAuthority ga : authentication.getAuthorities()) {
                if (needPermission.equals(ga.getAuthority())) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("无权限！");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
