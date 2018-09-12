package com.leonds.blog.console.controller;

import com.leonds.blog.console.service.CategoryService;
import com.leonds.blog.domain.dto.TreeNode;
import com.leonds.blog.domain.entity.Category;
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
@RequestMapping("/api/cate")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/tree")
    public Response getByPid(String pid) {
        List<Map<String, Object>> result = categoryService.getTreeByPid(pid);
        return Response.ok(result).build();
    }

    @PostMapping("/save")
    public Response save(@RequestBody Category category) {
        Category result = categoryService.save(category);
        TreeNode treeNode = TreeNode.builder()
                .id(result.getId())
                .name(result.getCode())
                .name(result.getName())
                .pid(result.getPid())
                .childNum(0)
                .children(new ArrayList<>())
                .build();
        return Response.ok(treeNode).build();
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
