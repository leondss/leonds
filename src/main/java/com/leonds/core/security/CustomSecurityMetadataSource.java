package com.leonds.core.security;

import com.leonds.blog.console.service.SysResourceService;
import com.leonds.blog.domain.entity.SysResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Leon
 */
public class CustomSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private SysResourceService sysResourceService;

    public CustomSecurityMetadataSource(SysResourceService sysResourceService) {
        this.sysResourceService = sysResourceService;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        final String requestUrl = ((FilterInvocation) object).getRequestUrl();
        String url = StringUtils.substringBefore(requestUrl, "?");
        SysResource resource = sysResourceService.getByUrl(url);
        Collection<ConfigAttribute> configAttributes = new ArrayList<>();
        if (null != resource) {
            String permission = resource.getPermission();

            ConfigAttribute configAttribute = new SecurityConfig(permission);
            configAttributes.add(configAttribute);
        } else {
            ConfigAttribute configAttribute = new SecurityConfig("NO_PERM");
            configAttributes.add(configAttribute);
        }
        return configAttributes;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
