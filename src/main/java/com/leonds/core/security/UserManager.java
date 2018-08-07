package com.leonds.core.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Leon
 */
public class UserManager {
    public static User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        } else {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                return (User) principal;
            }
        }
        return null;
    }

    public static User getUser() {
        User authentication = getAuthentication();
        if (authentication == null) {
            throw new AccessDeniedException("No Login");
        }
        return authentication;
    }

    public static String getUserId() {
        return getUser().getId();
    }
}
