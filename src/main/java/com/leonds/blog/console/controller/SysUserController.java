package com.leonds.blog.console.controller;

import com.leonds.blog.console.service.SysRoleService;
import com.leonds.blog.console.service.SysUserService;
import com.leonds.blog.domain.dto.SysUserDto;
import com.leonds.blog.domain.entity.SysResource;
import com.leonds.blog.domain.entity.SysUser;
import com.leonds.core.orm.Page;
import com.leonds.core.orm.PageRequest;
import com.leonds.core.resp.Response;
import com.leonds.core.security.UserManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Leon
 */
@RestController
@RequestMapping("/api/sysuser")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleService sysRoleService;

    @PostMapping("/save")
    public Response save(@RequestBody SysUserDto sysUserDto) {
        SysUser sysUser = new SysUser();
        sysUser.setId(sysUserDto.getId());
        sysUser.setUsername(sysUserDto.getUsername());
        sysUser.setPassword(sysUserDto.getPassword());
        sysUser.setStatus(sysUserDto.getStatus());
        sysUser.setRealName(sysUserDto.getRealName());
        sysUser.setMobile(sysUserDto.getMobile());
        SysUser user = sysUserService.save(sysUser, sysUserDto.getRoleIds());
        return Response.ok(user).build();
    }

    @GetMapping("/page")
    public Response page(@RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size,
                         String text, String status) {
        Page<Map<String, Object>> result = sysUserService.getPage(PageRequest.of(page, size), text, status);
        return Response.ok(result).build();
    }

    @GetMapping("/{id}")
    public Response get(@PathVariable("id") String id) {
        SysUser user = sysUserService.getById(id);
        List<String> userRoleIds = sysRoleService.getUserRoleIds(id);
        SysUserDto sysUserRoleDto = SysUserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .mobile(user.getMobile())
                .realName(user.getRealName())
                .roleIds(userRoleIds)
                .build();
        return Response.ok(sysUserRoleDto).build();
    }

    @PostMapping("/remove")
    public Response remove(@RequestBody List<String> ids) {
        if (!ids.isEmpty()) {
            sysUserService.remove(ids);
        }
        return Response.ok().build();
    }

    @PostMapping("/enable")
    public Response enable(@RequestBody List<String> ids) {
        if (!ids.isEmpty()) {
            sysUserService.enable(ids);
        }
        return Response.ok().build();
    }

    @PostMapping("/disable")
    public Response disable(@RequestBody List<String> ids) {
        if (!ids.isEmpty()) {
            sysUserService.disable(ids);
        }
        return Response.ok().build();
    }

    @PostMapping("/reset/password")
    public Response resetPassword(String id) {
        sysUserService.resetPassword(id);
        return Response.ok().build();
    }

    @PostMapping("/save/role")
    public Response saveRole(@RequestBody SysUserDto sysUserRoleDto) {
        sysRoleService.saveUserRole(sysUserRoleDto.getId(), sysUserRoleDto.getRoleIds());
        return Response.ok().build();
    }

    @PostMapping("/info")
    public Response getUserInfo(String token) {
        Map<String, Object> result = new HashMap<>();
        if (StringUtils.isNotBlank(token)) {
            SysUser sysUser = sysUserService.getById(UserManager.getUserId());
            if (sysUser != null) {
                List<String> userPerms = sysUserService.getUserPerms(sysUser.getId());
                List<SysResource> resources = sysUserService.getUserMenus(sysUser.getId());
                List<String> userMaxDayPerms = sysUserService.getUserMaxDayPerms(sysUser.getId());
                result.put("user", sysUser);
                result.put("perms", userPerms);
                result.put("resources", resources);

                Map<String, Object> dataPerms = new HashMap<>();
                dataPerms.put("maxDay", userMaxDayPerms.isEmpty() ? 0 : userMaxDayPerms.get(0));

                result.put("dataPerms", dataPerms);
            }
        }
        return Response.ok(result).build();
    }

}
