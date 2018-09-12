package com.leonds.blog.console.service.impl;

import com.leonds.blog.console.service.CategoryService;
import com.leonds.blog.domain.entity.Category;
import com.leonds.core.orm.Filters;
import com.leonds.core.orm.PersistenceManager;
import com.leonds.core.orm.SqlParams;
import com.leonds.core.utils.CheckUtils;
import com.leonds.core.utils.MessageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Leon
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private PersistenceManager pm;

    @Override
    public Category save(Category category) {
        CheckUtils.checkObject(category);
        if (StringUtils.isNotBlank(category.getId())) {
            Category categoryOfName = getByIdIsNotAndName(category.getId(), category.getName());
            CheckUtils.checkState(categoryOfName == null, MessageUtils.get("category.name.exists"));

            Category categoryOfCode = getByIdIsNotAndName(category.getId(), category.getCode());
            CheckUtils.checkState(categoryOfCode == null, MessageUtils.get("category.code.exists"));
        } else {
            Category categoryOfName = getByName(category.getName());
            CheckUtils.checkState(categoryOfName == null, MessageUtils.get("category.name.exists"));

            Category categoryOfCode = getByCode(category.getCode());
            CheckUtils.checkState(categoryOfCode == null, MessageUtils.get("category.code.exists"));
        }
        return pm.save(category);
    }

    @Override
    public Category getByName(String name) {
        return pm.findOne(Category.class, Filters.and(Filters.eq("name", name)));
    }

    @Override
    public Category getByCode(String code) {
        return pm.findOne(Category.class, Filters.and(Filters.eq("code", code)));
    }

    @Override
    public Category getByIdIsNotAndName(String id, String name) {
        return pm.findOne(Category.class, Filters.and(Filters.eq("name", name), Filters.neq("id", id)));
    }

    @Override
    public Category getByIdIsNotAndCode(String id, String code) {
        return pm.findOne(Category.class, Filters.and(Filters.eq("code", code), Filters.neq("id", id)));
    }

    @Override
    public Category getById(String id) {
        return pm.get(Category.class, id);
    }

    @Override
    public void remove(List<String> ids) {
        if (ids != null && !ids.isEmpty()) {
            pm.remove(Category.class, ids);
        }
    }

    @Override
    public List<Map<String, Object>> getTreeByPid(String pid) {
        SqlParams sqlParams = SqlParams.instance().append("pid", pid);
        return pm.find("getCategories", sqlParams);
    }
}
