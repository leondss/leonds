package com.leonds.console.service.impl;

import com.leonds.console.repository.SysResourceRepository;
import com.leonds.console.service.SysResourceService;
import com.leonds.core.CheckUtils;
import com.leonds.core.MessageUtils;
import com.leonds.domain.entity.SysResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Leon
 */
@Service
@Transactional
public class SysResourceServiceImpl implements SysResourceService {

    @Resource
    private SysResourceRepository sysResourceRepository;

    @Override
    public SysResource save(SysResource sysResource) {
        CheckUtils.checkObject(sysResource);
        SysResource newResource = new SysResource();
        SysResource oldResource;
        if (StringUtils.isBlank(sysResource.getId())) {
            oldResource = sysResourceRepository.findByName(sysResource.getName());
        } else {
            oldResource = sysResourceRepository.findByIdIsNotAndName(sysResource.getId(), sysResource.getName());
        }
        CheckUtils.checkState(oldResource == null, MessageUtils.get("sysResource.name.exists"));

        if (StringUtils.isBlank(sysResource.getId())) {
            BeanUtils.copyProperties(sysResource, newResource);
        } else {
            SysResource resource = getById(sysResource.getId());
            BeanUtils.copyProperties(resource, newResource);

            newResource.setName(sysResource.getName());
            if (StringUtils.isNotBlank(sysResource.getLink())) {
                newResource.setLink(sysResource.getLink());
            }
            if (StringUtils.isNotBlank(sysResource.getPermission())) {
                newResource.setPermission(sysResource.getPermission());
            }
            newResource.setPosition(sysResource.getPosition());
            newResource.setType(sysResource.getType());
            newResource.setPid(sysResource.getPid());
        }
        return sysResourceRepository.save(newResource);
    }

    @Override
    public SysResource getById(String id) {
        return sysResourceRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(List<String> ids) {
        ids.forEach(id -> sysResourceRepository.deleteById(id));
    }
}
