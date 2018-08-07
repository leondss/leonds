package com.leonds.blog.console.controller;

import com.leonds.blog.console.service.SysResourceService;
import com.leonds.blog.domain.dto.TreeNode;
import com.leonds.blog.domain.entity.SysResource;
import com.leonds.core.resp.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Leon
 */
@RestController
@RequestMapping("/api/sysresource")
public class SysResourceController {

    @Autowired
    private SysResourceService sysResourceService;

    @GetMapping("/tree")
    public Response getByPid(String pid) {
        List<Map<String, Object>> result = sysResourceService.getTreeByPid(pid);
        return Response.ok(result).build();
    }

    @PostMapping("/save")
    public Response save(@RequestBody SysResource sysResource) {
        SysResource result = sysResourceService.save(sysResource);
        TreeNode treeNode = TreeNode.builder()
                .id(result.getId())
                .url(result.getUrl())
                .path(result.getPath())
                .component(result.getComponent())
                .name(result.getName())
                .permission(result.getPermission())
                .pid(result.getPid())
                .position(result.getPosition())
                .type(result.getType())
                .icon(result.getIcon())
                .childNum(0)
                .children(new ArrayList<>())
                .build();
        return Response.ok(treeNode).build();
    }

    @PostMapping("/remove")
    public Response remove(@RequestBody List<String> ids) {
        if (!ids.isEmpty()) {
            sysResourceService.remove(ids);
        }
        return Response.ok().build();
    }

    @GetMapping("/{id}")
    public Response get(@PathVariable("id") String id) {
        SysResource result = sysResourceService.getById(id);
        return Response.ok(result).build();
    }

}
