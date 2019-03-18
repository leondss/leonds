package com.leonds.blog.www.service.impl;

import com.leonds.blog.domain.entity.Tag;
import com.leonds.blog.www.service.FrontTagService;
import com.leonds.core.orm.PersistenceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Leon
 */
@Service
public class FrontTagServiceImpl implements FrontTagService {
    @Autowired
    private PersistenceManager pm;

    @Override
    public List<Tag> getAll() {
        return pm.findAll(Tag.class);
    }
}
