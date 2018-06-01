package com.leonds.console.service;

import com.leonds.domain.dto.TreeNode;
import com.leonds.domain.entity.SysResource;

import java.util.List;

/**
 * @author Leon
 */
public interface SysResourceService {
    SysResource save(SysResource sysResource);

    SysResource getById(String id);

    void remove(List<String> ids);

    List<TreeNode> getTreeByPid(String pid);
}
