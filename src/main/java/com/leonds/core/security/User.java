package com.leonds.core.security;

import com.leonds.blog.domain.entity.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author Leon
 */
public class User implements UserDetails {
    private String id;

    private String username;

    private String password;

    private String realName;

    private String mobile;

    private String token;

    private Collection<? extends GrantedAuthority> authorities;

    private User(String id,
                 String username,
                 String password,
                 String realName,
                 String mobile,
                 Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.realName = realName;
        this.mobile = mobile;
        this.authorities = authorities;
    }

    public static User of(SysUser sysUser, Collection<? extends GrantedAuthority> authorities) {
        return new User(sysUser.getId(),
                sysUser.getUsername(),
                sysUser.getPassword(),
                sysUser.getRealName(),
                sysUser.getMobile(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getId() {
        return id;
    }

    public String getRealName() {
        return realName;
    }

    public String getMobile() {
        return mobile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
