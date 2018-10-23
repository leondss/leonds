package com.leonds.blog.console.controller;

import com.leonds.blog.console.service.SettingsService;
import com.leonds.blog.domain.entity.Settings;
import com.leonds.core.QueryParams;
import com.leonds.core.orm.Page;
import com.leonds.core.resp.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Leon
 */
@RestController
@RequestMapping("/api/settings")
public class SettingsController {

    @Autowired
    private SettingsService settingsService;

    @GetMapping("/list")
    public Response list(QueryParams queryParams) {
        Page<Settings> result = settingsService.getPage(queryParams);
        return Response.ok(result).build();
    }

    @PostMapping("/save")
    public Response save(@RequestBody Settings settings) {
        Settings result = settingsService.save(settings);
        return Response.ok(result).build();
    }

    @GetMapping("/{id}")
    public Response get(@PathVariable("id") String id) {
        Settings result = settingsService.getById(id);
        return Response.ok(result).build();
    }

    @PostMapping("/remove")
    public Response remove(@RequestBody List<String> ids) {
        settingsService.remove(ids);
        return Response.ok().build();
    }

}
