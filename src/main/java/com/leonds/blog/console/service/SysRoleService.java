package com.leonds.blog.console.service;

import com.leonds.blog.domain.entity.SysRole;
import com.leonds.core.orm.Page;
import com.leonds.core.orm.PageRequest;

import java.util.List;

/**
 * @author Leon
 */
public interface SysRoleService {
    SysRole save(SysRole sysRole, List<String> resourceIds);

    SysRole getById(String id);

    Page<SysRole> getPage(PageRequest pageRequest, String text);

    void remove(List<String> ids);

    void saveUserRole(String userId, List<String> roleIds);

    void saveRoleResource(String roleId, List<String> resourceIds);

    List<SysRole> getAll();

    List<String> getUserRoleIds(String userId);

    void removeUserRole(String userId);

    List<String> getRoleResource(String roleId);

    SysRole getByName(String name);

    SysRole getByIdIsNotAndName(String id, String name);

    SysRole getByCode(String code);

    SysRole getByIdIsNotAndCode(String id, String code);
}
