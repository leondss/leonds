package com.leonds.blog.console.service.impl;

import com.leonds.blog.console.service.SysUserService;
import com.leonds.blog.domain.entity.SysUser;
import com.leonds.core.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getByUsername(username);

        if (sysUser == null) {
            throw new UsernameNotFoundException(String.format("找不到用户 '%s'.", username));
        } else {
            List<String> userPerms = sysUserService.getUserPerms(sysUser.getId());
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            for (String perm : userPerms) {
                authorities.add(new SimpleGrantedAuthority(perm));
            }
            return User.of(sysUser, authorities);
        }
    }
}
