package com.leonds.blog.console.controller;

import com.leonds.blog.console.service.CategoryService;
import com.leonds.blog.domain.entity.Category;
import com.leonds.core.resp.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Leon
 */
@RestController
@RequestMapping("/api/cate")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public Response getList() {
        List<Category> result = categoryService.getAll();
        return Response.ok(result).build();
    }

    @PostMapping("/save")
    public Response save(@RequestBody Category category) {
        Category result = categoryService.save(category);
        return Response.ok(result).build();
    }

    @PostMapping("/remove")
    public Response remove(@RequestBody List<String> ids) {
        if (!ids.isEmpty()) {
            categoryService.remove(ids);
        }
        return Response.ok().build();
    }

    @GetMapping("/{id}")
    public Response get(@PathVariable("id") String id) {
        Category result = categoryService.getById(id);
        return Response.ok(result).build();
    }

}
