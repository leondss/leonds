package com.leonds.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;

/**
 * @author Leon
 */
@Service
public class CacheService {
    public static final String CACHE_KEY = "users";

    @Autowired
    private CacheManager cacheManager;


    public SecurityContext getSecurityContext(String token) {
        Cache users = cacheManager.getCache(CACHE_KEY);
        if (users != null) {
            return users.get(token, SecurityContext.class);
        }
        return null;
    }

    public void setSecurityContext(String token, SecurityContext securityContext) {
        Cache users = cacheManager.getCache(CACHE_KEY);
        if (users != null) {
            users.put(token, securityContext);
        }
    }

    public void removeSecurityContext(SecurityContext securityContext) {
        Cache users = cacheManager.getCache(CACHE_KEY);
        if (users != null) {
            users.evict(securityContext);
        }
    }

}
