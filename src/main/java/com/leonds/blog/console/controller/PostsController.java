package com.leonds.blog.console.controller;

import com.leonds.blog.console.service.PostsService;
import com.leonds.blog.domain.dto.PostsDto;
import com.leonds.blog.domain.entity.Posts;
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
@RequestMapping("/api/posts")
public class PostsController {

    @Autowired
    private PostsService postsService;

    @PostMapping("/save")
    public Response save(@RequestBody PostsDto dto) {
        Posts posts = postsService.save(dto);
        PostsDto result = postsService.getPostsDto(posts.getId());
        return Response.ok(result).build();
    }

    @PostMapping("/remove")
    public Response remove(@RequestBody List<String> ids) {
        if (ids != null && !ids.isEmpty()) {
            for (String id : ids) {
                postsService.remove(id);
            }
        }
        return Response.ok().build();
    }

    @GetMapping("/{id}")
    public Response get(@PathVariable("id") String id) {
        PostsDto result = postsService.getPostsDto(id);
        return Response.ok(result).build();
    }

    @GetMapping("/page")
    public Response page(@RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size,
                         String text) {
        Page<Posts> result = postsService.getPage(PageRequest.of(page, size), text);
        return Response.ok(result).build();
    }

}
