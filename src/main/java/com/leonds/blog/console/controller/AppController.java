package com.leonds.blog.console.controller;

import com.leonds.blog.console.service.SysUserService;
import com.leonds.blog.domain.entity.SysUser;
import com.leonds.core.ServiceException;
import com.leonds.core.resp.Response;
import com.leonds.core.security.User;
import com.leonds.core.security.UserManager;
import com.leonds.core.utils.CheckUtils;
import com.leonds.core.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Leon
 */
@Controller
public class AppController {
    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/login")
    @ResponseBody
    public Response login(String username, String password) {
        String token;
        try {
            CheckUtils.checkNotBlank(username, MessageUtils.get("sysUser.username.notBlank"));
            CheckUtils.checkNotBlank(password, MessageUtils.get("sysUser.password.notBlank"));
            User sysUser = (User) userDetailsService.loadUserByUsername(username);

            CheckUtils.checkNotNull(sysUser, MessageUtils.get("sysUser.not.exists"));
            CheckUtils.checkState(passwordEncoder.matches(password, sysUser.getPassword()), MessageUtils.get("sysUser.password.err"));

            token = UUID.randomUUID().toString();
            sysUser.setToken(token);

            List<String> userPerms = sysUserService.getUserPerms(sysUser.getId());
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            for (String perm : userPerms) {
                authorities.add(new SimpleGrantedAuthority(perm));
            }

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(sysUser, password, authorities);

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(context);

//            HttpSession session = request.getSession(true);
//            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());


        } catch (AuthenticationException e) {
            throw new ServiceException(e.getMessage());
        }

        return Response.ok(token).build();
    }

    @PostMapping("/modify/password")
    @ResponseBody
    public Response modifyPassword(String oldPassword, String newPassword, String confirmPassword) {
        CheckUtils.checkNotBlank(oldPassword, MessageUtils.get("sysUser.password.notBlank"));
        CheckUtils.checkNotBlank(newPassword, MessageUtils.get("sysUser.password.notBlank"));
        CheckUtils.checkNotBlank(confirmPassword, MessageUtils.get("sysUser.password.notBlank"));
        String userId = UserManager.getUserId();
        SysUser sysUser = sysUserService.getById(userId);
        if (sysUser != null) {
            CheckUtils.checkState(passwordEncoder.matches(oldPassword, sysUser.getPassword()), MessageUtils.get("sysUser.oldPassword.err"));
            CheckUtils.checkState(newPassword.equals(confirmPassword), MessageUtils.get("sysUser.confirmPassword.err"));
            sysUser.setPassword(passwordEncoder.encode(newPassword));
            sysUserService.save(sysUser);
        }
        return Response.ok().build();
    }
}
