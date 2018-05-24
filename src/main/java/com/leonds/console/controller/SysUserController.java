package com.leonds.console.controller;

import com.leonds.console.service.SysRoleService;
import com.leonds.console.service.SysUserService;
import com.leonds.core.resp.Response;
import com.leonds.domain.dto.SysUserDto;
import com.leonds.domain.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        SysUser user = sysUserService.save(sysUser, sysUserDto.getRoleIds());
        return Response.ok(user).build();
    }

    @GetMapping("/page")
    public Response page(@RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size,
                         String text, String status) {
        Page<SysUser> result = sysUserService.getPage(PageRequest.of(page, size), text, status);
        return Response.ok(result).build();
    }

    @GetMapping("/{id}")
    public Response get(@PathVariable("id") String id) {
        SysUser user = sysUserService.getById(id);
        List<String> userRoleIds = sysRoleService.getUserRoleIds(id);
        SysUserDto sysUserRoleDto = SysUserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
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
    public Response resetPassword(@RequestBody SysUser sysUser) {
        sysUserService.resetPassword(sysUser);
        return Response.ok().build();
    }

    @PostMapping("/save/role")
    public Response saveRole(@RequestBody SysUserDto sysUserRoleDto) {
        sysRoleService.saveUserRole(sysUserRoleDto.getId(), sysUserRoleDto.getRoleIds());
        return Response.ok().build();
    }

}
