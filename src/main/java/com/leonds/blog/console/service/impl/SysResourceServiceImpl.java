package com.leonds.blog.console.service.impl;

import com.leonds.blog.console.service.SysResourceService;
import com.leonds.blog.domain.entity.SysResource;
import com.leonds.core.orm.Filters;
import com.leonds.core.orm.PersistenceManager;
import com.leonds.core.orm.SqlParams;
import com.leonds.core.utils.CheckUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author Leon
 */
@Service
@Transactional
public class SysResourceServiceImpl implements SysResourceService {

    @Autowired
    private PersistenceManager pm;

    @Override
    public SysResource save(SysResource sysResource) {
        CheckUtils.checkObject(sysResource);
        SysResource newResource = new SysResource();

        if (StringUtils.isBlank(sysResource.getId())) {
            BeanUtils.copyProperties(sysResource, newResource);
        } else {
            SysResource resource = getById(sysResource.getId());
            BeanUtils.copyProperties(resource, newResource);

            newResource.setName(sysResource.getName());
            newResource.setComponent(sysResource.getComponent());
            newResource.setPermission(sysResource.getPermission());
            newResource.setPath(sysResource.getPath());
            newResource.setUrl(sysResource.getUrl());
            newResource.setPosition(sysResource.getPosition());
            newResource.setType(sysResource.getType());
            newResource.setPid(sysResource.getPid());
            newResource.setIcon(sysResource.getIcon());
        }
        return pm.save(newResource);
    }

    @Override
    public SysResource getById(String id) {
        return pm.get(SysResource.class, id);
    }

    @Override
    public void remove(List<String> ids) {
        ids.forEach(id -> pm.remove(SysResource.class, id));
    }

    @Override
    public List<Map<String, Object>> getTreeByPid(String pid) {
        SqlParams sqlParams = SqlParams.instance().append("pid", pid);
        return pm.find("getResources", sqlParams);
    }

    @Override
    public SysResource getByName(String name) {
        return pm.findOne(SysResource.class, Filters.and(Filters.eq("name", name)));
    }

    @Override
    public SysResource getByIdIsNotAndName(String id, String name) {
        return pm.findOne(SysResource.class, Filters.and(Filters.eq("name", name), Filters.neq("id", id)));
    }

    @Override
    public SysResource getByUrl(String url) {
        return pm.findOne(SysResource.class, Filters.and(Filters.eq("url", url)));
    }
}
