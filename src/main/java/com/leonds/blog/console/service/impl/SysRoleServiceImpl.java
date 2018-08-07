package com.leonds.blog.console.service.impl;

import com.leonds.blog.console.service.SysRoleService;
import com.leonds.blog.domain.entity.SysRole;
import com.leonds.blog.domain.entity.SysRoleResource;
import com.leonds.blog.domain.entity.SysUserRole;
import com.leonds.core.orm.*;
import com.leonds.core.utils.CheckUtils;
import com.leonds.core.utils.MessageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Leon
 */
@Service
@Transactional
public class SysRoleServiceImpl implements SysRoleService {
    @Autowired
    private PersistenceManager pm;

    @Override
    public SysRole save(SysRole sysRole, List<String> resourceIds) {
        CheckUtils.checkObject(sysRole);
        SysRole oldRole;
        if (StringUtils.isBlank(sysRole.getId())) {
            oldRole = getByName(sysRole.getName());
        } else {
            oldRole = getByIdIsNotAndName(sysRole.getId(), sysRole.getName());
        }
        CheckUtils.checkState(oldRole == null, MessageUtils.get("sysRole.name.exists"));

        SysRole newRole = new SysRole();
        if (StringUtils.isBlank(sysRole.getId())) {
            BeanUtils.copyProperties(sysRole, newRole);
        } else {
            SysRole role = getById(sysRole.getId());
            BeanUtils.copyProperties(role, newRole);
            newRole.setName(sysRole.getName());
            newRole.setDescription(sysRole.getDescription());
        }
        SysRole result = pm.save(newRole);
        if (resourceIds != null) {
            saveRoleResource(result.getId(), resourceIds);
        }
        return result;
    }

    @Override
    public SysRole getById(String id) {
        return pm.get(SysRole.class, id);
    }

    @Override
    public Page<SysRole> getPage(PageRequest pageRequest, String text) {
        Condition condition = null;
        if (StringUtils.isNotBlank(text)) {
            condition = Filters.and(
                    Filters.or(
                            Filters.like("name", text),
                            Filters.like("description", text)
                    )
            );
        }
        return pm.findPage(SysRole.class, pageRequest, condition);
    }

    @Override
    public void remove(List<String> ids) {
        ids.forEach(id -> pm.remove(SysRole.class, id));
    }

    @Override
    public void saveUserRole(String userId, List<String> roleIds) {
        pm.remove(SysUserRole.class, Filters.and(Filters.eq("user_id", userId)));
        roleIds.forEach(roleId -> {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(userId);
            sysUserRole.setRoleId(roleId);
            pm.save(sysUserRole);
        });
    }

    @Override
    public void saveRoleResource(String roleId, List<String> resourceIds) {
        pm.remove(SysRoleResource.class, Filters.and(Filters.eq("role_id", roleId)));
        resourceIds.forEach(resId -> {
            SysRoleResource sysRoleResource = new SysRoleResource();
            sysRoleResource.setRoleId(roleId);
            sysRoleResource.setResourceId(resId);
            pm.save(sysRoleResource);
        });
    }

    @Override
    public List<SysRole> getAll() {
        return pm.findAll(SysRole.class);
    }

    @Override
    public List<String> getUserRoleIds(String userId) {
        List<SysUserRole> sysUserRoles = pm.find(SysUserRole.class, Filters.and(Filters.eq("user_id", userId)));
        return sysUserRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
    }

    @Override
    public void removeUserRole(String userId) {
        pm.remove(SysUserRole.class, Filters.and(Filters.eq("user_id", userId)));
    }

    @Override
    public List<String> getRoleResource(String roleId) {
        return pm.find(SysRoleResource.class, Filters.and(Filters.eq("role_id", roleId)))
                .stream().map(SysRoleResource::getResourceId).collect(Collectors.toList());
    }

    @Override
    public SysRole getByName(String name) {
        return pm.findOne(SysRole.class, Filters.and(Filters.eq("name", name)));
    }

    @Override
    public SysRole getByIdIsNotAndName(String id, String name) {
        return pm.findOne(SysRole.class, Filters.and(Filters.eq("name", name), Filters.neq("id", id)));
    }

    @Override
    public SysRole getByCode(String code) {
        return pm.findOne(SysRole.class, Filters.and(Filters.eq("code", code)));
    }

    @Override
    public SysRole getByIdIsNotAndCode(String id, String code) {
        return pm.findOne(SysRole.class, Filters.and(Filters.eq("code", code), Filters.neq("id", id)));
    }
}
