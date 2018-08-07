package com.leonds.core.security;

import com.leonds.config.AppConfig;
import com.leonds.core.CacheService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Log4j2
public class CustomSecurityContextRepository implements SecurityContextRepository {

    private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();
    @Autowired
    private AppConfig appConfig;

    @Autowired
    private CacheService cacheService;

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        HttpServletRequest request = requestResponseHolder.getRequest();
        String token = getToken(request);
        SecurityContext context;
        if (StringUtils.isNotBlank(token)) {
            context = cacheService.getSecurityContext(token);
            if (context == null) {
                context = generateNewContext();
            }
        } else {
            context = generateNewContext();
        }
        return context;
    }

    protected SecurityContext generateNewContext() {
        return SecurityContextHolder.createEmptyContext();
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        final Authentication authentication = context.getAuthentication();
        if (authentication == null || trustResolver.isAnonymous(authentication)) {

            String token = getToken(request);
            if (StringUtils.isBlank(token)) {
                return;
            }
            SecurityContext securityContext = cacheService.getSecurityContext(token);
            if (securityContext != null) {
                cacheService.removeSecurityContext(securityContext);
            }
        } else {
            if (context.getAuthentication() instanceof UsernamePasswordAuthenticationToken &&
                    context.getAuthentication().getPrincipal() instanceof UserDetails) {
                String token = ((User) context.getAuthentication().getPrincipal()).getToken();
                SecurityContext securityContext = cacheService.getSecurityContext(token);
                if (securityContext == null) {
                    cacheService.setSecurityContext(token, context);
                }
            }
        }

    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        // TODO Auto-generated method stub
        return false;
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(appConfig.getJwt().getHeader());
        if (StringUtils.isNotBlank(token)) {
            return token;
        }
        String tokenParams = request.getParameter(appConfig.getJwt().getHeader());
        if (StringUtils.isNotBlank(tokenParams)) {
            return tokenParams;
        }
        return null;
    }

}
