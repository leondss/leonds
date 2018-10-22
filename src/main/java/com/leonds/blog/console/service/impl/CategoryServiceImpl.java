package com.leonds.blog.console.service.impl;

import com.leonds.blog.console.service.CategoryService;
import com.leonds.blog.console.service.SequenceService;
import com.leonds.blog.domain.entity.Category;
import com.leonds.blog.domain.enums.Sequence;
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

    @Autowired
    private SequenceService sequenceService;

    @Override
    public Category save(Category category) {
        CheckUtils.checkObject(category);
        if (StringUtils.isNotBlank(category.getId())) {
            Category categoryOfName = getByIdIsNotAndName(category.getId(), category.getName());
            CheckUtils.checkState(categoryOfName == null, MessageUtils.get("category.name.exists"));

        } else {
            Category categoryOfName = getByName(category.getName());
            CheckUtils.checkState(categoryOfName == null, MessageUtils.get("category.name.exists"));
            category.setSn(sequenceService.getSequence(Sequence.SEQ_CATE.name()));
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

    @Override
    public List<Category> getAll() {
        return pm.find("getCategories", SqlParams.instance(), Category.class);
    }
}
