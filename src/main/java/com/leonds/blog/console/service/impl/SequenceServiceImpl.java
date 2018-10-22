package com.leonds.blog.console.service.impl;

import com.leonds.blog.console.service.SequenceService;
import com.leonds.core.orm.PersistenceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Leon
 */
@Service
public class SequenceServiceImpl implements SequenceService {
    @Autowired
    private PersistenceManager pm;

    @Override
    public String getSequence(String name) {
        return pm.getSqlSession().selectOne("getSequence", name);
    }
}
