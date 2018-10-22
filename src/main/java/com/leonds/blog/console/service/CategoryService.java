package com.leonds.blog.console.service;


import com.leonds.blog.domain.entity.Category;

import java.util.List;
import java.util.Map;

/**
 * @author Leon
 */
public interface CategoryService {

    Category save(Category category);

    Category getByName(String name);

    Category getByCode(String code);

    Category getByIdIsNotAndName(String id, String name);

    Category getByIdIsNotAndCode(String id, String code);

    Category getById(String id);

    void remove(List<String> ids);

    List<Map<String, Object>> getTreeByPid(String pid);

    List<Category> getAll();

}
