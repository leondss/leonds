package com.leonds.console.service;

import com.leonds.domain.entity.SysRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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
}
