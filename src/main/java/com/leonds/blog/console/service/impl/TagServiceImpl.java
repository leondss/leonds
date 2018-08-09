package com.leonds.blog.console.service.impl;

import com.leonds.blog.console.service.TagService;
import com.leonds.blog.domain.entity.Tag;
import com.leonds.core.orm.Filters;
import com.leonds.core.orm.PersistenceManager;
import com.leonds.core.utils.CheckUtils;
import com.leonds.core.utils.MessageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Leon
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private PersistenceManager pm;

    @Override
    public Tag save(Tag tag) {
        CheckUtils.checkObject(tag);
        Tag tagOfName;
        if (StringUtils.isNotBlank(tag.getId())) {
            tagOfName = getByIdIsNotAndName(tag.getId(), tag.getName());
        } else {
            tagOfName = getByName(tag.getName());
        }
        CheckUtils.checkState(tagOfName == null, MessageUtils.get("tag.name.exists"));
        return pm.save(tag);
    }

    @Override
    public Tag getByName(String name) {
        return pm.findOne(Tag.class, Filters.and(Filters.eq("name", name)));
    }

    @Override
    public Tag getByIdIsNotAndName(String id, String name) {
        return pm.findOne(Tag.class, Filters.and(Filters.eq("name", name), Filters.neq("id", id)));
    }

    @Override
    public Tag getById(String id) {
        return pm.get(Tag.class, id);
    }

    @Override
    public void remove(List<String> ids) {
        if (ids != null && !ids.isEmpty()) {
            pm.remove(Tag.class, ids);
        }
    }
}
