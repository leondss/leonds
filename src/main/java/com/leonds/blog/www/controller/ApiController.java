package com.leonds.blog.www.controller;

import com.leonds.blog.domain.entity.Tag;
import com.leonds.blog.www.service.FrontPostsService;
import com.leonds.blog.www.service.FrontTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author Leon
 */
@RestController
public class ApiController {
    @Autowired
    private FrontTagService frontTagService;
    @Autowired
    private FrontPostsService frontPostsService;

    @GetMapping("/tag/all")
    public List<Tag> getAll() {
        return frontTagService.getAll();
    }

    @GetMapping("/index/count")
    public Map<String, Object> getIndexCount() {
        return frontPostsService.getIndexCount();
    }
}
