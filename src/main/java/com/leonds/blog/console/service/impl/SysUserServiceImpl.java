package com.leonds.blog.console.service.impl;

import com.google.common.collect.Collections2;
import com.leonds.blog.console.service.SysRoleService;
import com.leonds.blog.console.service.SysUserService;
import com.leonds.blog.domain.dto.TreeNode;
import com.leonds.blog.domain.entity.SysResource;
import com.leonds.blog.domain.entity.SysRole;
import com.leonds.blog.domain.entity.SysUser;
import com.leonds.blog.domain.enums.SysUserStatus;
import com.leonds.core.ServiceException;
import com.leonds.core.orm.*;
import com.leonds.core.utils.CheckUtils;
import com.leonds.core.utils.MessageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Leon
 */
@Service
@Transactional
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private PersistenceManager pm;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public SysUser save(SysUser sysUser) {
        return pm.save(sysUser);
    }

    @Override
    public SysUser save(SysUser sysUser, List<String> roleIds) {
        CheckUtils.checkObject(sysUser);
        SysUser oldUser;
        if (StringUtils.isBlank(sysUser.getId())) {
            oldUser = getByUsername(sysUser.getUsername());
        } else {
            oldUser = getByIdIsNotAndUsername(sysUser.getId(), sysUser.getUsername());
        }
        CheckUtils.checkState(oldUser == null, MessageUtils.get("sysUser.username.exists"));

        SysUser newUser = new SysUser();
        if (StringUtils.isBlank(sysUser.getId())) {
            BeanUtils.copyProperties(sysUser, newUser);
            newUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
            newUser.setStatus(SysUserStatus.NORMAL.getCode());
        } else {
            SysUser user = getById(sysUser.getId());
            BeanUtils.copyProperties(user, newUser);

            /*if (StringUtils.isNotBlank(sysUser.getUsername())) {
                newUser.setUsername(sysUser.getUsername());
            }
            if (StringUtils.isNotBlank(sysUser.getPassword())) {
                newUser.setPassword(sysUser.getPassword());
            }*/
        }
        SysUser result = pm.save(newUser);
        if (roleIds != null) {
            sysRoleService.saveUserRole(result.getId(), roleIds);
        }
        return result;
    }

    @Override
    public SysUser getByUsername(String username) {
        return pm.findOne(SysUser.class, Filters.and(Filters.eq("username", username)));
    }

    @Override
    public SysUser getByIdIsNotAndUsername(String id, String username) {
        return pm.findOne(SysUser.class, Filters.and(Filters.eq("username", username), Filters.neq("id", id)));
    }

    @Override
    public SysUser getById(String id) {
        return pm.get(SysUser.class, id);
    }

    @Override
    public Page<Map<String, Object>> getPage(PageRequest pageRequest, String text, String status) {
        SqlParams sqlParams = SqlParams.instance()
                .page(pageRequest)
                .orderByClause("creation_time desc")
                .append("text", text)
                .append("status", status);
        return pm.findPage("getUsers", sqlParams);
    }

    @Override
    public void remove(List<String> ids) {
        ids.forEach(id -> {
            pm.remove(SysUser.class, id);
            sysRoleService.removeUserRole(id);
        });
    }

    @Override
    public void enable(List<String> ids) {
        updateStatus(ids, SysUserStatus.DISABLE, SysUserStatus.NORMAL);
    }

    private void updateStatus(List<String> ids, SysUserStatus old, SysUserStatus current) {
        ids.forEach(id -> {
            SysUser sysUser = getById(id);
            if (sysUser != null && sysUser.getStatus() == old.getCode()) {
                sysUser.setStatus(current.getCode());
                pm.save(sysUser);
            }
        });
    }

    @Override
    public void disable(List<String> ids) {
        updateStatus(ids, SysUserStatus.NORMAL, SysUserStatus.DISABLE);
    }

    @Override
    public void resetPassword(String id) {
        if (StringUtils.isNotBlank(id)) {
            SysUser sysUser = getById(id);
            if (sysUser != null) {
                sysUser.setPassword(passwordEncoder.encode("123456"));
                pm.save(sysUser);
            }
        } else {
            throw new ServiceException(MessageUtils.get("sysUser.not.exists"));
        }
    }

    @Override
    public List<SysRole> getUserRoles(String userId) {
        SqlParams sqlparams = SqlParams.instance().append("userId", userId);
        return pm.find("getUserRoles", sqlparams, SysRole.class);
    }

    @Override
    public List<String> getUserPerms(String userId) {
        SqlParams sqlparams = SqlParams.instance().append("userId", userId);
        List<Map<String, Object>> perms = pm.find("getUserPerms", sqlparams);
        return perms.stream().map(input -> input.get("permission").toString()).collect(Collectors.toList());
    }

    @Override
    public List<TreeNode> getUserMenusTree(String userId) {
        List<SysResource> menus = getUserMenus(userId);
        Collection<SysResource> root = Collections2.filter(menus, i -> StringUtils.isBlank(i.getPid()));
        List<TreeNode> result = new ArrayList<>();
        for (SysResource sysResource : root) {
            TreeNode treeNode = buildMenuTree(sysResource.getId(), menus);
            result.add(treeNode);
        }
        return result;
    }

    @Override
    public List<SysResource> getUserMenus(String userId) {
        SqlParams sqlparams = SqlParams.instance().append("userId", userId);
        return pm.find("getUserMenus", sqlparams, SysResource.class);
    }

    @Override
    public List<SysUser> getAllUser() {
        return pm.findAll(SysUser.class);
    }

    @Override
    public List<String> getUserCategoryPerms(String userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        return pm.getSqlSession().selectList("getCategoryPerms", params);
    }

    @Override
    public List<String> getUserMaxDayPerms(String userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        return pm.getSqlSession().selectList("getMaxDayPerms", params);
    }

    public TreeNode buildMenuTree(final String pid, final List<SysResource> sysResources) {

        SysResource currentMenu = sysResources.stream().filter(sysMenu -> sysMenu.getId().equals(pid)).findFirst().get();

        TreeNode node = convertTreeNode(currentMenu);

        List<SysResource> sysMenusList = sysResources.stream().filter(sysMenu -> StringUtils.isNotBlank(sysMenu.getPid()) && sysMenu.getPid().equals(pid)).collect(Collectors.toList());

        List<TreeNode> childrens = new ArrayList<>();

        for (SysResource sysResource : sysMenusList) {
            childrens.add(convertTreeNode(sysResource));
        }

        for (TreeNode child : childrens) {
            TreeNode n = buildMenuTree(child.getId(), sysResources);
            node.getChildren().add(n);
        }
        return node;
    }

    private TreeNode convertTreeNode(SysResource sysResource) {
        return TreeNode.builder()
                .id(sysResource.getId())
                .url(sysResource.getUrl())
                .path(sysResource.getPath())
                .component(sysResource.getComponent())
                .name(sysResource.getName())
                .permission(sysResource.getPermission())
                .pid(sysResource.getPid())
                .position(sysResource.getPosition())
                .type(sysResource.getType())
                .icon(sysResource.getIcon())
                .children(new ArrayList<>())
                .build();
    }
}
