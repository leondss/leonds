package com.leonds.console.service.impl;

import com.google.common.collect.Lists;
import com.leonds.console.repository.SysRoleRepository;
import com.leonds.console.repository.SysRoleResourceRepository;
import com.leonds.console.repository.SysUserRoleRepository;
import com.leonds.console.service.SysRoleService;
import com.leonds.core.CheckUtils;
import com.leonds.core.MessageUtils;
import com.leonds.domain.entity.SysRole;
import com.leonds.domain.entity.SysRoleResource;
import com.leonds.domain.entity.SysUserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Leon
 */
@Service
@Transactional
public class SysRoleServiceImpl implements SysRoleService {
    @Autowired
    private SysRoleRepository sysRoleRepository;
    @Autowired
    private SysUserRoleRepository sysUserRoleRepository;
    @Autowired
    private SysRoleResourceRepository sysRoleResourceRepository;

    @Override
    public SysRole save(SysRole sysRole, List<String> resourceIds) {
        CheckUtils.checkObject(sysRole);
        SysRole oldRole;
        if (StringUtils.isBlank(sysRole.getId())) {
            oldRole = sysRoleRepository.findByName(sysRole.getName());
        } else {
            oldRole = sysRoleRepository.findByIdIsNotAndName(sysRole.getId(), sysRole.getName());
        }
        CheckUtils.checkState(oldRole == null, MessageUtils.get("sysRole.name.exists"));

        SysRole newRole = new SysRole();
        if (StringUtils.isBlank(sysRole.getId())) {
            BeanUtils.copyProperties(sysRole, newRole);
        } else {
            SysRole role = getById(sysRole.getId());
            BeanUtils.copyProperties(role, newRole);
            newRole.setName(sysRole.getName());
        }
        SysRole result = sysRoleRepository.save(newRole);
        if (resourceIds != null) {
            saveRoleResource(result.getId(), resourceIds);
        }
        return result;
    }

    @Override
    public SysRole getById(String id) {
        return sysRoleRepository.findById(id).orElse(null);
    }

    @Override
    public Page<SysRole> getPage(PageRequest pageRequest, String text) {
        Specification<SysRole> specification = (Specification<SysRole>) (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotBlank(text)) {
                Predicate name = cb.like(root.get("name").as(String.class), "%" + text + "%");
                predicates.add(name);
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return sysRoleRepository.findAll(specification, pageRequest);
    }

    @Override
    public void remove(List<String> ids) {
        ids.forEach(id -> sysRoleRepository.deleteById(id));
    }

    @Override
    public void saveUserRole(String userId, List<String> roleIds) {
        sysUserRoleRepository.deleteByUserId(userId);
        roleIds.forEach(roleId -> {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(userId);
            sysUserRole.setRoleId(roleId);
            sysUserRoleRepository.save(sysUserRole);
        });
    }

    @Override
    public void saveRoleResource(String roleId, List<String> resourceIds) {
        sysRoleResourceRepository.deleteByRoleId(roleId);
        resourceIds.forEach(resId -> {
            SysRoleResource sysRoleResource = new SysRoleResource();
            sysRoleResource.setRoleId(roleId);
            sysRoleResource.setResourceId(resId);
            sysRoleResourceRepository.save(sysRoleResource);
        });
    }

    @Override
    public List<SysRole> getAll() {
        return Lists.newArrayList(sysRoleRepository.findAll());
    }
}
