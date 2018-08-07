package com.leonds.blog.console.controller;

import com.leonds.blog.console.service.SysRoleService;
import com.leonds.blog.domain.dto.SysRoleDto;
import com.leonds.blog.domain.entity.SysRole;
import com.leonds.core.orm.Page;
import com.leonds.core.orm.PageRequest;
import com.leonds.core.resp.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Leon
 */
@RestController
@RequestMapping("/api/sysrole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @PostMapping("/save")
    public Response save(@RequestBody SysRoleDto sysRoleDto) {
        SysRole sysRole = new SysRole();
        sysRole.setName(sysRoleDto.getName());
        sysRole.setId(sysRoleDto.getId());
        sysRole.setDescription(sysRoleDto.getDescription());
        SysRole result = sysRoleService.save(sysRole, sysRoleDto.getResourceIds());
        return Response.ok(result).build();
    }

    @PostMapping("/remove")
    public Response remove(@RequestBody List<String> ids) {
        if (!ids.isEmpty()) {
            sysRoleService.remove(ids);
        }
        return Response.ok().build();
    }

    @GetMapping("/{id}")
    public Response get(@PathVariable("id") String id) {
        SysRole result = sysRoleService.getById(id);
        List<String> resourceIds = sysRoleService.getRoleResource(id);
        SysRoleDto sysRoleDto = SysRoleDto.builder()
                .name(result.getName())
                .id(result.getId())
                .description(result.getDescription())
                .resourceIds(resourceIds)
                .build();
        return Response.ok(sysRoleDto).build();
    }

    @GetMapping("/page")
    public Response page(@RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size,
                         String text) {
        Page<SysRole> result = sysRoleService.getPage(PageRequest.of(page, size), text);
        return Response.ok(result).build();
    }

    @GetMapping("/all")
    public Response all() {
        List<SysRole> sysRoles = sysRoleService.getAll();
        return Response.ok(sysRoles).build();
    }

    @GetMapping("/resource")
    public Response getRoleResource(String id) {
        List<String> result = sysRoleService.getRoleResource(id);
        return Response.ok(result).build();
    }

    @PostMapping("/save/resource")
    public Response saveRoleResource(@RequestBody SysRoleDto dto) {
        sysRoleService.saveRoleResource(dto.getId(), dto.getResourceIds());
        return Response.ok().build();
    }


}
