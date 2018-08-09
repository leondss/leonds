package com.leonds.blog.console.controller;

import com.leonds.blog.console.service.TagService;
import com.leonds.blog.domain.entity.Tag;
import com.leonds.core.resp.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Leon
 */
@RestController
@RequestMapping("/api/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @PostMapping("/save")
    public Response save(@RequestBody Tag tag) {
        Tag result = tagService.save(tag);
        return Response.ok(result).build();
    }

    @GetMapping("/{id}")
    public Response get(@PathVariable("id") String id) {
        Tag result = tagService.getById(id);
        return Response.ok(result).build();
    }

    @PostMapping("/remove")
    public Response remove(@RequestBody List<String> ids) {
        tagService.remove(ids);
        return Response.ok().build();
    }

}
