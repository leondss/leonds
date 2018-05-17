package com.leonds.console.controller;

import com.leonds.console.service.SysResourceService;
import com.leonds.core.resp.Response;
import com.leonds.domain.entity.SysResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Leon
 */
@RestController
@RequestMapping("/api/sysresource")
public class SysResourceController {

    @Autowired
    private SysResourceService sysResourceService;

    @PostMapping("/save")
    public Response save(@RequestBody SysResource sysResource) {
        SysResource result = sysResourceService.save(sysResource);
        return Response.ok(result).build();
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
