package com.leonds.blog.console.service;


import com.leonds.blog.domain.dto.TreeNode;
import com.leonds.blog.domain.entity.SysResource;
import com.leonds.blog.domain.entity.SysRole;
import com.leonds.blog.domain.entity.SysUser;
import com.leonds.core.orm.Page;
import com.leonds.core.orm.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * @author Leon
 */
public interface SysUserService {

    SysUser save(SysUser sysUser);

    SysUser save(SysUser sysUser, List<String> roleIds);

    SysUser getByUsername(String username);

    SysUser getByIdIsNotAndUsername(String id, String username);

    SysUser getById(String id);

    Page<Map<String, Object>> getPage(PageRequest pageRequest, String text, String status);

    void remove(List<String> ids);

    void enable(List<String> ids);

    void disable(List<String> ids);

    void resetPassword(String id);

    List<SysRole> getUserRoles(String userId);

    List<String> getUserPerms(String userId);

    List<TreeNode> getUserMenusTree(String userId);

    List<SysResource> getUserMenus(String userId);

    List<SysUser> getAllUser();

}
